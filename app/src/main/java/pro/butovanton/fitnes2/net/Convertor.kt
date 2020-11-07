package pro.butovanton.fitnes2.net

import pro.butovanton.fitnes2.db.detail.Data
import pro.butovanton.fitnes2.net.retrofitDataClass.Coordinates
import pro.butovanton.fitnes2.net.retrofitDataClass.Detail
import pro.butovanton.fitnes2.net.retrofitDataClass.Pressure
import pro.butovanton.fitnes2.utils.AndPermissionHelper.Utils

class Convertor {

    fun toRetrofit(data : Data) : Detail {
        return Detail(Utils.longDateToString(data.created),
               "00000000-0000-0000-0000-" + Utils.del2dot(data.device),
                      data.heatRate,
                      Pressure(data.pressureDiastol,
                               data.pressureSystol),
                      data.oxygen,
                      data.sugar,
                      data.temperaturebody,
                      data.breathung,
                      data.respiratoryRate,
                      Coordinates(data.latitude,
                                  data.longitude)
        )
    }
}