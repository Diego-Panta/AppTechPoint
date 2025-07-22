package com.example.apptechpoint

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.LatLngBounds
import android.graphics.Color
import androidx.appcompat.app.AlertDialog
import com.google.maps.android.PolyUtil
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var myMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var storesAdapter: StoresAdapter
    private val stores = mutableListOf<ComputerStore>()
    private var currentLocation: LatLng? = null
    private var currentLocationMarker: Marker? = null
    private var nearestStore: ComputerStore? = null
    private var routePolyline: Polyline? = null
    private val client = OkHttpClient()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar componentes
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupBottomSheet()
        setupFab()
        loadComputerStores()

        // Configurar mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupBottomSheet() {
        val bottomSheet =
            findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val recyclerView = findViewById<RecyclerView>(R.id.stores_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        storesAdapter = StoresAdapter(stores) { store ->
            val storeLatLng = LatLng(store.latitude, store.longitude)
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(storeLatLng, 16f))
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        recyclerView.adapter = storesAdapter
    }

    private fun setupFab() {
        val fabLocation = findViewById<FloatingActionButton>(R.id.fab_location)
        fabLocation.setOnClickListener {
            if (checkLocationPermission()) {
                getCurrentLocation()
            }
        }
    }

    private fun loadComputerStores() {
        val storesJson = """
        [
            {
                "name": "Galerías Shopping Center",
                "latitude": -9.07370,
                "longitude": -78.57712,
                "description": "Centro comercial con múltiples tiendas de tecnología y equipos de cómputo",
                "stores": ["Saga Falabella", "Ripley", "Tiendas especializadas en tecnología"]
            },
            {
                "name": "Centro Comercial Chimbote (Av. José Gálvez)",
                "latitude": -9.07376,
                "longitude": -78.58978,
                "description": "Centro comercial tradicional con tiendas de equipos informáticos",
                "stores": ["Tiendas de computación", "Servicio técnico especializado"]
            },
            {
                "name": "Galerías Alfa",
                "latitude": -9.07486,
                "longitude": -78.59126,
                "description": "Galería comercial con tiendas especializadas en tecnología",
                "stores": ["Computación Alfa", "Tiendas de accesorios tecnológicos"]
            },
            {
                "name": "Centro Comercial Chimbote Plaza Center",
                "latitude": -9.0741361,
                "longitude": -78.5880261,
                "description": "Plaza comercial con variedad de tiendas tecnológicas",
                "stores": ["Tiendas de computadoras", "Reparación de equipos"]
            },
            {
                "name": "MegaPlaza Chimbote",
                "latitude": -9.1024389,
                "longitude": -78.5576856,
                "description": "Centro comercial moderno con grandes tiendas de tecnología",
                "stores": ["Saga Falabella", "Ripley", "Hiraoka", "Tiendas especializadas"]
            },
            {
                "name": "Tu Centro Comercial Nuevo Chimbote",
                "latitude": -9.12798,
                "longitude": -78.51976,
                "description": "Centro comercial en Nuevo Chimbote con tiendas de equipos de cómputo",
                "stores": ["Tiendas de tecnología", "Servicio técnico especializado"]
            },
            {
            "name": "Tienda de Computo Perú",
            "latitude": -9.12850,
            "longitude": -78.52200,
            "description": "Venta de computadoras, laptops, componentes y reparación",
            "stores": ["Computadoras de escritorio", "Laptops", "Accesorios", "Servicio técnico"]
            },
            {
                "name": "Micro.Net",
                "latitude": -9.12980,
                "longitude": -78.52150,
                "description": "Venta de equipo, accesorios y reparación informática",
                "stores": ["PC", "Accesorios", "Reparación"]
            },
            {
                "name": "Chimbote Software",
                "latitude": -9.12650,
                "longitude": -78.52000,
                "description": "Tienda de electrónica, software y hardware",
                "stores": ["Computadoras", "Periféricos", "Software", "Servicio técnico"]
            },
            {
                "name": "Centro Comercial Cybertech",
                "latitude": -9.07150,
                "longitude": -78.58800,
                "description": "Complejo tecnológico con múltiples tiendas de computación y accesorios",
                "stores": ["Laptops", "Impresoras", "Accesorios", "Servicio técnico"]
            },
            {
                "name": "Compupluss",
                "latitude": -9.07390,
                "longitude": -78.58850,
                "description": "Tienda de informática y accesorios, muy recomendada",
                "stores": ["Accesorios informáticos", "Servicio técnico"]
            },
            {
                "name": "Computer House",
                "latitude": -9.07500,
                "longitude": -78.58700,
                "description": "Tienda de informática y tecnología en Av. Pardo",
                "stores": ["Computadoras", "Accesorios", "Reparación"]
            }
        ]
        """.trimIndent()

        try {
            val jsonArray = JSONArray(storesJson)
            stores.clear()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val storesArray = jsonObject.getJSONArray("stores")
                val storesList = mutableListOf<String>()

                for (j in 0 until storesArray.length()) {
                    storesList.add(storesArray.getString(j))
                }

                stores.add(
                    ComputerStore(
                        name = jsonObject.getString("name"),
                        latitude = jsonObject.getDouble("latitude"),
                        longitude = jsonObject.getDouble("longitude"),
                        description = jsonObject.getString("description"),
                        stores = storesList
                    )
                )
            }

            storesAdapter.notifyDataSetChanged()

        } catch (e: Exception) {
            Toast.makeText(this, "Error al cargar las tiendas: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(@NonNull googleMap: GoogleMap) {
        myMap = googleMap

        myMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = false
        }

        val chimboteCenter = LatLng(-9.0847, -78.5786)
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chimboteCenter, 13f))
        addStoreMarkers()

        myMap.setOnMarkerClickListener { marker ->
            val store = stores.find {
                it.latitude == marker.position.latitude &&
                        it.longitude == marker.position.longitude
            }
            store?.let {
                showStoreInfo(it)
            }
            true
        }

        checkLocationPermission()
    }

    private fun addStoreMarkers() {
        for (store in stores) {
            val position = LatLng(store.latitude, store.longitude)
            myMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(store.name)
                    .snippet(store.description)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }
    }

    private fun showStoreInfo(store: ComputerStore) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        Toast.makeText(this, "${store.name}\n${store.description}", Toast.LENGTH_LONG).show()
    }

    private fun checkLocationPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            myMap.isMyLocationEnabled = true
            true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            // Limpiar ruta anterior
            routePolyline?.remove()
            routePolyline = null

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    currentLocation = LatLng(it.latitude, it.longitude)

                    currentLocationMarker?.remove()

                    currentLocationMarker = myMap.addMarker(
                        MarkerOptions()
                            .position(currentLocation!!)
                            .title("Mi ubicación")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    )

                    val nearbyMessage = findNearbyStores()

                    // Mostrar AlertDialog en lugar de Toast
                    AlertDialog.Builder(this)
                        .setTitle("Tiendas más cercanas")
                        .setMessage(nearbyMessage)
                        .setPositiveButton("OK", null)
                        .show()

                } ?: run {
                    Toast.makeText(
                        this,
                        "No se pudo obtener la ubicación actual",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Error al obtener ubicación: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun findNearbyStores(): String {
        currentLocation?.let { userLocation ->
            val storesWithDistance = stores.map { store ->
                val storeLocation = LatLng(store.latitude, store.longitude)
                val distance = calculateDistance(userLocation, storeLocation)
                Pair(store, distance)
            }.sortedBy { it.second }

            val nearbyStores = storesWithDistance.take(3)

            // Guardar la tienda más cercana para dibujar la ruta
            nearestStore = nearbyStores.firstOrNull()?.first

            // Dibujar ruta a la tienda más cercana
            nearestStore?.let {
                drawRouteToStore(it)
            }

            return buildString {
                append("TIENDAS MÁS CERCANAS\n\n")
                nearbyStores.forEachIndexed { index, (store, distance) ->
                    append("${index + 1}. ${store.name}\n")
                    append("📍 Distancia: ${"%.2f".format(distance)} km\n")
                    append("🛒 Tiendas: ${store.stores.take(3).joinToString(", ")}\n\n")
                }
                append("(Ruta mostrada a la tienda más cercana)")
            }
        } ?: return "No se pudo determinar la ubicación actual"
    }

    private fun calculateDistance(pos1: LatLng, pos2: LatLng): Double {
        val results = FloatArray(1)
        Location.distanceBetween(
            pos1.latitude, pos1.longitude,
            pos2.latitude, pos2.longitude,
            results
        )
        return (results[0] / 1000).toDouble()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        myMap.isMyLocationEnabled = true
                        getCurrentLocation()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Permiso de ubicación necesario para mostrar tiendas cercanas",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // Función para dibujar la ruta
    private fun drawRouteToStore(store: ComputerStore) {
        currentLocation?.let { userLocation ->
            // Limpiar ruta anterior si existe
            routePolyline?.remove()

            val storeLocation = LatLng(store.latitude, store.longitude)

            // Crear bounds para ajustar el zoom del mapa
            val bounds = LatLngBounds.builder()
                .include(userLocation)
                .include(storeLocation)
                .build()

            // Ajustar cámara para mostrar ambos puntos
            myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

            // Obtener la ruta de la API de Directions
            getRouteFromApi(userLocation, storeLocation)
        }
    }

    // Función para obtener la ruta de la API de Google Maps
    private fun getRouteFromApi(origin: LatLng, destination: LatLng) {
        val apiKey = "AIzaSyC_SBmGhvcn8QfPYX530XbUwuHAzYQsRXw" // Reemplaza con tu API key

        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=${origin.latitude},${origin.longitude}&" +
                "destination=${destination.latitude},${destination.longitude}&" +
                "key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Error al obtener la ruta: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonData ->
                    try {
                        val jsonObject = JSONObject(jsonData)
                        val routes = jsonObject.getJSONArray("routes")

                        if (routes.length() > 0) {
                            val route = routes.getJSONObject(0)
                            val polyline = route.getJSONObject("overview_polyline")
                            val points = polyline.getString("points")
                            val decodedPath = PolyUtil.decode(points)

                            runOnUiThread {
                                // Dibujar la ruta en el mapa
                                routePolyline = myMap.addPolyline(
                                    PolylineOptions()
                                        .addAll(decodedPath)
                                        .color("#4285F4".toColorInt())
                                        .width(10f)
                                )
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this@MainActivity,
                                "Error al procesar la ruta",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
    }
}