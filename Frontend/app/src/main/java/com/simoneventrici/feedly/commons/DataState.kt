package com.simoneventrici.feedly.commons

// classe wrapper per un tipo di dato che deve essere fetchato da internet
// contiene il suo stato, oltre al valore effettivo
sealed class DataState<T>(val data: T?, val errorMsg: String? = null) {
    class Loading<T>: DataState<T>(data = null)
    class Success<T>(data: T): DataState<T>(data = data)
    class Error<T>(errorMsg: String): DataState<T>(data = null, errorMsg = errorMsg)
    class None<T>: DataState<T>(null)
}