package com.aastudio.sarollahi.moviebox.ui.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aastudio.sarollahi.moviebox.databinding.ActivityTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class TrailerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrailerBinding
    private var trailer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trailer = intent.getStringExtra(KEY)
        if (trailer != null) {
            lifecycle.addObserver(binding.youtubePlayerView)

            binding.youtubePlayerView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(trailer!!, 0f)
                    binding.youtubePlayerView.enterFullScreen()
                    youTubePlayer.play()
                }
            })
        } else {
            finish()
        }
    }

    companion object {
        const val KEY = "extra_trailer_id"
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }
}