package com.shoprunner.baleenproductdemo.csv

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.shoprunner.baleen.DataTrace
import com.shoprunner.baleen.ValidationError
import com.shoprunner.baleen.ValidationSuccess
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class DataValidationSerializer : StdSerializer<DataWithValidation>(DataWithValidation::class.java) {

    fun writeDataTrace(gen: JsonGenerator?, dataTrace: DataTrace) {
        gen?.writeStartObject()
        gen?.writeObjectField("stack", dataTrace.toList())
        gen?.writeObjectField("tags", dataTrace.tags)
        gen?.writeEndObject()
    }

    override fun serialize(pair: DataWithValidation?, gen: JsonGenerator?, provider: SerializerProvider?) {
        pair ?: return
        val (data, value) = pair
        gen?.writeStartObject()
        when (value) {
            is ValidationSuccess -> {
                gen?.writeObjectField("type", "Success")
                gen?.writeFieldName("dataTrace")
                writeDataTrace(gen, value.dataTrace)
                gen?.writeObjectField("value", value.value)
                gen?.writeObjectField("data", data)
            }
            is ValidationError -> {
                gen?.writeObjectField("type", "Error")
                gen?.writeFieldName("dataTrace")
                writeDataTrace(gen, value.dataTrace)
                gen?.writeObjectField("value", value.value)
                gen?.writeObjectField("message", value.message)
                gen?.writeObjectField("data", data)
            }
        }
        gen?.writeEndObject()
    }

}