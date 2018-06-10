package com.shoprunner.baleenproductdemo.csv

import com.shoprunner.baleen.Data
import com.shoprunner.baleen.ValidationResult

data class DataWithValidation(val data: Data, val validationResult: ValidationResult)