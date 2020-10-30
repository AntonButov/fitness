package pro.butovanton.fitnes2

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.htsmart.wristband2.bean.data.HeartRateData
import kotlinx.coroutines.*
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith
import pro.butovanton.fitnes2.db.Data

import pro.butovanton.fitnes2.mock.AppFakeDataProvider

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RoomTest {
    val dao = InjectorUtils.provideDao()
    val fakeHearths =  fakeConvert(AppFakeDataProvider.fakeHeartRate())

    @Test
    fun roomSaveAllTest() {

    runBlocking {
        dao.insertAll(fakeHearths)
    }
    val data = dao.getData()
        Assert.assertEquals(fakeHearths, data)
    }

    fun fakeConvert(fakeHeartRates : MutableList<HeartRateData>) : MutableList<Data> {
        val result = mutableListOf<Data>()
        for (fhearth in fakeHeartRates)
            result.add(Data(fhearth.timeStamp, fhearth.heartRate))
    return result
    }

    @Test
    fun insertLast() {
        runBlocking {
        dao.delete()
        val fakeHerth = fakeHearths.get(fakeHearths.lastIndex)
        val lastTime = dao.insertLast(fakeHerth )
        println("Last time = " + lastTime)
        val saveHearth = dao.getLastData()
        Assert.assertEquals(fakeHerth, saveHearth)
        dao.deleteLast()
        assert(dao.getData().size == 0)
        delay(30000)
        }
    }
}


