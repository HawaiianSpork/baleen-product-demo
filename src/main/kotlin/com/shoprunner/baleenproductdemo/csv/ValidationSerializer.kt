package com.shoprunner.baleenproductdemo.csv

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.shoprunner.baleen.Data
import com.shoprunner.baleen.Validation
import com.shoprunner.baleen.ValidationError
import com.shoprunner.baleen.ValidationResult
import com.shoprunner.baleen.ValidationSuccess
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class ValidationSerializer : StdSerializer<ValidationResult>(ValidationResult::class.java) {

    override fun serialize(value: ValidationResult?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeStartObject()
        when (value) {
            is ValidationSuccess -> {
                gen?.writeObjectField("type", "Success")
                gen?.writeObjectField("context", value.context)
            }
            is ValidationError -> {
                gen?.writeObjectField("type", "Error")
                gen?.writeObjectField("context", value.context)
                gen?.writeObjectField("message", value.message)
            }
        }
        gen?.writeEndObject()


    }

}