package pro.butovanton.fitnes2.net

import pro.butovanton.fitnes2.LocationClass
import pro.butovanton.fitnes2.db.Data
import pro.butovanton.fitnes2.net.retrofitDataClass.Coordinates
import pro.butovanton.fitnes2.net.retrofitDataClass.Detail
import pro.butovanton.fitnes2.net.retrofitDataClass.Pressure
import pro.butovanton.fitnes2.util.Utils

class Convertor {

    fun toRetrofit(data : Data) : Detail {
        return Detail(Utils.longDateToString(data.created),
                      data.device,
                      data.heatRate,
                      Pressure(data.pressureDiastol,
                               data.pressureSystol),
                      data.oxygen,
                      data.sugar,
                      data.temperature,
                      data.breathung,
                      Coordinates(data.latitude,
                                  data.longitude)
        )
    }
}