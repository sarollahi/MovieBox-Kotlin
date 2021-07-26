package com.aastudio.sarollahi.moviebox

sealed class Resource<T>(
    val data: T? = null,
    val msg: String? = null
) {
    class Loaded<T>(data: T) : Resource<T>(data)
    class NewData<T>(data: T) : Resource<T>(data)
    class Error<T>(msg: String?, data: T? = null) : Resource<T>(data, msg)
    class Loading<T> : Resource<T>()
}