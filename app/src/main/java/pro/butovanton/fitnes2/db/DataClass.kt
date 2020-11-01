package pro.butovanton.fitnes2.db

import android.location.Location
import com.htsmart.wristband2.bean.HealthyDataResult
import okhttp3.internal.notify
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.Device
import pro.butovanton.fitnes2.net.retrofitDataClass.Detail
import java.util.*

class DataClass () {

    private val created = 0
    private var device = setupDevise()

    private var location: Location? = null
    private lateinit var health: HealthyDataResult

    fun setupDevise(): String {
        return when (App.deviceState.device) {
            null -> ""
            else -> App.deviceState.device!!.address
        }
    }

    fun add(location: Location?) {
        this.location = location
    }

    fun add(health: HealthyDataResult) {
        this.health = health
    }

    fun getMOdelToRoom(): Data {
        if (device == null) device = ""
        return Data(
            created = Date().time,
            heatRate = health.heartRate,
            device = device,
            pressureDiastol = health.diastolicPressure,
            pressureSystol = health.systolicPressure,
            oxygen = health.oxygen,
            sugar = 0F,
            temperature = health.temperatureBody,
            breathung = 0,
            latitude = location?.latitude,
            longitude = location?.longitude
        )
    }
}
