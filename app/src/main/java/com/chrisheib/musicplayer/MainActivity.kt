package com.chrisheib.musicplayer

import android.media.MediaPlayer
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    var mediaPlayer : MediaPlayer? = null

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
            if (playButton.text == "▶"){
                downloadAndPlay()
            } else if (playButton.text == "■"){
                stopPlayer()
            }
        }
    }

    private fun downloadAndPlay(){

        // Plan:
        // 1.: Infos requesten (liedID),
        // 2.: Check, ob Datei vorhanden,
        // 3.: sonst: Datei anfordern (liedID),
        // 4.: Datei abspielen

        // https://blog.mindorks.com/how-to-download-a-file-in-android-and-show-the-progress-very-easily
        // https://www.baeldung.com/kotlin-khttp

        PRDownloader.initialize(applicationContext)
        val url = "http://192.168.2.109:80/"

        // https://gist.github.com/lopspower/76421751b21594c69eb2
        // https://github.com/lopspower/BestAndroidGists

        val path = applicationContext.getExternalFilesDir(null)?.absolutePath + "/download/"
        val fileName = "test.mp3"
        val fullPath = """$path$fileName"""

        val myFile = File(fullPath)
        if (myFile.exists()) {
            myFile.delete()
        }

        playButton.text = "⌛"
        progressBar.visibility = View.VISIBLE

        // 3.: Runterladen
        PRDownloader.download(url, path, fileName)
            .build()
            .setOnProgressListener { progress -> progressBar.progress = ((progress.currentBytes.div(progress.totalBytes.toFloat())) * 100).toDouble().roundToInt()}
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(applicationContext, "Success!", Toast.LENGTH_LONG).show()
                    // 4. Abspielen
                    mediaPlayer = MediaPlayer.create(applicationContext, Uri.parse(fullPath))
                    mediaPlayer?.start()
                    playButton.text = "■"
                    progressBar.visibility = View.GONE
                }

                override fun onError(error: Error?) {
                    Toast.makeText(
                        applicationContext,
                        "Error :( ${error.toString()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun stopPlayer(){
        mediaPlayer?.stop()
        playButton.text = "▶"
    }
}
