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
    maven("https://maven.createmod.net")
	maven("https://mvn.devos.one/snapshots/") // Create Fabric
	maven("https://mvn.devos.one/releases/") // Porting Lib
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") // Forge Config API Port
	maven("https://maven.jamieswhiteshirt.com/libs-release") // Reach Entity Attributes
}

loom {
	silentMojangMappingsLicense()
	accessWidenerPath = rootProject.file("src/main/resources/createbigcannons.accesswidener")
    runConfigs.all {
        isIdeConfigGenerated = true
        runDir = "../../../run"
        vmArgs("-Dmixin.debug.export=true")
    }
    runs.configureEach {
        vmArg("-XX:+AllowEnhancedClassRedefinition")
        vmArg("-XX:+IgnoreUnrecognizedVMOptions")
        vmArg("-Dmixin.debug.export=true")
        vmArg("-Dmixin.env.remapRefMap=true")
        vmArg("-Dmixin.env.refMapRemappingFile=${projectDir}/build/createSrgToMcp/output.srg")
    }
}

dependencies {
	minecraft("com.mojang:minecraft:${minecraftVersion}")
	mappings(loom.layered {
		officialMojangMappings { nameSyntheticMembers = false }
		parchment("org.parchmentmc.data:parchment-${minecraftVersion}:${mod.dep("parchment_version")}@zip")
	})

    modImplementation("net.fabricmc:fabric-loader:${mod.dep("fabric_loader_version")}")

    modImplementation("com.simibubi.create:create-${minecraftVersion}:${mod.dep("create_neoforge_version")}:slim") { isTransitive = false }
    modImplementation("net.createmod.ponder:Ponder-NeoForge-${minecraftVersion}:${mod.dep("ponder_neoforge_version")}")
    modCompileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${minecraftVersion}:${mod.dep("flywheel_neoforge_version")}")
    modRuntimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${minecraftVersion}:${mod.dep("flywheel_neoforge_version")}")
    modImplementation("com.tterrag.registrate:Registrate:${mod.dep("registrate_neoforge_version")}")

    compileOnly("net.neoforged.fancymodloader:loader:9.0.18")
    compileOnly("net.neoforged:neoforge:${mod.dep("neoforge_loader_version")}")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:${mod.dep("fabric_api_version")}+${minecraftVersion}")

    "io.github.llamalad7:mixinextras-common:${mod.dep("mixin_extras_version")}".let {
        annotationProcessor(it)
        implementation(it)
    }

    compileOnly("io.github.llamalad7:mixinextras-common:${mod.dep("mixin_extras_version")}")
    annotationProcessor(include("io.github.llamalad7:mixinextras-neoforge:${mod.dep("mixin_extras_version")}"){})

	// Ritchie's Projectile Library
    val rplSuffix = if (mod.dep("use_rpl_nightly").toBoolean()) "-build.${mod.dep("rpl_build")}" else ""
	modImplementation("com.rbasamoyai:ritchiesprojectilelib:${mod.dep("rpl_version")}+mc.${minecraftVersion}-neoforge$rplSuffix") { isTransitive = false }

	//modImplementation("com.copycatsplus:copycats:${mod.dep("copycats_version")}+mc.${minecraftVersion}-forge") {isTransitive=false}
}


java {
	withSourcesJar()
	val java = if (stonecutter.eval(minecraftVersion, ">=1.20.5"))
		JavaVersion.VERSION_21 else JavaVersion.VERSION_17
	targetCompatibility = java
	sourceCompatibility = java
}

afterEvaluate {
    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(listOf("-Xmaxerrs", "2000"))
    }
}
