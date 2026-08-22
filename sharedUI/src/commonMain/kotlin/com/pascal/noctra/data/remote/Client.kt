package com.pascal.noctra.data.remote

import co.touchlab.kermit.Logger
import com.pascal.noctra.BuildKonfig
import com.pascal.noctra.utils.Constant.TIMEOUT
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.Logger as KtorLogger

object KtorClientFactory {

    private const val MAX_NETWORK_RETRIES = 2
    private const val RETRY_BASE_DELAY_MS = 500L
    private const val RETRY_MAX_DELAY_MS = 3_000L
    private const val RETRY_RANDOMIZATION_MS = 250L

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private val refreshClient by lazy {
        createInternal(includeAuthHeader = false)
    }

    fun create() = createInternal(includeAuthHeader = true)

    private fun createInternal(
        includeAuthHeader: Boolean
    ) = HttpClient {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            logger = KtorLogger.DEFAULT
            level = LogLevel.ALL
            logger = object : KtorLogger {
                override fun log(message: String) {
                    Logger.e("Ktor Logging $message")
                }
            }
        }

        install(HttpRequestRetry) {
            maxRetries = MAX_NETWORK_RETRIES

            retryIf { request, response ->
                shouldRetryResponseRequest(request) && shouldRetryResponse(response.status)
            }

            retryOnExceptionIf { request, cause ->
                shouldRetryExceptionRequest(request) && shouldRetryException(cause)
            }

            modifyRequest {
                it.headers.append("X-Retry-Count", retryCount.toString())
            }

            exponentialDelay(
                baseDelayMs = RETRY_BASE_DELAY_MS,
                maxDelayMs = RETRY_MAX_DELAY_MS,
                randomizationMs = RETRY_RANDOMIZATION_MS,
            )
        }

        defaultRequest {
            url(BuildKonfig.BASE_URL)

            /** Using TOKEN
            val token = PrefLogin.getAccessToken()
            if (includeAuthHeader && token.isNotBlank() && !isAuthRequest(url.build().encodedPath)) {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            */
        }

        install(HttpTimeout) {
            connectTimeoutMillis = TIMEOUT
            requestTimeoutMillis = TIMEOUT
            socketTimeoutMillis = TIMEOUT
        }
    }

    suspend fun refreshToken(): Boolean {
        /** Using refresh token
        val refreshToken = PrefLogin.getRefreshToken()
        if (refreshToken.isBlank()) return false

        return runCatching {
            val response = refreshClient.post("auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenBody(refresh_token = refreshToken))
            }.body<BaseResponse<LoginResponse>>()

            val data = response.data ?: return false
            val newAccessToken = data.accessToken ?: data.token
            val newRefreshToken = data.refreshToken

            if (newAccessToken.isNullOrBlank() || newRefreshToken.isNullOrBlank()) {
                return false
            }

            PrefLogin.setAccessToken(newAccessToken)
            PrefLogin.setRefreshToken(newRefreshToken)
            true
        }.getOrElse { false }
        */

        return false
    }

    private fun isAuthRequest(path: String): Boolean =
        path.contains("/auth/", ignoreCase = true)

    private fun shouldRetryResponseRequest(request: HttpRequest): Boolean {
        return shouldRetryRequest(
            method = request.method,
            path = request.url.encodedPath,
            isMultipart = request.content.contentType?.match(ContentType.MultiPart.FormData) == true,
            retryableMethods = retryableResponseMethods,
        )
    }

    private fun shouldRetryExceptionRequest(request: HttpRequestBuilder): Boolean {
        return shouldRetryRequest(
            method = request.method,
            path = request.url.build().encodedPath,
            isMultipart = request.body is MultiPartFormDataContent,
            retryableMethods = retryableExceptionMethods,
        )
    }

    private fun shouldRetryRequest(
        method: HttpMethod,
        path: String,
        isMultipart: Boolean,
        retryableMethods: Set<HttpMethod>,
    ): Boolean {
        if (isAuthRequest(path)) return false
        if (isMultipart) return false
        return method in retryableMethods
    }

    private fun shouldRetryResponse(status: HttpStatusCode): Boolean {
        return status == HttpStatusCode.RequestTimeout ||
                status == HttpStatusCode.TooManyRequests ||
                status.value in 500..599
    }

    private fun shouldRetryException(cause: Throwable): Boolean {
        val exception = cause.unwrapCancellationException()
        return exception is IOException ||
                exception is HttpRequestTimeoutException ||
                exception is ConnectTimeoutException ||
                exception is SocketTimeoutException
    }

    private val retryableResponseMethods = setOf(
        HttpMethod.Get,
        HttpMethod.Head,
        HttpMethod.Options,
        HttpMethod.Put,
        HttpMethod.Delete,
    )

    private val retryableExceptionMethods = retryableResponseMethods + HttpMethod.Post
}
