package com.example.shop

import android.content.Intent
import android.graphics.Typeface
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import kotlinx.android.synthetic.main.activity_enter.*

class EnterActivity : AppCompatActivity(), ViewPropertyAnimatorListener {

    private lateinit var musicplayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)
        val font = Typeface.createFromAsset(assets, "fonts/Centaur.ttf")
        textViewARKADIA.setTypeface(font)
        textViewTradCenter.setTypeface(font)
        ViewCompat.animate(textViewARKADIA).scaleX(1.2f).scaleY(1.2f).setListener(this@EnterActivity).setDuration(3000)
        ViewCompat.animate(splash_image).scaleX(1.2f).scaleY(1.2f).setListener(this@EnterActivity).setDuration(3000)
        ViewCompat.animate(textViewTradCenter).scaleX(1.3f).scaleY(1.3f).setListener(this@EnterActivity).setDuration(3000)

        musicplayer = MediaPlayer.create(this, R.raw.drum)
        musicplayer.start()

    }

    override fun onAnimationEnd(view: View?) {


        val intent = Intent(this@EnterActivity, MainActivity::class.java)
        startActivity(intent)
        finish() //讓他不要return


    }

    override fun onDestroy() {
        musicplayer.stop()
        super.onDestroy()
    }

    override fun onAnimationCancel(view: View?) {

    }

    override fun onAnimationStart(view: View?) {

    }
}
