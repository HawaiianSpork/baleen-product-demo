package com.shoprunner.baleenproductdemo.spec

import com.shoprunner.baleen.Baleen
import com.shoprunner.baleen.types.LongType
import com.shoprunner.baleen.types.StringType


object Spec {


    val productSpec = Baleen.describe("Product") { p ->
        p.attr(name = "product_name",
                type = StringType(min = 1, max = 500),
                required = true)

        p.attr(name = "retail_price",
                type = LongType(),
                required = true)

    }
}
