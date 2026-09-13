package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.MockData
import com.example.model.PaymentMethod
import com.example.ui.theme.ExpressGreenPrimary
import com.example.ui.theme.ExpressOrangeAccent
import com.example.ui.util.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartCheckoutScreen(
    isBn: Boolean,
    onBack: () -> Unit,
    onOrderPlaced: (String) -> Unit
) {
    val cartItems by DeliveryRepository.cartItems.collectAsState()
    val appliedPromo by DeliveryRepository.appliedPromo.collectAsState()
    val selectedAddress by DeliveryRepository.selectedAddress.collectAsState()
    val customAddress by DeliveryRepository.customAddress.collectAsState()
    val customerName by DeliveryRepository.customerName.collectAsState()
    val customerPhone by DeliveryRepository.customerPhone.collectAsState()
    val selectedPayment by DeliveryRepository.selectedPaymentMethod.collectAsState()

    var promoInput by remember { mutableStateOf("") }
    var promoMessage by remember { mutableStateOf<String?>(null) }
    var addressDropdownExpanded by remember { mutableStateOf(false) }

    val subtotal = cartItems.sumOf { it.product.price * it.quantity }
    val deliveryFee = if (cartItems.isNotEmpty()) 35 else 0
    val discount = appliedPromo?.let { promo ->
        ((subtotal * promo.discountPercent) / 100).coerceAtMost(promo.maxDiscount)
    } ?: 0
    val grandTotal = (subtotal + deliveryFee - discount).coerceAtLeast(0)

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
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("cart_back_button")) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = ExpressGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (isBn) "কার্ট ও চেকআউট" else "Cart & Checkout",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (cartItems.isNotEmpty()) {
                        TextButton(onClick = { DeliveryRepository.clearCart() }) {
                            Text(
                                text = if (isBn) "খালি করুন" else "Clear",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 10.dp,
                    modifier = Modifier.navigationBarsPadding()
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
                                    text = if (isBn) "সর্বমোট প্রদেয় টাকা:" else "Total Payable:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = Strings.currency(grandTotal),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ExpressGreenPrimary
                                )
                            }

                            Button(
                                onClick = {
                                    val order = DeliveryRepository.placeOrder()
                                    if (order != null) {
                                        onOrderPlaced(order.id)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ExpressOrangeAccent),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("confirm_order_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBn) "অর্ডার নিশ্চিত করুন" else "Confirm Order",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🛒", fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isBn) "আপনার কার্ট বর্তমানে খালি আছে" else "Your cart is currently empty",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isBn) "রেস্তোরাঁ ও দোকান থেকে সুস্বাদু খাবার যোগ করুন" else "Browse restaurants to add delicious meals",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isBn) "কেনাকাটা করুন" else "Start Shopping")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("checkout_scroll_content"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Items in Cart
                item {
                    Text(
                        text = if (isBn) "অর্ডারের আইটেমসমূহ (${cartItems.size})" else "Order Items (${cartItems.size})",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(cartItems) { item ->
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
                            Text(text = item.product.iconEmoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isBn) item.product.nameBn else item.product.nameEn,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${Strings.currency(item.product.price)} x ${item.quantity} = ${Strings.currency(item.product.price * item.quantity)}",
                                    fontSize = 11.sp,
                                    color = ExpressGreenPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { DeliveryRepository.decrementCart(item.product.id) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Text("-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ExpressGreenPrimary)
                                }
                                Text(
                                    text = "${item.quantity}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                IconButton(
                                    onClick = { DeliveryRepository.addToCart(item.product) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Text("+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = ExpressGreenPrimary)
                                }
                            }
                        }
                    }
                }

                // Delivery Address Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = ExpressOrangeAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBn) "ডেলিভারি এলাকা ও ঠিকানা" else "Delivery Location & Address",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Area Dropdown
                            ExposedDropdownMenuBox(
                                expanded = addressDropdownExpanded,
                                onExpandedChange = { addressDropdownExpanded = it }
                            ) {
                                OutlinedTextField(
                                    value = selectedAddress,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(if (isBn) "পটুয়াখালী এলাকা নির্বাচন করুন" else "Select Neighborhood", fontSize = 11.sp) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = addressDropdownExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = addressDropdownExpanded,
                                    onDismissRequest = { addressDropdownExpanded = false }
                                ) {
                                    MockData.neighborhoods.forEach { area ->
                                        DropdownMenuItem(
                                            text = { Text(area, fontSize = 12.sp) },
                                            onClick = {
                                                DeliveryRepository.selectedAddress.value = area
                                                addressDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Custom house/road details input
                            OutlinedTextField(
                                value = customAddress,
                                onValueChange = { DeliveryRepository.customAddress.value = it },
                                label = { Text(if (isBn) "বাসা / রোড / ফ্লোর নম্বর" else "House / Road / Floor Details", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Customer Contact Information
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = ExpressGreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBn) "গ্রাহকের যোগাযোগের তথ্য" else "Customer Contact Info",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = customerName,
                                onValueChange = { DeliveryRepository.customerName.value = it },
                                label = { Text(if (isBn) "গ্রাহকের নাম" else "Customer Name", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = customerPhone,
                                onValueChange = { DeliveryRepository.customerPhone.value = it },
                                label = { Text(if (isBn) "মোবাইল নম্বর" else "Mobile Number", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Payment Method
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isBn) "পেমেন্ট মাধ্যম নির্বাচন" else "Payment Method",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            PaymentMethod.values().forEach { method ->
                                val isSelected = selectedPayment == method
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { DeliveryRepository.selectedPaymentMethod.value = method }
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { DeliveryRepository.selectedPaymentMethod.value = method },
                                        colors = RadioButtonDefaults.colors(selectedColor = ExpressGreenPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = Strings.paymentTitle(method, isBn),
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                        if (method != PaymentMethod.CASH_ON_DELIVERY) {
                                            Text(
                                                text = if (isBn) "(ডেমো পেমেন্ট গেটওয়ে)" else "(Demo Payment Gateway)",
                                                fontSize = 10.sp,
                                                color = ExpressOrangeAccent
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Promo Code Section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (isBn) "ডিসকাউন্ট প্রোমোকোড" else "Discount Promo Code",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = promoInput,
                                    onValueChange = { promoInput = it },
                                    placeholder = { Text("PATUAKHALI20", fontSize = 12.sp) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        if (DeliveryRepository.applyPromoCode(promoInput)) {
                                            promoMessage = if (isBn) "প্রোমোকোড প্রয়োগ করা হয়েছে!" else "Promo code applied!"
                                        } else {
                                            promoMessage = if (isBn) "ভুল প্রোমোকোড। 'PATUAKHALI20' চেষ্টা করুন।" else "Invalid code. Try 'PATUAKHALI20'"
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(text = if (isBn) "প্রয়োগ" else "Apply", fontSize = 12.sp)
                                }
                            }

                            if (promoMessage != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = promoMessage!!,
                                    fontSize = 11.sp,
                                    color = if (appliedPromo != null) ExpressGreenPrimary else MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }

                // Bill Breakdown Summary
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (isBn) "বিল ও ভাউচার বিবরণ" else "Price Breakdown",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = if (isBn) "আইটেম সাবটোটাল" else "Item Subtotal", fontSize = 12.sp)
                                Text(text = Strings.currency(subtotal), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = if (isBn) "ডেলিভারি চার্জ" else "Delivery Fee", fontSize = 12.sp)
                                Text(text = Strings.currency(deliveryFee), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }

                            if (discount > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (isBn) "প্রোমোকোড ডিসকাউন্ট (${appliedPromo?.code})" else "Promo Discount (${appliedPromo?.code})",
                                        fontSize = 12.sp,
                                        color = ExpressGreenPrimary
                                    )
                                    Text(
                                        text = "- ${Strings.currency(discount)}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ExpressGreenPrimary
                                    )
                                }
                            }

                            HorizontalDivider()

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isBn) "সর্বমোট" else "Total Amount",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = Strings.currency(grandTotal),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ExpressGreenPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
