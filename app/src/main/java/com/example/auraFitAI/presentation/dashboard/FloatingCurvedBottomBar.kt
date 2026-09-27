package com.example.auraFitAI.presentation.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auraFitAI.R

enum class NavDestination(val routeId: Int, val title: String, val icon: ImageVector) {
    EXPLORE(R.id.exploreFragment, "Explore", Icons.Default.Explore),
    HOME(R.id.homeFragment, "Home", Icons.Default.Home),
    AI_COACH(R.id.aiCoachFragment, "AI Coach", Icons.Default.FitnessCenter),
    PROFILE(R.id.profileFragment, "Profile", Icons.Default.Person)
}

@Composable
fun FloatingCurvedBottomBar(
    currentDestinationId: Int,
    onItemSelected: (NavDestination) -> Unit
) {
    val destinations = listOf(
        NavDestination.EXPLORE,
        NavDestination.HOME,
        NavDestination.AI_COACH,
        NavDestination.PROFILE
    )

    val selectedIndex = destinations.indexOfFirst { it.routeId == currentDestinationId }.coerceAtLeast(0)

    val isDark = isSystemInDarkTheme()
    val surfaceColor = if (isDark) Color(0xFF161820) else Color.White
    val inactiveColor = if (isDark) Color(0xFFA7A9B2) else Color(0xFF5D606E)
    val activeTextColor = if (isDark) Color(0xFFD0FD3E) else Color(0xFF1B5E20)

    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFD0FD3E), // Electric Lime
            Color(0xFF00C99E), // Button Start
            Color(0xFF008A57)  // Gradient End
        )
    )

    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "navIndexAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .height(90.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .shadow(16.dp, RoundedCornerShape(36.dp), spotColor = Color.Black.copy(alpha = 0.15f))
        ) {
            val width = size.width
            val height = size.height
            val itemWidth = width / destinations.size
            val centerXPx = (animatedIndex + 0.5f) * itemWidth

            val path = Path().apply {
                val cornerRadius = 36.dp.toPx()
                val bumpRadius = 36.dp.toPx()

                moveTo(cornerRadius, 0f)
                
                val waveWidth = 70.dp.toPx()
                val waveStart = (centerXPx - waveWidth).coerceAtLeast(cornerRadius)
                val waveEnd = (centerXPx + waveWidth).coerceAtMost(width - cornerRadius)

                lineTo(waveStart, 0f)

                cubicTo(
                    centerXPx - waveWidth * 0.5f, 0f,
                    centerXPx - bumpRadius * 0.8f, -bumpRadius * 0.9f,
                    centerXPx, -bumpRadius * 0.95f
                )
                cubicTo(
                    centerXPx + bumpRadius * 0.8f, -bumpRadius * 0.9f,
                    centerXPx + waveWidth * 0.5f, 0f,
                    waveEnd, 0f
                )

                lineTo(width - cornerRadius, 0f)
                quadraticBezierTo(width, 0f, width, cornerRadius)
                lineTo(width, height - cornerRadius)
                quadraticBezierTo(width, height, width - cornerRadius, height)
                lineTo(cornerRadius, height)
                quadraticBezierTo(0f, height, 0f, height - cornerRadius)
                lineTo(0f, cornerRadius)
                quadraticBezierTo(0f, 0f, cornerRadius, 0f)
                close()
            }

            drawPath(
                path = path,
                color = surfaceColor
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            destinations.forEachIndexed { index, destination ->
                val isSelected = index == selectedIndex
                
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onItemSelected(destination) },
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .offset(y = (-28).dp)
                                .size(56.dp)
                                .shadow(8.dp, CircleShape, spotColor = Color(0xFF00C99E).copy(alpha = 0.4f))
                                .background(gradientBrush, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.title,
                                tint = Color.Black,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Text(
                            text = destination.title,
                            color = activeTextColor,
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 6.dp)
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.title,
                                tint = inactiveColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = destination.title,
                                color = inactiveColor,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
