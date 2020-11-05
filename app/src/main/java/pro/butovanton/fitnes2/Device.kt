package pro.butovanton.fitnes2

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.htsmart.wristband2.WristbandApplication
import com.htsmart.wristband2.WristbandManager
import com.htsmart.wristband2.bean.BatteryStatus
import com.htsmart.wristband2.bean.HealthyDataResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import pro.butovanton.fitnes2.mock.DbMock
import pro.butovanton.fitnes2.mock.User
import pro.butovanton.fitnes2.mock.UserMock
import pro.butovanton.fitnes2.utils.Logs
import java.lang.String
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Device {

    private val mWristbandManager = WristbandApplication.getWristbandManager()
    private var mErrorDisposable: Disposable? = null
    private var mTestingHealthyDisposable : Disposable? = null
    private var bataryDisposable : Disposable? = null
    var device: BluetoothDevice? = null
    private val mUser: User = UserMock.mockUser1()

    fun disConnect() {
        mErrorDisposable?.dispose()
        mWristbandManager.close()
    }

    fun connect(contex : Context) {
                device = (App).deviceState.device
                val isBind = DbMock.isUserBind(contex, device, mUser)
                mWristbandManager.connect(
                    device!!,
                    String.valueOf(mUser.getId()),
                    //!isBind,
                    true,
                    mUser.isSex(),
                    mUser.getAge(),
                    mUser.getHeight(),
                    mUser.getWeight()
                )
    }

    fun setConnect(contex: Context) {
        DbMock.setUserBind(contex, device, mUser)
    }

    fun connecterObserver()  = mWristbandManager.observerConnectionState()

    fun getHealth() {
        var healthyType = 0;
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_HEART_RATE
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_OXYGEN
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_BLOOD_PRESSURE
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_RESPIRATORY_RATE
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_TEMPERATURE
        mTestingHealthyDisposable = mWristbandManager
            .openHealthyRealTimeData(healthyType)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(
                object : Consumer<Disposable?> {
                    @Throws(Exception::class)
                    override fun accept(disposable: Disposable?) {
                        Logs.d("real_time_data_start")
                    }
                })
            .doOnTerminate(
                object : Action {
                    @Throws(Exception::class)
                    override fun run() {
                        Logs.d("real_time_data_terminate")
                    }
                })
            .doOnDispose(
                object : Action {
                    @Throws(Exception::class)
                    override fun run() {
                        Logs.d("real_time_data_dispose")
                    }
                })
            .subscribe(
                object : Consumer<HealthyDataResult> {
                    @Throws(Exception::class)
                    override fun accept(result: HealthyDataResult) {
                        Logs.d("heartRate: " + result.heartRate + "\n")
                        Logs.d("oxygen: " + result.oxygen + "\n")
                        Logs.d("diastolicPressure: " + result.diastolicPressure + "\n")
                        Logs.d("systolicPressure: " + result.systolicPressure + "\n")
                        Logs.d("respiratoryRate: " + result.respiratoryRate + "\n")
                        Logs.d("temperatureBody:" + result.temperatureBody + "\n")
                        Logs.d("temperatureWrist : " + result.temperatureWrist + "\n")
                    }
                },
                object : Consumer<Throwable?> {
                    @Throws(Exception::class)
                    override fun accept(throwable: Throwable?) {
                        Logs.d("RealTimeData throable : " + throwable)
                    }
                })
}

    suspend fun getHealthSuspend() : HealthyDataResult? {
        var lastHealth : HealthyDataResult? = null
        return suspendCoroutine { cont ->
            var healthyType = 0;
            healthyType = healthyType or WristbandManager.HEALTHY_TYPE_HEART_RATE
            healthyType = healthyType or WristbandManager.HEALTHY_TYPE_OXYGEN
            healthyType = healthyType or WristbandManager.HEALTHY_TYPE_BLOOD_PRESSURE
            healthyType = healthyType or WristbandManager.HEALTHY_TYPE_RESPIRATORY_RATE
            healthyType = healthyType or WristbandManager.HEALTHY_TYPE_TEMPERATURE
            if (mTestingHealthyDisposable?.isDisposed == false)
                   mTestingHealthyDisposable?.dispose()
            mTestingHealthyDisposable = mWristbandManager
                .openHealthyRealTimeData(healthyType)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    Logs.d("real_time_data_start disposable = " + it.toString())
                }
                .doOnTerminate {
                    Logs.d("real_time_data_terminate")
                    cont.resume(lastHealth)
                }
                .doOnDispose {
                    Logs.d("real_time_data_dispose")
                }
                .subscribe({ health ->
                    Logs.d("heartRate: " + health.heartRate +
                            ", oxygen: " + health.oxygen +
                            ", diastolicPressure: " + health.diastolicPressure +
                            ", systolicPressure: " + health.systolicPressure +
                            ", respiratoryRate: " + health.respiratoryRate +
                            ", temperatureBody:" + health.temperatureBody +
                            ", temperatureWrist : " + health.temperatureWrist)
                    lastHealth = health
                }, { er ->
                    Logs.d("exption from devace" + er)
                })
        }
    }

    suspend fun dataSHealthyingle() : HealthyDataResult {
        return suspendCoroutine { cont ->
         mWristbandManager.requestLatestHealthy().subscribe { healph, troawble ->
             cont.resume(healph)
             //cont.resumeWithException(troawble)
             }
        }
    }


    suspend fun getBatary() : BatteryStatus{
        Logs.d("betaryRequest")
        if (bataryDisposable!=null && !bataryDisposable!!.isDisposed) bataryDisposable!!.dispose()
        return suspendCoroutine { continuation ->
            bataryDisposable = mWristbandManager.requestBattery()
                .subscribe { bataryStatus, throuble ->
                        Logs.d("betaryFromDevise = " + bataryStatus.percentage)
                        throuble?.let {
                        Logs.d("betaryThrowble = " + throuble.message) }
                        continuation.resume(bataryStatus)
                        bataryDisposable?.dispose()

            }
        }
    }

    private fun errors() {
          mErrorDisposable = mWristbandManager.observerConnectionError()
                .subscribe { connectionError ->
                    Logs.d("Connect Error occur and retry:" + connectionError.isRetry)
                }
    }

    fun isConnected(): Boolean {
        return mWristbandManager.isConnected
    }

    fun stop() {
        mErrorDisposable?.dispose()
        mTestingHealthyDisposable?.dispose()
        bataryDisposable?.dispose()
    }
}

