package com.shoprunner.baleenproductdemo.types

import com.shoprunner.baleen.Baleen
import com.shoprunner.baleen.ValidationError
import com.shoprunner.baleen.types.StringType

object TypesFull {


    val productType = Baleen.describe("Product") { p ->
        p.attr(name = "uniq_id",
                type = StringType(min = 1, max = 500),
                required = true)

        p.attr(name = "product_name",
                type = StringType(min = 1, max = 500),
                required = true)

//        p.attr(name = "retail_price",
//                type = StringCoercibleToLong(LongType(min = 0)),
//                required = true)

        val departments = listOf("children")

        p.attr(name = "department",
               type = StringType(min = 0, max = 100))

         .describe { attr ->
                    attr.test { dataTrace, value ->
                        val department = value["department"]
                        if (department != null && !departments.contains(department)) {
                            sequenceOf(ValidationError(dataTrace, "Department ($department) is not a valid value.", value))
                        } else {
                            emptySequence()
                        }
                    }
                }

    }
}
