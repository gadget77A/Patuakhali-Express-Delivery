package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DeliveryRepository
import com.example.model.OrderStatus
import com.example.model.ServiceCategory
import com.example.model.Shop
import com.example.ui.theme.*
import com.example.ui.util.Strings

@Composable
fun CustomerHomeScreen(
    isBn: Boolean,
    onSelectShop: (Shop) -> Unit,
    onSelectCategory: (ServiceCategory) -> Unit,
    onOpenTracking: (String) -> Unit,
    onBookParcel: () -> Unit
) {
    val shops by DeliveryRepository.shops.collectAsState()
    val orders by DeliveryRepository.orders.collectAsState()
    var searchInput by remember { mutableStateOf("") }
    var selectedCatFilter by remember { mutableStateOf<ServiceCategory?>(null) }

    val activeOrder = orders.firstOrNull {
        it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED
    }

    val filteredShops = shops.filter { shop ->
        (selectedCatFilter == null || shop.category == selectedCatFilter) &&
                (searchInput.isBlank() ||
                        shop.nameBn.contains(searchInput, ignoreCase = true) ||
                        shop.nameEn.contains(searchInput, ignoreCase = true) ||
                        shop.addressBn.contains(searchInput, ignoreCase = true))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("customer_home_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Search bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                OutlinedTextField(
                    value = searchInput,
                    onValueChange = { searchInput = it },
                    placeholder = {
                        Text(
                            text = Strings.searchPlaceholder(isBn),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = ExpressGreenPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchInput.isNotEmpty()) {
                            IconButton(onClick = { searchInput = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExpressGreenPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_text_field")
                )
            }
        }

        // Active Order Live Tracking Banner (if any)
        if (activeOrder != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { onOpenTracking(activeOrder.id) }
                        .testTag("active_order_banner"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = ExpressOrangeContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(ExpressOrangeAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DirectionsBike,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isBn) "চলমান অর্ডার: ${activeOrder.id}" else "Active Order: ${activeOrder.id}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF7A2E00)
                                    )
                                    Text(
                                        text = Strings.statusTitle(activeOrder.status, isBn),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ExpressOrangeAccent
                                    )
                                }
                            }

                            Button(
                                onClick = { onOpenTracking(activeOrder.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = ExpressOrangeAccent),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GpsFixed,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBn) "লাইভ ট্র্যাক" else "Live Track",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Mini progress bar
                        LinearProgressIndicator(
                            progress = { activeOrder.riderProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = ExpressOrangeAccent,
                            trackColor = Color.White
                        )
                    }
                }
            }
        }

        // Hero Banner with Patuakhali Express Art
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(145.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_delivery_hero),
                        contentDescription = "Patuakhali Express Hero",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay for legibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.75f),
                                        Color.Black.copy(alpha = 0.25f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            color = ExpressOrangeAccent,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isBn) "পটুয়াখালী সদর ও আশেপাশের এলাকা" else "Patuakhali Sadar & Suburbs",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isBn) "দ্রুততম হোম ডেলিভারি সেবা" else "Fastest Local Express Delivery",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = if (isBn) "খাবার, মুদি, ওষুধ বা পার্সেল — ঘরে বসেই অর্ডার করুন!" else "Food, groceries, medicine & parcels at your door!",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Service Categories (4 cards: Food, Grocery, Parcel, Medicine)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = if (isBn) "সেবাসমূহ" else "Our Services",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Food
                    CategoryCard(
                        title = if (isBn) "খাবার" else "Food",
                        subtitle = if (isBn) "রেস্তোরাঁ" else "Restaurants",
                        icon = "🍛",
                        bgColor = Color(0xFFFEF2F2),
                        accentColor = Color(0xFFEF4444),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedCatFilter = ServiceCategory.FOOD
                            onSelectCategory(ServiceCategory.FOOD)
                        }
                    )

                    // Grocery
                    CategoryCard(
                        title = if (isBn) "মুদি" else "Grocery",
                        subtitle = if (isBn) "বাজার" else "Market",
                        icon = "🥦",
                        bgColor = Color(0xFFF0FDF4),
                        accentColor = ExpressGreenPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedCatFilter = ServiceCategory.GROCERY
                            onSelectCategory(ServiceCategory.GROCERY)
                        }
                    )

                    // Parcel
                    CategoryCard(
                        title = if (isBn) "পার্সেল" else "Parcel",
                        subtitle = if (isBn) "কুরিয়ার" else "Courier",
                        icon = "📦",
                        bgColor = Color(0xFFEFF6FF),
                        accentColor = Color(0xFF3B82F6),
                        modifier = Modifier.weight(1f),
                        onClick = onBookParcel
                    )

                    // Medicine
                    CategoryCard(
                        title = if (isBn) "ওষুধ" else "Medicine",
                        subtitle = if (isBn) "ফার্মেসি" else "Pharmacy",
                        icon = "💊",
                        bgColor = Color(0xFFFAF5FF),
                        accentColor = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedCatFilter = ServiceCategory.MEDICINE
                            onSelectCategory(ServiceCategory.MEDICINE)
                        }
                    )
                }
            }
        }

        // Filter Pills Row
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCatFilter == null,
                        onClick = { selectedCatFilter = null },
                        label = { Text(if (isBn) "সকল (${shops.size})" else "All (${shops.size})", fontSize = 11.sp) }
                    )
                }

                ServiceCategory.values().forEach { cat ->
                    item {
                        FilterChip(
                            selected = selectedCatFilter == cat,
                            onClick = {
                                selectedCatFilter = if (selectedCatFilter == cat) null else cat
                            },
                            label = { Text(Strings.categoryTitle(cat, isBn), fontSize = 11.sp) }
                        )
                    }
                }
            }
        }

        // Section Title: Shops and Restaurants
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBn) "পটুয়াখালীর জনপ্রিয় দোকান ও রেস্তোরাঁ" else "Popular Shops & Restaurants",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "${filteredShops.size} ${if (isBn) "টি পাওয়া গেছে" else "available"}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Shop Cards List
        if (filteredShops.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isBn) "কোনো দোকান বা রেস্তোরাঁ পাওয়া যায়নি" else "No shops found matching your search",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(filteredShops) { shop ->
                ShopCardItem(
                    shop = shop,
                    isBn = isBn,
                    onClick = { onSelectShop(shop) }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    subtitle: String,
    icon: String,
    bgColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = icon, fontSize = 26.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ShopCardItem(
    shop: Shop,
    isBn: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .testTag("shop_card_${shop.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Store Avatar / Icon Emoji
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(text = shop.iconEmoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isBn) shop.nameBn else shop.nameEn,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Rating badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "★", fontSize = 10.sp, color = Color(0xFFD97706))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${shop.rating}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = if (isBn) shop.addressBn else shop.addressEn,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Metadata tags: Delivery time & Delivery fee
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = ExpressGreenPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${shop.deliveryTimeMin} ${if (isBn) "মিনিট" else "mins"}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text("•", color = MaterialTheme.colorScheme.outline)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DirectionsBike,
                            contentDescription = null,
                            tint = ExpressOrangeAccent,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isBn) "ডেলিভারি ৳${shop.deliveryFee}" else "Delivery ৳${shop.deliveryFee}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
