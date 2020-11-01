package pro.butovanton.fitnes2.net.retrofitDataClass


data class Detail(val created : String,
                  val device : String,
                  val pulse : Int,
                  val pressure : Pressure,
                  val oxygen : Int,
                  val sugar : Float,
                  val temperature : Float,
                  val breathing: Int,
                  val coordinates : Coordinates
)

data class Pressure(val diastol : Int,
                    val systol : Int)

data class Coordinates(val latitude: Double?,
                       val longitude : Double?)