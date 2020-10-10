package pro.butovanton.fitnes2

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*
import pro.butovanton.fitness.net.JSONPlaceHolderApi

class MService : Service() {

    var reportToModel : ReportToModel? = null
    private val mBinder: IBinder = LocalBinder()
    lateinit var job : Job

    var serverAvialCash : Boolean? = null

    inner class LocalBinder : Binder() {
        val service: MService
            get() = this@MService
    }

    val api = InjectorUtils.provideApi()

    override fun onCreate() {
        super.onCreate()
        job = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                val serverAvial = serverAvial()
                Log.d("DEBUG", "ServerAvable from service : " + serverAvial)
            outServerAvial(serverAvial)
            delay(60000)
            }
        }
    }

    private suspend fun serverAvial(): Boolean {
        try {
            val response = api.alert(JSONPlaceHolderApi.GUID)
            return true
        }
        catch (t: Throwable) {
            return false
        }
    }

    private fun outServerAvial(serverAvial : Boolean) {
        serverAvialCash = serverAvial
        reportToModel?.serverAvial(serverAvial)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DEBUG", "Service startComand.")

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d("DEBUG", "Service bind.")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
        Log.d("DEBUG", "Service unBind.")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d("DEBUG", "Task removed.")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.d("DEBUG", "Service destroy.")
    }
}