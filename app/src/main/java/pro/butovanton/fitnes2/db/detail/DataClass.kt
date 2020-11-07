package pro.butovanton.fitnes2.db.detail

import android.location.Location
import android.location.LocationManager
import com.htsmart.wristband2.bean.HealthyDataResult
import pro.butovanton.fitnes2.App
import pro.butovanton.fitnes2.utils.Logs
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

    fun add(health: HealthyDataResult?) {
        if (health == null)
            this.health = zeroHealthyData()
        else
            if (health.temperatureWrist < 30)
                this.health = zeroHealthyData()
            else
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
            temperaturebody = health.temperatureBody,
            temperaturewrist = health.temperatureWrist,
            breathung = 0,
            respiratoryRate = health.respiratoryRate,
            latitude = location?.latitude,
            longitude = location?.longitude
        )
    }

    private fun zeroHealthyData() : HealthyDataResult {
        health = HealthyDataResult()
        health.respiratoryRate = 0
        health.diastolicPressure = 0
        health.systolicPressure = 0
        health.temperatureWrist = 0F
        health.temperatureBody = 0F
        health.heartRate = 0
        health.oxygen = 0
        Logs.d("Добавляем 0")
    return health
    }


}
