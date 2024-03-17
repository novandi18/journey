package com.novandi.journey.presentation.ui.component.field

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.novandi.journey.presentation.ui.component.dialog.LargeDropdownMenu

@Composable
fun JDropdownDialog(
    icon: ImageVector,
    label: String,
    items: List<String>,
    selectedIndex: Int = -1,
    setSelectedItem: (Int) -> Unit,
) {
    LargeDropdownMenu(
        icon = icon,
        label = label,
        items = items,
        selectedIndex = selectedIndex,
        onItemSelected = { index, _ -> setSelectedItem(index) }
    )
}