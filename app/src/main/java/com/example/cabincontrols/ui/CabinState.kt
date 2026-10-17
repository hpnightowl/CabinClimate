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

import com.example.cabincontrols.design.AcState
import com.example.cabincontrols.design.FanSpeed

data class CabinState(
    val tempC: Int = 21,
    val ac: AcState = AcState.Off,
    val fan: FanSpeed = FanSpeed.Low,
    val events: List<String> = emptyList(),
) {
    val tempLabel: String
        get() = "$tempC°"

    val showAcBadge: Boolean
        get() = ac == AcState.On

    fun tempUp(): CabinState {
        val next = copy(tempC = (tempC + 1).coerceAtMost(MAX_TEMP_C))
        return next.withEvent("onTempUp -> ${next.tempLabel}")
    }

    fun tempDown(): CabinState {
        val next = copy(tempC = (tempC - 1).coerceAtLeast(MIN_TEMP_C))
        return next.withEvent("onTempDown -> ${next.tempLabel}")
    }

    fun toggleAc(): CabinState {
        val next = copy(ac = if (ac == AcState.Off) AcState.On else AcState.Off)
        return next.withEvent("onAcTap -> ac = ${next.ac.name}")
    }

    fun cycleFan(): CabinState {
        val next = copy(fan = FanSpeed.entries[(fan.ordinal + 1) % FanSpeed.entries.size])
        return next.withEvent("fan.onTap -> speed = ${next.fan.name}")
    }

    private fun withEvent(message: String): CabinState =
        copy(events = (listOf(message) + events).take(MAX_EVENTS))

    companion object {
        const val MIN_TEMP_C = 16
        const val MAX_TEMP_C = 30
        const val MAX_EVENTS = 6
    }
}
