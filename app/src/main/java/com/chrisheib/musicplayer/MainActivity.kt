package com.chrisheib.musicplayer

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // get reference to button
        val btn_click_me = findViewById(R.id.playButton) as Button

        // set on-click listener
        btn_click_me.setOnClickListener {
            buttonPlayOnClick()
        }
    }

    private fun buttonPlayOnClick(){
        // https://blog.mindorks.com/how-to-download-a-file-in-android-and-show-the-progress-very-easily
        PRDownloader.initialize(applicationContext);
        var url = "http://192.168.2.109:80/"
        val path = applicationContext.getExternalFilesDir(null)?.absolutePath
        //var dirPath = "/music/"
        var fileName = "test.mp3"
        val start = PRDownloader.download(url, path, fileName)
            .build()
            .setOnProgressListener { progress -> Toast.makeText(applicationContext,"Success! $progress",Toast.LENGTH_LONG).show()
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(applicationContext, "Success!", Toast.LENGTH_LONG).show()
                }

                override fun onError(error: Error?) {
                    Toast.makeText(
                        applicationContext,
                        "Error :( ${error.toString()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        val sampleUrl = "http://192.168.2.109:80/" // your URL here
        val mediaPlayer: MediaPlayer? = MediaPlayer().apply {
            //setAudioAttributes(AudioAttributes) //to send the object to the initialized state
            setDataSource(sampleUrl) //to set media source and send the object to the initialized state
            prepare() //to send the object to the prepared state, this may take time for fetching and decoding
            start() //to start the music and send the object to started state
        }
    }
}
