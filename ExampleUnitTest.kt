package com.example

import com.example.data.DeliveryRepository
import com.example.model.OrderStatus
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testOrderPlacementAndRiderLifecycle() {
        // 1. Add product to cart
        val sampleProduct = DeliveryRepository.products.value.first()
        DeliveryRepository.clearCart()
        DeliveryRepository.addToCart(sampleProduct)
        assertEquals(1, DeliveryRepository.cartItems.value.size)

        // 2. Customer places order
        val placedOrder = DeliveryRepository.placeOrder()
        assertNotNull(placedOrder)
        assertEquals(OrderStatus.CONFIRMED, placedOrder!!.status)
        assertNull(placedOrder.riderId) // unassigned initially

        // 3. Verify order appears at the top of orders list
        val latest = DeliveryRepository.orders.value.first()
        assertEquals(placedOrder.id, latest.id)

        // 4. Rider accepts the order
        DeliveryRepository.acceptRiderOrder(placedOrder.id)
        val acceptedOrder = DeliveryRepository.orders.value.first { it.id == placedOrder.id }
        assertNotNull(acceptedOrder.riderId)
        assertEquals(OrderStatus.PICKED_UP, acceptedOrder.status)

        // 5. Rider starts transit
        DeliveryRepository.updateOrderStatus(placedOrder.id, OrderStatus.ON_THE_WAY)
        val inTransitOrder = DeliveryRepository.orders.value.first { it.id == placedOrder.id }
        assertEquals(OrderStatus.ON_THE_WAY, inTransitOrder.status)

        // 6. Rider marks delivered
        val initialEarnings = DeliveryRepository.riderProfile.value.todayEarnings
        DeliveryRepository.updateOrderStatus(placedOrder.id, OrderStatus.DELIVERED)
        val deliveredOrder = DeliveryRepository.orders.value.first { it.id == placedOrder.id }
        assertEquals(OrderStatus.DELIVERED, deliveredOrder.status)
        assertEquals(initialEarnings + 50, DeliveryRepository.riderProfile.value.todayEarnings)
    }
}
