# TurnLanes-tagging plugin for JOSM

TurnLanes-tagging Editor plugin provides an alternative way to add [turn:lanes tags](https://wiki.openstreetmap.org/wiki/Key:turn#Turning_indications_per_lane) on highways

## Installing the plugin

![turnlanes91-21](https://cloud.githubusercontent.com/assets/1152236/16094397/b7184c3c-3305-11e6-8294-1a1af89a3e60.gif)

## How does it work?

**Adding turn lanes in unidirectional highways**

![4](https://cloud.githubusercontent.com/assets/1152236/16133679/c56fe05e-33de-11e6-8f18-cb5efa721cde.gif)

**Adding turn lanes in bidirectional highways**

![5](https://cloud.githubusercontent.com/assets/1152236/16133863/aa636bfe-33df-11e6-9161-6ccd6b10559c.gif)

## Shortcut to activate the plugin

Default shortcut `Alt+Shift+2`

## The main features are:

- The plugin is enabled after the highway is selected.
- The plugin contains the combinations of most common preset turn lanes according mapbox-data-team
- The plugin adds `turn:lanes` with its respective number of `lanes`.

*Example*

```
lanes=5
turn:lanes=left|through|slight_left;through|right|through;right
```

- The plugin adds `turn:lanes:backward`, `turn:lanes:both_ways` or  `turn:lanes:forward`  with theirs respective number of `lanes:backward`, `lanes:both_ways` or `lanes:forward` and the sum of all `lanes`.

*Example*

```
lanes:backward=2
lanes:both_ways=1
lanes:forward=3
lanes=6
turn:lanes:backward=through|through
turn:lanes:both_ways=left;reversible;right
turn:lanes:forward=left|slight_left|through
```

- The `recent turn lanes edit` tab contains recent turn lanes added by the user.

![2](https://cloud.githubusercontent.com/assets/1152236/16133282/8b005b76-33dc-11e6-98ed-db53be95473d.gif)


- If a unidirectional highway contains a certain number of lanes, the plugin automatically sets up the configuration for the number of lanes.


- If the highway contains `turn:lanes:backward`, `turn:lanes:both_ways` or `turn:lanes:forward` the plugin automatically sets up the configuration. 


![3](https://cloud.githubusercontent.com/assets/1152236/16133401/44d59642-33dd-11e6-9d90-1f9621cec6cf.gif)


- If the number of `lanes` does not match with the `turn:lanes`, the plugin will automatically fix the  number of `lanes` according to the `turn:lanes` added.



![1](https://cloud.githubusercontent.com/assets/1152236/16132547/32be798c-33d9-11e6-9208-77258d5fbb77.gif)

*Before*
```
lanes=2
turn:lanes=left|left|
```
*After*
```
lanes=3
turn:lanes=left|left|
```


**If you have any feedback on the plugin, you can find me on [twitter](https://twitter.com/Rub21tk) or open a [ticket](https://github.com/mapbox/turnlanes-tagging/issues/new)**






