package com.example.ui.customer

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeliveryRepository
import com.example.model.Product
import com.example.model.Shop
import com.example.ui.theme.ExpressGreenPrimary
import com.example.ui.theme.ExpressOrangeAccent
import com.example.ui.util.Strings

@Composable
fun ShopDetailScreen(
    shop: Shop,
    isBn: Boolean,
    onBack: () -> Unit,
    onOpenCart: () -> Unit
) {
    val allProducts by DeliveryRepository.products.collectAsState()
    val cartItems by DeliveryRepository.cartItems.collectAsState()
    val shopProducts = allProducts.filter { it.shopId == shop.id }

    val cartTotal = cartItems.sumOf { it.product.price * it.quantity }
    val totalItems = cartItems.sumOf { it.quantity }

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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("shop_back_button")) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = ExpressGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBn) shop.nameBn else shop.nameEn,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (isBn) shop.addressBn else shop.addressEn,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "★ ${shop.rating}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
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
                                text = "$totalItems ${if (isBn) "টি আইটেম কার্টে আছে" else "items in cart"}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = Strings.currency(cartTotal),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ExpressGreenPrimary
                            )
                        }

                        Button(
                            onClick = onOpenCart,
                            colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("view_cart_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBn) "কার্ট দেখুন" else "View Cart",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("shop_products_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Shop Highlights Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⏱", fontSize = 18.sp)
                            Text(
                                text = "${shop.deliveryTimeMin} ${if (isBn) "মিনিট" else "mins"}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isBn) "ডেলিভারি সময়" else "Delivery Time",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        VerticalDivider(modifier = Modifier.height(30.dp))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🛵", fontSize = 18.sp)
                            Text(
                                text = "৳${shop.deliveryFee}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ExpressOrangeAccent
                            )
                            Text(
                                text = if (isBn) "ডেলিভারি চার্জ" else "Delivery Fee",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        VerticalDivider(modifier = Modifier.height(30.dp))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📍", fontSize = 18.sp)
                            Text(
                                text = if (isBn) "সরাসরি" else "Direct",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ExpressGreenPrimary
                            )
                            Text(
                                text = if (isBn) "পটুয়াখালী সদর" else "Patuakhali",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Products Header
            item {
                Text(
                    text = if (isBn) "উপলব্ধ খাবার ও পণ্যসমূহ" else "Available Items",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Product Cards
            items(shopProducts) { product ->
                val inCart = cartItems.find { it.product.id == product.id }

                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                        // Product Icon Emoji Box
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = product.iconEmoji, fontSize = 32.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBn) product.nameBn else product.nameEn,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = if (isBn) product.descriptionBn else product.descriptionEn,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = Strings.currency(product.price),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ExpressGreenPrimary
                                )

                                if (product.originalPrice != null) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = Strings.currency(product.originalPrice),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "/ ${if (isBn) product.unitBn else product.unitEn}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Add to Cart / Quantity controls
                        if (inCart == null) {
                            Button(
                                onClick = { DeliveryRepository.addToCart(product) },
                                colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("add_to_cart_${product.id}")
                            ) {
                                Text(
                                    text = if (isBn) "যোগ করুন" else "Add",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ExpressGreenPrimary.copy(alpha = 0.12f))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                IconButton(
                                    onClick = { DeliveryRepository.decrementCart(product.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Text("-", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ExpressGreenPrimary)
                                }

                                Text(
                                    text = "${inCart.quantity}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ExpressGreenPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )

                                IconButton(
                                    onClick = { DeliveryRepository.addToCart(product) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Text("+", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = ExpressGreenPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
