package pro.butovanton.fitness.net

import okhttp3.ResponseBody
import org.json.JSONException

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Api {
    var networkService = NetworkService.instance
    var jsonPlaceHolderApi = networkService?.jSONApi

    fun alert() {

        jsonPlaceHolderApi?.alert(JSONPlaceHolderApi.GUID)?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
             //   TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
             //   TODO("Not yet implemented")
            }

        })
    }
}


