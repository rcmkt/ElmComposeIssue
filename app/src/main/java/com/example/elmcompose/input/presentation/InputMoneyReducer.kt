package com.example.elmcompose.input.presentation

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

object InputMoneyReducer :
    ScreenDslReducer<InputMoneyEvent, InputMoneyEvent.Ui, InputMoneyEvent.Internal, InputMoneyState, InputMoneyEffect, InputMoneyCommand>(
        InputMoneyEvent.Ui::class, InputMoneyEvent.Internal::class
    ) {

    override fun Result.internal(event: InputMoneyEvent.Internal): Any {
        return Unit
    }

    override fun Result.ui(event: InputMoneyEvent.Ui) = when (event) {
        is InputMoneyEvent.Ui.Action.UpdateInputState -> {
            state {
                copy(formattedInput = event.value)
            }
        }
        else -> Unit
    }
}