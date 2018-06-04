package com.shoprunner.baleenproductdemo

import com.shoprunner.baleen.dataTrace
import com.shoprunner.baleenproductdemo.csv.FlowableUtil
import java.io.FileReader

val filename = "/Users/mmaletich/git/baleen-product-demo/flipkart_com-ecommerce_sample.csv"


val feed = FlowableUtil.fromCsvWithHeader(dataTrace(filename),{ FileReader(filename) }, escape='\u0000')
//
println(feed.firstOrError().blockingGet())


//val csv = CSVReader(FileReader(filename), ',', '"', '\u0000')
//
//println(csv.readNext().joinToString())
//println(csv.readNext().joinToString())