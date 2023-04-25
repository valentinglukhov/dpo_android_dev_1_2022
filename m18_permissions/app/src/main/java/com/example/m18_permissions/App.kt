package com.example.m18_permissions

import android.app.Application
import androidx.room.Room
import com.example.m18_permissions.data.SightDatabase

class App : Application() {
    lateinit var sightDatabase: SightDatabase

    override fun onCreate() {
        super.onCreate()

        sightDatabase = Room.databaseBuilder(
            applicationContext,
            SightDatabase::class.java,
            "sightDatabase"
        ).build()
    }
}