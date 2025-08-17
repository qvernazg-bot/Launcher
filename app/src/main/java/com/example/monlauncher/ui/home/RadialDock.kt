package com.example.monlauncher.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.monlauncher.data.files.FileRepository
import kotlinx.coroutines.delay
import java.io.File
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RadialDock(
    repository: FileRepository = remember { FileRepository() },
    startDir: File = repository.rootDir
) {
    var expanded by remember { mutableStateOf(false) }
    var currentDir by remember { mutableStateOf(startDir) }
    var currentPage by remember(currentDir) {
        mutableStateOf(repository.children(currentDir))
    }

    val radius = 180.dp
    val arcDegrees = 140f
    val startDeg = -70f
    val rippleProgress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "ripple"
    )

    var rotation by remember { mutableStateOf(0f) }

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(expanded) {
                if (expanded) {
                    detectDragGestures { change, drag ->
                        rotation += drag.x
                        change.consume()
                    }
                }
            }
    ) {
        Box(modifier = Modifier.align(Alignment.Center)) {
            FloatingActionButton(onClick = {
                if (expanded && currentDir != repository.rootDir) {
                    currentDir.parentFile?.let {
                        currentDir = it
                        currentPage = repository.children(currentDir)
                    }
                } else {
                    expanded = !expanded
                }
            }) {
                val icon = if (currentDir == repository.rootDir) Icons.Default.Menu else Icons.Default.Folder
                Icon(icon, contentDescription = "Dock")
            }
        }

        if (expanded || rippleProgress > 0f) {
            val config = LocalConfiguration.current
            val density = LocalDensity.current
            val centerX = with(density) { (config.screenWidthDp.dp / 2).toPx() }
            val centerY = with(density) { (config.screenHeightDp.dp / 2).toPx() }
            val rPx = with(density) { (radius + 40.dp).toPx() }
            Canvas(modifier = Modifier.fillMaxSize()) {
                val p1 = rippleProgress
                val p2 = (rippleProgress * 0.8f).coerceIn(0f, 1f)
                drawCircle(
                    color = Color(0xFF6750A4).copy(alpha = 0.12f * (1 - p1)),
                    radius = rPx * p1,
                    center = Offset(centerX, centerY)
                )
                drawCircle(
                    color = Color(0xFF6750A4).copy(alpha = 0.08f * (1 - p2)),
                    radius = rPx * p2,
                    center = Offset(centerX, centerY)
                )
            }
        }

        if (expanded) {
            val count = currentPage.size
            val step = if (count > 1) arcDegrees / (count - 1) else 0f

            val config = LocalConfiguration.current
            val density = LocalDensity.current
            val centerX = with(density) { (config.screenWidthDp.dp / 2).toPx() }
            val centerY = with(density) { (config.screenHeightDp.dp / 2).toPx() }
            val rPx = with(density) { radius.toPx() }

            currentPage.forEachIndexed { i, file ->
                val angleDeg = startDeg + rotation / 5f + i * step
                val angle = Math.toRadians(angleDeg.toDouble())
                val targetX = centerX - (cos(angle) * rPx).toFloat()
                val targetY = centerY + (sin(angle) * rPx).toFloat()

                val anim = remember(file.absolutePath) { Animatable(0f) }
                LaunchedEffect(expanded) {
                    if (expanded) {
                        delay(i * 35L)
                        anim.animateTo(
                            1f,
                            spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow)
                        )
                    } else {
                        anim.snapTo(0f)
                    }
                }
                val x = centerX + (targetX - centerX) * anim.value
                val y = centerY + (targetY - centerY) * anim.value
                val scale = 0.7f + 0.35f * anim.value
                val alpha = anim.value
                val rotation = (1f - anim.value) * 18f

                Box(
                    Modifier
                        .offset { IntOffset(x.toInt() - 28, y.toInt() - 28) }
                        .size(56.dp)
                        .graphicsLayer {
                            this.alpha = alpha
                            this.scaleX = scale
                            this.scaleY = scale
                            this.rotationZ = rotation
                        }
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            if (file.isDirectory) {
                                currentDir = file
                                currentPage = repository.children(currentDir)
                            }
                        }
                        .padding(8.dp),
                ) {
                    val icon = if (file.isDirectory) Icons.Default.Folder else Icons.Default.InsertDriveFile
                    Icon(icon, contentDescription = file.name, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
