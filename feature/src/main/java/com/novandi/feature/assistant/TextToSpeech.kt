package com.novandi.feature.assistant

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.texttospeech.v1.AudioConfig
import com.google.cloud.texttospeech.v1.AudioEncoding
import com.google.cloud.texttospeech.v1.SynthesisInput
import com.google.cloud.texttospeech.v1.TextToSpeechClient
import com.google.cloud.texttospeech.v1.TextToSpeechSettings
import com.google.cloud.texttospeech.v1.VoiceSelectionParams
import com.novandi.feature.R
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

class TextToSpeech(private val context: Context) {
    private val maxTextLength = 500

    private fun splitText(text: String): List<String> {
        val inputTexts = mutableListOf<String>()
        var startIndex = 0
        var endIndex = maxTextLength

        while (startIndex < text.length) {
            if (endIndex >= text.length) {
                endIndex = text.length
            } else {
                while (endIndex > startIndex && !text[endIndex].isWhitespace()) {
                    endIndex--
                }
            }

            val inputText = text.substring(startIndex, endIndex)
            inputTexts.add(inputText.trim())

            startIndex = endIndex + 1
            endIndex = startIndex + maxTextLength
        }

        return inputTexts
    }

    suspend fun synthesize(text: String): ByteArray? = withContext(Dispatchers.IO) {
        try {
            val stream: InputStream = context.resources.openRawResource(R.raw.text_to_speech_credentials)
            val credentials: GoogleCredentials = GoogleCredentials.fromStream(stream)
                .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))
            val settingBuilder = TextToSpeechSettings.newBuilder()
            val sessionSettings = settingBuilder
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build()
            val client = TextToSpeechClient.create(sessionSettings)
            val voiceBuilder = VoiceSelectionParams.newBuilder()
                .setName("id-ID-Wavenet-A")
                .setLanguageCode("id-ID")
            val audioConfig = AudioConfig.newBuilder()
                .setAudioEncoding(AudioEncoding.MP3)

            val textInputs = splitText(text)
            val audioResults = mutableListOf<ByteArray>()

            for (inputText in textInputs) {
                val input = SynthesisInput.newBuilder()
                    .setText(inputText)
                    .build()
                val response = client.synthesizeSpeech(
                    input, voiceBuilder.build(), audioConfig.build()
                )
                audioResults.add(response.audioContent.toByteArray())
            }

            val byteArrayOutputStream = ByteArrayOutputStream()

            for (audioResult in audioResults) {
                byteArrayOutputStream.write(audioResult)
            }

            return@withContext byteArrayOutputStream.toByteArray()
        } catch (e: ApiException) {
            println("An API error occurred: ${e.message}")
            throw e
        } catch (e: StatusRuntimeException) {
            println("An API error occurred: ${e.message}")
            throw e
        }
    }
}