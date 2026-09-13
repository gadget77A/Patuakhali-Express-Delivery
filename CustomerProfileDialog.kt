package com.example.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeliveryRepository
import com.example.ui.theme.ExpressGreenContainer
import com.example.ui.theme.ExpressGreenPrimary
import com.example.ui.theme.ExpressOrangeAccent

@Composable
fun CustomerProfileDialog(
    isBn: Boolean,
    onDismiss: () -> Unit
) {
    val currentName by DeliveryRepository.customerName.collectAsState()
    val currentPhone by DeliveryRepository.customerPhone.collectAsState()

    var nameInput by remember { mutableStateOf(currentName) }
    var phoneInput by remember { mutableStateOf(currentPhone) }
    var otpInput by remember { mutableStateOf("1234") }
    var isOtpSent by remember { mutableStateOf(false) }
    var isVerified by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("customer_profile_dialog"),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ExpressGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = ExpressGreenPrimary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isBn) "গ্রাহক প্রোফাইল ও লগইন" else "Customer Profile & OTP Login",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text(if (isBn) "গ্রাহকের পুরো নাম" else "Full Name", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    label = { Text(if (isBn) "মোবাইল নম্বর" else "Mobile Number", fontSize = 11.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (!isOtpSent) {
                    FilledTonalButton(
                        onClick = { isOtpSent = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isBn) "ওটিপি (OTP) কোড পাঠান" else "Send OTP Code")
                    }
                } else {
                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = { otpInput = it },
                        label = { Text(if (isBn) "৪ ডিজিটের ওটিপি লিখুন (ডেমো: 1234)" else "Enter 4-digit OTP (Demo: 1234)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Surface(
                        color = ExpressGreenContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = ExpressGreenPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBn) "ডেমো অ্যাকাউন্ট যাচাই সম্পন্ন!" else "Demo Phone Verified!",
                                fontSize = 11.sp,
                                color = ExpressGreenPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    DeliveryRepository.customerName.value = nameInput
                    DeliveryRepository.customerPhone.value = phoneInput
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = ExpressGreenPrimary)
            ) {
                Text(if (isBn) "সংরক্ষণ করুন" else "Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (isBn) "বন্ধ করুন" else "Close")
            }
        }
    )
}
