package com.example.mytranslatepractice

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var textEn by remember { mutableStateOf(TextFieldValue()) }
            val enKoTranslator = remember {
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.KOREAN)
                    .build()
                Translation.getClient(options)
            }
            val koJaTranslator = remember {
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.KOREAN)
                    .setTargetLanguage(TranslateLanguage.JAPANESE)
                    .build()
                Translation.getClient(options)
            }
            var enabled by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(Unit) {
                val conditions = DownloadConditions.Builder()
                    .requireWifi()
                    .build()
                enKoTranslator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        enabled = true
                    }
                koJaTranslator.downloadModelIfNeeded(conditions)
                    .addOnCanceledListener {
                        enabled = true
                    }
                    .addOnFailureListener { exception ->

                    }
            }
            var textKoTranslated by remember { mutableStateOf("") }
            var textJaTranslated by remember { mutableStateOf("") }

            val context = LocalContext.current
            val speechRecognizerLauncher =
            //registerForActivityResult 를 compose  내부에서 사용할 때에는
                //registerForActivityResult만 rememberLauncherForActivityResult 로 변경해서 사용하면 된다.
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == RESULT_OK && result.data != null) {
                        val matches =
                            result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        if (!matches.isNullOrEmpty()) {
                            textEn = TextFieldValue(text = matches[0])
                        }
                    }
                }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        TextField(
                            value = textEn,
                            onValueChange = { textEn = it },
                            label = { Text("번역할 영어를 입력하세요") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        )
                    }

                    item {
                        Button(
                            onClick = {
                                // 음성 인식을 시작
                                val speechRecognizerIntent =
                                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                                speechRecognizerIntent.putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                )
                                // 영어 언어 코드를 설정
                                speechRecognizerIntent.putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE,
                                    "en-US"
                                )
                                speechRecognizerLauncher.launch(speechRecognizerIntent)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(text = "음성 입력")
                        }
                    }

                    item {
                        Button(
                            onClick = {
                                // 영어 음성을 번역하고 한국어와 일본어로 번역
                                enKoTranslator.translate(textEn.text)
                                    .addOnSuccessListener { translatedKoText ->
                                        textKoTranslated = translatedKoText

                                        koJaTranslator.translate(translatedKoText)
                                            .addOnSuccessListener { translatedJaText ->
                                                textJaTranslated = translatedJaText
                                            }
                                    }
                            },
                            enabled = enabled,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "번역")
                        }
                    }

                    item {
                        Text(
                            text = "한국어 번역: $textKoTranslated\n일본어 번역: $textJaTranslated",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}