package com.pascal.noctra.ui.component.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.pascal.noctra.ui.theme.AppTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search

@Composable
fun SearchComponent(
    modifier: Modifier = Modifier,
    hint: String = "",
    suggestions: List<String> = emptyList(),
    onSearch: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val filteredSuggestions = remember(searchText) {
        if (searchText.isBlank()) emptyList()
        else suggestions.filter {
            it.contains(searchText, ignoreCase = true)
        }
    }

    var textFieldWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(modifier = modifier) {

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .onGloballyPositioned {
                    with(density) {
                        textFieldWidth = it.size.width.toDp()
                    }
                }
                .border(
                    1.dp,
                    if (isFocused) Color.White else MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(16.dp)
                )
                .background(
                    MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(16.dp)
                ),
            value = searchText,
            onValueChange = {
                searchText = it
                expanded = it.isNotBlank()
            },
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    expanded = false
                    onSearch(searchText)
                }
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = FeatherIcons.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box {
                        if (searchText.isEmpty()) {
                            Text(
                                text = hint,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        DropdownMenu(
            expanded = expanded && filteredSuggestions.isNotEmpty(),
            onDismissRequest = { expanded = false },
            properties = PopupProperties(
                focusable = false
            ),
            modifier = Modifier
                .width(textFieldWidth)
                .heightIn(max = 500.dp)
                .background(MaterialTheme.colorScheme.outline)
        ) {
            filteredSuggestions.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        searchText = item
                        expanded = false
                        onSearch(item)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchDropdown() {
    AppTheme {
        SearchComponent(
            modifier = Modifier.padding(16.dp),
            onSearch = {
                println("Search: $it")
            }
        )
    }
}
