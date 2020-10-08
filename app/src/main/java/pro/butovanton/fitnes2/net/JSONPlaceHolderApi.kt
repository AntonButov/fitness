package pro.butovanton.fitness.net

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface JSONPlaceHolderApi {
    companion object {
        const val GUID = "00000000-0000-0000-0000-eab1d1c37ab2"
    }

    @GET("/v1/alerts/{GUID}")
    fun alert(@Path ("GUID") guid: String) : Call<ResponseBody>

    @POST("/v3/lock/resetKey")
    @FormUrlEncoded
    fun restEkey(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("lockId") lockId: Int, @Field("date") date: Long): Call<ResponseBody?>?

    @POST("/v3/lock/delete")
    @FormUrlEncoded
    fun deleteLock(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("lockId") lockId: Int, @Field("date") date: Long): Call<ResponseBody?>?

    @POST("/v3/lock/resetKeyboardPwd")
    @FormUrlEncoded
    fun resetPasscode(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("lockId") lockId: Int, @Field("pwdInfo") pwdInfo: String?, @Field("timestamp") timestamp: Long, @Field("date") date: Long): Call<ResponseBody?>?

    @POST("/v3/lock/updateLockData")
    @FormUrlEncoded
    fun updateLockData(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("lockId") lockId: Int, @Field("lockData") lockData: String?, @Field("date") date: Long): Call<ResponseBody?>?

    @POST("/v3/lock/changeAdminKeyboardPwd")
    @FormUrlEncoded
    fun changeAdminPasscode(@FieldMap params: Map<String?, String?>?): Call<ResponseBody?>?

    @POST("/v3/fingerprint/add")
    @FormUrlEncoded
    fun addFingerprint(@FieldMap params: Map<String?, String?>?): Call<ResponseBody?>?

    @GET("/v3/fingerprint/list")
    fun getUserFingerprintList(@QueryMap params: Map<String?, String?>?): Call<ResponseBody?>?

    @POST("/v3/fingerprint/delete")
    @FormUrlEncoded
    fun deleteFingerprint(@FieldMap params: Map<String?, String?>?): Call<ResponseBody?>?

    @POST("/v3/fingerprint/clear")
    @FormUrlEncoded
    fun clearFingerprints(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("lockId") lockId: Int, @Field("date") date: Long): Call<ResponseBody?>?

    @POST("/v3/identityCard/add")
    @FormUrlEncoded
    fun addICCard(@FieldMap params: Map<String?, String?>?): Call<ResponseBody?>?

    @GET("/v3/identityCard/list")
    fun getUserICCardList(@QueryMap params: Map<String?, String?>?): Call<ResponseBody?>?

    @POST("/v3/identityCard/delete")
    @FormUrlEncoded
    fun deleteICCard(@FieldMap params: Map<String?, String?>?): Call<ResponseBody?>?

    @POST("/v3/identityCard/changePeriod ")
    @FormUrlEncoded
    fun modifyICCardPeriod(@FieldMap params: Map<String?, String?>?): Call<ResponseBody?>?

    @POST("/v3/identityCard/clear")
    @FormUrlEncoded
    fun clearICCards(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("lockId") lockId: Int, @Field("date") date: Long): Call<ResponseBody?>?

    @POST("/v3/gateway/upgradeCheck")
    @FormUrlEncoded
    fun gatewayUpgradeCheck(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("gatewayId") gatewayId: Int, @Field("date") date: Long): Call<String?>?

    @POST("/v3/gateway/isInitSuccess")
    @FormUrlEncoded
    fun gatewayIsInitSuccess(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("gatewayNetMac") gatewayNetMac: String?, @Field("date") date: Long): Call<String?>?

    @POST("/v3/gateway/uploadDetail")
    @FormUrlEncoded
    fun uploadGatewayDetail(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("gatewayId") gatewayId: Int, @Field("modelNum") modelNum: String?, @Field("hardwareRevision") hardwareRevision: String?, @Field("firmwareRevision") firmwareRevision: String?, @Field("networkName") networkName: String?, @Field("date") date: Long): Call<String?>?

    @POST("/v3/gateway/list")
    @FormUrlEncoded
    fun getGatewayList(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("pageNo") pageNo: Int, @Field("pageSize") pageSize: Int, @Field("date") date: Long): Call<String?>?

    @POST("/v3/lock/upgradeCheck")
    @FormUrlEncoded
    fun lockUpgradeCheck(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("lockId") lockId: Int, @Field("date") date: Long): Call<String?>?

    @POST("/v3/lock/upgradeRecheck")
    @FormUrlEncoded
    fun lockUpgradeCheckAgain(@Field("clientId") clientId: String?, @Field("accessToken") accessToken: String?, @Field("firmwareInfo") firmwareInfo: String?, @Field("lockId") lockId: Int, @Field("date") date: Long): Call<String?>?

    @POST("/v3/wirelessKeyboard/add ")
    @FormUrlEncoded
    fun addWirelessKeypad(@FieldMap params: Map<String?, String?>?): Call<ResponseBody?>?

    @POST("/v3/user/register")
    @FormUrlEncoded
    fun registerUser(@Field("clientId") clientId: String?, @Field("clientSecret") clientSecret: String?, @Field("username") username: String?, @Field("password") password: String?, @Field("date") date: Long): Call<ResponseBody?>?

    @POST("/v3/user/list")
    @FormUrlEncoded
    fun getUserList(@Field("clientId") clientId: String?, @Field("clientSecret") clientSecret: String?, @Field("startDate") startDate: Long?, @Field("endDate") endDate: Long?, @Field("pageNo") pageNo: Int, @Field("pageSize") pageSize: Int, @Field("date") date: Long): Call<ResponseBody?>?

}