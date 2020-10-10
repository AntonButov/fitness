package pro.butovanton.fitnes2.ui.home

import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pro.butovanton.fitnes2.InjectorUtils
import pro.butovanton.fitnes2.R
import pro.butovanton.fitnes2.net.responses.AlertResponse
import pro.butovanton.fitness.net.JSONPlaceHolderApi

class HomeViewModel : ViewModel() {

    val api = InjectorUtils.provideApi()

    fun serverAvial() : Flow<Boolean> = flow  {
        while (true) {
            val serverAvial = getServerAvial()
            println("Сервер доступен = " + serverAvial)
            emit(serverAvial)
            delay(60000)
        }
    }

    private suspend fun getServerAvial(): Boolean {
        try {
            val response = api.alert(JSONPlaceHolderApi.GUID)
            return true
        }
        catch (t: Throwable) {
            return false
        }
    }
}