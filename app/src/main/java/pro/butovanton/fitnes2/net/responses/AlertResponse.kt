package pro.butovanton.fitnes2.net.responses

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




data class AlertResponse(val code : Int,
                         val description : String)