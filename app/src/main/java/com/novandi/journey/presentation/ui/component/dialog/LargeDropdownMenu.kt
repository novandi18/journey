package com.novandi.journey.presentation.ui.component.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.Dark
import com.novandi.journey.presentation.ui.theme.DarkGray40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light

@Composable
fun <T> LargeDropdownMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector,
    label: String,
    notSetLabel: String? = null,
    items: List<T>,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (T, Boolean, Boolean, () -> Unit) -> Unit = { item, selected, itemEnabled, onClick ->
        LargeDropdownMenuItem(
            text = item.toString(),
            selected = selected,
            enabled = itemEnabled,
            onClick = onClick,
        )
    },
) {
    var expanded by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    Box(modifier = modifier.height(IntrinsicSize.Min)) {
        OutlinedTextField(
            label = { Text(label) },
            value = items.getOrNull(selectedIndex)?.let { selectedItemToString(it) } ?: "",
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                val iconDropdown = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown
                Icon(imageVector = iconDropdown, "")
            },
            leadingIcon = {
                Icon(imageVector = icon, contentDescription = "")
            },
            onValueChange = {},
            readOnly = true
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable(enabled = enabled) { expanded = true },
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Light
            ) {
                val listState = rememberLazyListState()
                if (selectedIndex > -1) {
                    LaunchedEffect("ScrollToSelected") {
                        listState.scrollToItem(index = selectedIndex)
                    }
                }

                Column {
                    if (items.size > 10) {
                        TextField(
                            modifier = modifier.fillMaxWidth(),
                            value = search,
                            onValueChange = { search = it },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Light,
                                focusedContainerColor = Light,
                                cursorColor = Dark,
                                unfocusedTextColor = DarkGray80,
                                focusedTextColor = Dark
                            ),
                            placeholder = {
                                Text(text = stringResource(id = R.string.search), color = DarkGray40)
                            },
                            singleLine = true,
                            trailingIcon = {
                                if (search.isNotEmpty()) {
                                    IconButton(
                                        onClick = { search = "" }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = "",
                                            tint = Dark
                                        )
                                    }
                                }
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "",
                                    tint = Blue40
                                )
                            }
                        )
                    }

                    LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                        if (notSetLabel != null) {
                            item {
                                LargeDropdownMenuItem(
                                    text = notSetLabel,
                                    selected = false,
                                    enabled = false,
                                    onClick = {},
                                )
                            }
                        }
                        itemsIndexed(
                            items.filter { v -> v.toString().lowercase().contains(search.lowercase().trim()) }
                        ) { index, item ->
                            val selectedItem = index == selectedIndex

                            drawItem(item, selectedItem, true) {
                                onItemSelected(items.indexOf(item), item)
                                expanded = false
                            }

                            if (index < items.lastIndex) {
                                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}