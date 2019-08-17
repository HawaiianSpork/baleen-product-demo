package com.shoprunner.baleenproductdemo.types

import com.shoprunner.baleen.Baleen.describeAs
import com.shoprunner.baleen.types.LongType
import com.shoprunner.baleen.types.StringCoercibleToLong

val productType = "Product".describeAs {

    "retail_price".type(
        type = StringCoercibleToLong(LongType(min = 0)),
        required = true
    )
}