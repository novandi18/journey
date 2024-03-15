package com.novandi.journey.presentation.ui.component.field

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssistWalker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novandi.journey.R
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.JourneyTheme
import com.novandi.journey.presentation.ui.theme.Light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JDropdownField(
    label: String,
    icon: ImageVector,
    data: List<String>,
    itemSelected: Int = -1,
    setItemSelected: (Int) -> Unit,
    isReadOnly: Boolean = false
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }
    var search by remember { mutableStateOf("") }

    if (itemSelected != -1) search = data[itemSelected]

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (!isReadOnly) expanded = !expanded
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .menuAnchor(),
            value = if (data.isNotEmpty()) search else stringResource(id = R.string.loading),
            onValueChange = {
                if (!expanded) expanded = true
                search = it
            },
            label = {
                Text(
                    text = label,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            },
            trailingIcon = {
                if (!isReadOnly) ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            leadingIcon = {
                Icon(imageVector = icon, contentDescription = label)
            },
            singleLine = true,
            readOnly = data.isEmpty() || isReadOnly
        )
        ExposedDropdownMenu(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .background(Light),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (data.isNotEmpty()) {
                data.filter { v ->
                    v.lowercase().contains(
                        if (expanded) search.lowercase().trim() else ""
                    )
                }.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            search = item
                            setItemSelected(
                                data.indexOf(item)
                            )
                            expanded = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (itemSelected == index) Blue40.copy(alpha = .2f) else Light
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    JourneyTheme {
        JDropdownField(
            icon = Icons.Filled.AssistWalker,
            label = stringResource(id = R.string.disability_placeholder),
            data = listOf("OK", "YA", "NO"),
            setItemSelected = {},
            itemSelected = 1
        )
    }
}