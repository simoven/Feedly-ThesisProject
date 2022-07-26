package com.simoneventrici.feedly.presentation.authentication.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.simoneventrici.feedly.ui.theme.LighterGray
import com.simoneventrici.feedly.ui.theme.MainGreen
import com.simoneventrici.feedly.ui.theme.WhiteColor

@Composable
fun AuthTextField(
    textState: State<String>,
    onValueChange: (String) -> Unit,
    isPasswordField: Boolean = false
) {
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val passwordImage = if (passwordVisible.value)
            Icons.Outlined.Visibility
        else
            Icons.Outlined.VisibilityOff

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        TextField(
            value = textState.value,
            onValueChange = onValueChange,
            placeholder = { },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                textColor = WhiteColor,
                backgroundColor = LighterGray,
                cursorColor = MainGreen,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                if(isPasswordField) {
                    IconButton(onClick = { passwordVisible.value = !passwordVisible.value}){
                        Icon(imageVector = passwordImage, contentDescription = if(passwordVisible.value) "Hide password" else "Show password")
                    }
                }
            },
            visualTransformation = if(isPasswordField && !passwordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = if(isPasswordField) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions(keyboardType = KeyboardType.Text)
        )

    }
}