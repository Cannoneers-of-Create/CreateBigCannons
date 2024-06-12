# Translation Changes

This is a document for keeping track of every translation key and value change. The locale is en_us.

Note: Changes to en_us.json before Create Big Cannons 0.5.3.b => 0.5.4 will not be tracked. Please take note of them yourself.

---

## 0.5.4 → 5.5.0

Additions: \
&plus; `"block.createbigcannons.smoke_shell.tooltip": "SMOKE SHELL"` \
&plus; `"block.createbigcannons.smoke_shell.tooltip.behaviour1": "Releases a _temporary smoke cloud_."` \
&plus; `"block.createbigcannons.smoke_shell.tooltip.condition1": "On Detonation"` \
&plus; `"block.createbigcannons.smoke_shell.tooltip.summary": "Covers the battlefield with a _smoke cloud_ that obscures vision."` \
&plus; `"item.createbigcannons.delayed_impact_fuze.tooltip.durability": "Durability"` \
&plus; `"item.createbigcannons.delayed_impact_fuze.tooltip.durability.value": "This fuze can break through _%s_ blocks before breaking."` \
&plus; `"item.createbigcannons.impact_fuze.tooltip.durability": "Durability"` \
&plus; `"item.createbigcannons.impact_fuze.tooltip.durability.value": "This fuze can break through _%s_ blocks before breaking."` \
&plus; `"createbigcannons.ponder.munitions/adding_tracers.header": "Adding tracers to big cannon projectiles"` \
&plus; `"createbigcannons.ponder.munitions/adding_tracers.text_1": "In addition to autocannon munitions, tracers can also be added to big cannon projectiles."` \
&plus; `"createbigcannons.ponder.munitions/adding_tracers.text_2": "Unlike fuzing, tracer tips can be applied to any side of the projectile block."` \
&plus; `"createbigcannons.ponder.munitions/adding_tracers.text_3": "Right-click the projectile with an empty hand to remove any tracers present."` \
&plus; `"createbigcannons.ponder.munitions/adding_tracers.text_4": "As with fuzing, tracer application can be automated with Deployers."` \
&plus; `"createbigcannons.ponder.cannon_crafting/finishing_built_up_cannons.text_4": "It takes a while for the cannon layers to transform into cannon blocks."` \
&plus; `"createbigcannons.subtitle.fire_drop_mortar": "Drop mortar fires"` \
&plus; `"createbigcannons.subtitle.fire_machine_gun": "Machine gun fires"` \
&plus; `"createbigcannons.subtitle.flak_round_explosion": "Flak round explodes"` \
&plus; `"createbigcannons.subtitle.shrapnel_shell_explosion": "Shrapnel shell explodes"` \
&plus; `"death.attack.createbigcannons.flak": "%s was downed by flak"` \
&plus; `"entity.createbigcannons.flak_burst": "Flak Burst"` \
&plus; `"entity.createbigcannons.fluid_blob_burst": "Fluid Blob Burst"` \
&plus; `"entity.createbigcannons.grapeshot_burst": "Grapeshot Burst"` \
&plus; `"entity.createbigcannons.shrapnel_burst": "Shrapnel Burst"` \
&plus; `"createbigcannons.subtitle.fluid_shell_explosion": "Fluid shell explodes"` \
&plus; `"createbigcannons.subtitle.smoke_shell_detonate": "Smoke shell bursts"` \
&plus; `"createbigcannons.subtitle.mortar_stone_explode": "Mortar stone explodes"`

Key Changes: \
`"item.createbigcannons.delayed_impact_fuze.tooltip.shell_info"` → `"item.createbigcannons.delayed_impact_fuze.tooltip.shell_info.chance"` \
`"item.createbigcannons.impact_fuze.tooltip.shell_info"` → `"item.createbigcannons.impact_fuze.tooltip.shell_info.chance"`

Content Changes: \
`"block.createbigcannons.fluid_shell.tooltip.condition3"`: `"Note"` → `Note on Fluid Behavior"` \
`"createbigcannons.ponder.tag.cannon_crafting.description`: `"How to manufacture big cannons"` → `"How to manufacture cannons of different sizes and calibers"` \
`"createbigcannons.ponder.tag.munitions.description"`: `"Blocks that make up cannon loads, and what they can do"` → `"Blocks and items used by cannons, and what they can do"` \
`"createbigcannons.ponder.tag.operating_cannons.description"`: `"How to use big cannons safely and effectively"` → `"How to use cannons safely and effectively"` \
`"item.createbigcannons.delayed_impact_fuze.tooltip.summary"`: `"Detonates a _short time_ after _hitting_ something. Due to its _simple trigger mechanism_, it does not always trigger the timer."` → `"Detonates a _short time_ after _hitting_ something. Due to its _simple trigger mechanism_, it does not always trigger the timer. This must be mounted on the _front_ of a shell; it will not work as a _base fuze._"` \
`"item.createbigcannons.impact_fuze.tooltip.summary"`: `"Detonates when the projectile _hits something_. Due to its _simplicity_, it does not always detonate on impact."` → `"Detonates when the projectile _hits something_. Due to its _simplicity_, it does not always detonate on impact. This must be mounted on the _front_ of a shell; it will not work as a _base fuze._",` \
`"item.createbigcannons.proximity_fuze.tooltip.summary"`: `"Detonates when it _gets close_ to a block."` → `"Detonates when it _gets close_ to a block. This must be mounted on the _front_ of a shell; it will not work as a _base fuze._"` \
`"createbigcannons.ponder.cannon_crafting/finishing_built_up_cannons.text_3"`: `"It takes a while for the cannon layers to transform into cannon blocks."` → `"A built-up cannon block must have all the correct layers to be transformed."`

Removals: \
&minus; `"entity.createbigcannons.fluid_blob"` \
&minus; `"entity.createbigcannons.grapeshot"` \
&minus; `"entity.createbigcannons.shrapnel"`

## 0.5.3.b → 0.5.4

Additions: \
&plus; `"block.createbigcannons.autocannon_ammo_container.tooltip.main_ammo": "x%1$s %2$s"` \
&plus; `"block.createbigcannons.autocannon_ammo_container.tooltip.tracers": "Tracers: x%1$s %2$s"` \
&plus; `"block.createbigcannons.autocannon_ammo_container.tooltip.tracers": "Tracer Spacing: 1 tracer every %s round(s)"` \
&plus; `"tag.c.bronze_nuggets": "Bronze Nuggets"` \
&plus; `"tag.c.cast_iron_ingots": "Cast Iron Ingots"` \
&plus; `"tag.c.cast_iron_nuggets": "Cast Iron Nuggets"` \
&plus; `"tag.c.ingots.cast_iron": "Cast Iron Ingots (Forge Format)"` \
&plus; `"tag.c.nuggets.bronze": "Bronze Nuggets (Forge Format)"` \
&plus; `"tag.c.nuggets.cast_iron": "Cast Iron Nuggets (Forge Format)"` \
&plus; `"tag.c.nuggets.steel": "Steel Nuggets (Forge Format)"` \
&plus; `"tag.c.steel_nuggets": "Steel Nuggets"` \
&plus; `"tag.createbigcannons.autocannon_ammo_containers": "Autocannon Ammo Containers"` \
&plus; `"tag.createbigcannons.autocannon_cartridges": "Autocannon Cartridges"` \
&plus; `"tag.createbigcannons.autocannon_rounds": "Autocannon Rounds"` \
&plus; `"tag.createbigcannons.big_cannon_cartridges": "Big Cannon Cartridges"` \
&plus; `"tag.createbigcannons.big_cannon_projectiles": "Big Cannon Projectiles"` \
&plus; `"tag.createbigcannons.big_cannon_propellant": "Big Cannon Propellant"` \
&plus; `"tag.createbigcannons.big_cannon_propellant_bags": "Big Cannon Propellant Bags"` \
&plus; `"tag.createbigcannons.block_bronze": "Bronze Blocks"` \
&plus; `"tag.createbigcannons.block_cast_iron": "Cast Iron Blocks"` \
&plus; `"tag.createbigcannons.block_steel": "Steel Blocks"` \
&plus; `"tag.createbigcannons.dust_glowstone": "Glowstone Dusts"` \
&plus; `"tag.createbigcannons.dusts_redstone": "Redstone Dusts"` \
&plus; `"tag.createbigcannons.fuzes": "Fuzes"` \
&plus; `"tag.createbigcannons.gems_quartz": "Quartz Gems"` \
&plus; `"tag.createbigcannons.gunpowder": "Gunpowder"` \
&plus; `"tag.createbigcannons.impact_fuze_head": "Impact Fuze Head Components"` \
&plus; `"tag.createbigcannons.inexpensive_big_cartridge_sheet": "Inexpensive Big Cartridge Sheets"` \
&plus; `"tag.createbigcannons.ingot_brass": "Brass Ingots"` \
&plus; `"tag.createbigcannons.ingot_bronze": "Bronze Ingots"` \
&plus; `"tag.createbigcannons.ingot_cast_iron": "Cast Iron Ingots"` \
&plus; `"tag.createbigcannons.ingot_iron": "Iron Ingots"` \
&plus; `"tag.createbigcannons.ingot_steel": "Steel Ingots"` \
&plus; `"tag.createbigcannons.nitropowder": "Nitropowder"` \
&plus; `"tag.createbigcannons.nugget_bronze": "Bronze Nuggets"` \
&plus; `"tag.createbigcannons.nugget_cast_iron": "Cast Iron Nuggets"` \
&plus; `"tag.createbigcannons.nugget_copper": "Copper Nuggets"` \
&plus; `"tag.createbigcannons.nugget_iron": "Iron Nuggets"` \
&plus; `"tag.createbigcannons.nugget_steel": "Steel Nuggets"` \
&plus; `"tag.createbigcannons.sheet_brass": "Brass Sheets"` \
&plus; `"tag.createbigcannons.sheet_copper": "Copper Sheets"` \
&plus; `"tag.createbigcannons.sheet_iron": "Iron Sheets"` \
&plus; `"tag.createbigcannons.sheet_steel": "Steel Sheets"` \
&plus; `"tag.createbigcannons.spent_autocannon_casings": "Spent Autocannon Casings"` \
&plus; `"tag.createbigcannons.stone": "Stone"` \
&plus; `"emi.category.createbigcannons.built_up_heating": "Cannon Building"` \
&plus; `"emi.category.createbigcannons.cannon_casting": "Cannon Casting"` \
&plus; `"emi.category.createbigcannons.drill_boring": "Drill Boring"` \
&plus; `"emi.category.createbigcannons.incomplete_cannon_blocks": "Incomplete Cannon Blocks"` \
&plus; `"emi.category.createbigcannons.melting": "Basin Melting"` \
&plus; `"createbigcannons.ponder.cannon_kinetics/automating_quick_firing_breeches.text_3": "The Quick-Firing Breech must be closed in order for the arm to operate."` \
&plus; `"createbigcannons.ponder.cannon_kinetics/automating_quick_firing_breeches.text_4": "The arm will not load the big cannon if the breech is open."` \
&plus; `"createbigcannons.ponder.cannon_kinetics/automating_quick_firing_breeches.text_5": "Once the cannon fires, the arm will automatically extract empty cartridges when provided a deposit area."` \
&plus; `"createbigcannons.ponder.cannon_kinetics/automating_quick_firing_breeches.text_6": "Deposit areas should have an empty Big Cartridge filter to avoid item mismanagement."` \
&plus; `"createbigcannons.ponder.cannon_loader/base_contraption_loading.header": "Loading Big Cannons with base Create contraptions"` \
&plus; `"createbigcannons.ponder.cannon_loader/base_contraption_loading.text_1": "Mechanical Pistons, Gantries, and Rope Pulleys can also load big cannons."` \
&plus; `"createbigcannons.ponder.cannon_loader/base_contraption_loading.text_2": "Munition blocks can be pulled out of big cannons depending on the connectivity of certain blocks."` \
&plus; `"createbigcannons.ponder.cannon_loader/base_contraption_loading.text_3": "Unlike the Cannon Loader, other blocks can still be attached to the contraption."` \
&plus; `"createbigcannons.ponder.cannon_loader/base_contraption_loading.text_4": "This does not affect cannon loading in any way, although only aligned munition blocks can be inserted into big cannons."` \
&plus; `"block.createbigcannons.cannon_carriage.hotbar.fireRate.createbigcannons.cannon_carriage": "Rate of fire: %s RPM (scroll to change)"` \
&plus; `"block.createbigcannons.cannon_carriage.hotbar.fireRate.createbigcannons.cannon_mount": "Rate of fire: %s RPM (set signal strength on firing side to change)"` \
&plus; `"createbigcannons.goggles.cannon_mount.autocannon_rate_of_fire": "Autocannon Rate of Fire: "` \
&plus; `"createbigcannons.goggles.cannon_mount.autocannon_rate_of_fire.value": "%s RPM"`

Key Changes: \
`"item.createbigcannons.autocannon_ammo_container"` → `"block.createbigcannons.autocannon_ammo_container"`
