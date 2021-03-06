package com.mishaismenska.hackatonrsschoolapp.presentation.models

import android.icu.util.Measure
import com.mishaismenska.hackatonrsschoolapp.staticPresets.DrinkPreset
import java.time.LocalDateTime

data class DrinkUIModel(
    val type: DrinkPreset,
    val dateTaken: LocalDateTime,
    val volume: Measure
)
