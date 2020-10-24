package pro.butovanton.fitnes2

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.htsmart.wristband2.WristbandApplication
import com.htsmart.wristband2.WristbandManager
import com.htsmart.wristband2.bean.BatteryStatus
import com.htsmart.wristband2.bean.ConnectionState
import com.htsmart.wristband2.bean.HealthyDataResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.coroutines.delay
import pro.butovanton.fitnes2.mock.User
import pro.butovanton.fitnes2.mock.UserMock
import pro.butovanton.fitnes2.util.Logs
import java.lang.String
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
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

    fun connect() {
                device = (App).deviceState.device
                mWristbandManager.connect(
                    device!!,
                    String.valueOf(mUser.getId()),
                    true,
                    mUser.isSex(),
                    mUser.getAge(),
                    mUser.getHeight(),
                    mUser.getWeight()
                )
    }

    fun connecterObserver()  = mWristbandManager.observerConnectionState()

    fun data() {
        var healthyType = 0;
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_HEART_RATE
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_OXYGEN
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_BLOOD_PRESSURE
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_RESPIRATORY_RATE
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
                        Logs.d("heartRate: " + result.heartRate +  "\n")
                        Logs.d("oxygen: " + result.oxygen +  "\n")
                        Logs.d("diastolicPressure: " + result.diastolicPressure +  "\n")
                        Logs.d("systolicPressure: " + result.systolicPressure +  "\n")
                        Logs.d("respiratoryRate: " + result.respiratoryRate +  "\n")
                    }
                },
                object : Consumer<Throwable?> {
                    @Throws(Exception::class)
                    override fun accept(throwable: Throwable?) {
                        Logs.d("RealTimeData throable : " + throwable)
                    }
                })
}
    suspend fun getBatary() : BatteryStatus{
        return suspendCoroutine {continuation ->
            bataryDisposable = mWristbandManager.requestBattery().subscribe { bataryStatus, throwable ->
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

