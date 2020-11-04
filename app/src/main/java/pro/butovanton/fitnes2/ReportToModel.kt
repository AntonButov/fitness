package pro.butovanton.fitnes2

import com.htsmart.wristband2.bean.ConnectionState
import pro.butovanton.fitnes2.net.retrofitDataClass.AlertResponse

interface ReportToModel {
    fun serverAvial(sevrerAvial : Boolean)
    fun deviceAvial(deviceConnectState : ConnectionState)
    fun batary(batary : Int)
}