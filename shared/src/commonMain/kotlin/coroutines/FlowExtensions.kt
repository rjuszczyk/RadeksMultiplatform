/*
 * Copyright (C) 2018 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("FlowQuery")

package co.touchlab.sessionize.db.coroutines

import co.touchlab.stately.concurrency.ThreadLocalRef
import co.touchlab.stately.concurrency.value
import com.squareup.sqldelight.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/** Turns this [Query] into a [Flow] which emits whenever the underlying result set changes. */
@JvmName("toFlow")
fun <T : Any> Query<T>.asFlow(): Flow<Query<T>> = callbackFlow<Query<T>> {

  // using ThreadLocalRef as Query.Listener and all references in it get frozen on native
  // May be removed once fixed in SQLDelight https://github.com/square/sqldelight/issues/1390
  val onNextRef: ThreadLocalRef<() -> Unit> = ThreadLocalRef()
  onNextRef.value = {
    offer(this@asFlow)
  }

  val listener = object : Query.Listener {
    override fun queryResultsChanged() {
      onNextRef.value?.invoke()
    }
  }
  addListener(listener)
  offer(this@asFlow)
  awaitClose {
    removeListener(listener)
    onNextRef.remove()
  }
}.conflate()

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOne(
  context: CoroutineContext = Dispatchers.Default
): Flow<T> = map {
  withContext(context) {
    it.executeAsOne()
  }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOneOrDefault(
  defaultValue: T,
  context: CoroutineContext = Dispatchers.Default
): Flow<T> = map {
  withContext(context) {
    it.executeAsOneOrNull() ?: defaultValue
  }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOneOrNull(
  context: CoroutineContext = Dispatchers.Default
): Flow<T?> = map {
  withContext(context) {
    it.executeAsOneOrNull()
  }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToOneNotNull(
  context: CoroutineContext = Dispatchers.Default
): Flow<T> = mapNotNull {
  withContext(context) {
    it.executeAsOneOrNull()
  }
}

@JvmOverloads
fun <T : Any> Flow<Query<T>>.mapToList(): Flow<List<T>> = map {
  it.executeAsList()
}
