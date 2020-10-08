package pro.butovanton.fitness.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pro.butovanton.fitness.net.JSONPlaceHolderApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService private constructor() {
    private val mRetrofit: Retrofit
    val jSONApi: JSONPlaceHolderApi
        get() = mRetrofit.create(JSONPlaceHolderApi::class.java)

    companion object {
        private var mInstance: NetworkService? = null
        private const val BASE_URL = "https://212.248.50.40:8080/v1/"
        val instance: NetworkService?
            get() {
                if (mInstance == null) {
                    mInstance = NetworkService()
                }
                return mInstance
            }
    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
        mRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
    }
}