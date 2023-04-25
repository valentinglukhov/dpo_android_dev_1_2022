package com.example.m18_permissions.data

import android.content.Context
import com.example.m18_permissions.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Repository(context: Context?) {
    val sightDatabase = (context?.applicationContext as App).sightDatabase.sightDao()

    fun savePhoto (uri: String, name: String) {
        val sight = Sight(uri, name)
        CoroutineScope(Dispatchers.IO).launch {
            sightDatabase.savePhotoInfo(sight)
        }
    }

    fun getPhotoList (): Flow<List<Sight>> {
        return sightDatabase.getPhotoList()
    }
}