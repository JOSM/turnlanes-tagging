# TurnLanes-tagging plugin for JOSM

TurnLanes-tagging Editor plugin provides an alternative way to add [turn:lanes tags](https://wiki.openstreetmap.org/wiki/Key:turn#Turning_indications_per_lane) on highways.

## Installing the plugin

Install it from the JOSM plugin list: **Preferences → Plugins → search "turnlanes-tagging" → install → restart JOSM**.

If you wish to see the (red) lane outlines and arrow icons on the map, turn on the appropriate map style:

* In JOSM preferences look in the **Map Paint Styles** tab. This can also be reached from the Map Paint Styles window and clicking on the settings (two vertical sliders) icon.
* Select the **Lane and road attributes** style from the list of *Available styles* and add it to the list of *Active styles*.
* Activate the style by selecting its checkbox in the list of *Active styles*.

This should be selected in addition to your usual map style.

## Installing a custom build (jar file)

A locally built jar (e.g. with `ant dist`, or a jar downloaded from a GitHub release) can be installed manually without the plugin manager:

1. Close JOSM.
2. Copy the jar to the JOSM plugins directory, e.g.:
   * Linux: `~/.local/share/JOSM/plugins/`
   * macOS: `~/Library/JOSM/plugins/`
   * Windows: `%APPDATA%\JOSM\plugins\`
   * Flatpak: `~/.var/app/org.openstreetmap.josm/data/JOSM/plugins/`
3. Name it `turnlanes-tagging.jar` (this replaces the official version, if present).
4. Start JOSM; if necessary, enable the plugin in **Preferences → Plugins**.

The jar is self-contained (classes, icons and translations), so no other files are needed. Note that the JOSM plugin updater may reinstall the official version when plugins are updated; repeat the steps above to go back to the custom build.

## To activate the plugin

Select a way, then the default shortcut <kbd>Alt</kbd>+<kbd>Shift</kbd>+<kbd>2</kbd> (MacOS: <kbd>Option</kbd>-<kbd>Shift</kbd>-<kbd>2</kbd> or <kbd>⌥</kbd><kbd>⇧</kbd><kbd>2</kbd>) or the menu item **Data → Turn lanes tagging editor**.

## The main features are

* The plugin is enabled after a highway is selected.
* Presets with the most common turn-lane combinations.
* The `recent turn lanes edit` tab contains recent turn lanes added by the user.
* If the highway contains `turn:lanes:backward`, `turn:lanes:both_ways` or `turn:lanes:forward`, the plugin automatically sets up the configuration.
* If the number of `lanes` does not match the `turn:lanes`, the plugin automatically fixes the number of `lanes` according to the `turn:lanes`.

*Example*

```
lanes=5
turn:lanes=left|through|slight_left;through|right|through;right
```

```
lanes:backward=2
lanes:both_ways=1
lanes:forward=3
lanes=6
turn:lanes:backward=through|through
turn:lanes:both_ways=left;reversible;right
turn:lanes:forward=left|slight_left|through
```

### Live lane view in the editor

The lane editor draws the road while you edit it:

* Each lane panel is **tinted by travel direction**: blue = forward, red = backward, yellow = both ways.
* The **boundary lines are drawn between the lane panels**, following the settings:
  * dashed where lane changes are allowed, **solid beside a lane that may not cross** (from `change:lanes`),
  * asymmetric boundaries are drawn as two half-lines (solid + dashed) when only one side forbids crossing,
  * the **yellow centre divider** follows the `divider` setting (solid / double solid / dashed) and upgrades to solid on the restricted side,
  * `lane_markings=no` hides the white separators.
* A coloured label on each lane shows its `change:lanes` value.

### Lane-change restrictions (`change:lanes`)

Each lane can be given a lane-change restriction (`yes`, `no`, `not_left`, `not_right`), written as `change:lanes` (or `change:lanes:forward` / `change:lanes:backward` / `change:lanes:both_ways` for bidirectional highways). A warning is shown when some lanes have a restriction and others have an empty value, since JOSM does not handle empty pipe-separated values well.

### Road divider and lane markings

Use the **Divider** option to set `divider=solid_line|double_solid_line|dashed_line` and the **No lane markings** option to set `lane_markings=no`.

### Bicycle lanes

* **Bicycle lanes left / Bicycle lanes right** options set `cycleway:left=*` / `cycleway:right=*` (`lane`, `track`, `shared_lane` or `no`). The cycleway is shown as a green panel with a bicycle icon at the chosen side of the road; `track` additionally shows a grey curb gap. The cycleway lane is always separated from the general traffic lanes by a **solid line**.
* Each lane has a **Bike** checkbox: a checked lane becomes a designated bicycle lane (`bicycle:lanes=designated`) and is tinted green, so a bike lane can be placed **between general traffic lanes** (e.g. `bicycle:lanes=yes|designated|yes`). Existing `bicycle:lanes` tags are read back into the editor.

## Screenshots and videos

*Current media of the editor (lane preview, change:lanes lines, divider, bicycle lanes) are collected here.*

<!-- Planned captures:
     ![](screenshots/editor-unidirectional.png)
     ![](screenshots/editor-bidirectional.png)
     ![](screenshots/editor-change-lanes.png)
     ![](screenshots/editor-cycleway.png)
     ![](screenshots/editor-bike-lane-between.png)
     ![](screenshots/add-turn-lanes.gif)
-->

## Feedback

If you have any feedback on the plugin, you can find the author on [twitter](https://twitter.com/Rub21tk) or open a [ticket](https://github.com/JOSM/turnlanes-tagging/issues/new)
