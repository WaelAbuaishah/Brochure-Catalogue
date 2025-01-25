package com.android.data.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import java.lang.reflect.Type


/**
 * This [JsonAdapter] returns [defaultValue] for malformed object instead of throwing a [JsonDataException].
 * Provide which [T] to return [defaultValue] when instantiating via the provided [factory].
 */
class DefaultOnMalformedDataAdapter<T> private constructor(
    private val delegate: JsonAdapter<T>,
    private val defaultValue: T?
) : JsonAdapter<T>() {

    @FromJson override fun fromJson(reader: JsonReader): T? {
        // Use a peeked reader to leave the reader in a known state even if there's an exception.
        val peeked = reader.peekJson()
        val result: T? = try {
            // Attempt to decode to the target type with the peeked reader.
            println("Raw JSON being parsed: ${peeked.readJsonValue()}") // Debug the raw JSON
            delegate.fromJson(peeked)
        } catch (e: JsonDataException) {
            println("Invalid model. The model parsed with JsonDataException, it returned the default value: $defaultValue as a fallback\n" +
                " $e")
            defaultValue
        } finally {
            peeked.close()
        }
        // Skip the value back on the reader, no matter the state of the peeked reader.
        reader.skipValue()
        return result
    }

    @ToJson override fun toJson(writer: JsonWriter, value: T?) {
        delegate.toJson(writer, value)
    }

    companion object {
        fun <T> factory(typeClass: Class<T>, defaultValue: T? = null): Factory {
            return object : Factory {
                override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
                    if (typeClass != type) return null
                    val delegate: JsonAdapter<T> = moshi.nextAdapter(this, type, annotations)
                    return DefaultOnMalformedDataAdapter(delegate, defaultValue)
                }
            }
        }
    }
}
