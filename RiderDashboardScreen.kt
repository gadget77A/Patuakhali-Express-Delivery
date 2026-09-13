package com.example.ui.rider

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.components.LiveTrackingMap
import com.example.ui.theme.*
import com.example.ui.util.Strings

@Composable
fun RiderDashboardScreen(
    isBn: Boolean
) {
    val riderProfile by DeliveryRepository.riderProfile.collectAsState()
    val orders by DeliveryRepository.orders.collectAsState()
    var toastMsg by remember { mutableStateOf<String?>(null) }

    // Active order assigned to this rider
    val activeTask = orders.firstOrNull {
        it.riderId == riderProfile.id && it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED
    }

    // Unassigned requests available for pickup (orders placed by customers without a rider)
    val availableRequests = orders.filter {
        (it.status == OrderStatus.CONFIRMED || it.status == OrderStatus.PREPARING) && it.riderId == null
    }

    // Completed deliveries by this rider
    val completedOrders = orders.filter {
        it.riderId == riderProfile.id && it.status == OrderStatus.DELIVERED
    }

    Scaffold(
        snackbarHost = {
            if (toastMsg != null) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { toastMsg = null }) {
                            Text(if (isBn) "ঠিক আছে" else "OK", color = Color.White)
                        }
                    }
                ) {
                    Text(toastMsg!!)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("rider_dashboard_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Online / Offline Status Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (riderProfile.isOnline) ExpressGreenContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(if (riderProfile.isOnline) ExpressGreenPrimary else Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (riderProfile.isOnline) {
                                        if (isBn) "রাইডার অনলাইন (ডিউটি চালু)" else "Rider Online (On Duty)"
                                    } else {
                                        if (isBn) "রাইডার অফলাইন (বিরতিতে)" else "Rider Offline (On Break)"
                                    },
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (riderProfile.isOnline) ExpressGreenDark else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${riderProfile.nameBn} • ${riderProfile.vehicleNumber}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = riderProfile.isOnline,
                            onCheckedChange = { DeliveryRepository.toggleRiderOnline() },
                            colors = SwitchDefaults.colors(checkedThumbColor = ExpressGreenPrimary)
                        )
                    }
                }
            }

            // Rider Earnings Metrics Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Today's Earnings
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isBn) "আজকের মোট আয়" else "Today's Earnings",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = Strings.currency(riderProfile.todayEarnings),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ExpressGreenPrimary
                            )
                            Text(
                                text = "${riderProfile.completedTripsToday} ${if (isBn) "টি ট্রিপ সম্পন্ন" else "trips done"}",
                                fontSize = 10.sp,
                                color = ExpressGreenLight
                            )
                        }
                    }

                    // Weekly Earnings
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isBn) "সাপ্তাহিক আয়" else "Weekly Total",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = Strings.currency(riderProfile.weeklyEarnings),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ExpressOrangeAccent
                            )
                            Text(
                                text = if (isBn) "রেটিং: ★ 4.9 (৫০০+)" else "Rating: ★ 4.9 (500+)",
                                fontSize = 10.sp,
                                color = Color(0xFFD97706),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Active Delivery Task Section (if rider is currently handling an order)
            if (activeTask != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, ExpressOrangeAccent.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(ExpressOrangeAccent)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isBn) "চলমান ডেলিভারি টাস্ক" else "Active Delivery Task",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ExpressOrangeAccent
                                    )
                                }

                                Text(
                                    text = activeTask.id,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Pickup Shop
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Store,
                                    contentDescription = null,
                                    tint = ExpressGreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isBn) "পিকআপ: ${activeTask.shopNameBn}" else "Pickup: ${activeTask.shopNameEn}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = activeTask.pickupAddressBn,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Delivery Customer
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "${if (isBn) "গ্রাহক:" else "Customer:"} ${activeTask.customerName}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = activeTask.deliveryAddressBn,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Mini-Map Route
                            LiveTrackingMap(
                                order = activeTask,
                                isBn = isBn,
                                modifier = Modifier.height(190.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action Buttons: Call customer, Map navigation
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { toastMsg = "গ্রাহককে কল করা হচ্ছে: ${activeTask.customerPhone}" },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isBn) "কল করুন" else "Call", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = { toastMsg = "পটুয়াখালী জিপিএস টার্ন-বাই-টার্ন নেভিগেশন চালু হয়েছে" },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isBn) "নেভিগেশন" else "Navigate", fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Status Progression Workflow
                            when (activeTask.status) {
                                OrderStatus.PICKED_UP -> {
                                    Button(
                                        onClick = {
                                            DeliveryRepository.updateOrderStatus(activeTask.id, OrderStatus.ON_THE_WAY)
                                            toastMsg = "🛵 আপনি গন্তব্যের উদ্দেশ্যে রওনা হয়েছেন!"
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ExpressOrangeAccent),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = if (isBn) "🛵 গন্তব্যের উদ্দেশ্যে রওনা হন (On The Way)" else "🛵 Start Delivery (On The Way)",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                OrderStatus.ON_THE_WAY -> {
                                    Button(
                                        onClick = {
                                            DeliveryRepository.updateOrderStatus(activeTask.id, OrderStatus.DELIVERED)
                                            toastMsg = "🎉 অভিনন্দন! ডেলিভারি সম্পন্ন হয়েছে এবং ৳৫০ আপনার ব্যালেন্সে যোগ হয়েছে।"
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = if (isBn) "✅ ডেলিভারি সম্পন্ন করুন (+৳৫০)" else "✅ Mark Delivered (+৳50)",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                else -> {
                                    Button(
                                        onClick = {
                                            DeliveryRepository.updateOrderStatus(activeTask.id, OrderStatus.PICKED_UP)
                                            toastMsg = "📦 পার্সেল পিকআপ সম্পন্ন হয়েছে!"
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = if (isBn) "📦 দোকান থেকে পার্সেল পিকআপ করুন" else "📦 Pick Up from Store",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Available Delivery Requests (Live synchronized from Customer orders)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBn) "নতুন ডেলিভারি অনুরোধ (${availableRequests.size})" else "Available Delivery Requests (${availableRequests.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (availableRequests.isNotEmpty()) {
                        Text(
                            text = if (isBn) "সরাসরি গ্রহণ করুন" else "Accept Now",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ExpressGreenPrimary
                        )
                    }
                }
            }

            if (availableRequests.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🛵", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isBn) "বর্তমানে কোনো অপেক্ষমান অর্ডার নেই।" else "No pending requests in your area right now.",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isBn) "গ্রাহক ভিউ থেকে নতুন অর্ডার প্লেস করলে এখানে সাথে সাথে চলে আসবে।" else "Orders placed from Customer view will appear here instantly.",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            } else {
                items(availableRequests) { req ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ExpressGreenPrimary.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = req.id,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ExpressGreenContainer)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "+ ৳৫০ ফি",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ExpressGreenDark
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${if (isBn) "পিকআপ:" else "Pickup:"} ${if (isBn) req.shopNameBn else req.shopNameEn}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "${if (isBn) "গন্তব্য:" else "Dropoff:"} ${req.deliveryAddressBn}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    DeliveryRepository.acceptRiderOrder(req.id)
                                    toastMsg = "🎉 আপনি অর্ডার ${req.id} সফলভাবে গ্রহণ করেছেন!"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isBn) "অর্ডার গ্রহণ করুন (Accept & Pick Up)" else "Accept & Pick Up",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Completed Deliveries Today
            item {
                Text(
                    text = if (isBn) "আজকের সম্পন্ন ডেলিভারি ইতিহাস" else "Completed Deliveries Today",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(completedOrders) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(order.id, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(order.deliveryAddressBn, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = "+ ৳৫০",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = ExpressGreenPrimary
                        )
                    }
                }
            }
        }
    }
}
