package pro.butovanton.fitness.net

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.json.JSONException

import org.json.JSONObject
import pro.butovanton.fitnes2.InjectorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Api {

    var api = InjectorUtils.provideApi()

    fun alert() {

        api.alert(JSONPlaceHolderApi.GUID).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}


