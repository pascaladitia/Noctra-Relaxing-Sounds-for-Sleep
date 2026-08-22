package com.pascal.noctra.utils.base

import com.pascal.noctra.data.remote.KtorClientFactory
import com.pascal.noctra.data.remote.dtos.BaseResponse
import com.pascal.noctra.data.session.SessionManager
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

abstract class SafeApiCall {

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        isLenient = true
    }

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T,
    ): T {
        return safeApiCallInternal(
            apiCall = apiCall,
            retryOnAuthFailure = true,
        )
    }

    private suspend fun <T> safeApiCallInternal(
        apiCall: suspend () -> T,
        retryOnAuthFailure: Boolean,
    ): T {
        return try {
            apiCall()
        } catch (e: Exception) {
            val response = when (e) {
                is ClientRequestException -> e.response
                is ServerResponseException -> e.response
                else -> null
            }

            val errorBody = response?.bodyAsText()

            if (
                retryOnAuthFailure &&
                shouldRefreshToken(response) &&
                KtorClientFactory.refreshToken()
            ) {
                return safeApiCallInternal(
                    apiCall = apiCall,
                    retryOnAuthFailure = false,
                )
            }

            if (isSessionExpired(response)) {
                SessionManager.notifySessionExpired()
            }

            val message = when (e) {
                is ClientRequestException,
                is ServerResponseException -> {
                    parseError(errorBody)
                }

                is IOException -> {
                    "Network error. Please check your connection."
                }

                else -> {
                    e.message ?: "Unknown error"
                }
            }

            throw Exception(message)
        }
    }

    private fun shouldRefreshToken(response: HttpResponse?): Boolean {
        if (response == null) return false
        if (response.status.value != 401) return false
        if (response.request.url.encodedPath.contains("/auth/", ignoreCase = true)) return false
        return true
    }

    private fun isSessionExpired(response: HttpResponse?): Boolean {
        if (response == null) return false
        if (response.status.value != 401) return false
        return !response.request.url.encodedPath.contains("/auth/", ignoreCase = true)
    }

    fun parseError(errorBody: String?): String {
        if (errorBody.isNullOrBlank()) return "Unknown error"

        return try {
            val baseResponse =
                json.decodeFromString<BaseResponse<Unit>>(errorBody)

            baseResponse.status.orEmpty()

        } catch (e: Exception) {
            errorBody
        }
    }

    private fun buildErrorMessage(
        message: String?,
        errors: JsonElement?
    ): String {
        val builder = StringBuilder()

        if (!message.isNullOrBlank()) {
            builder.append(message.trim())
        }

        val errorLines = parseErrors(errors)

        if (errorLines.isNotEmpty()) {
            if (builder.isNotEmpty()) builder.append("\n")
            errorLines.forEach {
                builder.append("- ").append(it).append("\n")
            }
        }

        return builder.toString().trim()
    }

    private fun parseErrors(errors: JsonElement?): List<String> {
        if (errors == null || errors is JsonNull) return emptyList()

        return when (errors) {

            is JsonArray -> {
                errors.mapNotNull { it.jsonPrimitive.contentOrNull }
            }

            is JsonObject -> {
                errors.flatMap { (key, value) ->
                    when (value) {
                        is JsonArray -> {
                            value.mapNotNull {
                                it.jsonPrimitive.contentOrNull
                            }
                        }

                        is JsonPrimitive -> {
                            listOf("${key}: ${value.content}")
                        }

                        else -> emptyList()
                    }
                }
            }

            is JsonPrimitive -> {
                listOf(errors.content)
            }

            else -> emptyList()
        }
    }
}
