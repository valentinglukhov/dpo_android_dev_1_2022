package com.example.m19_location.ui.main

import android.animation.AnimatorInflater
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.m19_location.R
import com.example.m19_location.databinding.FragmentMainBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.launch

private const val SCREEN_STATE = "screenState"

class MainFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var binding: FragmentMainBinding
    private lateinit var mapKit: MapKit
    private lateinit var currentLocation: Point
    private var markersList = mutableListOf<PlacemarkMapObject>()
    private lateinit var mapObjects: MapObjectCollection

    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var isDescriptionOpened = false
    private var fromState = false

    private val pointTapListener = MapObjectTapListener { mapObject, _ ->
        val pointUserData = mapObject.userData
        if (pointUserData is PointUserData) {
            binding.xid.text = pointUserData.xid
            binding.name.text = pointUserData.name
        }
        openDescription()
        true
    }

    private val mapTapListener: InputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            closeDescription()
        }

        override fun onMapLongTap(p0: Map, p1: Point) {
        }
    }

    val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.all { it }) {
                viewModel.startLocation()
            }
        }

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    return MainViewModel(
                        requireContext(),
                        Repository()
                    ) as T
                } else {
                    throw IllegalArgumentException("")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
        mapKit = MapKitFactory.getInstance()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        viewModel.fusedClient.removeLocationUpdates(viewModel.locationCallback)
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val screenState = ScreenState(
            currentLocation.latitude,
            currentLocation.longitude,
            mapView.map.cameraPosition.zoom,
            mapView.map.cameraPosition.azimuth,
            mapView.map.cameraPosition.tilt,
            isDescriptionOpened,
            binding.xid.text.toString(),
            binding.name.text.toString()
        )
        outState.putParcelable(SCREEN_STATE, screenState)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        mapView = binding.mapView
        val user = mapKit.createUserLocationLayer(mapView.mapWindow)
        user.isVisible = true
        mapObjects = mapView.map.mapObjects.addCollection()
        mapView.map.addInputListener(mapTapListener)

        savedInstanceState?.let { bundle ->
            val screenState = bundle.getParcelable<ScreenState>(SCREEN_STATE)
            if (screenState != null) {
                mapView.map.move(
                    CameraPosition(
                        Point(screenState.latitude, screenState.longitude),
                        screenState.zoom,
                        screenState.azimuth,
                        screenState.tilt
                    )
                )
                if (screenState.isDescriptionOpened) {
                    binding.name.text = screenState.name
                    binding.xid.text = screenState.xid
                    openDescription()
                }
                fromState = true
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.geoLocationFlow.asLiveData().observe(viewLifecycleOwner) { point ->
                if (point != null) {
                    if (!this@MainFragment::currentLocation.isInitialized) {
                        currentLocation = point
                        if (!fromState) {
                            mapView.map.move(
                                CameraPosition(currentLocation, 17.0f, 0.0f, 0.0f),
                                Animation(Animation.Type.SMOOTH, 2F),
                                null
                            )
                        }
                        viewModel.getSightFlow(currentLocation)
                    } else {
                        currentLocation = point
                    }
                }
            }
            viewModel.sightsFlow.asLiveData().observe(viewLifecycleOwner) { sight ->
                if (sight != null) addMarker(sight)
            }
            viewModel.errorMessageFlow.asLiveData().observe(viewLifecycleOwner) { error ->
                if (!error.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.closeDescription.setOnClickListener {
            closeDescription()
        }

        binding.getLocation.setOnClickListener {
            if (this@MainFragment::currentLocation.isInitialized) {
                mapView.map.move(
                    CameraPosition(
                        currentLocation,
                        mapView.map.cameraPosition.zoom,
                        mapView.map.cameraPosition.azimuth,
                        mapView.map.cameraPosition.tilt
                    ),
                    Animation(Animation.Type.SMOOTH, 1F),
                    null
                )
            }
        }
        binding.zoomIn.setOnClickListener {
            mapView.map.move(
                CameraPosition(
                    mapView.map.cameraPosition.target,
                    mapView.map.cameraPosition.zoom.inc(),
                    mapView.map.cameraPosition.azimuth,
                    mapView.map.cameraPosition.tilt
                ),
                Animation(Animation.Type.SMOOTH, 0.1F),
                null
            )
        }
        binding.zoomOut.setOnClickListener {
            mapView.map.move(
                CameraPosition(
                    mapView.map.cameraPosition.target,
                    mapView.map.cameraPosition.zoom.dec(),
                    mapView.map.cameraPosition.azimuth,
                    mapView.map.cameraPosition.tilt
                ),
                Animation(Animation.Type.SMOOTH, 0.1F),
                null
            )
        }
        return binding.root
    }

    private fun addMarker(sights: Sight) {
        sights.features.forEach { sight ->
            val marker = mapObjects.addPlacemark(
                Point(sight.geometry.coordinates.get(1), sight.geometry.coordinates.get(0))
            ).also {
                it.setIconStyle(IconStyle().setScale(4.0F))
            }
            marker.userData = PointUserData(sight.properties.xid, sight.properties.name)
            marker.addTapListener(pointTapListener)
            markersList.add(marker)
        }
    }

    private fun checkPermissions() {
        if (permissions.all { permission ->
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            viewModel.startLocation()
        } else {
            launcher.launch(permissions)
        }
    }

    private fun openDescription() {
        isDescriptionOpened = true
        val animation =
            AnimatorInflater.loadAnimator(requireContext(), R.animator.description_slide_up)
        animation.setTarget(binding.descriptionCard)
        animation.start()
    }

    private fun closeDescription() {
        isDescriptionOpened = false
        val animation =
            AnimatorInflater.loadAnimator(requireContext(), R.animator.description_slide_down)
        animation.setTarget(binding.descriptionCard)
        animation.start()
    }
}