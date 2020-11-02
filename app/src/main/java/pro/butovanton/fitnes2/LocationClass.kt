package pro.butovanton.fitnes2

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.location.Location
import android.location.LocationManager
import java.security.AccessControlContext

class LocationClass(val context: Context) {

    private val locationManager : LocationManager = context.getSystemService(Service.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    fun getLocation() : Location? {
        var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location == null)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        return location
    }
}