package pro.butovanton.fitnes2

import com.htsmart.wristband2.bean.HealthyDataResult

class Analiser {

    private val healthyDataResult = HealthyDataResult()

    fun analis(health : HealthyDataResult) {
        if (health.diastolicPressure != 0)
            healthyDataResult.diastolicPressure += health.diastolicPressure/2
        if (health.heartRate !=0)
            healthyDataResult.heartRate += health.heartRate/2
        if (health.oxygen != 0)
            healthyDataResult.oxygen += health.oxygen/2
        if (health.respiratoryRate !=0)
            healthyDataResult.respiratoryRate += health.respiratoryRate/2
        if  (health.systolicPressure != 0)
            healthyDataResult.systolicPressure += health.systolicPressure/2
        if (health.temperatureBody != 0F)
            healthyDataResult.temperatureBody += health.temperatureBody/2
        if (health.temperatureWrist !=0F)
            healthyDataResult.temperatureWrist += health.temperatureWrist/2

    }

    fun getResult() : HealthyDataResult {
        return healthyDataResult
    }
}