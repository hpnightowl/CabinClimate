# Cabin Controls

A small Automotive Design for Compose sample that is **drawn from scratch** in Figma and **fully
annotated** in Kotlin. A climate panel: temperature with minus and plus buttons, an A/C switch, a fan
control and a vent. Every common customization type is used exactly once.

- Figma file: "Cabin Controls - DesignCompose sample" (file ID `W3CVPB19m75SbvfQ6cZBei`).
- The design is built by `demo/figma/cabin-controls-build.js`, a Figma Plugin API script.
- The full reference, with every annotation and parameter explained, is `demo/docs/designcompose-reference.md`.

## Read it in this order

1. `app/src/main/java/com/example/cabincontrols/design/CabinDesign.kt`: the contract between Figma and
   Kotlin. Every annotation is used once and explained in comments.
2. `ui/CabinState.kt`: the state, as an immutable value with testable actions.
3. `ui/CabinScreen.kt`: calling the generated `CabinDoc.Panel(...)` and wiring state into each parameter.
   The right-hand inspector lists every parameter with its annotation and current value.
4. `ui/VentFlow.kt`: a component replacement that follows the Figma layer's size and position.

## Run it

The app draws its design from a bundled snapshot, `app/src/main/assets/figma/CabinDoc_<id>.dcf`. Create
it once from the Figma file (this uses your Figma token, which you save in `%APPDATA%\figma_access_token`):

```powershell
cd demo
powershell -ExecutionPolicy Bypass -File .\export-cabin.ps1    # fetch, build, install, launch
powershell -ExecutionPolicy Bypass -File .\run-demo.ps1 -App cabin   # later runs
```

Without the snapshot the app starts but shows nothing (logcat: `No doc! CabinDoc / <id>`).

If Figma's REST API answers `429`, export from inside Figma instead: run the plugin "Cabin JSON export"
(`demo/figma/cabin-export-json.txt`), press Copy JSON, then run `demo\export-cabin-offline.ps1`.

## Two designs

The app starts with **one** design: whichever `assets/figma/CabinDoc_W3CVPB19m75SbvfQ6cZBei.dcf` it has. Both
designs are pages of the same Figma file with the same layer names, and both are kept under that same file name in
`demo/dcf/design-a` and `demo/dcf/design-b`, so swapping one for the other needs no code change:

```powershell
cd demo
.\switch-design.ps1 B                  # push Design B to a debug build and restart (about 3 s)
.\switch-design.ps1 A -How Rebuild     # or replace the asset and rebuild (about 6 s)
```

Launch with `adb shell am start -n com.example.cabincontrols/.MainActivity --ez compare true` to get a switcher:
**Design A**, **Design B** and **Both, same state** (`CabinDoc_CabinDesignB.dcf` is Design B for that mode;
`ui/Look.kt` lists them). `demo\export-cabin-offline.ps1` exports everything.

## Test it

```bash
./gradlew.bat testDebugUnitTest        # 8 tests: state, ranges, event log, enum names, fit-to-screen
./gradlew.bat assembleRelease          # minified release; the one R8 rule is in app/proguard-rules.pro
```

## Live Update (development only)

`./gradlew.bat assembleDebug -PliveUpdate=true` turns on Figma Live Update, which re-fetches the Figma
file while it is edited. It needs a token and the network, and is never enabled in release builds.
