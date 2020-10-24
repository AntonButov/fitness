package pro.butovanton.fitnes2

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.htsmart.wristband2.WristbandApplication
import com.htsmart.wristband2.WristbandManager
import com.htsmart.wristband2.bean.ConnectionState
import com.htsmart.wristband2.bean.HealthyDataResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import pro.butovanton.fitnes2.mock.User
import pro.butovanton.fitnes2.mock.UserMock
import java.lang.String

class Device {

    private val mWristbandManager = WristbandApplication.getWristbandManager()
    private var mStateDisposable: Disposable? = null
    private var mErrorDisposable: Disposable? = null
    var device: BluetoothDevice? = null
    private val mUser: User = UserMock.mockUser1()

    private lateinit var context: Context
    private var connectState = ConnectionState.DISCONNECTED

    fun disConnect() {
        mStateDisposable?.dispose()
        mErrorDisposable?.dispose()
        mWristbandManager.close()
    }

    private fun connectDevice() {
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


    fun connect() {
        connectDevice()
        connecterObserver()
        errors()
    }

    fun connecterObserver() {
           mStateDisposable = mWristbandManager.observerConnectionState()
                .subscribe { connectionState ->
                    Log.d("DEBUG", "connected state " + connectionState.toString())
                    if (connectionState == ConnectionState.CONNECTED) {
                    }
                    if (connectionState == ConnectionState.DISCONNECTED) {
                }
        }
    }

    fun healthDataObserver() {
        var healthyType = 0;
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_HEART_RATE
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_OXYGEN
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_BLOOD_PRESSURE
        healthyType = healthyType or WristbandManager.HEALTHY_TYPE_RESPIRATORY_RATE
        val mTestingHealthyDisposable = mWristbandManager
            .openHealthyRealTimeData(healthyType)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(
                object : Consumer<Disposable?> {
                    @Throws(Exception::class)
                    override fun accept(disposable: Disposable?) {
                        mBtnTestHealthy.setText(R.string.real_time_data_stop)
                    }
                })
            .doOnTerminate(
                object : Action {
                    @Throws(Exception::class)
                    override fun run() {
                        mBtnTestHealthy.setText(R.string.real_time_data_start)
                    }
                })
            .doOnDispose(
                object : Action {
                    @Throws(Exception::class)
                    override fun run() {
                        mBtnTestHealthy.setText(R.string.real_time_data_start)
                    }
                })
            .subscribe(
                object : Consumer<HealthyDataResult> {
                    @Throws(Exception::class)
                    override fun accept(result: HealthyDataResult) {
                        mTvHeartRate.setText(getString(R.string.heart_rate_value, result.heartRate))
                        mTvOxygen.setText(getString(R.string.oxygen_value, result.oxygen))
                        mTvBloodPressure.setText(
                            getString(
                                R.string.blood_pressure_value,
                                result.diastolicPressure,
                                result.systolicPressure
                            )
                        )
                        mTvRespiratoryRate.setText(
                            getString(
                                R.string.respiratory_rate_value,
                                result.respiratoryRate
                            )
                        )
                        Log.w("RealTimeData", "Heart rate = " + result.heartRate)
                    }
                },
                object : Consumer<Throwable?> {
                    @Throws(Exception::class)
                    override fun accept(throwable: Throwable?) {
                        Log.w("RealTimeData", "RealTimeData", throwable)
                    }
                })
}

    private fun errors() {
          mErrorDisposable = mWristbandManager.observerConnectionError()
                .subscribe { connectionError ->
                    Log.d(
                        "DEBUG",
                        "Connect Error occur and retry:" + connectionError.isRetry,
                        connectionError.throwable
                    )
                }
    }

    fun isConnected(): Boolean {
        return mWristbandManager.isConnected
    }

    fun stop() {
        mErrorDisposable?.dispose()
        mStateDisposable?.dispose()
    }
}

