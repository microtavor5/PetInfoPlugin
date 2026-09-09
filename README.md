# Pet Info
Saw a cool pet? Have no idea what it was or how to get it?
Now you can find out without having to even ask other players!

The Pet Info plugin for RuneLite allows you to right-click pets for info about them,
like how they are obtained, or who owns it.
It also allows you to highlight pets, and have their names displayed above their heads.

The plugin will attempt to fetch the most up-to-date pets from the GitHub's
main branch, and save a local fallback copy.

## What the plugin adds:
### Right Click "Info" Option
When the player's mouse is over a pet that is not their own in the game, the plugin adds a menu option of the form 
`Info <Pet Owner's Name>'s <Pet Name>`. When selected it prints info on what the pet is and how it is obtained 
in game.

### Right Click "Examine" Option
When the player's mouse is over a pet that is not their own, the plugin can also add an "Examine" option of the form
`Examine <Pet Owner's Name>'s <Pet Name>` to the menu. When selected it prints the pet's examine text.

### Pet Highlighting
The plugin can also make an overlay highlight on pets, with customization based on the type of pet.

### Customization
There are options in the menu to choose how the other player's name is colored and how the pet's name is colored.
#### Player Name Colors:
* White (Runescape's default for Player Character names)
* Yellow (Usually NPCs in Runescape, but makes for nice contrast)
* Level Differential (Color the player's name as their combat level number would be colored relative to the player's 
comabat level)
#### Pet Name Colors:
* Yellow (Runescape's default for NPCs)
* Pet type (see below)

The pet type color options are separate for each pet type (Bossing, Skilling, etc.). The colors can be chosen by the 
player and they affect both the pet highlighting and the pet name color.  

### FPS Improvement options
If your machine is older, or you spend a lot of time doing activities where there are many pets around the plugin might 
cause a dip in FPS when encountering a large amount of pets under the cursor. If this occurs, check the box for 
`Imprecise click boxes`.
#### How it helps:
_TL;DR: when selected, the plugin will add the menus if the cursor is near a pet, and not only if the cursor is 
directly on top of a pet._

Every tick the plugin needs to check what pets are under the player's cursor. To do this in the most precise way 
possible the pet's "Convex Hull" should be used, to see if the curser is actually on top of the pet's model.

Unfortunately, this is a costly procedure that can severely impact FPS if there are a lot of spawned pets in the scene.
To cut down on the number of times the plugin needs to call check against the convex hull, the plugin will first use 
"Axis Aligned Bounding Boxes" to see if the pet is anywhere near the cursor. AABB is a relatively fast operation, but 
is imprecise as far as the actual shape of the pet's model. So, only once we know what pets might be under the cursor 
using the AABB, does the plugin then check the convex hulls of only that smaller number of pets. 
This allows for a balance of higher FPS while still making the menus feel more like they are native game elements.   

But, since the convex hull check is expensive to do, the player can have the plugin skip the check entirely and just 
rely on the AABB check.

## Plans for the future
* A rewrite of the pet info descriptions for a more consistent experience.
* Maybe add a way to get the player's current KC or SL for relevant pets
  * This is a big maybe, it might be outside the scope of the plugin, since it is already covered by other plugins
  * There is no way to get the KC or SL the player was at when they _got_ the pet, so if added it would only be the 
current value.

###### Special thanks to the devs of the Implings and Menu Entry Swapper plugins
