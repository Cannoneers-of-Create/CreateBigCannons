# Create Multi-Loader Addon Template
A template based on Architectury for creating addons for Create on Forge, Fabric, and Quilt, simultaneously.

## How does it work?
This template is powered by the [Architectury](https://github.com/architectury) toolchain.
Architectury allows developers to create the majority of their mod in common, loader-agnostic code that
only touches Minecraft itself. This can be found in the [common](common) subproject. Each loader target 
also has its own subproject: those being [forge](forge) and [fabric](fabric). (Quilt support: you 
shouldn't need anything special and the Fabric version should work fine, but it is possible to add a 
`quilt` subproject if needed.) These loader-specific projects bridge between their respective loaders 
and the common code.

This system can be extended to work with Create as well as plain Minecraft. The common project gives
access to most of Create, Registrate, and Flywheel.

## Limitations
Minecraft has a lot of differences across loaders. You'll need to manage these differences using
abstractions. Architectury does provide an [API](https://github.com/architectury/architectury-api)
which you may use if desired, but it means you have another dependency to worry about.

This also applies to Create, which underwent significant changes in porting to Fabric. This means a lot 
of it will be different between loaders. The `common` project is only capable of referencing the code 
on one loader (Fabric in this template), so you should be careful to not reference things that don't 
exist on the other one. Test often, and check the code on both loaders. When you do need to use these 
changed things, that leads us to...

## Solutions
There's a bunch of ways to work around the differences.

First is Architectury API. It provides cross-loader abstractions that can be used in common code for
a decent amount of Minecraft. However, it means you need to worry about another dependency. It also
doesn't really help with Create.

Next is the `@ExpectPlatform` annotation. It allows the implementation of a method to be replaced
at compile time per-loader, letting you make your own abstractions. It is part of the Architectury
plugin and does not cause an extra dependency. However, it can only be placed on static methods. See 
[ExampleExpectPlatform](common/src/main/java/net/examplemod/ExampleExpectPlatform.java) in common 
for an example.

Finally, simply have a common interface with implementation based on the loader. You might have a
`PlatformHelper` common interface, with a static instance somewhere. On Fabric, set it to a
`FabricPlatformHelper`, and a `ForgePlatformHelper` on Forge. The implementation is kept as a detail
so you can use your helper from common code.

## Features
- Access to Create and all of its dependencies on both loaders
- Mojang Mappings base, with Quilt Mappings and Parchment providing Javadoc and parameters
- QuiltFlower decompiler for high quality Minecraft sources: `gradlew genSourcesWithQuiltflower`
- GitHub Actions automatic build workflow
- Machete Gradle plugin to shrink jar file sizes
- Developer QOL: Mod Menu, LazyDFU, JEI

## Use
Ready to get started? First you'll want to create a new repository using this template. You can do it
through GitHub with the big green button near the top that says `Use this template`. 

Once you've got your repository set up, you'll want to change all the branding to your mod instead 
of the template. Every `examplemod`, every placeholder. 

You're free to change your license: CC0 lets you do whatever you want. Base Create is MIT, for reference. 

Replace this README with information about your addon. Give it an icon and change the metadata in the 
[fabric.mod.json](fabric/src/main/resources/fabric.mod.json) and the
[mods.toml](forge/src/main/resources/META-INF/mods.toml).

Configure your dependencies. Each subproject `build.gradle` has optional dependencies commented.
Either remove them or uncomment them. For Fabric, set your preferred recipe viewer with 
`fabric_recipe_viewer` in the root [gradle.properties](gradle.properties).

Remember to remove any example code you don't need anymore.

Get modding!

## Notes
- Architectury does not merge jars; When you build, you get separate jars for each loader.
  There is an independent project that can merge these into one if desired called
  [Forgix](https://github.com/PacifistMC/Forgix).
- The file names and versions of jars are configured in the root [build.gradle](build.gradle). Feel 
free to change the format if desired, but make sure it follows SemVer to work well on Fabric.
- When publishing, you should always let GitHub Actions build your release jars. These builds will
have build number metadata, and will be compressed by the Machete plugin.

## Other Templates
- [Fabric-only template](https://github.com/Fabricators-of-Create/create-fabric-addon-template)
- [Forge-only template](https://github.com/kotakotik22/CreateAddonTemplate)

## Help
Questions? Join us in the #devchat channel of the [Create Discord](https://discord.com/invite/hmaD7Se).

## License

This template is available under the CC0 license. Feel free to do as you wish with it.