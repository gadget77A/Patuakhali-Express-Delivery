package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.theme.*

@Composable
fun LiveTrackingMap(
    order: Order,
    isBn: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 14f,
        targetValue = 44f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    val isRiderAssigned = order.riderId != null

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Color(0xFFF1F5F2))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f), RoundedCornerShape(22.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Payra River curve
            val riverPath = Path().apply {
                moveTo(0f, h * 0.22f)
                cubicTo(
                    w * 0.35f, h * 0.12f,
                    w * 0.65f, h * 0.40f,
                    w, h * 0.28f
                )
                lineTo(w, h * 0.46f)
                cubicTo(
                    w * 0.65f, h * 0.58f,
                    w * 0.35f, h * 0.28f,
                    0f, h * 0.38f
                )
                close()
            }
            drawPath(riverPath, color = Color(0xFFBAE6FD).copy(alpha = 0.85f))

            // 2. City Road Grid
            val roadColor = Color(0xFFD1D5DB)
            drawLine(roadColor, Offset(0f, h * 0.68f), Offset(w, h * 0.68f), strokeWidth = 6f)
            drawLine(roadColor, Offset(0f, h * 0.88f), Offset(w, h * 0.88f), strokeWidth = 4f)
            drawLine(roadColor, Offset(w * 0.20f, 0f), Offset(w * 0.20f, h), strokeWidth = 5f)
            drawLine(roadColor, Offset(w * 0.50f, h * 0.42f), Offset(w * 0.50f, h), strokeWidth = 7f)
            drawLine(roadColor, Offset(w * 0.80f, 0f), Offset(w * 0.80f, h), strokeWidth = 5f)

            // Payra Bridge connection
            drawLine(
                Color(0xFF64748B),
                Offset(w * 0.50f, h * 0.18f),
                Offset(w * 0.50f, h * 0.42f),
                strokeWidth = 9f,
                cap = StrokeCap.Round
            )

            // 3. Delivery Route
            val startX = w * 0.18f
            val startY = h * 0.68f
            val endX = w * 0.82f
            val endY = h * 0.68f

            // Full path line
            drawLine(
                color = Color(0xFF94A3B8),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 7f,
                cap = StrokeCap.Round
            )

            val currentProgress = order.riderProgress.coerceIn(0f, 1f)
            val riderX = startX + (endX - startX) * currentProgress
            val riderY = startY

            if (isRiderAssigned) {
                // Route covered so far in bold brand green
                drawLine(
                    color = ExpressGreenPrimary,
                    start = Offset(startX, startY),
                    end = Offset(riderX, riderY),
                    strokeWidth = 7f,
                    cap = StrokeCap.Round
                )

                // Rider pulsing sonar radar
                drawCircle(
                    color = ExpressOrangeAccent.copy(alpha = pulseAlpha),
                    radius = pulseRadius,
                    center = Offset(riderX, riderY)
                )

                // Rider center icon dot
                drawCircle(
                    color = ExpressOrangeAccent,
                    radius = 12f,
                    center = Offset(riderX, riderY)
                )
                drawCircle(
                    color = Color.White,
                    radius = 5f,
                    center = Offset(riderX, riderY)
                )
            } else {
                // If unassigned: Pulse at Shop to show search
                drawCircle(
                    color = Color(0xFF3B82F6).copy(alpha = pulseAlpha),
                    radius = pulseRadius * 1.3f,
                    center = Offset(startX, startY)
                )
            }

            // Origin Store Marker
            drawCircle(
                color = ExpressGreenDark,
                radius = 10f,
                center = Offset(startX, startY)
            )
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = Offset(startX, startY)
            )

            // Customer Destination Marker
            drawCircle(
                color = Color(0xFFEF4444),
                radius = 10f,
                center = Offset(endX, endY)
            )
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = Offset(endX, endY)
            )
        }

        // Top Left Map Area Badge
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .background(Color.White.copy(alpha = 0.94f), RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0284C7))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isBn) "পায়রা সেতু ও সদর ম্যাপ" else "Payra Bridge & Sadar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            }
        }

        // Top Right Live Telemetry or Searching Pill
        if (isRiderAssigned) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .shadow(4.dp, RoundedCornerShape(14.dp))
                    .background(ExpressGreenPrimary, RoundedCornerShape(14.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBike,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val remainingKm = String.format("%.1f", (1.0f - order.riderProgress) * 2.5f)
                    Text(
                        text = if (isBn) "২৮ কিমি/ঘণ্টা • $remainingKm কিমি বাকি" else "28 km/h • $remainingKm km left",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .shadow(4.dp, RoundedCornerShape(14.dp))
                    .background(Color(0xFF2563EB), RoundedCornerShape(14.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isBn) "নিকটস্থ রাইডার খোঁজা হচ্ছে..." else "Finding nearby rider...",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Bottom Shop -> Customer Route Strip
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        tint = ExpressGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = if (isBn) "পিকআপ পয়েন্ট" else "Pickup",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (isBn) order.shopNameBn else order.shopNameEn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }

                Text(
                    text = "➔",
                    color = ExpressOrangeAccent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = if (isBn) "ডেলিভারি ঠিকানা" else "Delivery",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = order.deliveryAddressBn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
