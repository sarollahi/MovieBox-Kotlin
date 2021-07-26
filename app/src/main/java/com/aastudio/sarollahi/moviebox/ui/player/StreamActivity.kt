/*
 * Copyright (C) 2021 Seyed Ahmad Sarollahi
 * All rights reserved.
 */

@file:Suppress("DEPRECATION")

package com.aastudio.sarollahi.moviebox.ui.player

import android.content.ComponentName
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aastudio.sarollahi.common.formatText
import com.aastudio.sarollahi.common.gone
import com.aastudio.sarollahi.common.hideSystemUI
import com.aastudio.sarollahi.common.resumePlayer
import com.aastudio.sarollahi.common.seekPlayer
import com.aastudio.sarollahi.common.show
import com.aastudio.sarollahi.common.showSystemUI
import com.aastudio.sarollahi.common.startPlayer
import com.aastudio.sarollahi.common.stopPlayer
import com.aastudio.sarollahi.moviebox.R
import com.aastudio.sarollahi.moviebox.databinding.ActivityStreamBinding
import com.github.se_bastiaan.torrentstream.StreamStatus
import com.github.se_bastiaan.torrentstream.Torrent
import com.github.se_bastiaan.torrentstream.TorrentOptions
import com.github.se_bastiaan.torrentstream.TorrentStream
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.masterwok.opensubtitlesandroid.services.OpenSubtitlesService
import org.koin.core.KoinComponent

class StreamActivity : AppCompatActivity(), KoinComponent, TorrentListener, Player.EventListener {

    companion object {
        const val MOVIE_URL = "extra_movie_url"
        const val MOVIE_TITLE = "extra_movie_title"
    }

    private lateinit var binding: ActivityStreamBinding
    private var moviePlayer: PlayerView? = null
    private var progressContainer: View? = null
    private var playerControlView: View? = null
    private var streamSpeed: TextView? = null
    private var streamProgressText: TextView? = null
    private var movieSubtitle: ImageButton? = null

    private lateinit var simplePlayer: SimpleExoPlayer
    private lateinit var torrentStream: TorrentStream
    private lateinit var mergeMediaSource: MergingMediaSource
    private lateinit var viewModel: StreamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        viewModel = StreamViewModel(OpenSubtitlesService(), applicationContext)

        setUpUI()
        val url = intent.getStringExtra(MOVIE_URL)
        val title = intent.getStringExtra(MOVIE_TITLE)
        if (!url.isNullOrEmpty() && !title.isNullOrEmpty()) {
            initTorrentStream(url)
            // observeObservers()
            viewsListener(title)
        }
    }

    private fun setUpUI() {
        playerControlView = layoutInflater.inflate(R.layout.exo_player_control_view, null, false)
        movieSubtitle = findViewById(R.id.movieSubtitle)

        moviePlayer = binding.moviePlayer
        progressContainer = binding.progressContainer
        streamSpeed = binding.streamSpeed
        streamProgressText = binding.streamProgressText
    }

    private fun viewsListener(movieName: String) {

        moviePlayer?.setControllerVisibilityListener {
            with(window?.decorView) {
                if (it == 0)
                    this?.systemUiVisibility = showSystemUI()
                else
                    this?.systemUiVisibility = hideSystemUI()
            }
        }

        movieSubtitle?.setOnClickListener {
            viewModel.searchMovieSubtitle(movieName)
        }
    }

    private fun initTorrentStream(url: String) {
        val torrentOptions: TorrentOptions = TorrentOptions.Builder()
            .saveLocation(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
            .removeFilesAfterStop(true)
            .build()
        torrentStream = TorrentStream.init(torrentOptions)
        torrentStream.startStream(url)
        torrentStream.addListener(this)
    }

    private fun initPlayer(path: String) {
//        val trackSelector = DefaultTrackSelector()
//        simplePlayer = newSimpleInstance(this, trackSelector)
//        moviePlayer?.player = simplePlayer
//        val factory: DefaultDataSourceFactory = get()
//        mediaSource = ExtractorMediaSource.Factory(factory).createMediaSource(Uri.parse(path))
//        mergeMediaSource = MergingMediaSource(mediaSource)
//        simplePlayer.apply {
//            startPlayer(mergeMediaSource)
//            addListener(this@StreamActivity)
//        }

        val exoplayer: SimpleExoPlayer?
        val playbackStateBuilder: PlaybackStateCompat.Builder =
            PlaybackStateCompat.Builder()
        val mediaSession =
            MediaSessionCompat(
                applicationContext,
                "ExoPlayer",
                ComponentName(applicationContext, "Exo"),
                null
            )

        exoplayer = SimpleExoPlayer.Builder(applicationContext).build()
        moviePlayer?.player = exoplayer

        val userAgent =
            Util.getUserAgent(applicationContext, applicationContext.getString(R.string.app_name))
        val mediaSource = ProgressiveMediaSource
            .Factory(DefaultDataSourceFactory(applicationContext, userAgent))
            .createMediaSource(Uri.parse(path))

        exoplayer.prepare(mediaSource)

        playbackStateBuilder.setActions(
            PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_FAST_FORWARD
        )

        mediaSession.setPlaybackState(playbackStateBuilder.build())
        mediaSession.isActive = true
        moviePlayer?.useController = false
        exoplayer.playWhenReady = true
        exoplayer.apply {
            startPlayer(mergeMediaSource)
            addListener(this@StreamActivity)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        simplePlayer.seekPlayer(simplePlayer.contentPosition, mergeMediaSource)
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playWhenReady && playbackState == Player.STATE_READY)
            progressContainer?.gone()
        else if (playWhenReady && playbackState == Player.STATE_BUFFERING)
            progressContainer?.show()
    }

    override fun onStreamReady(torrent: Torrent?) {
        initPlayer(torrent?.videoFile?.absolutePath!!)
    }

    override fun onStreamProgress(torrent: Torrent?, status: StreamStatus?) {
        try {
            streamSpeed?.formatText(R.string.streamDownloadSpeed, status?.downloadSpeed?.div(1024))
            streamProgressText?.formatText(R.string.streamProgress, status?.progress, "%")
        } catch (e: IllegalStateException) {
            println("ERROR: $e")
        }
    }

    override fun onPause() {
        super.onPause()
        torrentStream.currentTorrent.pause()
        if (this::simplePlayer.isInitialized) simplePlayer.stopPlayer()
    }

    override fun onResume() {
        super.onResume()
        if (torrentStream.currentTorrent != null)
            torrentStream.currentTorrent.resume()
        if (this::simplePlayer.isInitialized) simplePlayer.resumePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        torrentStream.removeListener(this)
        torrentStream.stopStream()
        if (this::simplePlayer.isInitialized)
            simplePlayer.release()
    }

//    override fun onSubtitleClicked(subtitle: OpenSubtitleItem) {
//        viewModel.downloadSubtitle(
//            subtitle,
//            Uri.fromFile(File("${getExternalFilesDir(null)?.absolutePath}/${subtitle.SubFileName}"))
//        )
//
//    }

    override fun onStreamPrepared(torrent: Torrent?) {
    }

    override fun onStreamStopped() {
    }

    override fun onStreamStarted(torrent: Torrent?) {
    }

    override fun onStreamError(torrent: Torrent?, e: Exception?) {
        println("Error: $e")
    }
}
