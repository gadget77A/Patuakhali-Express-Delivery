package com.example.model

enum class UserRole {
    CUSTOMER,
    RIDER,
    ADMIN
}

enum class ServiceCategory {
    FOOD,
    GROCERY,
    PARCEL,
    MEDICINE
}

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    PICKED_UP,
    ON_THE_WAY,
    DELIVERED,
    CANCELLED
}

enum class PaymentMethod {
    CASH_ON_DELIVERY,
    BKASH,
    NAGAD
}

data class Shop(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: ServiceCategory,
    val rating: Double,
    val reviewCount: Int,
    val deliveryTimeMin: Int,
    val deliveryFee: Int,
    val addressBn: String,
    val addressEn: String,
    val iconEmoji: String,
    val isFeatured: Boolean = false,
    val isOpen: Boolean = true
)

data class Product(
    val id: String,
    val shopId: String,
    val nameBn: String,
    val nameEn: String,
    val descriptionBn: String,
    val descriptionEn: String,
    val price: Int,
    val originalPrice: Int? = null,
    val category: ServiceCategory,
    val unitBn: String,
    val unitEn: String,
    val iconEmoji: String,
    val inStock: Boolean = true
)

data class CartItem(
    val product: Product,
    var quantity: Int,
    val instructions: String = ""
)

data class OrderItem(
    val productId: String,
    val nameBn: String,
    val nameEn: String,
    val quantity: Int,
    val price: Int,
    val iconEmoji: String
)

data class Order(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val serviceCategory: ServiceCategory,
    val shopNameBn: String,
    val shopNameEn: String,
    val pickupAddressBn: String,
    val deliveryAddressBn: String,
    val items: List<OrderItem>,
    val subtotal: Int,
    val deliveryFee: Int,
    val discount: Int,
    val totalAmount: Int,
    val paymentMethod: PaymentMethod,
    var status: OrderStatus,
    val orderTime: String,
    val riderId: String? = null,
    val riderName: String? = null,
    val riderPhone: String? = null,
    val riderVehicle: String? = null,
    val riderProgress: Float = 0.5f, // 0.0f at shop, 1.0f at customer
    val rating: Int? = null,
    val reviewText: String? = null
)

data class RiderProfile(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val phone: String,
    val vehicleNumber: String,
    val rating: Double,
    var isOnline: Boolean,
    var todayEarnings: Int,
    var weeklyEarnings: Int,
    var completedTripsToday: Int
)

data class PromoCode(
    val code: String,
    val discountPercent: Int,
    val maxDiscount: Int,
    val minOrder: Int,
    val descriptionBn: String,
    val descriptionEn: String
)
