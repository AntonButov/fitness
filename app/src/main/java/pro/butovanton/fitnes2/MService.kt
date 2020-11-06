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
import pro.butovanton.fitnes2.net.retrofitDataClass.AlertResponse
import pro.butovanton.fitnes2.utils.AndPermissionHelper.Utils
import pro.butovanton.fitnes2.utils.Logs

class MService : Service() {

    var reportToModel : ReportToModel? = null
        set(value) { field = value }
    private val mBinder: IBinder = LocalBinder()
    lateinit var jobAllert : Job
    var jobBase : Job? = null
    var jobShowAlerts : Job? = null
    lateinit var jobSendData : Job
    var timeOutOnSendData = 5L

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
        super.onCreate()

        jobAllert = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                if ((App).deviceState.isBind()) {
                    val allerts = serverAvial()
                    if (allerts != null) {
                        Logs.d("Количество allert = " + allerts.size)
                        if (allerts.size > 0) {
                            showAllert(allerts)
                        }
                    }
                    outServerAvial(allerts != null)
                }
                delay(60000)
            }
        }

        mStateDisposable = deviceClass.connecterObserver()
            .subscribe { connectionState ->
                Logs.d("connected state " + connectionState.toString())
                reportToModel?.deviceAvial(connectionState)
                deviceStateCash = connectionState
                when (connectionState) {
                    ConnectionState.CONNECTED -> {
                        deviceClass.setConnect(this)
                        jobBase = baseJob()
                    }
                    ConnectionState.DISCONNECTED -> jobBase?.cancel()
                }
            }

        jobSendData = GlobalScope.launch(Dispatchers.IO) {
            val convertor = Convertor()
            while (true) {
                    while (dao.getLastData() != null) {
                        val data = dao.getLastData()
                        if (data?.device.equals("")) dao.deleteLast()
                        else {
                        Logs.d("Начало отправки данных на сервер.")
                        val responseTimeOut = api.postDetail(convertor.toRetrofit(data!!))
                        if (responseTimeOut != 99L) {
                            Logs.d("Данные отправлены.")
                            outServerAvial(true)
                            timeOutOnSendData = responseTimeOut
                            dao.deleteLast()
                        }
                        else {
                            outServerAvial(false)
                            Logs.d("Отправить не удалось.")
                            delay(10000)
                        }

                    }}
                    delay(timeOutOnSendData * 60000)
            }
        }

        startForeground(101, updateNotification(baseContext))
    }

    fun baseJob(): Job {
        return GlobalScope.launch(Dispatchers.IO) {
            while (true) {
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
            delay(timeOutOnSendData * 60000)
            }
        }
    }

    suspend private fun bateryGet() {
        val batary = deviceClass.getBatary()
        Logs.d("Batary = " + batary.percentage)
        reportToModel?.batary(batary.percentage)
        bataryCash = batary.percentage
    }

    private suspend fun serverAvial(): List<AlertResponse>? {
        val allerts = api.alert("00000000-0000-0000-0000-" + Utils.del2dot(App.deviceState.device?.address!!))
    return allerts
    }

    private fun outServerAvial(serverAvial: Boolean) {
        serverAvialCash = serverAvial
        reportToModel?.serverAvial(serverAvial)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val state = App.deviceState
        when (state.shotDown) {
            true -> {
                deviceClass.disConnect()
                App.deviceState.shotDown = false
            }
            false -> {
                if (state.isBind()) {
                    if (!deviceClass.isConnected()) {
                        deviceClass.connect(this)
                    }
                } else
                    if (deviceClass.isConnected()) {
                        deviceClass.disConnect()
                    }
            }
        }

        return START_STICKY
    }

    fun disconnect() {
        deviceClass.disConnect()
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
        Logs.d("Service unBind.")
        return super.onUnbind(intent)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Logs.d("Task removed.")
    }

    override fun onDestroy() {
        super.onDestroy()
        jobAllert.cancel()
        jobBase?.cancel()
        jobShowAlerts?.cancel()
        deviceClass.stop()
        Logs.d("Service destroy.")
    }

     suspend fun showAllert(allerts: List<AlertResponse>) {
         App.deviceState.allerts = allerts
         GlobalScope.launch {
             startActivity(Intent(this@MService,AllertActivity::class.java))
             Logs.d("Показ аллертов 1")
             delay(6000)
             Logs.d("Показ аллертов 2")
             delay(6000)
             Logs.d("Показ аллертов 3")
             delay(6000)
         }
     }
}