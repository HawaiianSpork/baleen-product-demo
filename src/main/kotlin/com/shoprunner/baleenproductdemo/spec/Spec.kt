package com.shoprunner.baleenproductdemo.spec

import com.shoprunner.baleen.Baleen
import com.shoprunner.baleen.types.LongType
import com.shoprunner.baleen.types.StringType
import com.shoprunner.baleenproductdemo.type.StringCoercableToLong


object Spec {


    val productSpec = Baleen.describe("Product") { p ->
        p.attr(name = "uniq_id",
                type = StringType(min = 1, max = 500),
                required = true)

        p.attr(name = "product_name",
                type = StringType(min = 1, max = 500),
                required = true)

        p.attr(name = "retail_price",
                type = StringCoercableToLong(LongType(min = 0)),
                required = true)

        p.attr(name = "brand",
                type = StringType(min = 1, max = 500),
                required = true)

    }
}
