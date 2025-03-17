@file:Suppress("UnstableApiUsage")

plugins {
	`maven-publish`
	id("dev.architectury.loom")
	id("architectury-plugin")
}

val minecraftVersion = stonecutter.current.version

val ci = System.getenv("CI")?.toBoolean() ?: false
val release = System.getenv("RELEASE")?.toBoolean() ?: false
val nightly = ci && !release
val buildNumber = System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull()
version = "${mod.version}${if (release) "" else "-dev"}+mc.${minecraftVersion}-common${if (nightly) "-build.${buildNumber}" else ""}"
group = "${group}.common"
base.archivesName.set(mod.id)

architectury.common(stonecutter.tree.branches.mapNotNull {
	if (stonecutter.current.project !in it) null
	else it.project.prop("loom.platform")
})

repositories {
	maven("https://mvn.devos.one/snapshots/") // Create Fabric
	maven("https://mvn.devos.one/releases/") // Porting Lib
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") // Forge Config API Port
	maven("https://maven.jamieswhiteshirt.com/libs-release") // Reach Entity Attributes
}

loom {
	silentMojangMappingsLicense()
	accessWidenerPath = rootProject.file("src/main/resources/createbigcannons.accesswidener")
}

dependencies {
	minecraft("com.mojang:minecraft:${minecraftVersion}")
	mappings(loom.layered {
		officialMojangMappings { nameSyntheticMembers = false }
		parchment("org.parchmentmc.data:parchment-${minecraftVersion}:${mod.dep("parchment_version")}@zip")
	})

	modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader_version")}")
	modImplementation("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api_version")}")
	modCompileOnly("com.simibubi.create:create-fabric-${mod.dep("minecraft_version")}:${mod.dep("create_fabric_version")}")
	modCompileOnly("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api_version")}")

	"io.github.llamalad7:mixinextras-common:${mod.dep("mixin_extras")}".let {
		annotationProcessor(it)
		implementation(it)
	}

	// Ritchie's Projectile Library
	val rplSuffix = if (mod.dep("use_local_rpl_build").toBoolean()) "" else "-build.${mod.dep("rpl_build")}"
	modImplementation("com.rbasamoyai:ritchiesprojectilelib:${mod.dep("rpl_version")}+mc.${minecraftVersion}-common$rplSuffix") {
		isTransitive = false
	}

	modImplementation("com.copycatsplus:copycats:${mod.dep("copycats_version")}+mc.${minecraftVersion}-fabric") {isTransitive=false}
}


java {
	withSourcesJar()
	val java = if (stonecutter.eval(minecraftVersion, ">=1.20.5"))
		JavaVersion.VERSION_21 else JavaVersion.VERSION_17
	targetCompatibility = java
	sourceCompatibility = java
}

tasks.build {
	group = "versioned"
	description = "Must run through 'chiseledBuild'"
}
