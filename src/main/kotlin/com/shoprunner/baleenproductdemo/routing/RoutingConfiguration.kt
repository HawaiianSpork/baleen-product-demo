package com.shoprunner.baleenproductdemo.routing

import com.shoprunner.baleen.Context
import com.shoprunner.baleenproductdemo.controller.FeedHandler
import com.shoprunner.baleenproductdemo.csv.DataWithValidation
import com.shoprunner.baleenproductdemo.types.Types.productType
import io.reactivex.Flowable
import io.reactivex.rxkotlin.toFlowable
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router

@Configuration
class RoutingConfiguration {

    fun <T>paginate(req: ServerRequest, list: Flowable<T>): Flowable<T> {
        val pageOption = req.queryParam("page")
        val page = pageOption.map { Integer.parseInt(it) }.orElse(1)
        val pageSize = 100L
        return list.skip(pageSize * (page - 1)).take(pageSize)
    }

    fun validate(ctx: Context): List<DataWithValidation> {
        val validationResults = productType.validate(ctx).results

        return validationResults.map { DataWithValidation(ctx.data, it) }
    }

    @Bean
    fun routerFunction(handler: FeedHandler): RouterFunction<ServerResponse> = router {
        ("/api/products").nest {

            GET("/validations") { req ->
                val filename = req.queryParam("filename").get()
                val products = handler.getProducts(filename).`as`{ paginate(req, it) }

                val validationResults = products.flatMap { validate(it).toFlowable() }

                ok().body(validationResults)
            }
        }

        resources("/**", ClassPathResource("static/"))
    }
}

