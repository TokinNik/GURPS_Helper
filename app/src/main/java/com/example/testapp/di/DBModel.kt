package com.example.testapp.di

import com.example.testapp.db.MainDatabase

interface DBModel {

    fun getDB(): MainDatabase

}