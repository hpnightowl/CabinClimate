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

import android.content.Context
import com.example.cabincontrols.BuildConfig
import java.io.File

class DesignSource(val label: String, val short: String, val bytes: ByteArray?) {
    companion object {
        const val OVERRIDE_FILE = "CabinDoc_override.dcf"

        fun load(context: Context): DesignSource {
            if (BuildConfig.DEBUG) {
                val file = File(context.filesDir, OVERRIDE_FILE)
                if (file.isFile && file.length() > 0) {
                    return DesignSource(
                        "pushed file ${file.name}, ${file.length()} bytes",
                        "pushed file, ${file.length()} bytes",
                        file.readBytes(),
                    )
                }
            }
            return DesignSource(
                "packed in the app: assets/figma/${Look.A.snapshotFile}",
                "packed in the app",
                null,
            )
        }
    }
}
