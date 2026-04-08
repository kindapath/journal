package com.kindaboii.journal.common.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import journal.common.ui.generated.resources.Res
import journal.common.ui.generated.resources.icon_visibility_24
import journal.common.ui.generated.resources.icon_visibility_off_24
import org.jetbrains.compose.resources.painterResource

@Composable
fun PasswordOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(
                onClick = { isPasswordVisible = !isPasswordVisible },
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
            ) {
                Icon(
                    painter = painterResource(
                        if (isPasswordVisible) {
                            Res.drawable.icon_visibility_off_24
                        } else {
                            Res.drawable.icon_visibility_24
                        }
                    ),
                    contentDescription = if (isPasswordVisible) {
                        "Скрыть пароль"
                    } else {
                        "Показать пароль"
                    },
                )
            }
        },
        enabled = enabled,
        singleLine = true,
    )
}
