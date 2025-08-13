package com.example.monlauncher

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin


class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // If you created a custom theme wrapper, replace this with MonLauncherTheme { ... }
            MaterialTheme {
                val allApps by vm.allApps.collectAsStateWithLifecycle()
                val pinned by vm.pinnedPackages.collectAsStateWithLifecycle()

                val toShow = remember(allApps, pinned) {
                    if (pinned.isEmpty()) allApps
                    else pinned.mapNotNull { pkg -> allApps.find { it.packageName == pkg } }
                }

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("MonLauncher") },
                            actions = {
                                IconButton(onClick = {
                                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                                }) {
                                    Icon(Icons.Default.Settings, contentDescription = "Réglages")
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = MaterialTheme.colorScheme.onSurface,
                                actionIconContentColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                ) { inner ->
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(inner)
                    ) {
                        RadialDock(
                            apps = toShow,
                            onLaunch = { pkg -> launchPackage(pkg) },
                            pageSize = 10
                        )
                    }
                }
            }
        }
    }

    private fun launchPackage(packageName: String) {
        val launch = packageManager.getLaunchIntentForPackage(packageName)
            ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (launch != null) startActivity(launch)
    }
}

@Composable
fun RadialDock(
    apps: List<AppEntry>,
    onLaunch: (String) -> Unit,
    pageSize: Int = 10
) {
    var expanded by remember { mutableStateOf(false) }
    val pages = remember(apps, pageSize) { apps.chunked(pageSize) }
    var pageIndex by rememberSaveable { mutableStateOf(0) }
    val pageCount = pages.size.coerceAtLeast(1)
    pageIndex = pageIndex.coerceIn(0, pageCount - 1)
    val currentPage = pages.getOrElse(pageIndex) { emptyList() }

    // WOW anim
    val radius = 180.dp
    val arcDegrees = 140f
    val startDeg = -70f
    val rippleProgress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "ripple"
    )

    Box(Modifier.fillMaxSize()) {
        // Right dock handle
        Box(
            Modifier
                .fillMaxHeight()
                .padding(end = 12.dp)
                .wrapContentWidth(Alignment.End),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(horizontalAlignment = Alignment.End) {
                if (expanded && pageCount > 1) {
                    FloatingActionButton(
                        onClick = { pageIndex = (pageIndex - 1 + pageCount) % pageCount },
                        modifier = Modifier.padding(bottom = 8.dp).size(40.dp)
                    ) { Icon(Icons.Default.ExpandLess, contentDescription = "Prev page") }
                }
                FloatingActionButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.Menu, contentDescription = "Dock")
                }
                if (expanded && pageCount > 1) {
                    FloatingActionButton(
                        onClick = { pageIndex = (pageIndex + 1) % pageCount },
                        modifier = Modifier.padding(top = 8.dp).size(40.dp)
                    ) { Icon(Icons.Default.ExpandMore, contentDescription = "Next page") }
                }
            }
        }

        // Ripple canvas
        if (expanded || rippleProgress > 0f) {
            val config = LocalConfiguration.current
            val density = LocalDensity.current
            val centerX = with(density) { (config.screenWidthDp.dp - 56.dp).toPx() }
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
            val centerX = with(density) { (config.screenWidthDp.dp - 56.dp).toPx() }
            val centerY = with(density) { (config.screenHeightDp.dp / 2).toPx() }
            val rPx = with(density) { radius.toPx() }

            currentPage.forEachIndexed { i, app ->
                val angleDeg = startDeg + i * step
                val angle = Math.toRadians(angleDeg.toDouble())
                val targetX = centerX - (cos(angle) * rPx).toFloat()
                val targetY = centerY + (sin(angle) * rPx).toFloat()

                val anim = remember(app.packageName) { Animatable(0f) }
                LaunchedEffect(expanded, pageIndex) {
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

                val bmp = remember(app.packageName) { app.icon.toBitmap(112, 112).asImageBitmap() }
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
                        .clickable { onLaunch(app.packageName) }
                        .padding(8.dp)
                ) {
                    androidx.compose.foundation.Image(
                        bitmap = bmp,
                        contentDescription = app.label,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            if (pageCount > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pageCount) { idx ->
                        val active = idx == pageIndex
                        Box(
                            Modifier
                                .padding(3.dp)
                                .size(if (active) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (active) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                                )
                                .clickable { pageIndex = idx }
                        )
                    }
                }
            }
        }
    }
}
