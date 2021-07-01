package com.example.elmcompose.input.presentation

import com.example.elmcompose.input.dto.Account
import vivid.money.elmslie.core.store.Store
import java.math.BigDecimal

typealias InputMoneyStore =
        Store<InputMoneyEvent, InputMoneyEffect, InputMoneyState>

data class InputMoneyState(
    val pickedCurrency: String = Typography.dollar.toString(),
    val pickedAccount: Account? = null,

    val rate: BigDecimal? = null,

    val formattedInput: String = "",

    val isOrdering: Boolean = false,
)

sealed class InputMoneyEffect

sealed class InputMoneyCommand

sealed class InputMoneyEvent {
    sealed class Internal : InputMoneyEvent()

    sealed class Ui : InputMoneyEvent() {
        object System {
            object Init : Ui()
        }

        object Click {
            object PickPocket : Ui()
            object Exit : Ui()
        }

        object Action {
            data class UpdateInputState(val value: String) : Ui()
        }
    }
}