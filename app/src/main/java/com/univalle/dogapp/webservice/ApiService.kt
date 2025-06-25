package com.univalle.dogapp.webservice

import com.univalle.dogapp.model.BreedsResponse
import com.univalle.dogapp.model.BreedImageResponse
import com.univalle.dogapp.utils.Constants.END_POINT_BREEDS
import com.univalle.dogapp.utils.Constants.END_POINT_IMAGES
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET(END_POINT_BREEDS)
    suspend fun getBreeds(): BreedsResponse

    @GET(END_POINT_IMAGES)
    suspend fun getBreedImage(@Path("breed") breed: String): BreedImageResponse

}