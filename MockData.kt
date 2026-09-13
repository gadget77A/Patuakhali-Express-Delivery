package com.example.data

import com.example.model.*

object MockData {
    val shops = listOf(
        // Food
        Shop(
            id = "shop_1",
            nameBn = "পটুয়াখালী কাচ্চি বিরিয়ানি হাউজ",
            nameEn = "Patuakhali Kacchi Biryani House",
            category = ServiceCategory.FOOD,
            rating = 4.8,
            reviewCount = 320,
            deliveryTimeMin = 25,
            deliveryFee = 35,
            addressBn = "চকবাজার প্রধান সড়ক, পটুয়াখালী সদর",
            addressEn = "Chawkbazar Main Road, Patuakhali Sadar",
            iconEmoji = "🍛",
            isFeatured = true
        ),
        Shop(
            id = "shop_2",
            nameBn = "পায়রা রিভারভিউ ক্যাফে ও রেস্তোরাঁ",
            nameEn = "Payra Riverview Cafe & Restaurant",
            category = ServiceCategory.FOOD,
            rating = 4.6,
            reviewCount = 185,
            deliveryTimeMin = 30,
            deliveryFee = 40,
            addressBn = "লঞ্চঘাট রোড, পায়রা নদীর পাড়, পটুয়াখালী",
            addressEn = "Launchghat Road, Payra Riverfront, Patuakhali",
            iconEmoji = "🍔",
            isFeatured = true
        ),
        Shop(
            id = "shop_3",
            nameBn = "ধানসিঁড়ি ঐতিহ্যবাহী বাংলা খাবার",
            nameEn = "Dhansiri Traditional Bengali Food",
            category = ServiceCategory.FOOD,
            rating = 4.7,
            reviewCount = 240,
            deliveryTimeMin = 25,
            deliveryFee = 30,
            addressBn = "কলেজ রোড, সরকারি মহিলা কলেজের পাশে, পটুয়াখালী",
            addressEn = "College Road, Near Govt Mohila College",
            iconEmoji = "🍲",
            isFeatured = false
        ),
        Shop(
            id = "shop_4",
            nameBn = "বরিশাল মিষ্টি মেলা ও দধি ভান্ডার",
            nameEn = "Barisal Sweet Mela & Curd Shop",
            category = ServiceCategory.FOOD,
            rating = 4.9,
            reviewCount = 410,
            deliveryTimeMin = 20,
            deliveryFee = 25,
            addressBn = "নতুন বাজার মোড়, পটুয়াখালী",
            addressEn = "Nutun Bazaar Mor, Patuakhali",
            iconEmoji = "🧁",
            isFeatured = true
        ),

        // Grocery
        Shop(
            id = "shop_5",
            nameBn = "পায়রা ফ্রেশ গ্রোসারি ও কাঁচাবাজার",
            nameEn = "Payra Fresh Grocery & Daily Market",
            category = ServiceCategory.GROCERY,
            rating = 4.7,
            reviewCount = 150,
            deliveryTimeMin = 35,
            deliveryFee = 45,
            addressBn = "পৌর সুপার মার্কেট, পটুয়াখালী সদর",
            addressEn = "Pouro Super Market, Patuakhali Sadar",
            iconEmoji = "🥦",
            isFeatured = true
        ),
        Shop(
            id = "shop_6",
            nameBn = "নিউ মার্কেট চাল-ডাল ভান্ডার",
            nameEn = "New Market Essentials & Grain Store",
            category = ServiceCategory.GROCERY,
            rating = 4.5,
            reviewCount = 95,
            deliveryTimeMin = 40,
            deliveryFee = 50,
            addressBn = "নিউ মার্কেট, থানা রোড, পটুয়াখালী",
            addressEn = "New Market, Thana Road, Patuakhali",
            iconEmoji = "🌾",
            isFeatured = false
        ),

        // Medicine
        Shop(
            id = "shop_7",
            nameBn = "পদ্মা মেডিকেল কর্নার ও ফার্মেসি",
            nameEn = "Padma Medical Corner & Pharmacy",
            category = ServiceCategory.MEDICINE,
            rating = 4.9,
            reviewCount = 280,
            deliveryTimeMin = 15,
            deliveryFee = 30,
            addressBn = "সদর হাসপাতাল রোড (জরুরি গেট সংলগ্ন), পটুয়াখালী",
            addressEn = "Sadar Hospital Road, Emergency Gate, Patuakhali",
            iconEmoji = "💊",
            isFeatured = true
        ),
        Shop(
            id = "shop_8",
            nameBn = "লাইফকেয়ার সেন্ট্রাল ড্রাগ হাউজ",
            nameEn = "Lifecare Central Drug House (24/7)",
            category = ServiceCategory.MEDICINE,
            rating = 4.8,
            reviewCount = 190,
            deliveryTimeMin = 20,
            deliveryFee = 35,
            addressBn = "ডিসি স্কয়ার রোড, পটুয়াখালী",
            addressEn = "DC Square Road, Patuakhali",
            iconEmoji = "🩺",
            isFeatured = false
        ),

        // Parcel
        Shop(
            id = "shop_9",
            nameBn = "পটুয়াখালী এক্সপ্রেস দ্রুত পার্সেল হাব",
            nameEn = "Patuakhali Express Fast Parcel Hub",
            category = ServiceCategory.PARCEL,
            rating = 4.9,
            reviewCount = 520,
            deliveryTimeMin = 20,
            deliveryFee = 50,
            addressBn = "লঞ্চঘাট পয়েন্ট ও বাস টার্মিনাল, পটুয়াখালী",
            addressEn = "Launchghat Point & Bus Terminal, Patuakhali",
            iconEmoji = "📦",
            isFeatured = true
        )
    )

    val products = listOf(
        // Kacchi Biryani House
        Product(
            id = "prod_1",
            shopId = "shop_1",
            nameBn = "স্পেশাল বাসমতী খাসির কাচ্চি (১ প্লেট)",
            nameEn = "Special Mutton Basmati Kacchi (1 Plate)",
            descriptionBn = "আসল ঘিয়ে রান্না করা তুলতুলে খাসির মাংস ও আলু সমেত সুস্বাদু কাচ্চি বিরিয়ানি। সাথে সালাদ ও বোরহানি।",
            descriptionEn = "Authentic tender mutton cooked in aromatic basmati rice with spiced potato and borhani.",
            price = 280,
            originalPrice = 320,
            category = ServiceCategory.FOOD,
            unitBn = "প্লেট",
            unitEn = "Plate",
            iconEmoji = "🍛"
        ),
        Product(
            id = "prod_2",
            shopId = "shop_1",
            nameBn = "চিকেন দম বিরিয়ানি হাফ",
            nameEn = "Chicken Dum Biryani (Half)",
            descriptionBn = "দেশি মসলায় তৈরি খাস চিকেন রোস্ট পিস ও পোলাও চালের দম বিরিয়ানি।",
            descriptionEn = "Slow-cooked spiced chicken roast served over seasoned long grain rice.",
            price = 190,
            originalPrice = 220,
            category = ServiceCategory.FOOD,
            unitBn = "প্লেট",
            unitEn = "Plate",
            iconEmoji = "🍗"
        ),
        Product(
            id = "prod_3",
            shopId = "shop_1",
            nameBn = "স্পেশাল ঐতিহ্যবাহী বোরহানি (২৫০ মি.লি.)",
            nameEn = "Special Traditional Borhani (250ml)",
            descriptionBn = "টক দই, পুদিনা পাতা ও শাহী মসলার মশলাদার বোরহানি।",
            descriptionEn = "Spicy yogurt digestive drink infused with mint and roasted cumin.",
            price = 50,
            category = ServiceCategory.FOOD,
            unitBn = "গ্লাস",
            unitEn = "Glass",
            iconEmoji = "🥛"
        ),

        // Payra Riverview Cafe
        Product(
            id = "prod_4",
            shopId = "shop_2",
            nameBn = "ক্রিস্পি চিকেন বার্গার সাথে ফ্রাইস",
            nameEn = "Crispy Chicken Burger with Fries",
            descriptionBn = "ফ্রেশ লেটুস, চিজ স্লাইস ও মায়ো সহ মুচমুচে ফ্রাইড চিকেন বার্গার।",
            descriptionEn = "Fresh toasted bun with crispy golden fried chicken breast and crinkle fries.",
            price = 170,
            originalPrice = 200,
            category = ServiceCategory.FOOD,
            unitBn = "পিস",
            unitEn = "Pcs",
            iconEmoji = "🍔"
        ),
        Product(
            id = "prod_5",
            shopId = "shop_2",
            nameBn = "পায়রা হট কফি ও চকোলেট মাফিন",
            nameEn = "Payra Hot Coffee & Choco Muffin",
            descriptionBn = "ব্রু কফি ও গরম চকোলেট ওভেন বেকড মাফিন।",
            descriptionEn = "Freshly brewed hot cappuccino with a fresh chocolate chip muffin.",
            price = 120,
            category = ServiceCategory.FOOD,
            unitBn = "সেট",
            unitEn = "Set",
            iconEmoji = "☕"
        ),

        // Dhansiri Bangla
        Product(
            id = "prod_6",
            shopId = "shop_3",
            nameBn = "পটুয়াখালী পায়রা নদীর ইলিশ ভাজা ও খিচুড়ি",
            nameEn = "Payra River Fried Hilsa & Khichuri",
            descriptionBn = "টাটকা রূপালী ইলিশ ভাজা, তেল সমেত ভুনা খিচুড়ি ও বেগুন ভাজা।",
            descriptionEn = "Fresh Payra river Hilsa fry with aromatic yellow lentil khichuri.",
            price = 320,
            originalPrice = 360,
            category = ServiceCategory.FOOD,
            unitBn = "প্লেট",
            unitEn = "Plate",
            iconEmoji = "🐟"
        ),

        // Sweets
        Product(
            id = "prod_7",
            shopId = "shop_4",
            nameBn = "বরিশালের স্পেশাল চমচম (আধা কেজি)",
            nameEn = "Barisal Special Chomchom (500g)",
            descriptionBn = "খাঁটি গরুর দুধের ছানায় তৈরি নরম ও রসে ভরা ঐতিহ্যবাহী চমচম।",
            descriptionEn = "Fresh traditional Bengali sweet cottage cheese dessert dipped in sugar syrup.",
            price = 220,
            category = ServiceCategory.FOOD,
            unitBn = "বক্স",
            unitEn = "Box",
            iconEmoji = "🧁"
        ),
        Product(
            id = "prod_8",
            shopId = "shop_4",
            nameBn = "বগুড়ার স্পেশাল ক্ষীরসা মিষ্টি দই (হাঁড়ি)",
            nameEn = "Special Claypot Sweet Curd (Misti Doi)",
            descriptionBn = "মাটির হাঁড়িতে পাতা ক্ষীরযুক্ত সুস্বাদু মিষ্টি দই।",
            descriptionEn = "Rich caramel-colored baked sweet curd in a traditional clay pot.",
            price = 180,
            category = ServiceCategory.FOOD,
            unitBn = "হাঁড়ি",
            unitEn = "Pot",
            iconEmoji = "🍯"
        ),

        // Grocery - Payra Fresh
        Product(
            id = "prod_9",
            shopId = "shop_5",
            nameBn = "স্থানীয় টাটকা সবুজ শাকসবজি বান্ডিল",
            nameEn = "Local Fresh Mixed Veggies Bundle",
            descriptionBn = "আলু ১ কেজি, পেঁয়াজ ১ কেজি, টমেটো ৫০০ গ্রাম এবং ধনেপাতা।",
            descriptionEn = "Daily kitchen staples: Potato 1kg, Onion 1kg, Tomato 500g, Fresh Coriander.",
            price = 190,
            category = ServiceCategory.GROCERY,
            unitBn = "প্যাক",
            unitEn = "Pack",
            iconEmoji = "🥔"
        ),
        Product(
            id = "prod_10",
            shopId = "shop_5",
            nameBn = "খাঁটি সরিষার তেল (১ লিটার)",
            nameEn = "Pure Cold-Pressed Mustard Oil (1L)",
            descriptionBn = "ঘানিভাঙা খাঁটি দেশি সরিষার ঝাঁঝালো তেল।",
            descriptionEn = "Premium grade aromatic cold-pressed mustard cooking oil.",
            price = 260,
            originalPrice = 280,
            category = ServiceCategory.GROCERY,
            unitBn = "বোতল",
            unitEn = "Bottle",
            iconEmoji = "🫒"
        ),
        Product(
            id = "prod_11",
            shopId = "shop_6",
            nameBn = "প্রিমিয়াম মিনিকেট চাল (৫ কেজি বস্তা)",
            nameEn = "Premium Miniket Rice (5kg Bag)",
            descriptionBn = "পটুয়াখালী দক্ষিণাঞ্চলের চিকন ও ঝরঝরে প্রিমিয়াম মিনিকেট চাল।",
            descriptionEn = "High grade slender polished miniket rice 5kg pack.",
            price = 370,
            originalPrice = 400,
            category = ServiceCategory.GROCERY,
            unitBn = "বস্তা",
            unitEn = "Bag",
            iconEmoji = "🌾"
        ),

        // Medicine - Padma Medical Corner
        Product(
            id = "prod_12",
            shopId = "shop_7",
            nameBn = "প্যারাসিটামল ৫০০ মি.গ্রা. (২০ টি ট্যাবলেট)",
            nameEn = "Paracetamol 500mg (20 Tablets)",
            descriptionBn = "জর ও শরীর ব্যথার দ্রুত নিরাময়। অনুমোদিত ফার্মাসিউটিক্যালস।",
            descriptionEn = "Fever and mild pain relief tablet pack (Beximco / Square).",
            price = 30,
            category = ServiceCategory.MEDICINE,
            unitBn = "পাতা",
            unitEn = "Strips",
            iconEmoji = "💊"
        ),
        Product(
            id = "prod_13",
            shopId = "shop_7",
            nameBn = "টেস্টি স্যালাইন ও ওআরএস প্যাক (৫ টি)",
            nameEn = "ORS Oral Rehydration Salt (5 Sachets)",
            descriptionBn = "ডিহাইড্রেশন ও দুর্বলতা নিবারণে এসএমসি ওআরএস স্যালাইন।",
            descriptionEn = "Essential electrolyte balance rehydration pack by SMC.",
            price = 35,
            category = ServiceCategory.MEDICINE,
            unitBn = "প্যাক",
            unitEn = "Pack",
            iconEmoji = "💧"
        ),
        Product(
            id = "prod_14",
            shopId = "shop_7",
            nameBn = "অ্যান্টিসেপটিক ব্যান্ডেজ ও স্যাভলন সলিউশন",
            nameEn = "First Aid Antiseptic & Bandage Kit",
            descriptionBn = "কাটাছেঁড়ায় জরুরি প্রাথমিক চিকিৎসার জন্য ব্যান্ডেজ ও স্যাভলন।",
            descriptionEn = "Sterile waterproof adhesive bandages with 50ml Savlon.",
            price = 75,
            category = ServiceCategory.MEDICINE,
            unitBn = "কিট",
            unitEn = "Kit",
            iconEmoji = "🩹"
        ),

        // Parcel
        Product(
            id = "prod_15",
            shopId = "shop_9",
            nameBn = "পটুয়াখালী শহর জরুরি পার্সেল ডেলিভারি (<১ কেজি)",
            nameEn = "Intra-City Express Parcel (<1kg)",
            descriptionBn = "শহরের যেকোনো স্থানে ১ ঘণ্টার মধ্যে কাগজপত্র বা প্যাকেট পৌঁছে দিন।",
            descriptionEn = "Fast door-to-door small parcel & legal document delivery in Patuakhali.",
            price = 60,
            category = ServiceCategory.PARCEL,
            unitBn = "টিকেট",
            unitEn = "Ticket",
            iconEmoji = "📦"
        ),
        Product(
            id = "prod_16",
            shopId = "shop_9",
            nameBn = "ভারী পার্সেল / বক্স ডেলিভারি (১-৫ কেজি)",
            nameEn = "Medium Box & Hardware Delivery (1-5kg)",
            descriptionBn = "নিরাপদ কার্গো ও মাঝারি প্যাকেজ হোম ডেলিভারি সার্ভিস।",
            descriptionEn = "Safe transport for electronic goods, clothing boxes, and household items.",
            price = 110,
            category = ServiceCategory.PARCEL,
            unitBn = "টিকেট",
            unitEn = "Ticket",
            iconEmoji = "📫"
        )
    )

    val neighborhoods = listOf(
        "চকবাজার, পটুয়াখালী (Chawkbazar)",
        "লঞ্চঘাট মোড় (Launch Ghat)",
        "সরকারি কলেজ রোড (Govt College Road)",
        "ডিসি স্কয়ার ও আদালত চত্বর (DC Square)",
        "সদর হাসপাতাল এলাকা (Sadar Hospital)",
        "নতুন বাজার ও পৌর মার্কেট (Nutun Bazaar)",
        "পুলিশ লাইনস ও এসপি অফিস (Police Lines)",
        "শেরে বাংলা পার্ক রোড (Sher-e-Bangla Park)",
        "পায়রা সেতু অ্যাপ্রোচ মোড় (Payra Bridge Point)"
    )

    val promoCodes = listOf(
        PromoCode("PATUAKHALI20", 20, 80, 200, "পটুয়াখালী এক্সপ্রেস ২০% বিশেষ ছাড় (সর্বোচ্চ ৮০৳)", "20% off on all orders up to 80 BDT"),
        PromoCode("FREEBD", 10, 50, 150, "১০% দ্রুত ডেলিভারি ছাড়", "10% fast delivery discount"),
        PromoCode("PAYRA50", 15, 60, 250, "পায়রা রিভার উৎসব ছাড়", "15% off up to 60 BDT")
    )

    val initialOrders = listOf(
        Order(
            id = "PED-3091",
            customerName = "হাসান মাহমুদ (Hasan Mahmud)",
            customerPhone = "+8801711223344",
            serviceCategory = ServiceCategory.FOOD,
            shopNameBn = "পটুয়াখালী কাচ্চি বিরিয়ানি হাউজ",
            shopNameEn = "Patuakhali Kacchi Biryani House",
            pickupAddressBn = "চকবাজার প্রধান সড়ক, পটুয়াখালী সদর",
            deliveryAddressBn = "সরকারি কলেজ রোড, বাসা নং ৪২, পটুয়াখালী",
            items = listOf(
                OrderItem("prod_1", "স্পেশাল বাসমতী খাসির কাচ্চি (১ প্লেট)", "Special Mutton Basmati Kacchi", 2, 280, "🍛"),
                OrderItem("prod_3", "স্পেশাল ঐতিহ্যবাহী বোরহানি", "Special Borhani", 2, 50, "🥛")
            ),
            subtotal = 660,
            deliveryFee = 35,
            discount = 60,
            totalAmount = 635,
            paymentMethod = PaymentMethod.CASH_ON_DELIVERY,
            status = OrderStatus.ON_THE_WAY,
            orderTime = "আজ ১২:২০ PM",
            riderId = "rider_1",
            riderName = "মো: রফিকুল ইসলাম (Md. Rafiqul)",
            riderPhone = "+8801899112233",
            riderVehicle = "পটুয়াখালী-হ-৮৪২২ (TVS Metro)",
            riderProgress = 0.65f,
            rating = null,
            reviewText = null
        ),
        Order(
            id = "PED-3088",
            customerName = "নুসরাত জাহান (Nusrat Jahan)",
            customerPhone = "+8801912345678",
            serviceCategory = ServiceCategory.MEDICINE,
            shopNameBn = "পদ্মা মেডিকেল কর্নার ও ফার্মেসি",
            shopNameEn = "Padma Medical Corner",
            pickupAddressBn = "সদর হাসপাতাল রোড, পটুয়াখালী",
            deliveryAddressBn = "ডিসি স্কয়ার রোড, কোয়ার্টার ৫, পটুয়াখালী",
            items = listOf(
                OrderItem("prod_12", "প্যারাসিটামল ৫০০ মি.গ্রা.", "Paracetamol 500mg", 1, 30, "💊"),
                OrderItem("prod_13", "টেস্টি স্যালাইন ও ওআরএস প্যাক", "ORS Oral Rehydration Salt", 2, 35, "💧")
            ),
            subtotal = 100,
            deliveryFee = 30,
            discount = 0,
            totalAmount = 130,
            paymentMethod = PaymentMethod.BKASH,
            status = OrderStatus.DELIVERED,
            orderTime = "আজ সকাল ১০:১৫ AM",
            riderId = "rider_1",
            riderName = "মো: রফিকুল ইসলাম (Md. Rafiqul)",
            riderPhone = "+8801899112233",
            riderVehicle = "পটুয়াখালী-হ-৮৪২২",
            riderProgress = 1.0f,
            rating = 5,
            reviewText = "খুব দ্রুত ওষুধ পৌঁছে দিয়েছে। অনেক ধন্যবাদ!"
        ),
        Order(
            id = "PED-3095",
            customerName = "আনোয়ারুল কবির (Anwarul Kabir)",
            customerPhone = "+8801755667788",
            serviceCategory = ServiceCategory.GROCERY,
            shopNameBn = "পায়রা ফ্রেশ গ্রোসারি",
            shopNameEn = "Payra Fresh Grocery",
            pickupAddressBn = "পৌর সুপার মার্কেট, পটুয়াখালী",
            deliveryAddressBn = "পুলিশ লাইনস মোড়, পটুয়াখালী সদর",
            items = listOf(
                OrderItem("prod_9", "টাটকা সবুজ শাকসবজি বান্ডিল", "Local Veggies Bundle", 1, 190, "🥔"),
                OrderItem("prod_10", "খাঁটি সরিষার তেল (১ লিটার)", "Mustard Oil 1L", 1, 260, "🫒")
            ),
            subtotal = 450,
            deliveryFee = 45,
            discount = 40,
            totalAmount = 455,
            paymentMethod = PaymentMethod.CASH_ON_DELIVERY,
            status = OrderStatus.PREPARING,
            orderTime = "আজ ১২:৪৫ PM",
            riderId = null,
            riderName = null,
            riderPhone = null,
            riderVehicle = null,
            riderProgress = 0.0f
        )
    )

    val defaultRider = RiderProfile(
        id = "rider_1",
        nameBn = "মো: রফিকুল ইসলাম",
        nameEn = "Md. Rafiqul Islam",
        phone = "+8801899112233",
        vehicleNumber = "পটুয়াখালী-হ-৮৪২২ (হোন্ডা সাইন)",
        rating = 4.9,
        isOnline = true,
        todayEarnings = 780,
        weeklyEarnings = 4650,
        completedTripsToday = 8
    )
}
