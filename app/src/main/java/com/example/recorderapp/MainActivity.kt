package com.example.recorderapp

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val recordButton: RecordButton by lazy {
        findViewById(R.id.recordButton)
    }

    private val requiredPermissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val recordingFilePath: String by lazy {
        "${externalCacheDir?.absolutePath}/recording.3gp"
    }

    private var recorder: MediaRecorder? = null

    private var state = State.BEFORE_RECORDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAudioPermission()
        initViews()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted = requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!audioRecordPermissionGranted) {
            finish()
        }
    }

    private fun initViews() {
        recordButton.updateIconWithState(state)
    }

    private fun requestAudioPermission() {
        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }
        recorder?.start()
    }

    private fun stopRecording() {
        recorder?.run {
            stop()
            release()
        }
        recorder = null
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
}