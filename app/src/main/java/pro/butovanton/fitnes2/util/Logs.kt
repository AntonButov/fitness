package pro.butovanton.fitnes2.util

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.InjectorUtils
import pro.butovanton.fitnes2.db.blackbox.BlackBox
import pro.butovanton.fitnes2.db.blackbox.BlackboxDao
import java.util.*

final class Logs {


    companion object {

        private val daoBlack = InjectorUtils.provideDaoBlack()
        val logLive = MutableLiveData<String>()

        @JvmStatic
        fun d(message: String) {
            Log.d("DEBUG", message)
            send(message)
        }

        private fun send(message: String) {
//            logLive.value = message
            GlobalScope.launch(Dispatchers.IO) {
                daoBlack.insertLast(BlackBox(Date().time, message))
            }
        }

        fun deleteBlackBox() {
        GlobalScope.launch(Dispatchers.IO) {
            daoBlack.delete()
        }
        }

        suspend fun getBlackBox() : List<BlackBox> {
            return daoBlack.getAll()
        }
    }
}