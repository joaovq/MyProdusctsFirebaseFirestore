package br.com.joaovitorqueiroz.firebasefirestore.data.database

import android.util.Log
import br.com.joaovitorqueiroz.firebasefirestore.data.repository.Repository
import br.com.joaovitorqueiroz.firebasefirestore.model.Product
import com.google.firebase.firestore.FirebaseFirestore

class DataSource(private val firestore: FirebaseFirestore) : Repository<String, List<Product>> {

    private val products = mutableListOf<Product>()


    override suspend fun add(value: Product): String {
        var idInserted = ""
//        This format generate Id automatic. for selected id, use document(path) and set(data)
        firestore.collection(COLLECTION_PATH).add(mapOf(
            "nome" to value.nome,
            "valor" to value.valor,
            "imagem" to value.image
        )).addOnSuccessListener {
                documentReference ->
            Log.d(TAG_INSERT, "DocumentSnapshot added with ID: ${documentReference.id}")
            idInserted = documentReference.id
        }.addOnFailureListener { e ->
            Log.w(TAG_INSERT, "Error adding document", e)
        }

        return idInserted
    }

    override fun getAll(): List<Product> {
//  UNUSED
        firestore.collection(COLLECTION_PATH).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    Log.d(TAG_GET, "${document.id} => ${document.data}")
                }
            }
        }.addOnFailureListener { exception ->
            Log.w(TAG_GET, "Error getting documents.", exception)
        }

        if (products.isEmpty()) {
            Log.e(TAG_GET, "is Empty")
        }
        products.forEachIndexed { index, product ->
            Log.e(TAG_GET, "$index - $product")
        }

        return products

    }

    override  fun orderBy(field : String): List<Product> {
        val products = mutableListOf<Product>()

        firestore.collection(COLLECTION_PATH).orderBy(field).get().addOnSuccessListener { result ->
            for (document in result) {
                Log.d(TAG_GET, "\n ORDER BY : ${document.id} => ${document.data}")
                document.data.forEach {
                    products.add(it.value as Product)
                }
            }
        }.addOnFailureListener { exception ->
            Log.w(TAG_GET, "Error getting documents.", exception)
        }

        return products

    }

    override suspend fun delete(id : String) {
        firestore.collection(COLLECTION_PATH).document(id).delete().addOnCompleteListener {
            Log.d(TAG_DELETE, "Complete")
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
        }
    }

    override suspend fun update(product : Product, id : String) {
        val productsRef = firestore.collection(COLLECTION_PATH)
            .document(id)

// Set the "isCapital" field of the city 'DC'
        productsRef
            .update(mapOf(
                "nome" to product.nome,
                "valor" to product.valor,
                "imagem" to product.image
            ))
            .addOnSuccessListener {
                Log.d(TAG_UPDATE, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener { e -> Log.w(TAG_UPDATE, "Error updating document", e) }
    }

    companion object {
        private const val TAG_INSERT = "INSERT"
        private const val TAG_GET = "GET"
        private const val TAG_UPDATE = "UPDATE"
        private const val TAG_DELETE = "DELETE"
        private const val COLLECTION_PATH = "products"
    }
}