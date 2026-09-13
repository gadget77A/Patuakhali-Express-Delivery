package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.ExpressGreenPrimary
import com.example.ui.theme.ExpressOrangeAccent
import com.example.ui.util.Strings

@Composable
fun OrderHistoryScreen(
    isBn: Boolean,
    onTrackOrder: (String) -> Unit
) {
    val orders by DeliveryRepository.orders.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("orders_history_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBn) "আমার অর্ডার সমূহ" else "My Orders",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${orders.size} ${if (isBn) "টি মোট অর্ডার" else "Total orders"}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (orders.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📋", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isBn) "আপনার কোনো পূর্ববর্তী অর্ডার নেই" else "No order history found",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(orders) { order ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTrackOrder(order.id) }
                        .testTag("order_item_${order.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = order.id,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = order.orderTime,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Status Tag
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when (order.status) {
                                            OrderStatus.DELIVERED -> ExpressGreenPrimary.copy(alpha = 0.15f)
                                            OrderStatus.ON_THE_WAY -> ExpressOrangeAccent.copy(alpha = 0.15f)
                                            OrderStatus.CANCELLED -> MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                                            else -> Color(0xFF3B82F6).copy(alpha = 0.15f)
                                        }
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = Strings.statusTitle(order.status, isBn),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (order.status) {
                                        OrderStatus.DELIVERED -> ExpressGreenPrimary
                                        OrderStatus.ON_THE_WAY -> ExpressOrangeAccent
                                        OrderStatus.CANCELLED -> MaterialTheme.colorScheme.error
                                        else -> Color(0xFF3B82F6)
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isBn) order.shopNameBn else order.shopNameEn,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ExpressGreenPrimary
                        )

                        Text(
                            text = order.items.joinToString(", ") { "${if (isBn) it.nameBn else it.nameEn} (${it.quantity})" },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${if (isBn) "মোট বিল:" else "Total:"} ${Strings.currency(order.totalAmount)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Button(
                                onClick = { onTrackOrder(order.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GpsFixed,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBn) "ট্র্যাকিং দেখুন" else "Track",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
