package com.shoprunner.baleenproductdemo.types

import com.shoprunner.baleen.Baleen.describeAs
import com.shoprunner.baleen.types.LongType
import com.shoprunner.baleen.types.StringCoercibleToLong
import com.shoprunner.baleenproductdemo.csv.getDataFromCsv
import com.shoprunner.baleenproductdemo.csv.validateWith

fun getProductsWithValidationResults(fileName: String) =
    getDataFromCsv(fileName).validateWith(productType)

val productType = "Product".describeAs {

    "retail_price".type(
        type = StringCoercibleToLong(LongType()),
        required = true
    )
}