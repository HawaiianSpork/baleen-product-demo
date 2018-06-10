package com.shoprunner.baleenproductdemo

import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.FileReader
import java.io.FileWriter

val filename = "/Users/mmaletich/git/baleen-product-demo/flipkart_com-ecommerce_sample.csv"

val outFilename = "/Users/mmaletich/git/baleen-product-demo/presentation_samples/purses.csv"
val imageColumn = 8

val imageUrls = mutableListOf<String>()

CSVWriter(FileWriter(outFilename)).use { writer ->
    CSVReader(FileReader(filename), ',', '"','\u0000').use{ reader ->
        writer.writeNext(reader.readNext())
        reader.filter { line ->
            line[4].contains(">> Bags >>")
        }.withIndex().forEach { (index, row) ->
            imageUrls.add(row[imageColumn])
            row[imageColumn] = "images/$index.jpg"
            writer.writeNext(row)
        }
    }
}