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

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.android.designcompose.ComponentReplacementContext
import com.example.cabincontrols.design.AcState
import com.example.cabincontrols.design.FanSpeed

private val CoolAir = Color(0xFF3FA9F5)

@Composable
fun VentFlow(
    context: ComponentReplacementContext,
    ac: AcState,
    fan: FanSpeed,
    accent: Color = CoolAir,
) {
    val flow = rememberInfiniteTransition(label = "vent flow")
    val phase =
        flow.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 2400 / (fan.ordinal + 1), easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "phase",
        )
    val color = if (ac == AcState.On) accent else Color(0xFF8A99A8)
    val lines = fan.ordinal + 2

    Canvas(Modifier.then(context.layoutModifier).fillMaxSize()) {
        val gap = size.height / (lines + 1)
        val stroke = size.height * 0.07f
        for (i in 1..lines) {
            val y = gap * i
            val dash = size.width * 0.22f
            val period = dash * 2f
            val shift = ((phase.value + i * 0.17f) % 1f) * period
            var x = -period + shift
            while (x < size.width) {
                val from = x.coerceIn(0f, size.width)
                val to = (x + dash).coerceIn(0f, size.width)
                if (to > from) {
                    drawLine(
                        color = color,
                        start = Offset(from, y),
                        end = Offset(to, y),
                        strokeWidth = stroke,
                        cap = StrokeCap.Round,
                    )
                }
                x += period
            }
        }
        drawRect(color = color.copy(alpha = 0.18f), style = Stroke(width = stroke / 2f))
    }
}
