package com.qverzey.drawtogether.data.model

data class ResponseInfo(
    val message: String? = null,
    val error: String? = null
) {
    val isSuccess: Boolean
        get() = error == null && message != null
}