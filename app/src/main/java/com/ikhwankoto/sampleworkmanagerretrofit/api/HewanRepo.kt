package com.ikhwankoto.sampleworkmanagerretrofit.api

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody

class HewanRepo() {

    companion object {
        fun getListDataHewan(): Observable<HewanDao> {
            val jsonObject = JsonObject()
            jsonObject.addProperty("id", 1)
            return ApiClient.createWebService().getListDataHewan(
                RequestBody.create(
                    MediaType.parse("application/json"),
                    jsonObject.toString()
                )
            )
        }
    }

}