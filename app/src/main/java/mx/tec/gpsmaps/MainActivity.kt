package mx.tec.gpsmaps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), LocationListener {
    lateinit var locationManager: LocationManager
    lateinit var txtLocation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE)
                                                        as LocationManager
        txtLocation = findViewById(R.id.txtLocation)
        checkPermissions(this)
    }

    override fun onLocationChanged(p0: Location) {
        txtLocation.text = "Latitud: ${p0.latitude}\nLongitud: ${p0.longitude}"
    }

    private fun checkPermissions(context: Activity){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            //Si el permiso no se concedió, explicar al usuario porque se ocupa
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)){
                val builder = AlertDialog.Builder(context)
                builder.setMessage("El acceso a la ubicación se requiere por...")
                    .setTitle("Permiso de ubicación requerido")
                builder.setPositiveButton("OK"){ dialog, id ->
                    ActivityCompat.requestPermissions(context,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 45)
                }
                val dialogo = builder.create()
                dialogo.show()
            }else{
                // Si no se necesita mostrar una explicación al usuario
                ActivityCompat.requestPermissions(context,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 45)
            }
        }else{
            //El permiso se concedió, se pide la localización
            // 1. Proveedor: GPS
            // 2. Tiempo de actualización: en milisegundos (10000)
            // 3. Distancia en metros: 5m flotante -> 5f
            // 4. Contexto (this@MainActivity)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                1f,
                this)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            45 -> {
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    // ¿Qué hacer? Cerrar la aplicación? Volver a preguntar?
                }else{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000,
                        1f,
                        this@MainActivity)
                }
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        //super.onStatusChanged(provider, status, extras)
    }
}