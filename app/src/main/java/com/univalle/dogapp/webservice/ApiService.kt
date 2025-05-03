package com.univalle.dogapp.webservice

import com.univalle.dogapp.model.ProductModelResponse
import com.univalle.dogapp.utils.Constants.END_POINT
import retrofit2.http.GET

interface ApiService {
    @GET(END_POINT)
    suspend fun getProducts(): MutableList<ProductModelResponse>
}