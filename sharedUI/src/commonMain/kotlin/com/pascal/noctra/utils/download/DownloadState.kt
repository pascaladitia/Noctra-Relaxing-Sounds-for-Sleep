package com.pascal.noctra.utils.download

sealed interface DownloadState {
    data class Progress(val percent: Int) : DownloadState
    data class Success(val filePath: String) : DownloadState
    data class Error(val message: String) : DownloadState
}