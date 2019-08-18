package com.shoprunner.baleenproductdemo.csv

import com.shoprunner.baleen.Context
import com.shoprunner.baleen.DataDescription
import com.shoprunner.baleen.csv.FlowableUtil
import com.shoprunner.baleen.dataTrace
import io.reactivex.Flowable
import io.reactivex.rxkotlin.toFlowable
import java.io.FileReader

fun getDataFromCsv(fileName: String) = FlowableUtil.fromCsvWithHeader(
    readerSupplier = { FileReader(fileName) },
    escape = '\u0000',
    dataTrace = dataTrace(fileName))

fun DataDescription.validateWithData(ctx: Context): Iterable<DataWithValidation> =
    this.validate(ctx).results.map { DataWithValidation(ctx.data, it) }

fun Flowable<Context>.validateWith(dataDescription: DataDescription) : Flowable<Flowable<DataWithValidation>> =
    this.map { dataDescription.validateWithData(it).toFlowable() }