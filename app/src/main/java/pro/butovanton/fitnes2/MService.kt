package pro.butovanton.fitnes2

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.htsmart.wristband2.bean.ConnectionState
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import pro.butovanton.fitnes2.db.detail.DataClass
import pro.butovanton.fitnes2.net.Convertor
import pro.butovanton.fitnes2.util.Logs
import pro.butovanton.fitnes2.util.Utils

class MService : Service() {

    var reportToModel : ReportToModel? = null
        set(value) { field = value }
    private val mBinder: IBinder = LocalBinder()
    lateinit var job : Job
    lateinit var jobBase : Job
    lateinit var jobSendData : Job

    lateinit var mStateDisposable : Disposable

    var serverAvialCash : Boolean? = null
    var bataryCash : Int? = null
    var deviceStateCash : ConnectionState? = null
    val data = DataClass()

    inner class LocalBinder : Binder() {
        val service: MService
            get() = this@MService
    }

    private val api = InjectorUtils.provideApi()
    private val deviceClass = InjectorUtils.provideDevice()
    private val locationClass = InjectorUtils.provideLocation()
    private val dao = InjectorUtils.provideDao()

    override fun onCreate() {
        super.onCreate() /*
        job = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                if ((App).deviceState.isBind()) {
                    val serverAvial = serverAvial()
                    Logs.d("ServerAvable from service = " + serverAvial)
                    outServerAvial(serverAvial)
                }
                delay(60000)
            }
        }

*/
        mStateDisposable = deviceClass.connecterObserver()
            .subscribe { connectionState ->
                Logs.d("connected state " + connectionState.toString())
                reportToModel?.deviceAvial(connectionState)
                deviceStateCash = connectionState
            }

        jobBase = GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                if (deviceClass.isConnected()) {
                    bateryGet()
                    Logs.d("Получение локации.")
                    val location = locationClass.getLocation()
                    Logs.d("Location = " + location)
                    data.add(location)
                    Logs.d("Начало получения даных с часов.")
                    val health = deviceClass.getHealthSuspend()
                    health?.let {
                                //analise this
                                data.add(it)
                                dao.insertLast(data.getMOdelToRoom())
                                Logs.d("Данные записаны в БД " + it.toString())
                         }
                    delay(12000)
                } else
                    delay(20000)

            }
        }

        jobSendData = GlobalScope.launch(Dispatchers.IO) {
            val convertor = Convertor()
           while (true) {
                    val data = dao.getLastData()
                    if (data != null) {
                        if (data.device.equals("")) dao.deleteLast()
                        else {
                        Logs.d("Начало отправки данных на сервер.")
                        if (api.postDetail(convertor.toRetrofit(data))) {
                            Logs.d("Данные отправлены.")
                            dao.deleteLast()
                        }
                        else
                            Logs.d("Отправить не удалось.")

                    }}
                    if ((App).deviceState.isBind()) {
                           val serverAvial = serverAvial()
                           Logs.d("ServerAvable from service = " + serverAvial)
                           outServerAvial(serverAvial)
                    }
                    delay(60000)
            }
        }

        startForeground(101, updateNotification(baseContext))
    }

    suspend private fun bateryGet() {
        val batary = deviceClass.getBatary()
        Logs.d("Batary = " + batary.percentage)
        reportToModel?.batary(batary.percentage)
        bataryCash = batary.percentage
    }

    private suspend fun serverAvial(): Boolean {
        try {
            val response = api.alert("00000000-0000-0000-0000-" + Utils.del2dot(App.deviceState.device?.address!!))
            return true
        }
        catch (t: Throwable) {
            return false
        }
    }

    private fun outServerAvial(serverAvial: Boolean) {
        serverAvialCash = serverAvial
        reportToModel?.serverAvial(serverAvial)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if ((App).deviceState.isBind()) {
            if (!deviceClass.isConnected()) {
                    deviceClass.connect()
            }
        }
        else
            if (deviceClass.isConnected()) {
                deviceClass.disConnect()
            }

        return START_STICKY
    }

private fun updateNotification(context: Context): Notification? {
    val info = context.resources.getString(R.string.fitness_run)
    val action = PendingIntent.getActivity(context,
        0, Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_CANCEL_CURRENT) // Flag indicating that if the described PendingIntent already exists, the current one should be canceled before generating a new one.
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val builder: NotificationCompat.Builder
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val CHANNEL_ID = "Fitness_channel"
        val channel = NotificationChannel(CHANNEL_ID, "Fitness",
            NotificationManager.IMPORTANCE_HIGH)
        channel.description = "Fitness channel description"
        manager.createNotificationChannel(channel)
        builder = NotificationCompat.Builder(context, CHANNEL_ID)
    } else builder = NotificationCompat.Builder(context)
    return builder.setContentIntent(action)
        .setContentTitle(info)
        .setTicker(info)
        .setSmallIcon(R.drawable.ic_fitness_icon)
        .setContentIntent(action)
        .setOngoing(true).build()
}

    override fun onBind(intent: Intent): IBinder? {
        Logs.d("Service bind.")
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
        Logs.d("Service unBind.")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Logs.d("Task removed.")
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        deviceClass.stop()
        Logs.d("Service destroy.")
    }
}