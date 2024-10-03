package com.example.apprememberit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast

class ReconocimientoVozHelper(
    private val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: () -> Unit,
    private val onListening: () -> Unit,
    private val onProcessing: () -> Unit
) {
    private val speechRecognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES") // Idioma español
    }

    init {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {
                onListening()
            }

            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                onProcessing()
            }

            override fun onError(error: Int) {
                onError()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val recognizedText = matches?.firstOrNull() ?: "No se pudo reconocer"
                onResult(recognizedText) // Enviar resultado al callback
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    //Iniciar el reconocimiento de voz
    fun startListening() {
        speechRecognizer.startListening(recognizerIntent)
    }

    // Detener el reconocimiento de voz
    fun stopListening() {
        speechRecognizer.stopListening()
    }
}