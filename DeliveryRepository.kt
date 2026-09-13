package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

object DeliveryRepository {
    private val scope = CoroutineScope(Dispatchers.Default)
    private var prefs: SharedPreferences? = null

    val isBengali = MutableStateFlow(true)
    val userRole = MutableStateFlow(UserRole.CUSTOMER)

    val shops = MutableStateFlow(MockData.shops)
    val products = MutableStateFlow(MockData.products)

    val orders = MutableStateFlow<List<Order>>(MockData.initialOrders)
    val cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val riderProfile = MutableStateFlow(MockData.defaultRider)

    val activeTrackingOrderId = MutableStateFlow<String?>("PED-3091")

    val selectedCategory = MutableStateFlow<ServiceCategory?>(null)
    val searchQuery = MutableStateFlow("")

    val selectedAddress = MutableStateFlow(MockData.neighborhoods.first())
    val customAddress = MutableStateFlow("বাসা নং ১২, রোড ৩, পটুয়াখালী")
    val customerName = MutableStateFlow("হাসান মাহমুদ")
    val customerPhone = MutableStateFlow("+8801711223344")
    val selectedPaymentMethod = MutableStateFlow(PaymentMethod.CASH_ON_DELIVERY)
    val appliedPromo = MutableStateFlow<PromoCode?>(null)

    // Admin state
    val baseDeliveryFee = MutableStateFlow(35)
    val availablePromos = MutableStateFlow(MockData.promoCodes)

    init {
        // Continuous live GPS progress simulation for orders currently ON_THE_WAY
        scope.launch {
            while (true) {
                delay(2500)
                var hasChanges = false
                val updated = orders.value.map { order ->
                    if (order.status == OrderStatus.ON_THE_WAY) {
                        hasChanges = true
                        val next = (order.riderProgress + 0.04f).coerceAtMost(0.96f)
                        order.copy(riderProgress = next)
                    } else {
                        order
                    }
                }
                if (hasChanges) {
                    orders.value = updated
                }
            }
        }
    }

    fun init(context: Context) {
        prefs = context.getSharedPreferences("patuakhali_delivery_prefs", Context.MODE_PRIVATE)
        loadPersistedState()
    }

    private fun loadPersistedState() {
        val p = prefs ?: return
        try {
            isBengali.value = p.getBoolean("is_bengali", true)
            customerName.value = p.getString("customer_name", "হাসান মাহমুদ") ?: "হাসান মাহমুদ"
            customerPhone.value = p.getString("customer_phone", "+8801711223344") ?: "+8801711223344"
            selectedAddress.value = p.getString("customer_address", MockData.neighborhoods.first()) ?: MockData.neighborhoods.first()

            val ordersJsonStr = p.getString("saved_orders", null)
            if (!ordersJsonStr.isNullOrBlank()) {
                val jsonArr = JSONArray(ordersJsonStr)
                val loadedList = mutableListOf<Order>()
                for (i in 0 until jsonArr.length()) {
                    val obj = jsonArr.getJSONObject(i)
                    val itemsArr = obj.getJSONArray("items")
                    val itemsList = mutableListOf<OrderItem>()
                    for (j in 0 until itemsArr.length()) {
                        val itemObj = itemsArr.getJSONObject(j)
                        itemsList.add(
                            OrderItem(
                                productId = itemObj.getString("productId"),
                                nameBn = itemObj.getString("nameBn"),
                                nameEn = itemObj.getString("nameEn"),
                                quantity = itemObj.getInt("quantity"),
                                price = itemObj.getInt("price"),
                                iconEmoji = itemObj.optString("iconEmoji", "🍛")
                            )
                        )
                    }

                    loadedList.add(
                        Order(
                            id = obj.getString("id"),
                            customerName = obj.getString("customerName"),
                            customerPhone = obj.getString("customerPhone"),
                            serviceCategory = ServiceCategory.valueOf(obj.getString("serviceCategory")),
                            shopNameBn = obj.getString("shopNameBn"),
                            shopNameEn = obj.getString("shopNameEn"),
                            pickupAddressBn = obj.getString("pickupAddressBn"),
                            deliveryAddressBn = obj.getString("deliveryAddressBn"),
                            items = itemsList,
                            subtotal = obj.getInt("subtotal"),
                            deliveryFee = obj.getInt("deliveryFee"),
                            discount = obj.getInt("discount"),
                            totalAmount = obj.getInt("totalAmount"),
                            paymentMethod = PaymentMethod.valueOf(obj.getString("paymentMethod")),
                            status = OrderStatus.valueOf(obj.getString("status")),
                            orderTime = obj.getString("orderTime"),
                            riderId = if (obj.has("riderId") && !obj.isNull("riderId")) obj.getString("riderId") else null,
                            riderName = if (obj.has("riderName") && !obj.isNull("riderName")) obj.getString("riderName") else null,
                            riderPhone = if (obj.has("riderPhone") && !obj.isNull("riderPhone")) obj.getString("riderPhone") else null,
                            riderVehicle = if (obj.has("riderVehicle") && !obj.isNull("riderVehicle")) obj.getString("riderVehicle") else null,
                            riderProgress = obj.optDouble("riderProgress", 0.0).toFloat(),
                            rating = if (obj.has("rating")) obj.getInt("rating") else null,
                            reviewText = if (obj.has("reviewText")) obj.getString("reviewText") else null
                        )
                    )
                }
                if (loadedList.isNotEmpty()) {
                    orders.value = loadedList
                    activeTrackingOrderId.value = loadedList.firstOrNull {
                        it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED
                    }?.id ?: loadedList.firstOrNull()?.id
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun persistState() {
        val p = prefs ?: return
        try {
            val jsonArr = JSONArray()
            orders.value.forEach { order ->
                val obj = JSONObject()
                obj.put("id", order.id)
                obj.put("customerName", order.customerName)
                obj.put("customerPhone", order.customerPhone)
                obj.put("serviceCategory", order.serviceCategory.name)
                obj.put("shopNameBn", order.shopNameBn)
                obj.put("shopNameEn", order.shopNameEn)
                obj.put("pickupAddressBn", order.pickupAddressBn)
                obj.put("deliveryAddressBn", order.deliveryAddressBn)
                obj.put("subtotal", order.subtotal)
                obj.put("deliveryFee", order.deliveryFee)
                obj.put("discount", order.discount)
                obj.put("totalAmount", order.totalAmount)
                obj.put("paymentMethod", order.paymentMethod.name)
                obj.put("status", order.status.name)
                obj.put("orderTime", order.orderTime)
                obj.put("riderId", order.riderId)
                obj.put("riderName", order.riderName)
                obj.put("riderPhone", order.riderPhone)
                obj.put("riderVehicle", order.riderVehicle)
                obj.put("riderProgress", order.riderProgress.toDouble())
                order.rating?.let { obj.put("rating", it) }
                order.reviewText?.let { obj.put("reviewText", it) }

                val itemsArr = JSONArray()
                order.items.forEach { item ->
                    val itObj = JSONObject()
                    itObj.put("productId", item.productId)
                    itObj.put("nameBn", item.nameBn)
                    itObj.put("nameEn", item.nameEn)
                    itObj.put("quantity", item.quantity)
                    itObj.put("price", item.price)
                    itObj.put("iconEmoji", item.iconEmoji)
                    itemsArr.put(itObj)
                }
                obj.put("items", itemsArr)
                jsonArr.put(obj)
            }

            p.edit()
                .putString("saved_orders", jsonArr.toString())
                .putBoolean("is_bengali", isBengali.value)
                .putString("customer_name", customerName.value)
                .putString("customer_phone", customerPhone.value)
                .putString("customer_address", selectedAddress.value)
                .apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleLanguage() {
        isBengali.value = !isBengali.value
        prefs?.edit()?.putBoolean("is_bengali", isBengali.value)?.apply()
    }

    fun setUserRole(role: UserRole) {
        userRole.value = role
    }

    fun addToCart(product: Product) {
        val current = cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val item = current[index]
            current[index] = item.copy(quantity = item.quantity + 1)
        } else {
            current.add(CartItem(product = product, quantity = 1))
        }
        cartItems.value = current
    }

    fun decrementCart(productId: String) {
        val current = cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val item = current[index]
            if (item.quantity > 1) {
                current[index] = item.copy(quantity = item.quantity - 1)
            } else {
                current.removeAt(index)
            }
            cartItems.value = current
        }
    }

    fun removeFromCart(productId: String) {
        cartItems.value = cartItems.value.filter { it.product.id != productId }
    }

    fun clearCart() {
        cartItems.value = emptyList()
        appliedPromo.value = null
    }

    fun applyPromoCode(codeStr: String): Boolean {
        val match = availablePromos.value.find { it.code.equals(codeStr.trim(), ignoreCase = true) }
        return if (match != null) {
            appliedPromo.value = match
            true
        } else {
            false
        }
    }

    fun removePromoCode() {
        appliedPromo.value = null
    }

    fun placeOrder(): Order? {
        val currentCart = cartItems.value
        if (currentCart.isEmpty()) return null

        val firstShopId = currentCart.first().product.shopId
        val shop = shops.value.find { it.id == firstShopId } ?: MockData.shops.first()

        val subtotal = currentCart.sumOf { it.product.price * it.quantity }
        val fee = shop.deliveryFee
        val discount = appliedPromo.value?.let { promo ->
            val calc = (subtotal * promo.discountPercent) / 100
            calc.coerceAtMost(promo.maxDiscount)
        } ?: 0

        val total = (subtotal + fee - discount).coerceAtLeast(0)

        val newId = "PED-${Random.nextInt(1000, 9999)}"
        val orderItems = currentCart.map {
            OrderItem(
                productId = it.product.id,
                nameBn = it.product.nameBn,
                nameEn = it.product.nameEn,
                quantity = it.quantity,
                price = it.product.price,
                iconEmoji = it.product.iconEmoji
            )
        }

        val fullDeliveryAddress = "${selectedAddress.value}, ${customAddress.value.ifBlank { "বাসা" }}"

        // Customer places order: starts as CONFIRMED without rider assigned yet.
        // It immediately appears in Rider view under "Available Requests" to accept!
        val newOrder = Order(
            id = newId,
            customerName = customerName.value.ifBlank { "সম্মানিত গ্রাহক" },
            customerPhone = customerPhone.value.ifBlank { "+8801700000000" },
            serviceCategory = shop.category,
            shopNameBn = shop.nameBn,
            shopNameEn = shop.nameEn,
            pickupAddressBn = shop.addressBn,
            deliveryAddressBn = fullDeliveryAddress,
            items = orderItems,
            subtotal = subtotal,
            deliveryFee = fee,
            discount = discount,
            totalAmount = total,
            paymentMethod = selectedPaymentMethod.value,
            status = OrderStatus.CONFIRMED,
            orderTime = "আজ এইমাত্র",
            riderId = null,
            riderName = null,
            riderPhone = null,
            riderVehicle = null,
            riderProgress = 0.05f
        )

        orders.value = listOf(newOrder) + orders.value
        activeTrackingOrderId.value = newId
        clearCart()
        persistState()
        return newOrder
    }

    fun cancelOrder(orderId: String) {
        orders.value = orders.value.map {
            if (it.id == orderId && (it.status == OrderStatus.PENDING || it.status == OrderStatus.CONFIRMED)) {
                it.copy(status = OrderStatus.CANCELLED)
            } else it
        }
        persistState()
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        orders.value = orders.value.map {
            if (it.id == orderId) {
                val progress = when (newStatus) {
                    OrderStatus.PENDING, OrderStatus.CONFIRMED -> 0.05f
                    OrderStatus.PREPARING -> 0.20f
                    OrderStatus.PICKED_UP -> 0.40f
                    OrderStatus.ON_THE_WAY -> 0.65f
                    OrderStatus.DELIVERED -> 1.0f
                    OrderStatus.CANCELLED -> 0.0f
                }
                it.copy(status = newStatus, riderProgress = progress)
            } else it
        }

        if (newStatus == OrderStatus.DELIVERED) {
            val rider = riderProfile.value
            riderProfile.value = rider.copy(
                todayEarnings = rider.todayEarnings + 50,
                weeklyEarnings = rider.weeklyEarnings + 50,
                completedTripsToday = rider.completedTripsToday + 1
            )
        }
        persistState()
    }

    fun rateOrder(orderId: String, rating: Int, review: String) {
        orders.value = orders.value.map {
            if (it.id == orderId) {
                it.copy(rating = rating, reviewText = review)
            } else it
        }
        persistState()
    }

    fun toggleRiderOnline() {
        val rider = riderProfile.value
        riderProfile.value = rider.copy(isOnline = !rider.isOnline)
    }

    fun acceptRiderOrder(orderId: String) {
        val rider = riderProfile.value
        orders.value = orders.value.map {
            if (it.id == orderId) {
                it.copy(
                    riderId = rider.id,
                    riderName = rider.nameBn,
                    riderPhone = rider.phone,
                    riderVehicle = rider.vehicleNumber,
                    status = OrderStatus.PICKED_UP,
                    riderProgress = 0.35f
                )
            } else it
        }
        persistState()
    }
}
