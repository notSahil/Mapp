package com.example.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.collections.List as List1

class Home : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    //location
    lateinit var lm: LocationManager
    lateinit var loc: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth= FirebaseAuth.getInstance()
        var currentUser=auth.currentUser

//        Reference
        val logout=findViewById<Button>(R.id.idLogout)

        if(currentUser==null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        logout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
   // Location
//        if(ActivityCompat.checkSelfPermission(this,Manifest))

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),111)
        lm=getSystemService(LOCATION_SERVICE) as LocationManager
        loc= lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!

        var ll = object :LocationListener{
            override fun onLocationChanged(p0: Location) {
               reverseGeocode(p0)
            }

        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,100.2f,ll)
    }

    private fun reverseGeocode(loc: Location) {
        var gc= Geocoder(this,  Locale.getDefault())
        var addresses = gc.getFromLocation(loc!!.latitude, loc.longitude,2)

        var address : Address = addresses.get(0)
        textView1.setText("Current Location of Your Device is \n${address.getAddressLine(0)}\n${address.locale}")
    }
}