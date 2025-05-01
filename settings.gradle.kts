pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/")
		maven("https://maven.architectury.dev/")
		maven("https://maven.minecraftforge.net/")
		maven("https://maven.quiltmc.org/repository/release")
		maven("https://maven.kikugie.dev/snapshots")
		gradlePluginPortal()
	}
}

plugins {
	id("dev.kikugie.stonecutter") version "0.7-alpha.10"
}

stonecutter {
	centralScript = "build.gradle.kts"
	kotlinController = true
	create(rootProject) {
		// Root `src/` functions as the 'common' project
		versions("1.20.1")
		branch("fabric") // Copies versions from root
		branch("forge") { versions("1.20.1") }
		//branch("neoforge") { versions("1.21.1") }
	}
}

rootProject.name = "CreateBigCannons"
