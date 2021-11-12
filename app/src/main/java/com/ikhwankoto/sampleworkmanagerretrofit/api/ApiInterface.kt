package com.ikhwankoto.sampleworkmanagerretrofit.api

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST("_dev/sample_api_hewan.php")
    fun getListDataHewan(
        @Body body: RequestBody
    ): Observable<HewanDao>

    @POST("_dev/sample_api_hewan.php")
    fun getListDataHewan2(): Call<HewanDao>

    @POST("_dev/sample_api_hewan.php")
    suspend fun getListDataHewan3(): HewanDao?

    @POST("_dev/sample_api_hewan.php")
    fun getListDataHewan4(): Single<HewanDao>

}