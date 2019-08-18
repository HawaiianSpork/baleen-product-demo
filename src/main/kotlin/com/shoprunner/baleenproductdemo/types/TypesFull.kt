package com.shoprunner.baleenproductdemo.types

import com.shoprunner.baleen.Baleen.describeAs
import com.shoprunner.baleen.ValidationError
import com.shoprunner.baleen.types.LongType
import com.shoprunner.baleen.types.StringCoercibleToLong
import com.shoprunner.baleen.types.StringType

private val departments = listOf("children")

val productTypeFull = "Product".describeAs {

    "uniq_id".type(
        type = StringType(min = 1, max = 500),
        required = true)

    "product_name".type(
        type = StringType(min = 1, max = 500),
        required = true)

    "retail_price".type(
        type = StringCoercibleToLong(LongType(min = 0)),
        required = true)

    "department".type(
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
