package com.example.ui.admin

import androidx.compose.foundation.background
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
import com.example.model.PromoCode
import com.example.ui.theme.*
import com.example.ui.util.Strings

enum class AdminTab {
    OVERVIEW,
    ORDERS,
    MERCHANTS,
    SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    isBn: Boolean
) {
    var currentAdminTab by remember { mutableStateOf(AdminTab.OVERVIEW) }
    val orders by DeliveryRepository.orders.collectAsState()
    val shops by DeliveryRepository.shops.collectAsState()
    val riderProfile by DeliveryRepository.riderProfile.collectAsState()
    val promos by DeliveryRepository.availablePromos.collectAsState()
    var baseFee by remember { mutableStateOf("35") }

    val totalRevenue = orders.filter { it.status == OrderStatus.DELIVERED }.sumOf { it.totalAmount }
    val pendingCount = orders.count { it.status == OrderStatus.PENDING || it.status == OrderStatus.CONFIRMED }
    val activeCount = orders.count { it.status == OrderStatus.PREPARING || it.status == OrderStatus.PICKED_UP || it.status == OrderStatus.ON_THE_WAY }
    val deliveredCount = orders.count { it.status == OrderStatus.DELIVERED }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_dashboard_root")
    ) {
        // Admin Sub-tabs: Overview, Orders, Merchants, Settings
        ScrollableTabRow(
            selectedTabIndex = currentAdminTab.ordinal,
            containerColor = MaterialTheme.colorScheme.surface,
            edgePadding = 16.dp
        ) {
            Tab(
                selected = currentAdminTab == AdminTab.OVERVIEW,
                onClick = { currentAdminTab = AdminTab.OVERVIEW },
                text = { Text(if (isBn) "ওভারভিউ" else "Overview", fontSize = 12.sp) }
            )
            Tab(
                selected = currentAdminTab == AdminTab.ORDERS,
                onClick = { currentAdminTab = AdminTab.ORDERS },
                text = { Text(if (isBn) "অর্ডার (${orders.size})" else "Orders (${orders.size})", fontSize = 12.sp) }
            )
            Tab(
                selected = currentAdminTab == AdminTab.MERCHANTS,
                onClick = { currentAdminTab = AdminTab.MERCHANTS },
                text = { Text(if (isBn) "দোকান ও পার্টনার" else "Merchants", fontSize = 12.sp) }
            )
            Tab(
                selected = currentAdminTab == AdminTab.SETTINGS,
                onClick = { currentAdminTab = AdminTab.SETTINGS },
                text = { Text(if (isBn) "ফি ও প্রমোকোড" else "Fees & Promos", fontSize = 12.sp) }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (currentAdminTab) {
                AdminTab.OVERVIEW -> {
                    // Revenue & KPIs
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = ExpressGreenPrimary)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = if (isBn) "মোট সংগৃহীত রেভিনিউ" else "Total Platform Revenue",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = Strings.currency(totalRevenue),
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isBn) "পটুয়াখালী এক্সপ্রেস ডেলিভারি অ্যাডমিন কন্ট্রোল" else "Patuakhali Express Operations Panel",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    // Stat Metrics 2x2 Grid
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = if (isBn) "মোট অর্ডার" else "Total Orders",
                                value = "${orders.size}",
                                color = Color(0xFF3B82F6),
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = if (isBn) "অপেক্ষমান" else "Pending",
                                value = "$pendingCount",
                                color = Color(0xFFF59E0B),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricCard(
                                title = if (isBn) "চলমান ডেলিভারি" else "Active Deliveries",
                                value = "$activeCount",
                                color = ExpressOrangeAccent,
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = if (isBn) "সম্পন্ন অর্ডার" else "Completed",
                                value = "$deliveredCount",
                                color = ExpressGreenPrimary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Active Rider Telemetry Summary
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isBn) "সক্রিয় রাইডার মনিটর" else "Active Riders Monitor",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(riderProfile.nameBn, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                        Text(riderProfile.phone, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (riderProfile.isOnline) ExpressGreenPrimary.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = if (riderProfile.isOnline) "অনলাইন (ডিউটি)" else "অফলাইন",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (riderProfile.isOnline) ExpressGreenPrimary else Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                AdminTab.ORDERS -> {
                    // Order Management List with quick status changes
                    items(orders) { order ->
                        AdminOrderControlCard(
                            order = order,
                            isBn = isBn,
                            onStatusChange = { newStatus ->
                                DeliveryRepository.updateOrderStatus(order.id, newStatus)
                            }
                        )
                    }
                }

                AdminTab.MERCHANTS -> {
                    // Shop / Merchant Management
                    item {
                        Text(
                            text = if (isBn) "নিবন্ধিত দোকান ও রেস্তোরাঁ (${shops.size})" else "Registered Merchants (${shops.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(shops) { shop ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(shop.iconEmoji, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (isBn) shop.nameBn else shop.nameEn,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = shop.addressBn,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(ExpressGreenPrimary.copy(alpha = 0.12f))
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "খোলা",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ExpressGreenPrimary
                                    )
                                }
                            }
                        }
                    }
                }

                AdminTab.SETTINGS -> {
                    // Delivery fee settings
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isBn) "ডেলিভারি ফি সেটিংস" else "Delivery Fee Settings",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = baseFee,
                                    onValueChange = { baseFee = it },
                                    label = { Text(if (isBn) "শহর বেস ডেলিভারি চার্জ (৳)" else "Base Delivery Fee (BDT)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            }
                        }
                    }

                    // Promo Code Management
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (isBn) "সক্রিয় প্রোমোকোডসমূহ" else "Active Promo Codes",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                promos.forEach { promo ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(promo.code, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ExpressGreenPrimary)
                                            Text(if (isBn) promo.descriptionBn else promo.descriptionEn, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Text("${promo.discountPercent}% OFF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = color)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrderControlCard(
    order: com.example.model.Order,
    isBn: Boolean,
    onStatusChange: (OrderStatus) -> Unit
) {
    var expandedDropdown by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(order.id, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(Strings.currency(order.totalAmount), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = ExpressGreenPrimary)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${if (isBn) "গ্রাহক:" else "Customer:"} ${order.customerName} (${order.customerPhone})",
                fontSize = 11.sp
            )
            Text(
                text = "${if (isBn) "ঠিকানা:" else "Address:"} ${order.deliveryAddressBn}",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Status Control Dropdown
            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = it }
            ) {
                OutlinedTextField(
                    value = Strings.statusTitle(order.status, isBn),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(if (isBn) "স্ট্যাটাস পরিবর্তন" else "Change Status", fontSize = 10.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    OrderStatus.values().forEach { st ->
                        DropdownMenuItem(
                            text = { Text(Strings.statusTitle(st, isBn)) },
                            onClick = {
                                onStatusChange(st)
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }
        }
    }
}
