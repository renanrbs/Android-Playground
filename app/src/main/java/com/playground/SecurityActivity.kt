package com.playground

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.playground.ui.theme.PlaygroundTheme
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets

class SecurityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            PlaygroundTheme{
                SecurityActivityContent()
            }
        }
    }

    @Composable
    fun SecurityActivityContent() {
        val state = remember { SecurityActivityState(applicationContext) }
        val sharedPrefKey = "secureTextKey"
        val secureFileName = "secureFile"

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Secure text: \"${state.secureText}\"",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { state.readStringFromSecureSharedPref(sharedPrefKey) }) {
                Text(text = "Read from SharedPrefs")
            }

            Button(onClick = { state.readStringFromSecureFile(secureFileName) }) {
                Text(text = "Read from secure file")
            }

            Button(onClick = { state.writeStringOnSecureSharedPref(sharedPrefKey, "text1") }) {
                Text(text = "Write text1 on SharedPrefs")
            }

            Button(onClick = { state.writeStringOnSecureSharedPref(sharedPrefKey, "text2") }) {
                Text(text = "Write text2 on SharedPrefs")
            }

            Button(onClick = { state.writeStringOnSecureFile(secureFileName, "text3") }) {
                Text(text = "Write text3 on secure file")
            }

            Button(onClick = { state.writeStringOnSecureFile(secureFileName, "text4") }) {
                Text(text = "Write text4 on secure file")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        PlaygroundTheme {
            SecurityActivityContent()
        }
    }

    private class SecurityActivityState(private val context: Context) {
        private val mainKey = MasterKey.Builder(context, masterKeyAlias)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        private val secureSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            context,
            secureSharedPreferencesName,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        var secureText: String by mutableStateOf("")

        fun writeStringOnSecureSharedPref(key:String, value: String) {
            with(secureSharedPreferences.edit()) {
                putString(key, value)
                apply()
            }
        }

        fun readStringFromSecureSharedPref(key: String): String {
            secureText = secureSharedPreferences.getString(key, "")!!
            return secureText
        }

        fun writeStringOnSecureFile(filename: String, fileContent: String) {
            val fileToWrite = "$filename.txt"
            val baseFile = File(context.filesDir, fileToWrite)

            if(baseFile.exists()) {
                baseFile.delete()
            }

            val encryptedFile = EncryptedFile.Builder(
                context,
                baseFile,
                mainKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            encryptedFile.openFileOutput().apply {
                write(fileContent.toByteArray(StandardCharsets.UTF_8))
                flush()
                close()
            }
        }

        fun readStringFromSecureFile(filename: String): String {
            val fileToRead = "$filename.txt"
            val encryptedFile = EncryptedFile.Builder(
                context,
                File(context.filesDir, fileToRead),
                mainKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            val inputStream = encryptedFile.openFileInput()
            val byteArrayOutputStream = ByteArrayOutputStream()
            var nextByte: Int = inputStream.read()
            while (nextByte != -1) {
                byteArrayOutputStream.write(nextByte)
                nextByte = inputStream.read()
            }

            val plaintext: ByteArray = byteArrayOutputStream.toByteArray()

            secureText = String(plaintext, StandardCharsets.UTF_8)

            return secureText
        }
    }

    companion object {
        private const val secureSharedPreferencesName = "secureSharedPrefs"
        private const val masterKeyAlias = "SecureActivityMasterKeyAlias"
    }
}