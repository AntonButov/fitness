package pro.butovanton.fitness.net

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.json.JSONException

import org.json.JSONObject
import pro.butovanton.fitnes2.InjectorUtils
import pro.butovanton.fitnes2.net.responses.AlertResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Api {

    var api = InjectorUtils.provideApi()

    fun alert() {

        api.alert(JSONPlaceHolderApi.GUID).enqueue(object : Callback<List<AlertResponse>> {
            override fun onResponse(call: Call<List<AlertResponse>>, response: Response<List<AlertResponse>>) {
                Log.d("DEBUG", "Retrofit response : " + response.toString())
            }

            override fun onFailure(call: Call<List<AlertResponse>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}


