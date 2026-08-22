package com.pascal.noctra.ui.component.form

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle

import androidx.compose.ui.unit.dp
import com.pascal.noctra.ui.theme.AppTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronDown
import compose.icons.feathericons.Eye
import compose.icons.feathericons.EyeOff
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FormComponent(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    enabled: Boolean = false
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .border(1.dp, LightGray, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            enabled = enabled,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 1.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun FormBasicComponent(
    modifier: Modifier = Modifier,
    title: AnnotatedString,
    hintText: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    isNumber: Boolean = false,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()
    val border = if (isFocused.value) MaterialTheme.colorScheme.primary else LightGray

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .border(1.dp, border, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            enabled = enabled,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = if (isNumber) KeyboardType.Number
                else KeyboardType.Text
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hintText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )

        if (isError) {
            TextError2(title)
        }
    }
}

@Composable
fun FormClickedComponent(
    modifier: Modifier = Modifier,
    title: AnnotatedString,
    hintText: String,
    value: String,
    icon: ImageVector = FeatherIcons.ChevronDown,
    singleLine: Boolean = true,
    enabled: Boolean = false,
    onIconClick: () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .border(1.dp, LightGray, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                .clickable { onIconClick() },
            value = value,
            onValueChange = {},
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            enabled = enabled,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 1.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hintText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    innerTextField()
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterEnd),
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEmailComponent(
    modifier: Modifier = Modifier,
    title: String,
    hintText: String,
    isShowTitle: Boolean = true,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()
    val border = if (isFocused.value) MaterialTheme.colorScheme.primary else LightGray

    Column {
        if (isShowTitle) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .border(1.dp, border, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 1.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = hintText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )

        if (isError) {
            TextError(title)
        }
    }
}

@Composable
fun FormPasswordComponent(
    modifier: Modifier = Modifier,
    title: String,
    hintText: String,
    value: String,
    isShowTitle: Boolean = true,
    isMandatory: Boolean = false,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    isPasswordVisible: Boolean,
    onIconClick: () -> Unit,
    onEnter: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()
    val border = if (isFocused.value) MaterialTheme.colorScheme.primary else LightGray

    Column {
        if (isShowTitle) {
            Row {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                if (isMandatory) {
                    Text(
                        text = "*",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Red
                        )
                    )
                }

            }

        }

        Spacer(modifier = Modifier.height(6.dp))

        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .border(1.dp, border, RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp)),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.NumberPassword
            ),
            keyboardActions = KeyboardActions(onNext = {
                onEnter()
            }),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.weight(1f)
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = hintText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                            innerTextField()
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(16.dp)
                                .clickable {
                                    onIconClick()
                                },
                            imageVector = if (isPasswordVisible) FeatherIcons.Eye else FeatherIcons.EyeOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        )

        if (isError) {
            TextError(title)
        }
    }
}

@Composable
fun TextError(text: String) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$text tidak boleh kosong.",
            style = MaterialTheme.typography.labelMedium.copy(
                color = Red
            ),
        )
    }
}

@Composable
fun TextError2(text: AnnotatedString) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$text tidak boleh kosong.",
            style = MaterialTheme.typography.labelMedium.copy(
                color = Red
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormPreview() {
    AppTheme {
        FormComponent(
            title = "Name",
            value = "test",
            onValueChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormBasicPreview() {
    AppTheme {
        FormBasicComponent(
            title = buildAnnotatedString {
                append("Nama")
                withStyle(style = SpanStyle(color = Red)) {
                    append("*")
                }
            },
            hintText = "Masukan nama",
            value = "test",
            onValueChange = {},
            isError = false,
            enabled = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormClickedPreview() {
    AppTheme {
        FormClickedComponent(
            title = buildAnnotatedString {
                append("Nama")
                withStyle(style = SpanStyle(color = Red)) {
                    append("*")
                }
            },
            value = "test",
            hintText = "hint",
            onIconClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormEmailPreview() {
    AppTheme {
        FormEmailComponent(
            title = "Name",
            hintText = "Masukan nama",
            value = "test",
            onValueChange = {},
            isError = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FormPasswordPreview() {
    AppTheme {
        FormPasswordComponent(
            title = "Name",
            hintText = "Masukan nama",
            value = "test",
            onValueChange = {},
            isError = false,
            isPasswordVisible = false,
            onIconClick = {},
            onEnter = {}
        )
    }
}