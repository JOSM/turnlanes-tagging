# TurnLanes-tagging plugin for JOSM

Easy editor to add turn lanes tags in highways.

*Add turn lanes in unidirectional road*
![turnlanes91-21](https://cloud.githubusercontent.com/assets/1152236/15969096/08082a88-2ef4-11e6-91f0-5895294bfefd.gif)

<!-- 
![turnlanes910](https://cloud.githubusercontent.com/assets/1152236/15968525/cc6d5676-2ef1-11e6-9c4d-6ff779949fcb.gif)
 -->

*Add turn lanes in bidirectional road*
![turnlanes91-21](https://cloud.githubusercontent.com/assets/1152236/15969751/fa089a46-2ef6-11e6-9dd1-683fa25e527d.gif)

Default shortcut `Alt+Shift+2`

# Functionalities :

- The plugin is enabled  after highway selection.
- The plugin contain  most common preset turn lanes.
- The plugin add `turn:lanes` with its respective number of `lanes`.
- the plugin add `turn:lanes:backward`,`turn:lanes:both_ways` and `turn:lanes:forward`  with its respective number of `lanes:backward`, `lanes:both_ways` and `lanes:forward` and the sum of all lanes `lanes`.
- The plugin save the recent edition.
- If the highway contain the number of lanes, the plugin automatically will set up a configuration with the number of lanes for unidirectional.
- If the highway contain `turn:lanes:backward`or `turn:lanes:both_ways` or `turn:lanes:forward` the plugin automatically will set up the configuration. 
- If the number of lanes does not match with the `turn:lanes` the plugin will fix the `lanes` according the `turn:lanes`
Example: 
`lanes=3` and `turn:lanes=left|through|through|right`
The plugin will set up the next configuration and will fix the number of lanes to 4. the same thing apply for bidirectional road.
![image](https://cloud.githubusercontent.com/assets/1152236/15967223/662e446a-2eec-11e6-97b0-cd12c947e016.png)


**Any feedback just ping me or open a [ticket](https://github.com/mapbox/turnlanes-tagging/issues/new) **