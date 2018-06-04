package com.shoprunner.baleenproductdemo.controller

import com.shoprunner.baleen.dataTrace
import com.shoprunner.baleenproductdemo.csv.FlowableUtil
import org.springframework.stereotype.Component
import java.io.FileReader

@Component
class FeedHandler {
    fun getProducts(fileName: String) = FlowableUtil.fromCsvWithHeader(dataTrace(fileName), { FileReader(fileName) }, escape='\u0000')
            .filter{
                val foo = it.data["product_category_tree"]
                when(foo) {
                    is String -> foo.contains(">> Bags >>")
                    else -> false
                }
            }


}