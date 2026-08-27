package com.pascal.noctra.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pascal.noctra.ui.component.button.ButtonComponent
import com.pascal.noctra.ui.component.button.ButtonOutlineComponent
import com.pascal.noctra.ui.component.form.FormPasswordComponent
import com.pascal.noctra.ui.theme.AppTheme

@Composable
fun ShowDialog(
    modifier: Modifier = Modifier,
    message: String,
    textButton: String,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Dialog(
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        ),
        onDismissRequest = { onClick() },
    ) {
        Column(
            modifier = modifier
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = message,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            ButtonOutlineComponent(
                modifier = Modifier,
                color = color,
                text = textButton
            ) {
                onClick()
            }
        }
    }
}

@Composable
fun ShowChoiceDialog(
    modifier: Modifier = Modifier,
    message: String,
    textButton: String,
    color: Color = MaterialTheme.colorScheme.primary,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        ),
        onDismissRequest = { onCancel() },
    ) {
        Column(
            modifier = modifier
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = message,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                ButtonComponent(
                    color = color,
                    text = textButton
                ) {
                    onConfirm()
                }
                Spacer(modifier = Modifier.height(16.dp))
                ButtonOutlineComponent(
                    color = color,
                    text = "Tutup"
                ) {
                    onCancel()
                }
            }
        }
    }
}

@Composable
fun ShowFormDialog(
    modifier: Modifier = Modifier,
    textButton: String,
    color: Color = MaterialTheme.colorScheme.primary,
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Dialog(
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        ),
        onDismissRequest = { onCancel() },
    ) {
        Column(
            modifier = modifier
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            FormPasswordComponent(
                title = "Pin",
                hintText = "Masukan Pin",
                value = text,
                isShowTitle = true,
                onValueChange = {
                    text = it
                    isError = false
                },
                isError = isError,
                isPasswordVisible = isPasswordVisible,
                onIconClick = { isPasswordVisible = !isPasswordVisible }
            ) {
                onConfirm(text)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                ButtonComponent(
                    color = color,
                    text = textButton
                ) {
                    if (text.isNotBlank()) onConfirm(text) else isError = true
                }
                Spacer(modifier = Modifier.height(16.dp))
                ButtonOutlineComponent(
                    color = color,
                    text = "Tutup"
                ) {
                    onCancel()
                }
            }
        }
    }
}

const val DIALOG_DISSMISS = 0
const val DIALOG_ERROR = 1
const val DIALOG_CHOICE = 2
const val DIALOG_PERMISSION = 3
const val DIALOG_MOCK = 4
const val DIALOG_DOWNLOAD = 5
const val DIALOG_PASSWORD = 6
const val DIALOG_FORM = 7
const val DIALOG_QUERY = 8
const val DIALOG_CHECKIN = 9

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    AppTheme {
        ShowDialog(message = "error message", textButton = "Tutup") {}
    }
}

@Preview(showBackground = true)
@Composable
fun DialogChoicePreview() {
    AppTheme {
        ShowChoiceDialog(message = "error message", textButton = "Keluar", onConfirm = {}, onCancel = {})
    }
}

@Preview(showBackground = true)
@Composable
fun DialogFormPreview() {
    AppTheme {
        ShowFormDialog(textButton = "Keluar", onConfirm = {}, onCancel = {})
    }
}