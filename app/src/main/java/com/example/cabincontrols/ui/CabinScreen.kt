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

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.designcompose.DesignDocSettings
import com.android.designcompose.LocalDesignDocSettings
import com.android.designcompose.squoosh.SmartAnimateTransition
import com.example.cabincontrols.design.CabinDoc
import com.example.cabincontrols.design.FanControls
import java.io.ByteArrayInputStream
import java.io.InputStream

private val AppBackground = Color(0xFF0A0F16)
private val InspectorSurface = Color(0xFF111A24)
private val TextPrimary = Color(0xFFE8EEF4)
private val TextMuted = Color(0xFF8FA1B3)
private val Accent = Color(0xFFFFB703)

@Composable
fun CabinScreen() {
    var state by remember { mutableStateOf(CabinState()) }

    val tempText = remember { derivedStateOf { state.tempLabel } }
    val update: ((CabinState) -> CabinState) -> Unit = { change -> state = change(state) }
    val settings = remember {
        DesignDocSettings(
            customVariantTransition = {
                SmartAnimateTransition(tween(280, easing = CubicBezierEasing(0.37f, 0f, 0.63f, 1f)))
            },
        )
    }
    SingleScreen(state, tempText, update, settings)
}

@Composable
private fun SingleScreen(
    state: CabinState,
    tempText: State<String>,
    update: ((CabinState) -> CabinState) -> Unit,
    settings: DesignDocSettings,
) {
    val context = LocalContext.current
    val source = remember { DesignSource.load(context) }
    val stream = remember(source) { source.bytes?.let { ByteArrayInputStream(it) } }

    Column(Modifier.fillMaxSize().background(AppBackground).systemBarsPadding().padding(20.dp)) {
        Text(
            "Same Kotlin. The design is the file: ${source.label}",
            color = TextMuted,
            fontSize = 15.sp,
            maxLines = 1,
        )
        Spacer(Modifier.height(14.dp))
        Row(Modifier.weight(1f).fillMaxWidth()) {
            Box(Modifier.weight(1.15f).fillMaxSize(), contentAlignment = Alignment.Center) {
                ScaleToFit { DesignPanel("single", state, tempText, update, settings, stream) }
            }
            Spacer(Modifier.width(20.dp))
            Inspector(state, source.short, Modifier.weight(1f).fillMaxSize())
        }
    }
}

@Composable
private fun DesignPanel(
    key: String,
    state: CabinState,
    tempText: State<String>,
    update: ((CabinState) -> CabinState) -> Unit,
    settings: DesignDocSettings,
    dcfInputStream: InputStream?,
) {
    CompositionLocalProvider(LocalDesignDocSettings provides settings) {
        CabinDoc.Panel(
            key = key,
            dcfInputStream = dcfInputStream,
            title = "Cabin Climate",
            zone = "DRIVER ZONE",
            temp = tempText,
            showAcBadge = state.showAcBadge,
            onTempUp = { update { it.tempUp() } },
            onTempDown = { update { it.tempDown() } },
            ac = state.ac,
            onAcTap = { update { it.toggleAc() } },
            acLabel = "A/C",
            fanSpeed = state.fan,
            fan = FanControls(onTap = { update { it.cycleFan() } }, label = "Fan"),
            vent = { context -> VentFlow(context, state.ac, state.fan) },
        )
    }
}

private data class ParamRow(val annotation: String, val signature: String, val value: String)

@Composable
private fun Inspector(state: CabinState, designFile: String, modifier: Modifier = Modifier) {
    val rows = listOf(
        ParamRow("design file (.dcf)", "source", designFile),
        ParamRow("@Design(\"#title\")", "title: String", "\"Cabin Climate\""),
        ParamRow("@Design(\"#zone\")", "zone: String", "\"DRIVER ZONE\""),
        ParamRow("@Design(\"#temp\")", "temp: State<String>", "\"${state.tempLabel}\""),
        ParamRow(
            "@Design(\"#ac-badge\")",
            "showAcBadge: Boolean",
            state.showAcBadge.toString(),
        ),
        ParamRow("@Design(\"#temp-up\")", "onTempUp: TapCallback", "tap + to call"),
        ParamRow("@Design(\"#temp-down\")", "onTempDown: TapCallback", "tap - to call"),
        ParamRow("@DesignVariant(\"#AcSwitch\")", "ac: AcState", "AcState.${state.ac.name}"),
        ParamRow("@Design(\"#AcSwitch\")", "onAcTap: TapCallback", "tap the switch"),
        ParamRow("@Design(\"#ac-label\")", "acLabel: String", "\"A/C\""),
        ParamRow(
            "@DesignVariant(\"#FanSpeed\")",
            "fanSpeed: FanSpeed",
            "FanSpeed.${state.fan.name}",
        ),
        ParamRow("@DesignModule", "fan: FanControls", "FanControls(onTap, label = \"Fan\")"),
        ParamRow(
            "@Design(\"#vent-slot\")",
            "vent: @Composable (ComponentReplacementContext) -> Unit",
            "VentFlow(...)",
        ),
    )
    Column(
        modifier.clip(RoundedCornerShape(24.dp)).background(InspectorSurface).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            "CabinDoc.Panel(...)",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            "Parameter, Figma layer and value now. Scroll for more.",
            color = TextMuted,
            fontSize = 13.sp,
            maxLines = 1,
        )
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            rows.forEach { InspectorRow(it) }
        }
        Text(
            "EVENTS  (newest first)",
            color = TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
        Column(
            Modifier.fillMaxWidth().height(60.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            if (state.events.isEmpty()) {
                Text(
                    "No taps yet",
                    color = TextMuted,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                )
            }
            state.events.take(3).forEach {
                Text(
                    it,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun InspectorRow(row: ParamRow) {
    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        Text(row.annotation, color = Accent, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        Text(
            "${row.signature}  =  ${row.value}",
            color = TextPrimary,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            maxLines = 1,
        )
    }
}
