package com.simoneventrici.feedlyBackend.persistence.dao

interface Dao<T> {
    fun getAll(): List<T>
    fun save(elem: T)
}