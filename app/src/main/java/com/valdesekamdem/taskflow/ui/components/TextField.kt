package com.valdesekamdem.taskflow.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    singleLine: Boolean = false,
    fontWeight: FontWeight? = LocalTextStyle.current.fontWeight,
    minLines: Int = 1,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
) {
    val placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = keyboardOptions,
        textStyle = textStyle.copy(
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = fontWeight
        ),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Box(modifier = modifier) {
                if (value.isEmpty()) {
                    Text(
                        placeholder,
                        style = textStyle,
                        color = placeholderColor,
                        minLines = minLines,
                    )
                }
                innerTextField()
            }
        },
    )
}
