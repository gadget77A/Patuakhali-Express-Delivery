package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.DeliveryRepository
import com.example.model.OrderStatus
import com.example.model.Shop
import com.example.model.UserRole
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.components.AppBottomBar
import com.example.ui.components.AppTopBar
import com.example.ui.components.CustomerTab
import com.example.ui.customer.*
import com.example.ui.rider.RiderDashboardScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize persistent repository state with context
        DeliveryRepository.init(applicationContext)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PatuakhaliDeliveryApp()
            }
        }
    }
}

enum class CustomerScreenView {
    MAIN_TABS,
    SHOP_DETAIL,
    CART_CHECKOUT,
    ORDER_TRACKING,
    PARCEL_BOOKING
}

@Composable
fun PatuakhaliDeliveryApp() {
    val userRole by DeliveryRepository.userRole.collectAsState()
    val isBn by DeliveryRepository.isBengali.collectAsState()
    val cartItems by DeliveryRepository.cartItems.collectAsState()
    val orders by DeliveryRepository.orders.collectAsState()
    val activeTrackingId by DeliveryRepository.activeTrackingOrderId.collectAsState()

    var customerTab by remember { mutableStateOf(CustomerTab.HOME) }
    var customerView by remember { mutableStateOf(CustomerScreenView.MAIN_TABS) }
    var selectedShop by remember { mutableStateOf<Shop?>(null) }
    var viewingOrderId by remember { mutableStateOf<String?>(null) }
    var showProfileDialog by remember { mutableStateOf(false) }

    val hasActiveOrder = orders.any {
        it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_root_scaffold"),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                currentRole = userRole,
                onRoleChange = { newRole ->
                    DeliveryRepository.setUserRole(newRole)
                    customerView = CustomerScreenView.MAIN_TABS
                },
                isBn = isBn,
                onToggleLanguage = { DeliveryRepository.toggleLanguage() },
                cartCount = cartItems.sumOf { it.quantity },
                onCartClick = { customerView = CustomerScreenView.CART_CHECKOUT },
                onProfileClick = { showProfileDialog = true }
            )
        },
        bottomBar = {
            if (userRole == UserRole.CUSTOMER && customerView == CustomerScreenView.MAIN_TABS) {
                AppBottomBar(
                    currentTab = customerTab,
                    onTabSelected = { tab ->
                        customerTab = tab
                        if (tab == CustomerTab.TRACKING) {
                            viewingOrderId = activeTrackingId ?: orders.firstOrNull()?.id
                        }
                    },
                    isBn = isBn,
                    hasActiveOrder = hasActiveOrder
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = userRole,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "role_switch_animation"
            ) { role ->
                when (role) {
                    UserRole.CUSTOMER -> {
                        when (customerView) {
                            CustomerScreenView.MAIN_TABS -> {
                                when (customerTab) {
                                    CustomerTab.HOME, CustomerTab.SHOPS -> {
                                        CustomerHomeScreen(
                                            isBn = isBn,
                                            onSelectShop = { shop ->
                                                selectedShop = shop
                                                customerView = CustomerScreenView.SHOP_DETAIL
                                            },
                                            onSelectCategory = { _ ->
                                                // Category filtered directly in Home screen
                                            },
                                            onOpenTracking = { orderId ->
                                                viewingOrderId = orderId
                                                customerView = CustomerScreenView.ORDER_TRACKING
                                            },
                                            onBookParcel = {
                                                customerView = CustomerScreenView.PARCEL_BOOKING
                                            }
                                        )
                                    }
                                    CustomerTab.TRACKING -> {
                                        OrderTrackingScreen(
                                            orderId = viewingOrderId ?: activeTrackingId ?: orders.firstOrNull()?.id ?: "PED-3091",
                                            isBn = isBn,
                                            onBack = { customerTab = CustomerTab.HOME },
                                            onContactSupport = {
                                                // trigger support info
                                            }
                                        )
                                    }
                                    CustomerTab.ORDERS -> {
                                        OrderHistoryScreen(
                                            isBn = isBn,
                                            onTrackOrder = { orderId ->
                                                viewingOrderId = orderId
                                                customerView = CustomerScreenView.ORDER_TRACKING
                                            }
                                        )
                                    }
                                }
                            }

                            CustomerScreenView.SHOP_DETAIL -> {
                                selectedShop?.let { shop ->
                                    ShopDetailScreen(
                                        shop = shop,
                                        isBn = isBn,
                                        onBack = { customerView = CustomerScreenView.MAIN_TABS },
                                        onOpenCart = { customerView = CustomerScreenView.CART_CHECKOUT }
                                    )
                                }
                            }

                            CustomerScreenView.CART_CHECKOUT -> {
                                CartCheckoutScreen(
                                    isBn = isBn,
                                    onBack = { customerView = CustomerScreenView.MAIN_TABS },
                                    onOrderPlaced = { newOrderId ->
                                        viewingOrderId = newOrderId
                                        customerView = CustomerScreenView.ORDER_TRACKING
                                    }
                                )
                            }

                            CustomerScreenView.ORDER_TRACKING -> {
                                OrderTrackingScreen(
                                    orderId = viewingOrderId ?: activeTrackingId ?: "PED-3091",
                                    isBn = isBn,
                                    onBack = { customerView = CustomerScreenView.MAIN_TABS },
                                    onContactSupport = {
                                        // support info
                                    }
                                )
                            }

                            CustomerScreenView.PARCEL_BOOKING -> {
                                ParcelDeliveryScreen(
                                    isBn = isBn,
                                    onBack = { customerView = CustomerScreenView.MAIN_TABS },
                                    onOrderPlaced = { parcelOrderId ->
                                        viewingOrderId = parcelOrderId
                                        customerView = CustomerScreenView.ORDER_TRACKING
                                    }
                                )
                            }
                        }
                    }

                    UserRole.RIDER -> {
                        RiderDashboardScreen(isBn = isBn)
                    }

                    UserRole.ADMIN -> {
                        AdminDashboardScreen(isBn = isBn)
                    }
                }
            }
        }
    }

    if (showProfileDialog) {
        CustomerProfileDialog(
            isBn = isBn,
            onDismiss = { showProfileDialog = false }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
