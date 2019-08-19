package com.shoprunner.baleenproductdemo.types

import com.shoprunner.baleen.Baleen.describeAs
import com.shoprunner.baleenproductdemo.csv.getDataFromCsv
import com.shoprunner.baleenproductdemo.csv.validateWith


val productDescription = "Product".describeAs {
    // TODO add some tests
}

fun getProductsWithValidationResults(fileName: String) =
    getDataFromCsv(fileName).validateWith(productDescription)
