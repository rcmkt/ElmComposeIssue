package com.example.elmcompose.input.dto

import java.math.BigDecimal

data class Account(
    val name: String,
    val balance: BigDecimal,
    val currency: String,
)
