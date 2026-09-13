package com.example.ui.util

import com.example.model.OrderStatus
import com.example.model.PaymentMethod
import com.example.model.ServiceCategory
import com.example.model.UserRole

object Strings {
    fun appName(isBn: Boolean) = if (isBn) "পটুয়াখালী এক্সপ্রেস" else "Patuakhali Express"
    fun appSubtitle(isBn: Boolean) = if (isBn) "পটুয়াখালীর নিজস্ব ডেলিভারি প্ল্যাটফর্ম" else "Patuakhali's Local Delivery Platform"

    fun roleCustomer(isBn: Boolean) = if (isBn) "গ্রাহক (Customer)" else "Customer"
    fun roleRider(isBn: Boolean) = if (isBn) "ডেলিভারি রাইডার" else "Rider"
    fun roleAdmin(isBn: Boolean) = if (isBn) "অ্যাডমিন প্যানেল" else "Admin"

    fun searchPlaceholder(isBn: Boolean) = if (isBn) "খাবার, মুদি বা ওষুধ খুঁজুন..." else "Search food, groceries or medicine..."
    fun categoryAll(isBn: Boolean) = if (isBn) "সকল সেবা" else "All Services"

    fun categoryTitle(category: ServiceCategory, isBn: Boolean) = when (category) {
        ServiceCategory.FOOD -> if (isBn) "খাবার ও রেস্তোরাঁ" else "Food & Restaurant"
        ServiceCategory.GROCERY -> if (isBn) "মুদি ও কাঁচাবাজার" else "Grocery & Market"
        ServiceCategory.PARCEL -> if (isBn) "এক্সপ্রেস পার্সেল" else "Express Parcel"
        ServiceCategory.MEDICINE -> if (isBn) "ওষুধ ও ফার্মেসি" else "Medicine & Health"
    }

    fun categorySubtitle(category: ServiceCategory, isBn: Boolean) = when (category) {
        ServiceCategory.FOOD -> if (isBn) "গরম গরম বিরিয়ানি, ফাস্টফুড ও নাস্তা" else "Hot biryani, fast food & snacks"
        ServiceCategory.GROCERY -> if (isBn) "টাটকা শাকসবজি, চাল-ডাল ও নিত্যপ্রয়োজনীয়" else "Fresh veggies, staples & daily items"
        ServiceCategory.PARCEL -> if (isBn) "শহরের যেকোনো স্থানে দ্রুত পার্সেল ডেলিভারি" else "Fast intra-city parcel & document courier"
        ServiceCategory.MEDICINE -> if (isBn) "জরুরি ওষুধ ও প্রেসক্রিপশন সাপ্লাই" else "Emergency medicines & healthcare"
    }

    fun statusTitle(status: OrderStatus, isBn: Boolean) = when (status) {
        OrderStatus.PENDING -> if (isBn) "অপেক্ষমান" else "Pending"
        OrderStatus.CONFIRMED -> if (isBn) "অর্ডার নিশ্চিত" else "Confirmed"
        OrderStatus.PREPARING -> if (isBn) "তৈরি হচ্ছে" else "Preparing"
        OrderStatus.PICKED_UP -> if (isBn) "পিকআপ সম্পন্ন" else "Picked Up"
        OrderStatus.ON_THE_WAY -> if (isBn) "ডেলিভারির পথে" else "On The Way"
        OrderStatus.DELIVERED -> if (isBn) "ডেলিভারি সম্পন্ন" else "Delivered"
        OrderStatus.CANCELLED -> if (isBn) "বাতিলকৃত" else "Cancelled"
    }

    fun paymentTitle(payment: PaymentMethod, isBn: Boolean) = when (payment) {
        PaymentMethod.CASH_ON_DELIVERY -> if (isBn) "ক্যাশ অন ডেলিভারি (COD)" else "Cash on Delivery (COD)"
        PaymentMethod.BKASH -> if (isBn) "বিকাশ পেমেন্ট (bKash)" else "bKash Online"
        PaymentMethod.NAGAD -> if (isBn) "নগদ পেমেন্ট (Nagad)" else "Nagad Online"
    }

    fun currency(amount: Int) = "৳$amount"
}
