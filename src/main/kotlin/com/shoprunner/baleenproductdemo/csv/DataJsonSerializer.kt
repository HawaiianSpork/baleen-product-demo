package com.shoprunner.baleenproductdemo.csv

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.shoprunner.baleen.Data
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class DataJsonSerializer : StdSerializer<Data>(Data::class.java) {

    override fun serialize(value: Data?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeStartObject()
        value?.keys?.forEach { key ->
            gen?.writeObjectField(key, value[key])
        }
        gen?.writeEndObject()


    }

}