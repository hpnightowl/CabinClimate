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

package com.example.cabincontrols.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.android.designcompose.ComponentReplacementContext
import com.android.designcompose.TapCallback
import com.android.designcompose.annotation.Design
import com.android.designcompose.annotation.DesignComponent
import com.android.designcompose.annotation.DesignDoc
import com.android.designcompose.annotation.DesignModule
import com.android.designcompose.annotation.DesignModuleClass
import com.android.designcompose.annotation.DesignProperty
import com.android.designcompose.annotation.DesignVariant

// =================================================================================================
// THE CONTRACT BETWEEN FIGMA AND KOTLIN
//
//   #CabinPanel                      root frame, auto layout, vertical
//     #header
//       #title                       text    "Cabin Climate"
//       #ac-badge                    frame   (shown only while A/C is on)
//     #temperature
//       #temp-down                   frame   (a button)
//       #temp-block
//         #temp                      text    "21°"
//         #zone                      text    "DRIVER ZONE"
//       #temp-up                     frame   (a button)
//     #controls
//       #AcSwitch                    instance of the component set #AcSwitch (variants Off, On)
//       #FanSpeed                    instance of the component set #FanSpeed (Low, Medium, High)
//       #vent-slot                   empty frame the app fills with its own Compose UI
// =================================================================================================

enum class AcState {
    Off,
    On,
}

enum class FanSpeed {
    Low, Medium, High,
}

@DesignModuleClass
class FanControls(
    @DesignProperty(node = "#FanSpeed") val onTap: TapCallback,
    @DesignProperty(node = "#fan-label") val label: String,
)

@DesignDoc(id = "W3CVPB19m75SbvfQ6cZBei")
interface Cabin {
    @DesignComponent(node = "#CabinPanel", isRoot = true)
    fun Panel(
        @Design(node = "#title") title: String,
        @Design(node = "#zone") zone: String,
        @Design(node = "#temp") temp: State<String>,
        @Design(node = "#ac-badge") showAcBadge: Boolean,
        @Design(node = "#temp-up") onTempUp: TapCallback,
        @Design(node = "#temp-down") onTempDown: TapCallback,
        @DesignVariant(property = "#AcSwitch") ac: AcState,
        @Design(node = "#AcSwitch") onAcTap: TapCallback,
        @Design(node = "#ac-label") acLabel: String,
        @DesignVariant(property = "#FanSpeed") fanSpeed: FanSpeed,
        @DesignModule fan: FanControls,
        @Design(node = "#vent-slot") vent: @Composable (ComponentReplacementContext) -> Unit,
    )
}
