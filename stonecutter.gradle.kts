import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
	`maven-publish`
	id("dev.kikugie.stonecutter")
	id("dev.architectury.loom") version "1.7.+" apply false
	id("architectury-plugin") version "3.4-SNAPSHOT" apply false
	id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}
stonecutter active "1.20.1" /* [SC] DO NOT EDIT */

//todo: not sure if we even need this
stonecutter.parameters {
	val loader = metadata.project.substringAfterLast("-")
	consts(loader, "fabric", "forge", "neoforge")
}

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
	group = "project"
	ofTask("buildAndCollect")
}

stonecutter registerChiseled tasks.register("chiseledPublish", stonecutter.chiseled) {
	group = "project"
	ofTask("publish")
}

for (it in stonecutter.tree.branches) {
	if (it.id.isEmpty()) continue
	val loader = it.id.upperCaseFirst()

	// Builds loader-specific versions into `build/libs/{mod.version}/{loader}`
	stonecutter registerChiseled tasks.register("chiseledBuild$loader", stonecutter.chiseled) {
		group = "project"
		versions { branch, _ -> branch == it.id }
		ofTask("buildAndCollect")
	}

	// Publishes loader-specific versions
	stonecutter registerChiseled tasks.register("chiseledPublish$loader", stonecutter.chiseled) {
		group = "project"
		versions { branch, _ -> branch == it.id }
		ofTask("publish")
	}
}

// Runs active versions for each loader
for (it in stonecutter.tree.nodes) {
	if (it.metadata != stonecutter.current || it.branch.id.isEmpty()) continue
	val types = listOf("Client", "Server")
	val loader = it.branch.id.uppercaseFirstChar()
	for (type in types) it.tasks.register("runActive$type$loader") {
		group = "project"
		dependsOn("run$type")
	}
}

subprojects {
	apply(plugin = "maven-publish")
	repositories {
		mavenCentral()
		// mappings
		maven("https://maven.parchmentmc.org")
		maven("https://maven.quiltmc.org/repository/release")
		// our repo
		maven("https://maven.realrobotix.me/master/")

		//maven("https://maven.shedaniel.me/")
		maven("https://maven.blamejared.com/")
		maven("https://maven.tterrag.com/")
		maven("https://maven.createmod.net/")
		exclusiveContent {
			forRepository {
				maven {
					name = "Modrinth"
					url = uri("https://api.modrinth.com/maven")
				}
			}
			filter {
				includeGroup("maven.modrinth")
			}
		}
		exclusiveContent {
			forRepository {
				maven {
					name = "CurseMaven"
					url = uri("https://cursemaven.com")
				}
			}
			filter {
				includeGroup("curse.maven")
			}
		}
		flatDir{
			dir("libs")
		}
	}
	publishing {
		repositories {
			maven {
				name = "GitHubPackages"
				url = uri("https://maven.pkg.github.com/cannoneers-of-create/createbigcannons")
				credentials {
					username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
					password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
				}
			}
			maven {
				name = "realRobotixMaven"
				url = uri("https://maven.realrobotix.me/createbigcannons")
				credentials(PasswordCredentials::class)
			}
			mavenLocal()
		}
	}
}
