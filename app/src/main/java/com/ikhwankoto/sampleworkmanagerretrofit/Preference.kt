package com.ikhwankoto.sampleworkmanagerretrofit

import android.content.Context

class Preference {

    var db = "myPref"

    companion object {
        var tagCountWork = "count_one"
        var tagCountSecondWork = "count_second"
        var tagCountPeriodicWork = "count_periodic"
    }

    fun setCountValue(ctx: Context, newValue: Int, tag: String) {
        val sharedPref = ctx.getSharedPreferences(db, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt(tag, newValue)
            commit()
        }
    }

    fun getCountValue(ctx: Context, tag: String): Int {
        val sharedPref = ctx.getSharedPreferences(db, Context.MODE_PRIVATE)
        return sharedPref.getInt(tag, 0)
    }

}