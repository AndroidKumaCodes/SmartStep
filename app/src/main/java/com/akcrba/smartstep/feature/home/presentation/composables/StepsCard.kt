package com.akcrba.smartstep.feature.home.presentation.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akcrba.smartstep.R
import java.text.NumberFormat

// Should be defined as constant to avoid magic numbers inside the composable
private val CardBackgroundColor = Color(0xFF3B45B8)

@Composable
internal fun StepsCard(
    steps: Int,
    goal: Int,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp),
) {
    // Optimization: Calculate progress once. coerceIn ensures we don't crash or draw weirdly if steps > goal
    val progress = if (goal > 0) {
        (steps.toFloat() / goal.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }

    Surface(
        modifier = modifier
            .shadow(
                elevation = 18.dp,
                shape = shape,
                ambientColor = Color.Black.copy(alpha = 0.22f),
                spotColor = Color.Black.copy(alpha = 0.22f),
            ),
        shape = shape,
        color = CardBackgroundColor,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Icon badge
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.16f),
            ) {
                Icon(
                    painter = painterResource(R.drawable.sneakers),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(12.dp),
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = steps.formatWithComma(),
                color = Color.White,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1).sp,
            )

            Text(
                text = "/$goal Steps",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
            )

            Spacer(Modifier.height(20.dp))

            CustomLinearProgressIndicator(progress = progress)
        }
    }
}

@Composable
private fun CustomLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    height: Dp = 14.dp,
    shape: Shape = RoundedCornerShape(percent = 50),
    backgroundColor: Color = Color.White.copy(alpha = 0.12f),
    indicatorColor: Color = Color.White,
    animationDuration: Int = 1000,
) {
    // 1. Maintain a local state for the animation target
    var currentProgress by remember { mutableFloatStateOf(0f) }

    // 2. Trigger the state change when the 'progress' parameter changes or on load
    LaunchedEffect(progress) {
        currentProgress = progress
    }

    // Create an animated float state that interpolates to the new progress value
    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(durationMillis = animationDuration),
        label = "ProgressAnimation",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(backgroundColor),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = animatedProgress)
                .fillMaxHeight()
                .padding(3.dp)
                .clip(shape)
                .background(indicatorColor),
        )
    }
}

private fun Int.formatWithComma(): String = NumberFormat.getIntegerInstance().format(this)

@Preview(name = "StepsCard", showBackground = true, backgroundColor = 0xFFF2F2F7)
@Composable
private fun StepsCardPreview() {
    StepsCard(
        steps = 4523,
        goal = 10000,
    )
}
