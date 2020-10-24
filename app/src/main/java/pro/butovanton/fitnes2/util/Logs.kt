package pro.butovanton.fitnes2.util

import android.util.Log

final class Logs {
    companion object {
        @JvmStatic
       fun d(message : String) {
           Log.d("DEBUG", message)
       }
    }

}