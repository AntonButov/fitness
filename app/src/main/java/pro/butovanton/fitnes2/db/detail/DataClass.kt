package pro.butovanton.fitnes2.db.detail

import android.location.Location
import android.location.LocationManager
import com.htsmart.wristband2.bean.HealthyDataResult
import pro.butovanton.fitnes2.App
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
        if (location == null) {
            location = Location(LocationManager.PASSIVE_PROVIDER)
            location!!.latitude = 0.0
            location!!.longitude = 0.0
        }
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
            respiratoryRate = health.respiratoryRate,
            latitude = location?.latitude,
            longitude = location?.longitude
        )
    }
}
