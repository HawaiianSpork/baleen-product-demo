package com.shoprunner.baleenproductdemo.controller

import com.shoprunner.baleen.csv.FlowableUtil
import com.shoprunner.baleen.dataTrace
import org.springframework.stereotype.Component
import java.io.FileReader

@Component
class FeedHandler {
    fun getProducts(fileName: String) = FlowableUtil.fromCsvWithHeader(dataTrace(fileName), { FileReader(fileName) }, escape='\u0000')
}