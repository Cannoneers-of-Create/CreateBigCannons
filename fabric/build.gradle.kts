@file:Suppress("UnstableApiUsage")

import java.util.*


plugins {
	`maven-publish`
	id("dev.architectury.loom")
	id("architectury-plugin")
	id("com.gradleup.shadow")
}

val loader = property("loom.platform")!!
val minecraftVersion: String = stonecutter.current.version
val common: Project = requireNotNull(stonecutter.node.sibling("")) {
	"No common project for $project"
}.project

val ci = System.getenv("CI")?.toBoolean() ?: false
val release = System.getenv("RELEASE")?.toBoolean() ?: false
val nightly = ci && !release
val buildNumber = System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull()
version = "${mod.version}${if (release) "" else "-dev"}+mc.${minecraftVersion}-${loader}${if (nightly) "-build.${buildNumber}" else ""}"
group = "${mod.group}.$loader"
base {
	archivesName.set(mod.id)
}
architectury {
	platformSetupLoomIde()
	fabric()
}

val commonBundle: Configuration by configurations.creating {
	isCanBeConsumed = false
	isCanBeResolved = true
}

val shadowBundle: Configuration by configurations.creating {
	isCanBeConsumed = false
	isCanBeResolved = true
}

configurations {
	compileClasspath.get().extendsFrom(commonBundle)
	runtimeClasspath.get().extendsFrom(commonBundle)
	get("developmentFabric").extendsFrom(commonBundle)
}

loom {
	silentMojangMappingsLicense()
	accessWidenerPath = common.loom.accessWidenerPath
	runConfigs {
		create("DataGenFabric") {
			client()

			name("Fabric Data Generation")
			vmArg("-Dfabric-api.datagen")
			vmArg("-Dfabric-api.datagen.output-dir=${project.rootProject.file("fabric/src/generated/resources")}")
			vmArg("-Dfabric-api.datagen.modid=createbigcannons")
			vmArg("-Dporting_lib.datagen.existing_resources=${project.rootProject.file("src/main/resources")}")
			vmArg("-Dcreatebigcannons.datagen.platform=fabric")
		}
		create("DataGenForge") {
			client()

			name("Forge Data Generation (Fabric)")
			vmArg("-Dfabric-api.datagen")
			vmArg("-Dfabric-api.datagen.output-dir=${project.rootProject.file("forge/src/generated/resources")}")
			vmArg("-Dfabric-api.datagen.modid=createbigcannons")
			vmArg("-Dporting_lib.datagen.existing_resources=${project.rootProject.file("src/main/resources")}")
			vmArg("-Dcreatebigcannons.datagen.platform=forge")
		}
		all {
			isIdeConfigGenerated = true
			runDir = "../../../run"
			//vmArgs("-Dmixin.debug.export=true")
		}
	}
}

repositories {
	maven("https://maven.terraformersmc.com/releases/") // Mod Menu
	maven("https://mvn.devos.one/snapshots/") // Create Fabric, Forge Tags, Milk Lib, Registrate Fabric
	maven("https://mvn.devos.one/releases/") // Porting Lib Releases
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") // Forge config api port
	maven("https://maven.cafeteria.dev/releases") // Fake Player API
	maven("https://maven.jamieswhiteshirt.com/libs-release") // Reach Entity Attributes
	maven("https://jitpack.io/") // Mixin Extras, Fabric ASM
	maven("https://maven.ladysnake.org/releases") // Trinkets
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	mappings(loom.layered {
		officialMojangMappings { nameSyntheticMembers = false }
		parchment("org.parchmentmc.data:parchment-${minecraftVersion}:${mod.dep("parchment_version")}@zip")
	})
	modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader")}")
	modApi("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api_version")}")

	"io.github.llamalad7:mixinextras-fabric:${mod.dep("mixin_extras")}".let {
		annotationProcessor(it)
		implementation(it)
	}

	// Create - dependencies are added transitively
	modImplementation("com.simibubi.create:create-fabric:${mod.dep("create_fabric_version")}")

	// Development QOL
	modLocalRuntime("maven.modrinth:lazydfu:${mod.dep("lazydfu_version")}")
	modImplementation("com.terraformersmc:modmenu:${mod.dep("modmenu_version")}")

	// Recipe Viewers - Create Fabric supports JEI, REI, and EMI.
	// See root gradle.properties to choose which to use at runtime.
	when (mod.dep("fabric_recipe_viewer").lowercase(Locale.ROOT)) {
		"jei" -> modLocalRuntime("mezz.jei:jei-${minecraftVersion}-fabric:${mod.dep("jei_version")}")
		"rei" -> modLocalRuntime("me.shedaniel:RoughlyEnoughItems-fabric:${mod.dep("rei_version")}")
		"emi" -> modLocalRuntime("dev.emi:emi-fabric:${mod.dep("emi_version")}")
		"disabled" -> {}
		else -> println("Unknown recipe viewer specified: ${mod.dep("fabric_recipe_viewer")}. Must be JEI, REI, EMI, or disabled.")
	}
	// if you would like to add integration with them, uncomment them here.
	modCompileOnly("mezz.jei:jei-${minecraftVersion}-fabric:${mod.dep("jei_version")}") { isTransitive = false }
	modCompileOnly("mezz.jei:jei-${minecraftVersion}-common:${mod.dep("jei_version")}")
	modCompileOnly("me.shedaniel:RoughlyEnoughItems-api-fabric:${mod.dep("rei_version")}")
	modCompileOnly("me.shedaniel:RoughlyEnoughItems-default-plugin-fabric:${mod.dep("rei_version")}")
	modCompileOnly("dev.emi:emi-fabric:${mod.dep("emi_version")}")

	modLocalRuntime("curse.maven:spark-361579:${mod.dep("spark_fabric_file")}") // Spark

	// Ritchie's Projectile Library
	val rplSuffix = if (mod.dep("use_local_rpl_build").toBoolean()) "" else "-build.${mod.dep("rpl_build")}"
	modImplementation(include("com.rbasamoyai:ritchiesprojectilelib:${mod.dep("rpl_version")}+mc.${minecraftVersion}-fabric" + rplSuffix) { isTransitive = false })

	// Fixes, integration
	//modImplementation("curse.maven:free-cam-557076:${freecam_fabric_file}") // Freecam
	modImplementation("com.copycatsplus:copycats:${mod.dep("copycats_version")}+mc.${minecraftVersion}-fabric") { isTransitive = false }
	// Trinkets and CCA
	modLocalRuntime("dev.emi:trinkets:${mod.dep("trinkets_fabric_version")}")
	modCompileOnly("dev.emi:trinkets:${mod.dep("trinkets_fabric_version")}") { exclude(group = "com.terraformersmc") }
	modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${mod.dep("cca_fabric_version")}")
	modCompileOnly("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${mod.dep("cca_fabric_version")}")

	commonBundle(project(common.path, "namedElements")) { isTransitive = false }
	shadowBundle(project(common.path, "transformProductionFabric")) { isTransitive = false }
}

java {
	withSourcesJar()
	val java = if (stonecutter.eval(minecraftVersion, ">=1.20.5"))
		JavaVersion.VERSION_21 else JavaVersion.VERSION_17
	targetCompatibility = java
	sourceCompatibility = java
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			artifact(tasks.remapJar)
			artifact(tasks.remapSourcesJar)
			group = mod.group
			artifactId = mod.id
		}
	}
}

tasks.shadowJar {
	exclude("architectury.common.json")
	configurations = listOf(shadowBundle)
	archiveClassifier = "dev-shadow"
}

tasks.remapJar {
	injectAccessWidener = true
	input = tasks.shadowJar.get().archiveFile
	archiveClassifier = null
	dependsOn(tasks.shadowJar)
}

tasks.jar {
	archiveClassifier = "dev"
}

tasks.processResources {
	properties(listOf("fabric.mod.json"),
		"version" to mod.version,
		"fabric_loader_version" to mod.dep("fabric_loader_version"),
		"fabric_api_version" to mod.dep("fabric_api_version"),
		"minecraft_version" to minecraftVersion,
		"create_version" to mod.dep("create_fabric_version"), // on fabric, use the entire version, unlike forge
        "create_version_range" to mod.dep("create_fabric_version_range"),
        "rpl_version_range" to mod.dep("rpl_fabric_version_range"),
		"copycats_breaks" to mod.dep("copycats_breaks_fabric"),
		"trinkets_breaks" to mod.dep("trinkets_breaks_fabric")
	)
}

sourceSets.main {
	resources { // include generated resources in resources
		srcDir("src/generated/resources")
		exclude("src/generated/resources/.cache")
	}
}

tasks.register<Copy>("buildAndCollect") {
	group = "build"
	from(tasks.remapJar.get().archiveFile, tasks.remapSourcesJar.get().archiveFile)
	into(rootProject.layout.buildDirectory.file("libs/${mod.version}/$loader"))
	dependsOn("build")
}

tasks.register("runDataGen") {
	group = "loom"
	description = "Generate data for Create Big Cannons"
	dependsOn("runDataGenFabric", "runDataGenForge")
}
