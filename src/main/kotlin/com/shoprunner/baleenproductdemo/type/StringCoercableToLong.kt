package com.shoprunner.baleenproductdemo.type

import com.shoprunner.baleen.BaleenType
import com.shoprunner.baleen.Context
import com.shoprunner.baleen.ValidationError
import com.shoprunner.baleen.ValidationResult
import com.shoprunner.baleen.types.LongType


class StringCoercableToLong(val longType: LongType) : BaleenType{
    override fun name() = "string coercable to ${longType.name()}"

    override fun validate(ctx: Context, value: Any?): Iterable<ValidationResult> =
            when (value) {
                null -> longType.validate(ctx, value)
                !is String -> listOf(ValidationError(ctx, "is not a string"))
                else -> {
                    val long = value.toLongOrNull()
                    if (long == null) {
                        listOf(ValidationError(ctx, "could not be parsed to long."))
                    } else {
                        longType.validate(ctx, long)
                    }
                }
            }


}