package com.example.elmcompose.input

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.elmcompose.BaseFragment
import com.example.elmcompose.input.dto.Account
import com.example.elmcompose.input.presentation.*
import com.example.elmcompose.input.presentation.InputMoneyStoreFactory
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import vivid.money.elmslie.core.store.Store
import java.math.BigDecimal

internal class InputMoneyFragment :
    BaseFragment<InputMoneyEvent, InputMoneyEffect, InputMoneyState>() {

    override val initEvent = InputMoneyEvent.Ui.System.Init

    override fun createStore(): InputMoneyStore {
        return InputMoneyStoreFactory().create()
    }

    @Composable
    override fun Content(
        store: Store<InputMoneyEvent, InputMoneyEffect, InputMoneyState>,
        state: InputMoneyState
    ) {
        Scaffold(
            topBar = {
                AppBar(
                    text = "Buy",
                    onNavigationClick = { store.accept(InputMoneyEvent.Ui.Click.Exit) },
                    contentPadding = rememberInsetsPaddingValues(
                        LocalWindowInsets.current.statusBars,
                        applyBottom = false,
                    ),
                )
            },
        ) { contentPadding ->

            LazyColumn(
                modifier = Modifier.padding(contentPadding)
            ) {
                item {
                    RateItem(
                        state.rate ?: BigDecimal.ONE,
                        state.pickedCurrency,
                    )
                }
                state.pickedAccount?.let {
                    item {
                        AccountItem(
                            account = state.pickedAccount,
                            onPocketClick = { store.accept(InputMoneyEvent.Ui.Click.PickPocket) }
                        )
                    }
                }

                item { HeaderItem(text = "Quantity") }

                item {
                    InputItem(
                        value = state.formattedInput,
                        isEnabled = !state.isOrdering,
                        onValueChange = {
                            store.accept(InputMoneyEvent.Ui.Action.UpdateInputState(it))
                        },
                    )
                }
            }
        }
    }

    @Composable
    private fun AppBar(
        text: String,
        navigationIcon: ImageVector = Icons.Filled.ArrowBack,
        contentPadding: PaddingValues = PaddingValues(0.dp),
        onNavigationClick: () -> Unit
    ) {
        Log.d("Debug", "AppBar")
        TopAppBar(
            title = {
                Text(
                    text = text,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onPrimary
                )
            },
            backgroundColor = Color.White,
            contentColor = MaterialTheme.colors.onPrimary,
            navigationIcon = {
                IconButton(onClick = onNavigationClick) {
                    Icon(navigationIcon, null)
                }
            },
            contentPadding = contentPadding,
            elevation = 0.dp,
        )
    }

    @Composable
    private fun HeaderItem(
        text: String,
    ) {
        Log.d("Debug", "HeaderItem: $text")
        Text(
            text = text,
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }

    @Composable
    private fun RateItem(
        rate: BigDecimal,
        currency: String,
    ) {
        Log.d("Debug", "RateItem")
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    painter = ColorPainter(Color.Gray),
                    contentDescription = null
                )

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = "1 EUR = ${rate.toString()} $currency",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onPrimary
                    )

                    Text(
                        text = "Buy",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(color = Color.Gray)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        }
    }

    @Composable
    private fun AccountItem(
        account: Account,
        onPocketClick: () -> Unit,
    ) {
        Log.d("Debug", "AccountItem")
        Column {
            HeaderItem("From")
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = Color.Gray)
                    .clickable { onPocketClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    painter = ColorPainter(Color.DarkGray),
                    contentDescription = null
                )

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onPrimary,
                    )
                    Text(
                        text = account.balance.toString(),
                        style = MaterialTheme.typography.subtitle2,
                        color = Color.White,
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
        }
    }

    @Composable
    private fun InputItem(
        value: String,
        isEnabled: Boolean,
        onValueChange: (String) -> Unit
    ) {
        Log.d("Debug", "InputItem")
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            placeholder = { Text(text = "0", style = MaterialTheme.typography.h5) },
            textStyle = MaterialTheme.typography.h5,
            enabled = isEnabled,
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.onPrimary,
            )
        )
    }
}