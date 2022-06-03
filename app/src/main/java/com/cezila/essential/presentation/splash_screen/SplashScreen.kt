package com.cezila.essential.presentation.splash_screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cezila.essential.R
import com.cezila.essential.presentation.destinations.DrinkScreenDestination
import com.cezila.essential.ui.theme.BackgroundColor
import com.cezila.essential.ui.theme.SoftPink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

@Composable
@Destination(start = true)
fun AnimatedSplash(
    navigator: DestinationsNavigator
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 3000
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000)
        navigator.navigate(
            DrinkScreenDestination()
        )
    }
    SplashScreen(alpha = alphaAnim.value)
}

@Composable
fun SplashScreen(alpha: Float) {

    val fontTitle = FontFamily(
        Font(R.font.gotham)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ESSENTIAL",
            color = SoftPink,
            fontSize = 50.sp,
            fontFamily = fontTitle,
            modifier = Modifier.alpha(alpha = alpha)
        )
        Image(
            painter = painterResource(R.drawable.logo_splash),
            modifier = Modifier
                .size(360.dp)
                .alpha(alpha = alpha),
            contentDescription = "Logo Icon"
        )
        Text(
            text = "Let's drink!",
            color = SoftPink,
            fontSize = 12.sp,
            fontFamily = fontTitle,
            modifier = Modifier.alpha(alpha = alpha)
        )
    }
}