package pro.butovanton.fitnes2

import android.location.Location
import android.location.LocationManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.htsmart.wristband2.bean.HealthyDataResult
import com.htsmart.wristband2.bean.data.HeartRateData
import kotlinx.coroutines.*
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith
import pro.butovanton.fitnes2.db.detail.DataClass

import pro.butovanton.fitnes2.mock.AppFakeDataProvider

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RoomTest {
    val dao = InjectorUtils.provideDao()
    val fakeHearths =  AppFakeDataProvider.fakeHeartRate()
    val fakeHearth = getFakeDataResult(fakeHearths.get(fakeHearths.lastIndex))
    val fakeLocation = getFakeLocation()

    @Test
    fun insertLast() {
        val dataClass = DataClass()
        dataClass.add(fakeLocation)
        dataClass.add(fakeHearth)
        runBlocking {
        dao.delete()
        val dateToSave = dataClass.getMOdelToRoom()
        val lastTime = dao.insertLast(dateToSave)
        println("Last time = " + lastTime)
        val saveHearth = dao.getLastData()
        Assert.assertEquals(dateToSave, saveHearth)
        dao.deleteLast()
        assert(dao.getData().size == 0)
        }
    }

    @JvmName("getFakeLocation1")
    private fun getFakeLocation() : Location {
        val location = Location(LocationManager.GPS_PROVIDER)
        location.latitude = 46.44
        location.longitude = 54.343
    return location
    }

    private fun getFakeDataResult(fakeHeartRate: HeartRateData) : HealthyDataResult{
        val heartRateData = HealthyDataResult()
        heartRateData.heartRate = fakeHeartRate.heartRate
        heartRateData.temperatureWrist = 42.2F
        heartRateData.temperatureBody = 36.6F
        heartRateData.systolicPressure = 120
        heartRateData.diastolicPressure = 80
        heartRateData.respiratoryRate = 11
    return heartRateData
    }

}


