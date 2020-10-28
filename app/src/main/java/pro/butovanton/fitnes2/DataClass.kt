package pro.butovanton.fitnes2

import android.location.Location
import com.htsmart.wristband2.bean.HealthyDataResult
import java.util.*

class DataClass () {

    private val created = Date().time
    private val device = (App).deviceState.device?.address
    private var location : Location? = null
    private lateinit var health : HealthyDataResult

    fun add(location: Location?) {
        this.location = location
    }

    fun add(health : HealthyDataResult) {
        this.health = health
    }


}