package com.example.hasscontrolsprovider.mapper

import com.squareup.moshi.*
import timber.log.Timber
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MoshiDateAdapter : JsonAdapter<ZonedDateTime>() {

    private val dateFormatter = DateTimeFormatter.ISO_DATE_TIME

    @FromJson
    override fun fromJson(reader: JsonReader): ZonedDateTime? {
        return try {
            val dateAsString = reader.nextString()
            ZonedDateTime.from(dateFormatter.parse(dateAsString))
        } catch (e: Exception) {
            Timber.w(e, "Failed to parse date from JSON")
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: ZonedDateTime?) {
        value?.let {
            writer.value(dateFormatter.format(it))
        }
    }
}