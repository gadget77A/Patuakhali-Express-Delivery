package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeliveryRepository
import com.example.model.OrderStatus
import com.example.model.UserRole
import com.example.ui.components.LiveTrackingMap
import com.example.ui.theme.*
import com.example.ui.util.Strings

@Composable
fun OrderTrackingScreen(
    orderId: String,
    isBn: Boolean,
    onBack: () -> Unit,
    onContactSupport: () -> Unit
) {
    val orders by DeliveryRepository.orders.collectAsState()
    val order = orders.find { it.id == orderId } ?: orders.firstOrNull()

    var showRatingDialog by remember { mutableStateOf(false) }
    var selectedStars by remember { mutableIntStateOf(5) }
    var reviewFeedback by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    if (order == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(if (isBn) "কোনো সক্রিয় অর্ডার পাওয়া যায়নি" else "No active order found")
        }
        return
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("tracking_back_button")) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = ExpressGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBn) "লাইভ ডেলিভারি ট্র্যাকিং" else "Live Delivery Tracking",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "অর্ডার নং: ${order.id}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when (order.status) {
                                    OrderStatus.ON_THE_WAY -> ExpressOrangeAccent.copy(alpha = 0.15f)
                                    OrderStatus.DELIVERED -> ExpressGreenPrimary.copy(alpha = 0.15f)
                                    OrderStatus.CANCELLED -> MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                                    else -> Color(0xFF3B82F6).copy(alpha = 0.15f)
                                }
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = Strings.statusTitle(order.status, isBn),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (order.status) {
                                OrderStatus.ON_THE_WAY -> ExpressOrangeAccent
                                OrderStatus.DELIVERED -> ExpressGreenPrimary
                                OrderStatus.CANCELLED -> MaterialTheme.colorScheme.error
                                else -> Color(0xFF3B82F6)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("tracking_scroll_view"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Live Interactive GPS Canvas Map
            item {
                LiveTrackingMap(
                    order = order,
                    isBn = isBn
                )
            }

            // ETA & Status Stepper Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isBn) "আনুমানিক ডেলিভারি সময়" else "Estimated Arrival Time",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = when (order.status) {
                                        OrderStatus.DELIVERED -> if (isBn) "ডেলিভারি সম্পন্ন হয়েছে ✅" else "Delivered Successfully ✅"
                                        OrderStatus.CANCELLED -> if (isBn) "অর্ডার বাতিল করা হয়েছে ❌" else "Order Cancelled ❌"
                                        OrderStatus.ON_THE_WAY -> if (isBn) "১০ - ১৫ মিনিট (পথে আছে)" else "10 - 15 Mins (On the way)"
                                        OrderStatus.PICKED_UP -> if (isBn) "১৫ - ২০ মিনিট (পিকআপ সম্পন্ন)" else "15 - 20 Mins (Picked up)"
                                        else -> if (isBn) "২০ - ২৫ মিনিট (প্রস্তুত হচ্ছে)" else "20 - 25 Mins (Preparing)"
                                    },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (order.status == OrderStatus.DELIVERED) ExpressGreenPrimary else ExpressOrangeAccent
                                )
                            }

                            if (order.status == OrderStatus.DELIVERED) {
                                Button(
                                    onClick = { showRatingDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(if (isBn) "রেটিং দিন" else "Rate Order", fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Status Progression Stepper
                        OrderTimelineStepper(status = order.status, isBn = isBn)
                    }
                }
            }

            // Rider Card: If assigned show profile; if not assigned show searching radar card
            item {
                if (order.riderName != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(ExpressGreenContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsBike,
                                    contentDescription = null,
                                    tint = ExpressGreenPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = order.riderName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = order.riderVehicle ?: "হোন্ডা সাইন ১২৫",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "★ 4.9",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD97706)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isBn) "৫০০+ ট্রিপ সম্পন্ন" else "500+ Trips",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Call & Chat buttons
                            Row {
                                FilledTonalIconButton(
                                    onClick = {
                                        toastMessage = if (isBn) "কল করা হচ্ছে: ${order.riderPhone}..." else "Calling ${order.riderPhone}..."
                                    },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Call",
                                        tint = ExpressGreenPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                FilledTonalIconButton(
                                    onClick = {
                                        toastMessage = if (isBn) "মেসেজ পাঠানো হয়েছে!" else "Message sent to rider!"
                                    },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Chat,
                                        contentDescription = "Message",
                                        tint = ExpressOrangeAccent,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Rider searching card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF3B82F6)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isBn) "নিকটস্থ রাইডার অনুসন্ধান করা হচ্ছে..." else "Finding nearest rider...",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E3A8A)
                                    )
                                    Text(
                                        text = if (isBn) "পটুয়াখালী সদরের রাইডারদের কাছে রিকোয়েস্ট পাঠানো হয়েছে" else "Request broadcasted to nearby Patuakhali riders",
                                        fontSize = 11.sp,
                                        color = Color(0xFF3B82F6)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Test shortcut to switch to rider role and accept
                            OutlinedButton(
                                onClick = {
                                    DeliveryRepository.setUserRole(UserRole.RIDER)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1D4ED8))
                            ) {
                                Icon(Icons.Default.DirectionsBike, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isBn) "রাইডার ভিউতে গিয়ে অর্ডারটি গ্রহণ করুন ➔" else "Switch to Rider View to Accept ➔",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Order Item Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isBn) "অর্ডারের বিবরণ" else "Order Details",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isBn) order.shopNameBn else order.shopNameEn,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ExpressGreenPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        order.items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(item.iconEmoji, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${if (isBn) item.nameBn else item.nameEn} x ${item.quantity}",
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    text = Strings.currency(item.price * item.quantity),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = if (isBn) "মোট প্রদেয় বিল" else "Total Amount", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                text = Strings.currency(order.totalAmount),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = ExpressGreenPrimary
                            )
                        }
                    }
                }
            }

            // Cancel Order Option (if still Pending or Confirmed)
            if (order.status == OrderStatus.PENDING || order.status == OrderStatus.CONFIRMED) {
                item {
                    OutlinedButton(
                        onClick = { DeliveryRepository.cancelOrder(order.id) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isBn) "অর্ডার বাতিল করুন" else "Cancel Order", fontSize = 13.sp)
                    }
                }
            }

            // Customer Support Helpline
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(onClick = onContactSupport)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = ExpressGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isBn) "সহায়তা বা অভিযোগ প্রয়োজন?" else "Need Help or Support?",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isBn) "পটুয়াখালী এক্সপ্রেস হটলাইন: 01700-000000" else "Patuakhali Express Hotline: 01700-000000",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
                }
            }
        }
    }

    // Rating Dialog
    if (showRatingDialog) {
        AlertDialog(
            onDismissRequest = { showRatingDialog = false },
            title = {
                Text(
                    text = if (isBn) "ডেলিভারি অভিজ্ঞতা কেমন ছিল?" else "How was the delivery experience?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isBn) "পটুয়াখালী এক্সপ্রেস রাইডার সেবা রেটিং দিন" else "Rate Patuakhali Express Rider",
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        (1..5).forEach { star ->
                            IconButton(onClick = { selectedStars = star }) {
                                Text(
                                    text = if (star <= selectedStars) "★" else "☆",
                                    fontSize = 32.sp,
                                    color = Color(0xFFF59E0B)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = reviewFeedback,
                        onValueChange = { reviewFeedback = it },
                        placeholder = { Text(if (isBn) "যেমন: খাবার খুব দ্রুত ও গরম এসেছে!" else "e.g. Fast and warm delivery!") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        DeliveryRepository.rateOrder(order.id, selectedStars, reviewFeedback)
                        showRatingDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary)
                ) {
                    Text(if (isBn) "জমা দিন" else "Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRatingDialog = false }) {
                    Text(if (isBn) "বাতিল" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun OrderTimelineStepper(status: OrderStatus, isBn: Boolean) {
    val steps = listOf(
        OrderStatus.CONFIRMED to (if (isBn) "নিশ্চিত" else "Confirmed"),
        OrderStatus.PREPARING to (if (isBn) "প্রস্তুত" else "Preparing"),
        OrderStatus.PICKED_UP to (if (isBn) "পিকআপ" else "Picked Up"),
        OrderStatus.ON_THE_WAY to (if (isBn) "পথে" else "On Way"),
        OrderStatus.DELIVERED to (if (isBn) "ডেলিভারি" else "Delivered")
    )

    val currentStepIndex = when (status) {
        OrderStatus.PENDING, OrderStatus.CONFIRMED -> 0
        OrderStatus.PREPARING -> 1
        OrderStatus.PICKED_UP -> 2
        OrderStatus.ON_THE_WAY -> 3
        OrderStatus.DELIVERED -> 4
        OrderStatus.CANCELLED -> -1
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, pair ->
            val isDone = index <= currentStepIndex
            val isCurrent = index == currentStepIndex

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> ExpressOrangeAccent
                                isDone -> ExpressGreenPrimary
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    } else {
                        Text("${index + 1}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = pair.second,
                    fontSize = 9.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = if (isDone) ExpressGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}
