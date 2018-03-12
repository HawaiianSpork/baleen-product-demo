package com.shoprunner.baleenproductdemo.csv

import com.opencsv.CSVReader
import com.shoprunner.baleen.Context
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import java.io.FileReader
import com.shoprunner.baleen.DataTrace
import com.shoprunner.baleen.Data
import io.reactivex.rxkotlin.toFlowable

object FlowableUtil {

    class CsvData(val headMap: Map<String, Int>,
                  val row: Array<String>,
                  override val keys: Set<String>) : Data {

        override fun containsKey(key: String): Boolean = keys.contains(key)

        override fun get(key: String): Any? {
            val index = headMap[key] ?: return null
            if (row.size <= index) return null
            return row[index]
        }
    }

    fun fromCsvWithHeader(fileName: String, delimiter: Char = ','): Flowable<Context> {
        val readerFactory = { CSVReader(FileReader(fileName), delimiter) }

        val rows = Flowable.using(readerFactory, { Flowable.fromIterable(it) }, { it.close() })
        val head = rows.take(1).cache()
        val rest = rows.skip(1)

        val headSet = head.map { it.toSet() }.cache()
        val headMap = head.map { mapOf(*(it.withIndex().map { Pair(it.value, it.index) }.toTypedArray())) }.cache()

        val trace: DataTrace = listOf(fileName)

        val lineNumbers = IntRange(2, Int.MAX_VALUE)

        val dataFlow = Flowables.zip(headMap.repeat(), rest, headSet.repeat(), ::CsvData)
        return Flowables.zip(dataFlow, lineNumbers.toFlowable(), { data, lineNumber -> Context(data, trace + "line $lineNumber") })
    }
}