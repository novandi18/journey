package com.novandi.journey.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.novandi.journey.R

object PoppinsFont {
    val Poppins = FontFamily(
        Font(R.font.poppins_regular),
        Font(R.font.poppins_medium, weight = FontWeight.Medium),
        Font(R.font.poppins_semibold, weight = FontWeight.SemiBold),
        Font(R.font.poppins_bold, weight = FontWeight.Bold),
        Font(R.font.poppins_italic, style = FontStyle.Italic)
    )
}

private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = PoppinsFont.Poppins),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = PoppinsFont.Poppins),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = PoppinsFont.Poppins),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = PoppinsFont.Poppins),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = PoppinsFont.Poppins),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = PoppinsFont.Poppins),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = PoppinsFont.Poppins),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = PoppinsFont.Poppins),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = PoppinsFont.Poppins),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = PoppinsFont.Poppins),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = PoppinsFont.Poppins),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = PoppinsFont.Poppins),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = PoppinsFont.Poppins),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = PoppinsFont.Poppins),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = PoppinsFont.Poppins)
)