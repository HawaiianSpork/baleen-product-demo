package com.shoprunner.baleenproductdemo.controller

import com.shoprunner.baleenproductdemo.csv.FlowableUtil
import com.shoprunner.baleenproductdemo.spec.Spec
import io.reactivex.rxkotlin.toFlowable
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import org.springframework.http.MediaType.APPLICATION_JSON

@Component
class FeedHandler {
    fun getProducts() = FlowableUtil.fromCsvWithHeader("/Users/mmaletich/git/baleen-product-demo/flipkart_com-ecommerce_sample.csv", escape='\u0000')
            .filter{
                val foo = it.data["product_category_tree"]
                when(foo) {
                    is String -> foo.contains(">> Bags >>")
                    else -> false
                }
            }


}