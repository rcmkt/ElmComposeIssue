package com.example.elmcompose.input.presentation

import com.example.elmcompose.input.dto.Account
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.NoOpActor
import java.math.BigDecimal

internal class InputMoneyStoreFactory {

    fun create(): InputMoneyStore {
        return ElmStore(
            initialState = InputMoneyState(
                pickedAccount = Account(
                    name = "For traveling",
                    balance = BigDecimal("1000"),
                    currency = Typography.dollar.toString()
                )
            ),
            reducer = InputMoneyReducer,
            actor = NoOpActor(),
        )
    }
}