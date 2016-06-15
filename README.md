# TurnLanes-tagging plugin for JOSM

TurnLanes-tagging Editor provides an alternative way to add [turn:lanes tags](http://wiki.openstreetmap.org/wiki/Key:turn#Turning_indications_per_lane) in highways

## Installation

![turnlanes91-21](https://cloud.githubusercontent.com/assets/1152236/16094397/b7184c3c-3305-11e6-8294-1a1af89a3e60.gif)

## How it works?

**Add turn lanes in unidirectional highways**
![turnlanes91-21](https://cloud.githubusercontent.com/assets/1152236/15969096/08082a88-2ef4-11e6-91f0-5895294bfefd.gif)

**Add turn lanes in bidirectional highways**
![turnlanes91-21](https://cloud.githubusercontent.com/assets/1152236/15969751/fa089a46-2ef6-11e6-9dd1-683fa25e527d.gif)

## Shortcut

Default shortcut `Alt+Shift+2`

## Its main features are:

- The plugin is enabled after highway selection.
- The plugin contain  most common preset turn lanes.
- The plugin add `turn:lanes` with its respective number of `lanes`.
- The plugin add `turn:lanes:backward`,`turn:lanes:both_ways` and `turn:lanes:forward`  with its respective number of `lanes:backward`, `lanes:both_ways` and `lanes:forward` and the sum of all lanes `lanes`.
- The plugin save the recent edition.
- If the highway contain the number of lanes, the plugin automatically will set up a configuration with the number of lanes for unidirectional.
- If the highway contain `turn:lanes:backward`or `turn:lanes:both_ways` or `turn:lanes:forward` the plugin automatically will set up the configuration. 
- If the number of lanes does not match with the `turn:lanes` the plugin will fix the `lanes` according the `turn:lanes`
Example: 
`lanes=3` and `turn:lanes=left|through|through|right`

**Any feedback just ping me on [twitter](https://twitter.com/Rub21tk) or open a [ticket](https://github.com/mapbox/turnlanes-tagging/issues/new) **






