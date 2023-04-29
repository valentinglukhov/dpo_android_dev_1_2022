package com.example.m18_permissions.ui.main

import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.m18_permissions.data.Repository
import com.example.m18_permissions.databinding.FragmentPhotoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"

class PhotoFragment : Fragment() {

    val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.values.all { it }) {
                startCamera()
                Toast.makeText(requireContext(), permissions.values.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    private var imageCapture: ImageCapture? = null
    private lateinit var executor: Executor
    private val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(System.currentTimeMillis())


    private lateinit var binding: FragmentPhotoBinding

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    return MainViewModel(
                        requireContext(),
                        Repository(context?.applicationContext)
                    ) as T
                } else {
                    throw IllegalArgumentException("")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executor = ContextCompat.getMainExecutor(requireContext())
        checkPermissions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        startCamera()
        binding = FragmentPhotoBinding.inflate(layoutInflater)
        binding.takePhoto.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                takePhoto()
            }
        }
        return binding.root
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

        ) {
            Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            launcher.launch(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context?.contentResolver!!,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues

        ).build()

        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(requireContext(), name.toString(), Toast.LENGTH_SHORT).show()
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.repository.savePhoto(
                            outputFileResults.savedUri.toString(),
                            name
                        )
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.photoPreview.surfaceProvider)
            imageCapture = ImageCapture.Builder().build()

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        }, executor)
    }
}