package pro.butovanton.fitnes2

import com.htsmart.wristband2.bean.ConnectionState

interface ReportToModel {
    fun serverAvial(sevrerAvial : Boolean)
    fun deviceAvial(deviceConnectState : ConnectionState)
    fun batary(batary : Int)
    //fun data(data : DataClass)
}