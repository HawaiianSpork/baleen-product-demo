package com.shoprunner.baleenproductdemo

import com.shoprunner.baleen.DataTrace
import com.shoprunner.baleen.ValidationError
import com.shoprunner.baleen.ValidationSuccess
import com.shoprunner.baleen.dataTrace

//val filename = "/Users/mmaletich/git/baleen-product-demo/flipkart_com-ecommerce_sample.csv"
//
//
//val feed = FlowableUtil.fromCsvWithHeader(dataTrace(filename),{ FileReader(filename) }, escape='\u0000')
//println(feed.firstOrError().blockingGet())

// ExampleTest

val dataTrace = dataTrace("In example program")

fun testGreaterThan4(dt: DataTrace, value: Int) =
    when {
        value > 4 -> ValidationSuccess(dt, value)
        else -> ValidationError(dt, "Did not validate", value)
    }

println(testGreaterThan4(dataTrace, 5))


// Function Compisition

fun testStringGreaterThan4(dt: DataTrace, value: String) =
    when {
        value.toIntOrNull() != null ->
            when {
                value.toInt() > 4 -> ValidationSuccess(dt, value)
                else -> ValidationError(dt, "Did not validate", value)
            }
        else -> ValidationError(dt, "Was not an integer", value)
    }

println(testStringGreaterThan4(dataTrace, "5"))


//fun stringCoercibleToInt(fn: (DataTrace, Int) -> ValidationResult) =
//    fun(dt: DataTrace, value: String): ValidationResult {
//        when {
//            value.toIntOrNull() != null -> fn(dt, value.toInt())
//            else -> ValidationError(dt, "Was not an integer", value)
//        }
//    }


//val testStringGreaterThan4 = stringCoercibleToInt(:testGreaterThan4)
//
//testStringGreaterThan4(dataTrace,"4")