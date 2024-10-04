package com.example.s5sum2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var currentLatLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear el contenedor principal del mapa
        val rootLayout = FrameLayout(this).apply {
            id = FrameLayout.generateViewId()
        }
        setContentView(rootLayout)

        // Cargar el fragmento del mapa dinámicamente
        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(rootLayout.id, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)

        // botón flotante para compartir ubicación
        val shareButton = FloatingActionButton(this).apply {
            setImageResource(android.R.drawable.ic_menu_share)
            setOnClickListener {
                shareCurrentLocation()
            }
        }

        val buttonParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            marginEnd = 32
            bottomMargin = 32
            gravity = android.view.Gravity.BOTTOM or android.view.Gravity.END
        }

        rootLayout.addView(shareButton, buttonParams)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Verificar y pedir permisos si no están concedidos
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        // Activar la capa de ubicación si se conceden los permisos
        mMap.isMyLocationEnabled = true

        // Obtener la ubicación actual y centrar el mapa
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(currentLatLng!!).title("Tu ubicación actual"))

                currentLatLng?.let {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
                }
            } else {
                val chileLatLng = LatLng(-33.4489, -70.6693)
                mMap.addMarker(MarkerOptions().position(chileLatLng).title("Santiago de Chile"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chileLatLng, 10f))
                Toast.makeText(this, "No se pudo obtener la ubicación actual. Mostrando Santiago de Chile.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun shareCurrentLocation() {
        // Comprobar que tenemos una ubicación actual antes de compartirla
        currentLatLng?.let {
            val uri = "http://maps.google.com/maps?q=loc:${it.latitude},${it.longitude}"
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Aquí está mi ubicación actual: $uri")
            }
            startActivity(Intent.createChooser(intent, "Compartir ubicación usando"))
        } ?: run {
            Toast.makeText(this, "No se ha podido determinar la ubicación actual", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap.isMyLocationEnabled = true
            }
        }
    }
}
