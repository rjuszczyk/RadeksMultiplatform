package pl.rjuszczyk.radeksmultiplatform.repository

import co.touchlab.firebase.firestore.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class FirebaseTestRepository {
    suspend fun addItem(item: String) {
        getFirebaseInstance().collection("testdata").suspendAdd(
                mapOf(
                    "name" to item,
                    "timestamp" to timestampNow()
                )
            )
    }

    val allItems: Flow<List<String>?> =
        getFirebaseInstance().collection("testdata")
            .orderBy("timestamp", QueryDirection.DESCENDING)
            .asFlow()
            .map {
                it.documents_.map {
                    it.data_()?.get("name") as String?
                }.filterNotNull().map {
                    it
                }
            }
            .map { it as List<String>? }
            .onStart {
                emit(null)
            }
}