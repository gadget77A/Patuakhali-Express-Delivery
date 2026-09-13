package com.example.ui.customer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeliveryRepository
import com.example.data.MockData
import com.example.model.*
import com.example.ui.theme.ExpressGreenPrimary
import com.example.ui.theme.ExpressOrangeAccent
import com.example.ui.util.Strings
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcelDeliveryScreen(
    isBn: Boolean,
    onBack: () -> Unit,
    onOrderPlaced: (String) -> Unit
) {
    var pickupArea by remember { mutableStateOf(MockData.neighborhoods[0]) }
    var pickupDetail by remember { mutableStateOf("") }
    var dropoffArea by remember { mutableStateOf(MockData.neighborhoods[1]) }
    var dropoffDetail by remember { mutableStateOf("") }
    var recipientName by remember { mutableStateOf("") }
    var recipientPhone by remember { mutableStateOf("") }

    var selectedWeightType by remember { mutableStateOf("light") } // light, medium, heavy
    var pickupDropdown by remember { mutableStateOf(false) }
    var dropoffDropdown by remember { mutableStateOf(false) }

    val fee = when (selectedWeightType) {
        "light" -> 60
        "medium" -> 90
        else -> 140
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
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = ExpressGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = if (isBn) "পটুয়াখালী এক্সপ্রেস দ্রুত পার্সেল" else "Express Parcel Courier",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isBn) "ডেলিভারি চার্জ:" else "Delivery Charge:",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = Strings.currency(fee),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ExpressGreenPrimary
                        )
                    }

                    Button(
                        onClick = {
                            val newId = "PED-P${Random.nextInt(1000, 9999)}"
                            val order = Order(
                                id = newId,
                                customerName = if (recipientName.isBlank()) "পার্সেল গ্রাহক" else recipientName,
                                customerPhone = if (recipientPhone.isBlank()) "+8801700112233" else recipientPhone,
                                serviceCategory = ServiceCategory.PARCEL,
                                shopNameBn = "পটুয়াখালী এক্সপ্রেস দ্রুত পার্সেল হাব",
                                shopNameEn = "Patuakhali Express Fast Parcel Hub",
                                pickupAddressBn = "$pickupArea, $pickupDetail",
                                deliveryAddressBn = "$dropoffArea, $dropoffDetail",
                                items = listOf(
                                    OrderItem(
                                        productId = "parcel_custom",
                                        nameBn = if (isBn) "শহরব্যাপী জরুরি পার্সেল" else "Express Intra-city Parcel",
                                        nameEn = "Express Intra-city Parcel",
                                        quantity = 1,
                                        price = fee,
                                        iconEmoji = "📦"
                                    )
                                ),
                                subtotal = fee,
                                deliveryFee = 0,
                                discount = 0,
                                totalAmount = fee,
                                paymentMethod = PaymentMethod.CASH_ON_DELIVERY,
                                status = OrderStatus.CONFIRMED,
                                orderTime = "আজ এইমাত্র",
                                riderId = "rider_1",
                                riderName = DeliveryRepository.riderProfile.value.nameBn,
                                riderPhone = DeliveryRepository.riderProfile.value.phone,
                                riderVehicle = DeliveryRepository.riderProfile.value.vehicleNumber,
                                riderProgress = 0.10f
                            )
                            DeliveryRepository.orders.value = listOf(order) + DeliveryRepository.orders.value
                            DeliveryRepository.activeTrackingOrderId.value = newId
                            onOrderPlaced(newId)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ExpressOrangeAccent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("book_parcel_button")
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isBn) "পার্সেল বুক করুন" else "Book Parcel", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Info
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ExpressGreenPrimary.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🚀", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isBn) "পটুয়াখালী শহরের সর্বত্র ৩০-৪৫ মিনিটে পার্সেল পৌঁছে দিন!" else "30-45 mins express parcel delivery anywhere in Patuakhali!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ExpressGreenPrimary
                            )
                            Text(
                                text = if (isBn) "কাগজপত্র, ওষুধ, উপহার সামগ্রী বা যেকোনো প্যাকেট" else "Documents, emergency items, gifts or small packages",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Pickup Info
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isBn) "১. পিকআপ লোকেশন (যেখান থেকে পার্সেল নিবে)" else "1. Pickup Location",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = pickupDropdown,
                            onExpandedChange = { pickupDropdown = it }
                        ) {
                            OutlinedTextField(
                                value = pickupArea,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(if (isBn) "পিকআপ এলাকা" else "Pickup Area") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = pickupDropdown) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = pickupDropdown,
                                onDismissRequest = { pickupDropdown = false }
                            ) {
                                MockData.neighborhoods.forEach { area ->
                                    DropdownMenuItem(
                                        text = { Text(area) },
                                        onClick = {
                                            pickupArea = area
                                            pickupDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = pickupDetail,
                            onValueChange = { pickupDetail = it },
                            label = { Text(if (isBn) "বিস্তারিত ঠিকানা / দোকান / বাসার নাম" else "Detailed pickup street / landmark") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }

            // Dropoff Info
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isBn) "২. ডেলিভারি লোকেশন ও প্রাপকের তথ্য" else "2. Dropoff & Recipient Info",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = dropoffDropdown,
                            onExpandedChange = { dropoffDropdown = it }
                        ) {
                            OutlinedTextField(
                                value = dropoffArea,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(if (isBn) "ডেলিভারি এলাকা" else "Delivery Area") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropoffDropdown) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = dropoffDropdown,
                                onDismissRequest = { dropoffDropdown = false }
                            ) {
                                MockData.neighborhoods.forEach { area ->
                                    DropdownMenuItem(
                                        text = { Text(area) },
                                        onClick = {
                                            dropoffArea = area
                                            dropoffDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = dropoffDetail,
                            onValueChange = { dropoffDetail = it },
                            label = { Text(if (isBn) "প্রাপকের বাসা / রোড নম্বর" else "Recipient address / road") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = recipientName,
                            onValueChange = { recipientName = it },
                            label = { Text(if (isBn) "প্রাপকের নাম" else "Recipient Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = recipientPhone,
                            onValueChange = { recipientPhone = it },
                            label = { Text(if (isBn) "প্রাপকের ফোন নম্বর" else "Recipient Phone") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            }

            // Weight Category Selector
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (isBn) "৩. পার্সেল ওজন ও সাইজ" else "3. Parcel Weight",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedWeightType == "light",
                                onClick = { selectedWeightType = "light" },
                                label = { Text(if (isBn) "হালকা (<১ কেজি) • ৬০৳" else "Light (<1kg) • 60৳", fontSize = 11.sp) }
                            )
                            FilterChip(
                                selected = selectedWeightType == "medium",
                                onClick = { selectedWeightType = "medium" },
                                label = { Text(if (isBn) "মাঝারি (১-৩ কেজি) • ৯০৳" else "Medium (1-3kg) • 90৳", fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }
        }
    }
}
