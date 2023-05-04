//package com.example.m19_location.ui.main
//
//import android.app.Activity
//import android.graphics.Color
//import android.graphics.Point
//import android.os.Bundle
//import android.os.Handler
//import android.view.ViewGroup
//import android.widget.TextView
//import android.widget.Toast
//import com.example.m19_location.R
//import com.yandex.mapkit.MapKitFactory
//import com.yandex.mapkit.geometry.*
//import com.yandex.mapkit.map.*
//import com.yandex.mapkit.mapview.MapView
//import com.yandex.runtime.image.AnimatedImageProvider
//import com.yandex.runtime.image.ImageProvider
//import com.yandex.runtime.ui_view.ViewProvider
//import java.util.*
//
///**
// * This example shows how to add simple objects such as polygons, circles and polylines to the map.
// * It also shows how to display images instead.
// */
//class MapObjectsActivity : Activity() {
//    private val CAMERA_TARGET = Point(59.952, 30.318)
//    private val ANIMATED_RECTANGLE_CENTER = Point(59.956, 30.313)
//    private val TRIANGLE_CENTER = Point(59.948, 30.313)
//    private val POLYLINE_CENTER = CAMERA_TARGET
//    private val CIRCLE_CENTER = Point(59.956, 30.323)
//    private val DRAGGABLE_PLACEMARK_CENTER = Point(59.948, 30.323)
//    private val ANIMATED_PLACEMARK_CENTER = Point(59.948, 30.318)
//    private val OBJECT_SIZE = 0.0015
//    private var mapView: MapView? = null
//    private var mapObjects: MapObjectCollection? = null
//    private var animationHandler: Handler? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        setContentView(R.layout.map_objects)
//        super.onCreate(savedInstanceState)
//        mapView = findViewById<MapView>(R.id.mapview)
//        mapView.getMap().move(
//            CameraPosition(CAMERA_TARGET, 15.0f, 0.0f, 0.0f)
//        )
//        mapObjects = mapView.getMap().mapObjects.addCollection()
//        animationHandler = Handler()
//        createMapObjects()
//    }
//
//    override fun onStop() {
//        mapView!!.onStop()
//        MapKitFactory.getInstance().onStop()
//        super.onStop()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        MapKitFactory.getInstance().onStart()
//        mapView!!.onStart()
//    }
//
//    private fun createMapObjects() {
//        val animatedImage = AnimatedImageProvider.fromAsset(this, "animation.png")
//        val rectPoints = ArrayList<Point>()
//        rectPoints.add(
//            Point(
//                ANIMATED_RECTANGLE_CENTER.latitude - OBJECT_SIZE,
//                ANIMATED_RECTANGLE_CENTER.longitude - OBJECT_SIZE
//            )
//        )
//        rectPoints.add(
//            Point(
//                ANIMATED_RECTANGLE_CENTER.latitude - OBJECT_SIZE,
//                ANIMATED_RECTANGLE_CENTER.longitude + OBJECT_SIZE
//            )
//        )
//        rectPoints.add(
//            Point(
//                ANIMATED_RECTANGLE_CENTER.latitude + OBJECT_SIZE,
//                ANIMATED_RECTANGLE_CENTER.longitude + OBJECT_SIZE
//            )
//        )
//        rectPoints.add(
//            Point(
//                ANIMATED_RECTANGLE_CENTER.latitude + OBJECT_SIZE,
//                ANIMATED_RECTANGLE_CENTER.longitude - OBJECT_SIZE
//            )
//        )
//        val rect = mapObjects!!.addPolygon(
//            Polygon(LinearRing(rectPoints), ArrayList())
//        )
//        rect.strokeColor = Color.TRANSPARENT
//        rect.fillColor = Color.TRANSPARENT
//        rect.setAnimatedImage(animatedImage, 32.0f)
//        val trianglePoints = ArrayList<Point>()
//        trianglePoints.add(
//            Point(
//                TRIANGLE_CENTER.latitude + OBJECT_SIZE,
//                TRIANGLE_CENTER.longitude - OBJECT_SIZE
//            )
//        )
//        trianglePoints.add(
//            Point(
//                TRIANGLE_CENTER.latitude - OBJECT_SIZE,
//                TRIANGLE_CENTER.longitude - OBJECT_SIZE
//            )
//        )
//        trianglePoints.add(
//            Point(
//                TRIANGLE_CENTER.latitude,
//                TRIANGLE_CENTER.longitude + OBJECT_SIZE
//            )
//        )
//        val triangle = mapObjects!!.addPolygon(
//            Polygon(LinearRing(trianglePoints), ArrayList())
//        )
//        triangle.fillColor = Color.BLUE
//        triangle.strokeColor = Color.BLACK
//        triangle.strokeWidth = 1.0f
//        triangle.zIndex = 100.0f
//        createTappableCircle()
//        val polylinePoints = ArrayList<Point>()
//        polylinePoints.add(
//            Point(
//                POLYLINE_CENTER.latitude + OBJECT_SIZE,
//                POLYLINE_CENTER.longitude - OBJECT_SIZE
//            )
//        )
//        polylinePoints.add(
//            Point(
//                POLYLINE_CENTER.latitude - OBJECT_SIZE,
//                POLYLINE_CENTER.longitude - OBJECT_SIZE
//            )
//        )
//        polylinePoints.add(
//            Point(
//                POLYLINE_CENTER.latitude,
//                POLYLINE_CENTER.longitude + OBJECT_SIZE
//            )
//        )
//        val polyline = mapObjects!!.addPolyline(Polyline(polylinePoints))
//        polyline.setStrokeColor(Color.BLACK)
//        polyline.zIndex = 100.0f
//        val mark = mapObjects!!.addPlacemark(DRAGGABLE_PLACEMARK_CENTER)
//        mark.opacity = 0.5f
//        mark.setIcon(ImageProvider.fromResource(this, R.drawable.mark))
//        mark.isDraggable = true
//        createPlacemarkMapObjectWithViewProvider()
//        createAnimatedPlacemark()
//    }
//
//    // Strong reference to the listener.
//    private val circleMapObjectTapListener = MapObjectTapListener { mapObject, point ->
//        if (mapObject is CircleMapObject) {
//            val circle = mapObject
//            val randomRadius = 100.0f + 50.0f * Random().nextFloat()
//            val curGeometry = circle.geometry
//            val newGeometry = Circle(curGeometry.center, randomRadius)
//            circle.geometry = newGeometry
//            val userData = circle.userData
//            if (userData is CircleMapObjectUserData) {
//                val circleUserData = userData
//                val toast = Toast.makeText(
//                    applicationContext,
//                    "Circle with id " + circleUserData.id + " and description '"
//                            + circleUserData.description + "' tapped",
//                    Toast.LENGTH_SHORT
//                )
//                toast.show()
//            }
//        }
//        true
//    }
//
//    private inner class CircleMapObjectUserData internal constructor(
//        val id: Int,
//        val description: String
//    )
//
//    private fun createTappableCircle() {
//        val circle = mapObjects!!.addCircle(
//            Circle(CIRCLE_CENTER, 100f), Color.GREEN, 2f, Color.RED
//        )
//        circle.zIndex = 100.0f
//        circle.userData = CircleMapObjectUserData(42, "Tappable circle")
//
//        // Client code must retain strong reference to the listener.
//        circle.addTapListener(circleMapObjectTapListener)
//    }
//
//    private fun createPlacemarkMapObjectWithViewProvider() {
//        val textView = TextView(this)
//        val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLACK)
//        val params = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        textView.layoutParams = params
//        textView.setTextColor(Color.RED)
//        textView.text = "Hello, World!"
//        val viewProvider = ViewProvider(textView)
//        val viewPlacemark = mapObjects!!.addPlacemark(Point(59.946263, 30.315181), viewProvider)
//        val random = Random()
//        val delayToShowInitialText = 5000 // milliseconds
//        val delayToShowRandomText = 500 // milliseconds;
//
//        // Show initial text `delayToShowInitialText` milliseconds and then
//        // randomly change text in textView every `delayToShowRandomText` milliseconds
//        animationHandler!!.postDelayed(object : Runnable {
//            override fun run() {
//                val randomInt = random.nextInt(1000)
//                textView.text = "Some text version $randomInt"
//                textView.setTextColor(colors[randomInt % colors.size])
//                viewProvider.snapshot()
//                viewPlacemark.setView(viewProvider)
//                animationHandler!!.postDelayed(this, delayToShowRandomText.toLong())
//            }
//        }, delayToShowInitialText.toLong())
//    }
//
//    private fun createAnimatedPlacemark() {
//        val imageProvider = AnimatedImageProvider.fromAsset(this, "animation.png")
//        val animatedPlacemark =
//            mapObjects!!.addPlacemark(ANIMATED_PLACEMARK_CENTER, imageProvider, IconStyle())
//        animatedPlacemark.useAnimation().play()
//    }
//}