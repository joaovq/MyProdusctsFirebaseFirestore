package br.com.joaovitorqueiroz.firebasefirestore.data.repository

import br.com.joaovitorqueiroz.firebasefirestore.model.Product

interface Repository<K,T> {
    suspend fun add(value : Product) : K
    fun getAll() : T
     fun orderBy(field : String) :  T
    suspend fun delete(id : String)
    suspend fun update(product : Product, id : String)
}