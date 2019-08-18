package com.shoprunner.baleenproductdemo.routing

import com.shoprunner.baleenproductdemo.types.getProductsWithValidationResults
import io.reactivex.Flowable
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

    fun <T>page(req: ServerRequest, list: Flowable<T>): Flowable<T> {
        val pageOption = req.queryParam("page")
        val page = pageOption.map { Integer.parseInt(it) }.orElse(1)
        return list.page(page)
    }

    fun <T>Flowable<T>.page(pageNumber: Int): Flowable<T> {
        val pageSize = 100L
        return this.skip(pageSize * (pageNumber - 1)).take(pageSize)
    }

    @Bean
    fun routerFunction(): RouterFunction<ServerResponse> = router {
        ("/api/products").nest {

            GET("/validations") { req ->
                val filename = req.queryParam("filename").get()
                val validationResults = getProductsWithValidationResults(filename)
                    .`as`{ page(req, it) }
                    .flatMap { it }

                ok().body(validationResults)
            }
        }

        resources("/**", ClassPathResource("static/"))
    }
}

