package pro.butovanton.fitnes2.util

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import pro.butovanton.fitnes2.App

final class Logs {
    companion object {

        val logLive = MutableLiveData<String>()

        @JvmStatic
        fun d(message: String) {
            Log.d("DEBUG", message)
            send(message)
        }

        private fun send(message: String) {
        logLive.value = message
        }
    }
}