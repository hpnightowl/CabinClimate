/*
 * Copyright 2026 hpnightowl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cabincontrols.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import kotlin.math.roundToInt


fun fitScale(naturalWidth: Int, naturalHeight: Int, maxWidth: Int, maxHeight: Int): Float {
    if (naturalWidth <= 0 || naturalHeight <= 0 || maxWidth <= 0 || maxHeight <= 0) return 0f
    return minOf(maxWidth / naturalWidth.toFloat(), maxHeight / naturalHeight.toFloat())
}

@Composable
fun ScaleToFit(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val natural = measurables.first().measure(Constraints())
        val scale =
            fitScale(natural.width, natural.height, constraints.maxWidth, constraints.maxHeight)
        layout(
            (natural.width * scale).roundToInt().coerceAtLeast(constraints.minWidth),
            (natural.height * scale).roundToInt().coerceAtLeast(constraints.minHeight),
        ) {
            natural.placeWithLayer(0, 0) {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0f, 0f)
            }
        }
    }
}
