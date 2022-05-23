package com.easy_life.shoplist.activities

import android.app.Application
import com.easy_life.shoplist.db.MainDataBase

class MainApp: Application() {
    val database by lazy { MainDataBase.getDataBase(this)}
}