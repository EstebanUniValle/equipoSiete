package com.univalle.dogapp.model

data class BreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)