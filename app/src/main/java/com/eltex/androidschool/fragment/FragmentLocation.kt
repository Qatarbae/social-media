package com.eltex.androidschool.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentLocationBinding
import com.eltex.androidschool.model.Coordinates
import com.eltex.androidschool.viewmodel.LocationViewModel
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentLocation : Fragment() {

    companion object {
        private val START_POINT = Point(55.751280, 37.629720)
        private val START_POSITION = CameraPosition(START_POINT, 17.0f, 150.0f, 30.0f)

        private val SMOOTH_ANIMATION = Animation(Animation.Type.SMOOTH, 0.4f)
        private const val ZOOM_STEP = 1f
    }

    private var coord: Coordinates? = null

    private var mapView: MapView? = null
    private var map: Map? = null
    private var placemarkMapObject: PlacemarkMapObject? = null

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()
    private val locationViewModel by activityViewModels<LocationViewModel>()

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            placemarkMapObject?.geometry = point
            coord = Coordinates(point.latitude, point.longitude)
        }

        override fun onMapLongTap(map: Map, point: Point) = Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLocationBinding.inflate(inflater, container, false)

        mapView = binding.mapview

        map = requireNotNull(mapView?.mapWindow?.map)
        map?.addInputListener(inputListener)

        coord = locationViewModel.selectedCoordinates.value
        val initialPoint = coord?.let { Point(it.lat, it.long) } ?: START_POINT

        createPlacemark(initialPoint)

        coord?.let {
            map?.move(CameraPosition(initialPoint, 17.0f, 150.0f, 30.0f))
        } ?: map?.move(START_POSITION)

        binding.buttonPlus.setOnClickListener { changeZoomInStep(ZOOM_STEP) }
        binding.buttonMinus.setOnClickListener { changeZoomInStep(-ZOOM_STEP) }

        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                toolbarViewModel.saveClicked(false)
                locationViewModel.setSelectedCoordinates(coord)
                findNavController().navigateUp()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        toolbarViewModel.setSaveVisibility(true)
    }

    override fun onStop() {
        mapView?.onStop()
        super.onStop()
        locationViewModel.setSelectedCoordinates(coord)
        toolbarViewModel.setSaveVisibility(false)
    }

    private fun createPlacemark(point: Point) {
        val imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.placemark_icon)

        placemarkMapObject = map?.mapObjects?.addPlacemark()?.apply {
            geometry = point
            setIcon(imageProvider)
        }
    }

    private fun changeZoomInStep(value: Float) {
        with(requireNotNull(map?.cameraPosition)) {
            map?.move(
                CameraPosition(target, zoom + value, azimuth, tilt),
                SMOOTH_ANIMATION,
                null,
            )
        }
    }
}