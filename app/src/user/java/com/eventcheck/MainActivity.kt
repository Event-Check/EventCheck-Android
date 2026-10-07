package com.eventcheck

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.eventcheck.navigation.AppNavHost
import com.eventcheck.ui.theme.EventCheckTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventCheckTheme {
                var showAnimation by remember { mutableStateOf(true) }

                if (showAnimation) {
                    MainScreen(onAnimationFinished = { showAnimation = false })
                } else {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun MainScreen(onAnimationFinished: () -> Unit) {
    val context = LocalContext.current
    val window = (context as? Activity)?.window

    if (window != null) {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        DisposableEffect(Unit) {
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            onDispose {
                controller.show(WindowInsetsCompat.Type.statusBars())
                controller.isAppearanceLightStatusBars = true
                controller.isAppearanceLightNavigationBars = true
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_user_animation))

        val customTypeface = remember {
            ResourcesCompat.getFont(context, R.font.sf_pro_display_bold) ?: Typeface.DEFAULT
        }

        // Comprehensive Font Map to map ANY font requested by the Lottie file to our font
        val fontMap = remember(customTypeface) {
            mapOf(
                "SF Pro Rounded" to customTypeface,
                "SFProRounded-Regular" to customTypeface,
                "SF Pro Display" to customTypeface,
                "SF Pro" to customTypeface,
                "Audiowide" to customTypeface,
                "Audiowide Regular" to customTypeface,
                "sf_pro_display_bold" to customTypeface,
                "sans-serif" to customTypeface,
            )
        }

        LottieAnimation(
            composition = composition,
            iterations = 1,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
            fontMap = fontMap,
        )

        LaunchedEffect(composition) {
            val comp = composition
            if (comp != null) {
                kotlinx.coroutines.delay(comp.duration.toLong().milliseconds)
                onAnimationFinished()
            }
        }
    }
}
