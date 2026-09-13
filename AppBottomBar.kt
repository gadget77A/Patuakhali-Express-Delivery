package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ExpressGreenPrimary
import com.example.ui.theme.ExpressOrangeAccent

enum class CustomerTab {
    HOME,
    SHOPS,
    TRACKING,
    ORDERS
}

@Composable
fun AppBottomBar(
    currentTab: CustomerTab,
    onTabSelected: (CustomerTab) -> Unit,
    isBn: Boolean,
    hasActiveOrder: Boolean
) {
    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .testTag("bottom_navigation_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        // Home
        NavigationBarItem(
            selected = currentTab == CustomerTab.HOME,
            onClick = { onTabSelected(CustomerTab.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = if (isBn) "হোম" else "Home",
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == CustomerTab.HOME) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ExpressGreenPrimary,
                selectedTextColor = ExpressGreenPrimary,
                indicatorColor = ExpressGreenPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_home")
        )

        // Shops
        NavigationBarItem(
            selected = currentTab == CustomerTab.SHOPS,
            onClick = { onTabSelected(CustomerTab.SHOPS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = "Shops",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = if (isBn) "দোকানপাট" else "Shops",
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == CustomerTab.SHOPS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ExpressGreenPrimary,
                selectedTextColor = ExpressGreenPrimary,
                indicatorColor = ExpressGreenPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_shops")
        )

        // Live Tracking
        NavigationBarItem(
            selected = currentTab == CustomerTab.TRACKING,
            onClick = { onTabSelected(CustomerTab.TRACKING) },
            icon = {
                BadgedBox(
                    badge = {
                        if (hasActiveOrder) {
                            Badge(
                                containerColor = ExpressOrangeAccent,
                                contentColor = Color.White
                            ) {
                                Text("GPS", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = "Tracking",
                        modifier = Modifier.size(22.dp)
                    )
                }
            },
            label = {
                Text(
                    text = if (isBn) "ট্র্যাকিং" else "Tracking",
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == CustomerTab.TRACKING) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ExpressGreenPrimary,
                selectedTextColor = ExpressGreenPrimary,
                indicatorColor = ExpressGreenPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_tracking")
        )

        // Orders
        NavigationBarItem(
            selected = currentTab == CustomerTab.ORDERS,
            onClick = { onTabSelected(CustomerTab.ORDERS) },
            icon = {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = "Orders",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = if (isBn) "অর্ডার" else "Orders",
                    fontSize = 11.sp,
                    fontWeight = if (currentTab == CustomerTab.ORDERS) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ExpressGreenPrimary,
                selectedTextColor = ExpressGreenPrimary,
                indicatorColor = ExpressGreenPrimary.copy(alpha = 0.15f)
            ),
            modifier = Modifier.testTag("nav_orders")
        )
    }
}
