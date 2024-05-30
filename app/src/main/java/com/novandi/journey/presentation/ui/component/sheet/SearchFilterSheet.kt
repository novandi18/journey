package com.novandi.journey.presentation.ui.component.sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novandi.journey.presentation.ui.theme.Blue40
import com.novandi.journey.presentation.ui.theme.DarkGray80
import com.novandi.journey.presentation.ui.theme.Light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterSheet(
    data: List<String>,
    selectedItem: String,
    onSelected: (String) -> Unit,
    onDismissed: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissed,
        containerColor = Light
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            items(data.size) {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSelected(data[it])
                    },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = if (data[it] == selectedItem) Blue40 else Light,
                        contentColor = if (data[it] == selectedItem) Light else DarkGray80
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        text = data[it],
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}