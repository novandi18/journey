package com.novandi.journey.presentation.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.novandi.journey.presentation.service.NotificationService
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.viewmodel.MainViewModel
import com.novandi.utility.service.isServiceRunning
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        Handler(Looper.getMainLooper()).postDelayed({
            keepSplashOnScreen = false
        }, 2000L)

        lifecycleScope.launch {
            setContent {
                val context = LocalContext.current
                val roleId by viewModel.roleId.observeAsState()
                if (!isServiceRunning(context, NotificationService::class.java) && roleId == 1) {
                    val token by viewModel.token.observeAsState()
                    val userId by viewModel.userId.observeAsState()
                    if (
                        (token != null && token!!.isNotEmpty()) &&
                        (userId != null && userId!!.isNotEmpty())
                    ) {
                        val service = Intent(context, NotificationService::class.java)
                        val bundle = Bundle()
                        bundle.putString(NotificationService.TOKEN, token)
                        bundle.putString(NotificationService.USER_ID, userId)
                        service.putExtras(bundle)
                        context.startService(service)
                    }
                }

                JourneyTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Box(modifier = Modifier.safeDrawingPadding()) {
                            JourneyApp(
                                startedDestination = viewModel.startedDestination.collectAsState().value
                            )
                        }
                    }
                }
            }
        }
    }
}