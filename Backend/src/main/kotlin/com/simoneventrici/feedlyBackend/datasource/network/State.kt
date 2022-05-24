package com.simoneventrici.feedlyBackend.datasource.network

data class State<T>(
    val data: T?,
    val errorMsg: String? = null
)
