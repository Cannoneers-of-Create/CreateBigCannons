import org.gradle.kotlin.dsl.support.uppercaseFirstChar

plugins {
	`maven-publish`
	id("dev.kikugie.stonecutter")
	id("dev.architectury.loom") version "1.9.+" apply false
	id("architectury-plugin") version "3.4.+" apply false
	id("com.gradleup.shadow") version "8.3.5" apply false
}
stonecutter active "1.20.1" /* [SC] DO NOT EDIT */

subprojects {
	apply(plugin = "maven-publish")
	repositories {
		mavenCentral()
		// mappings
		strictMaven("https://maven.parchmentmc.org", "org.parchmentmc.data")
		strictMaven("https://maven.quiltmc.org/repository/release", "org.quiltmc")
		// our repo
		strictMaven("https://maven.realrobotix.me/master/", "com.rbasamoyai", "com.copycatsplus")

		//maven("https://maven.shedaniel.me/")
		maven("https://maven.blamejared.com/")
		maven("https://maven.tterrag.com/")
		maven("https://maven.createmod.net/")
		strictMaven("https://api.modrinth.com/maven", "maven.modrinth")
		strictMaven("https://cursemaven.com", "curse.maven")
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
