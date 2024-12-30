# Changelog

## [5.8.0] - Unreleased

*"I'm free &ndash; I'm FREE, \
and freedom tastes of reality..."* \
&ndash; "I'm Free", The Who (1969)

**Create Big Cannons 5.8.0. Comes with a few overhauls to cannon mounts, improvised explosives, and fixes.**

### Added
- Added Cannon Mount Extension
  - Replaces Yaw Controller for cannon interface extension
  - Can be placed in six directions as opposed to the Yaw Controller
  - The direction affects which block it will interface with
  - Has a range of one block
- Added Fixed Cannon Mount, a manually-aimed cannon mount variant that cannot be turned with kinetics
  - Made with use in vehicle mods in mind
- Added more config to Proximity Fuze behavior
- Added Wired Fuze
  - Unlike other fuzes, it is only activated on shells placed as blocks, powered with redstone, rather than in flight
  - Only works with big cannon shells in base Create Big Cannons
  - On detonation, the detonation effect of the fuzed shell is spawned in world
  - Can be used for improvised explosives
- Added ability for big cannon shell blocks and propellant blocks to explode
  - Propellant can catch fire and explode, shells cannot
  - Explosion power can be configured per munition block
  - Behavior can be toggled in server config
- Munitions can be blocked from exploding and/or catching fire by waterlogging
- Waterlogging propellant will make it damp
  - Damp propellant that isn't waterlogged is vulnerable to ignition and explosion
  - Damp propellant can be dried either by waiting (random tick) or placing the projectile in the Nether, similar to sponges
  - Damp propellant cannot be dried in furnaces
  - Damp propellant is indicated by an item tooltip as well as blocks emitting water particles
  - Damp behavior can be configured per projectile
  - Damp behavior can be toggled in server config
### Changed
- Cannon Mount can now be turned around both axes without the Yaw Controller
- Cannon Mount now disassembles on rotation with wrench
- Updated Cannon Mount and Yaw Controller yaw shaft texture to no longer use the special black shaft texture
- The Yaw Controller is no longer obtainable in-game
  - Has not been removed from the mod to preserve already existing builds
- Updated movement checks for mounts and mount extensions
- Cannon Mounts can now be placed upside-down
  - Cannon Mounts can also be flipped by using the wrench
- Changed Cannon Mount item model to be more consistent with in-world appearance
- Reduced default Proximity Fuze arming delay to 5 gameticks
### Fixed
- Fixed rendering of pitch shaft for down-facing cannons
- Fixed display on some ponders
- Fixed autocannon tracer lighting and non-tracer rendering
- Fixed conductivity of certain cannon mount blocks
- Fixed pick-cloning Big Cartridge power value
- Fixed Quick-Firing Breech smoke on assembled cannons not persisting
- Fixed Quick-Firing Breech side lever not actually ejecting the contents
- Fixed Quick-Firing Breech accepting input from the sides
- Fixed some waterloggable blocks not becoming waterlogged when placed in water
- Fixed Rope Pulley integration making it unmovable in some cases (issue #719)
- Fixed cannon welds not breaking when block is broken
- Fixed cannon connected textures updating and appearance when welded

## [5.7.2] - 2024-12-18

**Create Big Cannons 5.7.2. Critical patch for Copycats+ compat.**

### Fixed
- Fixed Copycats+ compat

## [5.7.1] - 2024-12-15

**Create Big Cannons 5.7.1. Now supports Create 0.5.1 patch J on Forge/NeoForge and Fabric/Quilt.
Only 1.20.1 is supported, as Create 0.5.1.j only supports that version. Mod versions on Minecraft 1.18.2 and 1.19.2 will no longer receive support.
5.7.0 was initially released but has been bypassed due to critical fixes required.**

### Added
- Added highlighting and documentation for base fuzes
  - Can be toggled in client config
- Added fuzed models for big cannon shell items
### Changed
- Updated Polish translation courtesy of KarolOfGutovo
- Changed AP Shell texture to better show base fuze location
- Changed wording on some tooltips
- Made the Quick-Firing Breech openable using the side lever
- Copycats+ is now supported for 2.1.4
  - Added more support for Copycats+ block armor properties
  - Copycats+ 1.2.5 is the minimum supported version
### Fixed
- Fixed autocannon round duplication glitch
- Fixed projectile ground tracking
- Fixed strength attributes for incomplete and unbored sliding breeches to be consistent with complete sliding breeches
- Fixed elevator assembly issue
- Fixed sticky piston loading
- Fixed pulley sync

## [5.7.0] - 2024-12-14 [YANKED]

See 5.7.1 for details.

## [5.6.0] - 2024-10-15

**Create Big Cannons 5.6.0. Now supports Create 0.5.1 patch I.\
Comes with a few fixes and updates.**

### Added
- Added Create: Unify as a required dependency for obtaining bronze and steel
- Added Finnish translation courtesy of Apinoita
### Changed
- Updated Create Big Cannons logo to new style
- Updated Ukrainian translation courtesy of kyryloborsch
- Updated Chinese (Simplified) translation courtesy of Yizhouuu
### Fixed
- Fixed cannon carriages being broken by projectiles
- Fixed first-person handle autocannon carriage movement jitter
- Fixed incorrect big cannon failure ponder text
- Fixed impact fuzes not being activated by entity impact
  - This fix involves code changes that may break Create Big Cannon addons.
- [1.19.2, 1.20.1] Fixed blast wave effect relying on thread-unsafe level random

## [5.5.1]

**Create Big Cannons 5.5.1. A few critical fixes for 5.5.0 as well as some code changes.**

### Added
- Added version checks for compat mods on 1.18.2
- [Development] Added ability to get big cannon projectile from all big cannon projectile item stacks
### Changed
- Optimized entity bounds for big cannons and autocannons to optimize lighting
- Changed impact explosion queueing to better allow compatibility with Valkyrien Skies
- All Autocannon Recoil Springs on an autocannon now play the firing animation
  - Format for storing Autocannon Recoil Spring positions has changed, reassemble autocannons to fix any issues
- Registered custom shaders properly so that Create Big Cannons shaders don't cause logspam on crash
### Fixed
- Fixed Trinkets compat crashing
- Fixed gas mask overlay rendering as solid color when holding Just Enough Guns firearm
- Fixed entity culling for big cannons and autocannons
- Fixed entity lighting
- Fixed contraption vector transforms
- Fixed big cannon projectiles spawning too far from barrel
- Fixed autocannon assembly shooting projectiles the wrong way
  - Added assembly failure message
- Fixed Fluid Shell item filling
- Fixed tracer not rendering for Drop Mortar Shell
- Fixed initial projectile chunkloading breaking Valkyrien Skies Quick-Firing Breeches
- Fixed illegal autocannon connections on creative block placement
- Fixed cannon casting connecting blocks that shouldn't connect

## [5.5.0]

*"messi messi messi immens messi encara messi encara messi encara messi"*

**Create Big Cannons 5.5.0, with many bug fixes and QoL changes, as well as an aesthetic overhaul to many of the visual and sound effects.
Comes with version formatting change, but gameplay changes are otherwise not major.**

### Added
- Added some config options to use subheadings in Create config screen
- Added durability and fuze position tooltip to Impact Fuze and Delayed Impact Fuze
- Added config to switch between cannon trail styles
- Added cannon trail particle
  - By default appears the same as campfire smoke
  - Particle texture definition can be changed
- Added new style for autocannon plumes (`createbigcannons:autocannon_plume`) and drop mortar plumes (`createbigcannons:drop_mortar_plume`) separate from big cannon plume (`createbigcannons:big_cannon_plume`)
  - Added separate clientside visibility config options for these plumes
- Added Drop Mortar Shell and Smoke Shell to "Fuzing Munitions" ponder
- Added Drop Mortar Shell, Fluid Shell, and Smoke Shell to Munitions ponder tag
- Added Cannon Loads ponder to Big Cartridge
- Added item tooltips for Smoke Shell
- Added contained fluid tooltip for Fluid Shell item
- Added fuze tooltips for fuzed big cannon munitions
- Added filled Big Cartridge to creative search tab
- Added separate config for screen shake intensity
- Added ability to add Tracer Tips to big cannon projectiles
  - Added config to make all big cannon projectiles have tracers regardless if they have tracers, where applicable
  - Solid Shot and AP Shot now have block entities to allow tracer placement. Blocks missing them may need to be replaced.
- Added screen shake config options
  - Screen shake intensity (client)
  - Screen shake restitution (client)
  - Screen shake decay (client)
  - Screen shake range (server)
  - Screen shake power (server)
  - Screen shake propagation speed (server)
    - The faster it is, the less delay there is for players at a distance
- Added cannon mount angle goggle display config options
  - Added config for angle precision; ranges from 0 to 4 digits
  - Added config for yaw display range; can either be -180 to +180 degrees or 0 to +360 degrees, defaults to the latter
- Added pick block functionality to Cannon Carriage entity (yields its item)
- Added flak and shrapnel burst cloud effects
  - Has appearance and visibility client config options
  - Amount of particles can also be configured in the shrapnel
- Added new burst sounds for flak and shrapnel
- Added config for different types of big cannon projectile smoke trail: Long, Short, and No Trail
- Added smoke trails to autocannon projectiles
  - Has config for type of trail: Long, Short, and No Trail
- Added `ignores_invulnerability` field to projectiles, ignoring projectile invulnerability
  - The old `ignores_entity_armor` field now only affects if entity armor is bypassed, not invulnerability
  - By default, all CBC projectiles ignore invulnerability, but differ in if they bypass armor
- Added custom fluid-based explosion effects to Fluid Shell
  - Added registry for fluid shell explosion sound and visual effects based on fluid
  - If no effect is registered, defaults to regular explosion and sound effect
- Added Pinch of Gunpowder for cheaper machine gun rounds, made in the Crafting Table
  - By default, 1 Gunpowder makes 9 Pinches of Gunpowder
  - Added item tag `#createbigcannons:gunpowder_pinch`
- Added Guncotton
  - Recipe: Mix Paper + Gunpowder + Water + Redstone
  - Added pre-Nether recipe pathway for Congealed Nitro: Mix Guncotton + Slimeball + Water + Redstone
  - Added Packed Guncotton, can be crafted by compressing 3 pieces of Guncotton
  - HE and AP Shells now use Packed Guncotton instead of TNT for their explosive
 - Added item tags
   - `#createbigcannons:guncotton` - Guncotton
   - `#createbigcannons:can_be_nitrated` - Paper
   - `#createbigcannons:high_explosive_materials` - Packed Guncotton
   - `#createbigcannons:nitro_acidifiers` - Redstone
   - `#createbigcannons:gelatinizers` - Slimeball
- Added resource pack system for custom effects by block, supports blocks and tags (`assets/block_hit_effects`)
  - Default effect on contact with fluid blocks creates splashes
    - Supported fluids: water, lava, honey, chocolate, all Create Big Cannons molten metals
  - Default effect on contact with solid blocks creates particles
  - Effect visibility can be configured in the client config menu
- Added resource pack system for effect scale by entity type (`assets/projectile_hit_effects`)
- Impacts now transform their surroundings
  - Can be configured in data pack: `data/block_impact_transforms`
- Added special visual impact effects for various blocks
  - Blocks that produce sparks, mainly metals, are marked with `#createbigcannons:spark_effect_on_impact`
  - Blocks that produce splinters, mainly woods, are marked with `#createbigcannons:splinter_effect_on_impact`
  - Blocks that produce shards, mainly glass, are marked with `#createbigcannons:glass_effect_on_impact`
  - Blocks tagged `#minecraft:leaves` produce leaves
- Added fluid drag
  - Has data pack config
  - Currently supports water, lava, honey, and chocolate
- Added projectile skipping on fluids
  - Added separate fluid bounce chance config
- Added air absorption and delay to cannon sounds
  - Delay is configurable
- Added shell flight and falling sounds for big cannon projectiles
- Added flyby sounds for autocannon and machine gun projectiles
- Added new shell explosion visual and sound effects
- Added global wind for projectile explosion and Smoke Shell effects
- Added smoke cloud merging to reduce entity count
- Added chunkloading option for smoke clouds to reduce entity lingering
  - Can be configured
- Added gas cloud to optimize potion fluid shell effect instead of spawning multiple area effect clouds
- Added Gas Mask
  - Comes with Curios and Trinkets compat
- Added fallback graphics for certain objects affected by Iris/Oculus shaders
  - Cannon smoke falls back to default textures
  - Splinters, glass shards, and leaves use regular block particles
  - Emissive textures are not supported by the mod, please make your own resource pack if you want that.
- [Integration] Added integration with FramedBlocks content added in 1.19.2 and 1.20.1
### Removed
- Removed legacy cannon plume appearance client config, can now only turn plumes on or off in clientside config
- Removed single-projectile shrapnel, grapeshot, and fluid blob entities
### Changed
- Updated Ritchie's Projectile Library dependency to 2.0.0
  - Improved chunkloading performance
  - Replaced `PreciseProjectile` interface with tag `#ritchiesprojectilelib:precise_motion`
  - Changed screen shake to use Ritchies Projectile Library code rather than Create Big Cannons code
- Separated smoke shell smoke particle (`createbigcannons:smoke_shell_smoke`) from cannon smoke particle (`createbigcannons:cannon_smoke`)
  - Both still use the same textures and particle definition by default
- Changed how big cannon plumes look
- Changed description of some ponder categories
- Rework cannon plume to use better particles
- Changed default cannon mount angle goggle display precision from 1 to 2 digits
- Improved cannon heating ponder information to clarify layering
- Improved sound effects of cannon firing
- Tweaked speed and entity damage attributes of Drop Mortar Shell
- Tweaked shrapnel spread and shrapnel damage values of Shrapnel Shell
- Tweaked grapeshot spread and grapeshot damage values of Bag of Grapeshot
- Changed all shrapnel projectiles to a single projectile burst entity to reduce entity count
  - Includes: Shrapnel Shell, Bag of Grapeshot, Fluid Shell, and Flak Autocannon Round
  - Harder to create chunkbans with new system
  - [Addons, integration] Fluid Blob effects registry has been changed to work with Fluid Blob bursts rather than single Fluid Blobs. This will break already existing integration.
- Separated flak burst from shrapnel burst
- Changed screen shake delay to be on client
- [Addons, integration] Added ability to register multiple fluid blob effect handlers for a single fluid type
- Refactor projectile properties to be less inheritance-based (BREAKING)
  - New approach involves projectile-specific serializers that projectiles must reference instead of getting wildcard
  - This will break existing serializers and retrieval of properties
  - Some config files will be broken
    - All fields that are either `explosion_power` or `explosive_power` are now `explosive_power`
- Rework drag kinematics to ease factoring in dimensional density
  - All projectiles are affected
  - Previously, the drag equation was `vel' = vel * drag * dimensional density`
  - The drag equation is now `vel' = vel - vel * vel * drag * dimensional density`
  - Changed default drag values of big cannon and autocannon projectiles to 0.001 by default
  - Changed default drag values of projectile burst sub-projectiles to 0 by default
- Tweaked Mortar Stone to be able to handle equivalent of 3 Powder Charges, up from 1 Powder Charge
- Improved in-world effects of fluid blob fluids
  - Added ability for fluid blob to affect entities in-flight
  - Water-filled Fluid Blob harms entities sensitive to water and douses entities
  - Lava-filled fluid blob now lights certain blocks, opposite to water's effects
  - Potion-filled Fluid Blob applies its effect to entities while in flight
- Improved quick firing breech opening effects
  - Add more particles on opening and give them a bit of velocity
  - Add a short pickup delay for ejected items, can be configured on server
  - Can configure between item going directly into inventory and dropping item, default latter
- Improved smoke shell detonation sound
- Made shrapnel, grapeshot, and flak impact effect louder and added more particles
- Improved explosion effect for Mortar Stone
  - Added sound, particles, and screen shake
- Autocannon rounds are now visually larger than machine gun rounds in flight
- x2 Big Cartridge Sheets (Brass only)
- +2 Autocannon Cartridge sheets
- Reduced Gunpowder cost of Filled Autocannon Cartridge from 3 to 1
- Rework penetration and bouncing (BREAKING)
  - Separate hardness and toughness (default: `hardness == 1`, `toughness == blast resistance`)
    - JSONs must be rewritten
  - Most projectiles now have additional config fields `penetration`, `toughness`, and `deflection`
  - Projectiles now shatter if `block hardness - projectile penetration > projectile toughness`, prevents some fuzes from detonating
  - Renamed Resistance Inspection Tool to Block Armor Inspection Tool
  - Block Armor Inspection Tool now displays info in a more elegant form similar to goggles
  - Block Armor Inspection Tool also displays information on hovered blocks in the inventory
- Improved terminal projectile trajectory appearance on clientside
- Improved blast propagation for big cannon blast to take current player position into account
- Rebalanced projectiles
- Rebalanced some block armor values
- Rebalanced autocannons to be more accurate
- Rebalanced big cannon materials to be more accurate with shorter barrels for stronger materials
  - Around 25% reduction in barrel size for steel (12 -> 9 for max charge)
  - Almost 50% reduction in barrel size for nethersteel (24 -> 14 for max charge)
### Fixed
- Fixed invalid cannon propellant combinations due to spelling mistake in data pack folder name
- Fixed cannon loader pushing without head
- Fixed lag from cannon being tested during explosion, courtesy of Endalion
- Fixed Disable All Failures config option not disabling squibbing
- Fixed manual autocannons breaking on tick after squib
- Fixed Create Big Cannons projectiles exceeding 10 m/gt (200 m/gt) losing their velocity when loaded
- Fixed Smoke Shell and Drop Mortar Shell being invalid blocks for `createbigcannons:fuzed_block` block entity
- Fixed Cannon Carriage rotation info tooltip and syncing
- Fixed Disable All Failures applying to big cannon projectiles with 0 cannon power
- Fixed schematics not saving data of many blocks by adding `#create:safe_nbt`
- Fixed potential crash on cannon failure in some Create Big Cannons contraption code
- Fixed `/gamerule doTileDrops false` not applying to Create Big Cannons pole contraptions
- Fixed small charge projectiles not emitting cannon smoke
- Fixed Cannon Cast crashing game through stack overflow
- Fixed projectile ballistics not being accurate on client
- Fixed autocannon tracer rendering intersecting with barrel
- Fixed intersection loading occurring when disabled by using pistons and pulleys
- Fixed cooldown loading involving rapidly disassembling quick-fire big cannons and autocannons
- Fixed partial block damage display
- Fixed cannon cast leakage duplication
- Fixed ghost Yaw Controller effect
- Fixed autocannons being able to assemble with two breeches
- Fixed projectile hitbox not centered on visual projectile
- Fixed projectiles not chunkloading on world load and on spawn
- [Integration] Fixed FramedBlocks armor values
- [1.20.1] Fixed issue with drill boring scrap loot in 1.20.1

## [0.5.4]

*"Now, occasionally you're going to get stuck. It's nothing to get upset about. Don't go hurling your controller at the cat, because it might throw something back, and it'll only escalate, and then, well..."* \
&ndash; Stephen Fry, LittleBigPlanet (2008)

**Create Big Cannons 0.5.4, with more QoL features and minor fixes.\
Now supports Create Fabric 0.5.1.f patch 1.**

### Added
- Added ability for some base Create contraptions to load cannons*
 - Mechanical Piston, Gantry, and Rope Pulley
- Added Cannon Loader breaking features similar to Mechanical Piston
- Added block hardness compatibility with Create's Copycat blocks
- Added default autocannon rounds to search tab
- Added assembly connectivity between Cannon Mount/Cannon Carriage and unassembled cannons
- Added assembly connectivity between Yaw Controller and Cannon Mount
- Added minimum spread option to big cannon materials
- Added spread reduction option to big cannon materials
- Added tags for various Create Big Cannons munitions items
  - `#createbigcannons:fuzes`
  - `#createbigcannons:autocannon_ammo_containers`
  - `#createbigcannons:autocannon_cartridges`
  - `#createbigcannons:autocannon_rounds`
  - `#createbigcannons:spent_autocannon_casings`
  - `#createbigcannons:big_cannon_projectiles`
  - `#createbigcannons:big_cannon_propellant`
    - Includes:
      - `#createbigcannons:big_cannon_cartridges`
      - `#createbigcannons:big_cannon_propellant_bags`
- Added ability to place Autocannon Ammo Containers as blocks
- Added tooltip to Autocannon Ammo Container showing contained items and spacing
- Added Creative Autocannon Ammo Container, an endless source of autocannon ammo
- Added block properties compatibility with Create Copycat blocks
- Added block properties compatibility with Copycats+
- Added cannon pitch config for different cannon mounts and different cannon types
- Added Flak and Shrapnel explosion config
- Added crafting table munition disassembly for fuzes and tracers on autocannon rounds
- Added autocannon RPM display to cannon mount tooltip and action bar when controlling autocannon
- Added tags for nethersteel materials
  - `#forge:nuggets/nethersteel` and `#c:nethersteel_nuggets` (item)
  - `#forge:ingots/nethersteel` and `#c:nethersteel_ingots` (item)
  - `#forge:storage_blocks/nethersteel` and `#c:nethersteel_blocks` (item)
  - `#createbigcannons:molten_nethersteel` (fluid)
- [Fabric] Added REI compatibility
- [Fabric] Added EMI compatibility
- [Forge] Added block properties compatibility with Framed Blocks
### Changes
- Reduced the stress cost of the Cannon Loader to match that of the Mechanical Piston
- Reduced default blob count of Fluid Shell
- Changed minimum spread of cannons, varying by material strength
- Changed `max_safe_charges` big cannon config option to `max_safe_propellant_stress`
  - The old field still works, but a warning will be printed in the game log highlighting any deprecated files.
- Changed `squib_ratio_barrels` and `squib_ratio_propellant` to `minimum_velocity_per_barrel`
  - The old fields still work, but a warning will be printed in the game log highlighting any deprecated files.
- Changed minimum timing for Timed Fuze from 20 ticks (1 second) to 1 tick, reduced maximum time from 25s 15t to 24s 15t
- Changed minimum timing for Delayed Impact Fuze from 20 ticks (1 second) to 1 tick, reduced maximum time from 6s to 5s
- Improved data pack config for block hardness and block properties for terminal ballistics in general
- Update ponder documentation for base contraption loading, cannon block breaking, and quick-firing breech loading
- Changed molten cast iron and molten nethersteel recipes to use tag rather than raw fluid
- Changed nethersteel item melting to use nethersteel item tags instead of Create Big Cannons nethersteel items
### Fixes
- Fixed drop mortar not dropping stored item if disassembled or broken before fired
- Fixed drop mortar holding entire stack
- Fixed glitch-loading, with toggle to re-enable it*
- Fixed attachment checks
- Fixed Cannon Loader not breaking other blocks when destroyed
- Fixed Cannon Loader not assembling when new under certain conditions
- Fixed block properties of some blocks
- Fixed back-fuzed Delayed Impact Fuze going off when it should not
- Fixed primed Timed Fuze and Delayed Impact Fuze despawning before detonating
- Fixed munitions not dropping when breaking cannon blocks
- Fixed preferred placement for screw breech when next to kinetic blocks
- Fixed Mechanical Arm taking entire stack when loading quick-firing breech
- Fixed Fluid Stack serializer crashing other mods
- Fixed Fluid tags for certain recipes
- Fixed Create Big Cannons GUIs obstructing recipe viewer items in JEI, REI, and EMI
- Fixed Cannon Builder not pushing on unconnected layers of the same shape
- Fixed zoom in for autocannon not working
- Fixed autocannon ammo ejection
- Fixed Fluid tags for fluid-based recipes
- Fixed cannon casting not working on relog
- Fixed incorrect display value for Mortar Stone tooltip
- Fixed motion of Cannon Loader, Cannon Drill, and Cannon Builder
- Fixed tracer tip crafting in table

*May have issues with contraption-modifying mods such as Create: Interactive
