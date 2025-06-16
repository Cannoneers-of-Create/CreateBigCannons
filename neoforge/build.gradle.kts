@file:Suppress("UnstableApiUsage")


plugins {
	`maven-publish`
	id("dev.architectury.loom")
	id("architectury-plugin")
	id("com.gradleup.shadow")
}

val loader = prop("loom.platform")!!
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
base.archivesName = mod.id
architectury {
	platformSetupLoomIde()
	neoForge()
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
	get("developmentNeoForge").extendsFrom(commonBundle)
}

loom {
	silentMojangMappingsLicense()
	accessWidenerPath = common.loom.accessWidenerPath

	runConfigs.all {
		isIdeConfigGenerated = true
		runDir = "../../../run"
		vmArgs("-Dmixin.debug.export=true")
	}
}

repositories {
    maven("https://maven.neoforged.net/releases/")
	maven("https://maven.theillusivec4.top/") // Curios
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
}

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	mappings(loom.layered {
		officialMojangMappings { nameSyntheticMembers = false }
		parchment("org.parchmentmc.data:parchment-${minecraftVersion}:${common.mod.dep("parchment_version")}@zip")
	})
    "neoForge"("net.neoforged:neoforge:${common.mod.dep("neoforge_loader_version")}")

	"io.github.llamalad7:mixinextras-neoforge:${common.mod.dep("mixin_extras_version")}".let {
		annotationProcessor(it)
		implementation(it)
	}

	// Create and its dependencies
	modImplementation("com.simibubi.create:create-${minecraftVersion}:${common.mod.dep("create_neoforge_version")}:slim") { isTransitive = false }
	modImplementation("net.createmod.ponder:Ponder-NeoForge-${minecraftVersion}:${common.mod.dep("ponder_neoforge_version")}")
	modCompileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${minecraftVersion}:${common.mod.dep("flywheel_neoforge_version")}")
	modRuntimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${minecraftVersion}:${common.mod.dep("flywheel_neoforge_version")}")
	modImplementation("com.tterrag.registrate:Registrate:${common.mod.dep("registrate_neoforge_version")}")

	// Development QOL
	modLocalRuntime("mezz.jei:jei-${minecraftVersion}-neoforge:${common.mod.dep("jei_version")}") { isTransitive = false }

	// if you would like to add integration with JEI, uncomment this line.
	modCompileOnly("mezz.jei:jei-${minecraftVersion}-neoforge-api:${common.mod.dep("jei_version")}")

	//modImplementation("curse.maven:spark-361579:${common.mod.dep("spark_forge_file")}") // Spark

	// Ritchie's Projectile Library
	val rplSuffix = if (common.mod.dep("use_local_rpl_build").toBoolean()) "" else "-build.${mod.dep("rpl_build")}"
	modImplementation(include("com.rbasamoyai:ritchiesprojectilelib:${common.mod.dep("rpl_version")}+mc.${minecraftVersion}-neoforge$rplSuffix"){ isTransitive = false })
	// Create: Unify
	// modImplementation("maven.modrinth:create-unify:${mod.dep("unify_forge_file")}") fixme

	// Fixes, integration
	//modImplementation("com.copycatsplus:copycats:${mod.dep("copycats_version")}+mc.${minecraftVersion}-forge") { isTransitive = false }
    modImplementation("maven.modrinth:framedblocks:${mod.dep("framedblocks_version")}")
	forgeRuntimeLibrary("com.github.ben-manes.caffeine:caffeine:3.1.1") // For FramedBlocks

	// Curios
	modRuntimeOnly("top.theillusivec4.curios:curios-neoforge:${common.mod.dep("curios_version")}+${minecraftVersion}")
	modCompileOnly("top.theillusivec4.curios:curios-neoforge:${common.mod.dep("curios_version")}+${minecraftVersion}:api")

    commonBundle(project(common.path, "namedElements")) { isTransitive = false }
    shadowBundle(project(common.path, "transformProductionNeoForge")) { isTransitive = false }
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

tasks.jar {
	archiveClassifier = "dev"
}

tasks.remapJar {
	injectAccessWidener = true
	input = tasks.shadowJar.get().archiveFile
	archiveClassifier = null
	dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
	configurations = listOf(shadowBundle)
	archiveClassifier = "dev-shadow"
	exclude("fabric.mod.json", "architectury.common.json")
}

tasks.processResources {
    properties(listOf("META-INF/neoforge.mods.toml"),
        "id" to mod.id,
        "name" to mod.id,
        "version" to mod.version,
        "neoforge_version" to common.mod.dep("neoforge_loader_version"),
        "minecraft_version" to minecraftVersion,
        "create_version" to mod.dep("create_neoforge_version").substringBefore("-"),
        //"unify_version" to mod.dep("unify_forge_version"),
        "copycats_version" to mod.dep("copycats_version"),
        "framedblocks_version" to mod.dep("framedblocks_version"),
        "curios_version" to mod.dep("curios_version")
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
