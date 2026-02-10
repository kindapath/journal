package com.kindaboii.journal.features.entries.impl.data.datasource.remote.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

/**
 * Custom serializer that handles both List<String> and JSON-encoded string representations.
 *
 * Supabase JSONB columns can return data in different formats:
 * - As a proper JSON array: ["Calm", "Focused"]
 * - As a double-encoded string: "[\"Calm\",\"Focused\"]"
 *
 * This serializer handles both cases.
 */
object JsonStringListSerializer : KSerializer<List<String>> {
    private val listSerializer = ListSerializer(String.serializer())

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("JsonStringList")

    override fun deserialize(decoder: Decoder): List<String> {
        // Try to decode as JsonElement to inspect what we received
        return if (decoder is JsonDecoder) {
            val element = decoder.decodeJsonElement()
            when (element) {
                is JsonArray -> {
                    // It's already an array, decode normally
                    Json.decodeFromJsonElement(listSerializer, element)
                }
                is JsonPrimitive -> {
                    // It's a string, parse it as JSON
                    val jsonString = element.jsonPrimitive.content
                    if (jsonString.isEmpty() || jsonString == "[]") {
                        emptyList()
                    } else {
                        try {
                            Json.decodeFromString(listSerializer, jsonString)
                        } catch (e: Exception) {
                            // If parsing fails, return empty list
                            emptyList()
                        }
                    }
                }
                else -> emptyList()
            }
        } else {
            // Fallback for non-JSON decoders
            listSerializer.deserialize(decoder)
        }
    }

    override fun serialize(encoder: Encoder, value: List<String>) {
        listSerializer.serialize(encoder, value)
    }
}