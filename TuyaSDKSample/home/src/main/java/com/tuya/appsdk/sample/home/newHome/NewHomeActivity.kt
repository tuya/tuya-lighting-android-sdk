/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Tuya Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tuya.appsdk.sample.home.newHome

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.tuya.appsdk.sample.resource.HomeModel
import com.tuya.appsdk.sample.user.R
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.home.sdk.bean.HomeBean
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback


/**
 * Create Home Sample
 *
 * @author yueguang [](mailto:developer@tuya.com)
 * @since 2021/1/18 6:09 PM
 */
class NewHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity_new_home)

        val toolbar: Toolbar = findViewById<View>(R.id.topAppBar) as Toolbar
        toolbar.setNavigationOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnDone).setOnClickListener {
            //Create Home
            val strHomeName = findViewById<EditText>(R.id.etHomeName).text.toString()
            val strCity = findViewById<EditText>(R.id.etCity).text.toString()

            TuyaHomeSdk.getHomeManagerInstance().createHome(
                strHomeName,
                // Get location by yourself, here just sample as Shanghai's location
                120.52,
                30.40,
                strCity,
                arrayListOf(),
                object : ITuyaHomeResultCallback {
                    override fun onSuccess(bean: HomeBean?) {
                        HomeModel.INSTANCE.setCurrentHome(this@NewHomeActivity, bean?.homeId?:0)
                        finish()
                        Toast.makeText(
                            this@NewHomeActivity,
                            "Create Home success",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onError(errorCode: String?, errorMsg: String?) {
                        Toast.makeText(
                            this@NewHomeActivity,
                            "Create Home error->$errorMsg",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            )
        }
    }
}