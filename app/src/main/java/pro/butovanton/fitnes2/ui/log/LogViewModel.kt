package pro.butovanton.fitnes2.ui.log

import androidx.lifecycle.ViewModel
import pro.butovanton.fitnes2.db.blackbox.BlackBox
import pro.butovanton.fitnes2.utils.Logs

class LogViewModel : ViewModel() {

    val log = Logs.logLive

    suspend fun getBlackBox() : List<BlackBox> {
       return Logs.getBlackBox()
    }
}