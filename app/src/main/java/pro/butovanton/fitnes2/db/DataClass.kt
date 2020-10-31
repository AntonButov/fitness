package pro.butovanton.fitnes2.db

import android.location.Location
import com.htsmart.wristband2.bean.HealthyDataResult
import okhttp3.internal.notify
import pro.butovanton.fitnes2.App
import java.util.*

class DataClass () {

    private val created = 0
    private var device = App.deviceState.device?.address
    private var location : Location? = null
    private lateinit var health : HealthyDataResult

    fun add(location: Location?) {
        this.location = location
    }

    fun add(health : HealthyDataResult) {
        this.health = health
    }

    fun getMOdelToRoom() : Data {
        if (device == null) device = ""
        return Data(
            created = Date().time,
            heatRate = health.heartRate,
            device = device,
            pressureDiastol = health.diastolicPressure,
            pressureSystol = health.systolicPressure,
            oxygen = health.oxygen,
            sugar = 0,
            temperature = health.temperatureBody,
            breathung = 0,
            latitude = location?.latitude,
            longitude = location?.longitude
            )
    }
}