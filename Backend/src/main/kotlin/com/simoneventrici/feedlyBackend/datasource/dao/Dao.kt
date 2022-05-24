package com.simoneventrici.feedlyBackend.datasource.dao

interface Dao<T> {
    fun getAll(): List<T>
    fun save(elem: T)
}