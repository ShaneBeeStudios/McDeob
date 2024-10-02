# McDeob

[![Java CI with Maven](https://github.com/ShaneBeeStudios/McDeob/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/ShaneBeeStudios/McDeob/actions)
[![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/ShaneBeeStudios/McDeob?include_prereleases)](https://github.com/ShaneBeeStudios/McDeob/releases/latest)
[![GitHub All Releases](https://img.shields.io/github/downloads/ShaneBeeStudios/McDeob/total)](https://github.com/ShaneBeeStudios/McDeob/releases)
[![GitHub issues](https://img.shields.io/github/issues/ShaneBeeStudios/McDeob)](https://github.com/ShaneBeeStudios/McDeob/issues)
![GitHub closed issues](https://img.shields.io/github/issues-closed/ShaneBeeStudios/McDeob)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/ShaneBeeStudios/McDeob)](https://github.com/ShaneBeeStudios/McDeob/pulls)
[![GitHub](https://img.shields.io/github/license/ShaneBeeStudios/McDeob)](https://github.com/ShaneBeeStudios/McDeob/blob/master/LICENSE)

**McDeob** is a re-mapping and deobfuscation tool for Minecraft server/client jars.   
This tool will download the appropriate Minecraft server/client jar along with the mappings for that version, 
then it will proceed to remap the jar and optionally decompile it. Simply unzip that file to find all your remapped/decompiled Minecraft code.

## Requirements
- A computer
- Java 16+
- 3GB of RAM (recommended minimum)
- A few minutes of your time

## Running
Details for installing and running **McDeob** can be found on the [**WIKI**](https://github.com/ShaneBeeStudios/McDeob/wiki)

### Note:
The remapping process takes around 2 minutes (give or take). Currently there is no progress in the app, so just wait til the app says it's completed.   
The decompiling process takes about 30 seconds (give or take). Currently there is no progress in the app, so just wait til the app says it's completed.    
(These numbers are based on a test run on my Mac (MacbookPro with M1 chip), times may vary on other machines.)

### SUPER IMPORTANT NOTE
The files output by this app are for your own personal use only.   
The mappings used to decompile are provided by Mojang, but come with a hefty copyright from Microsoft.   
You can use these files personally however you see fit, but you are not permitted to distribute them, since they do contain proprietery Minecraft code.    
That said, you may **NOT** upload the resulting files to something like GitHub.

## Preview
<img width="612" alt="Screenshot 2024-09-29 at 2 16 19 PM" src="https://github.com/user-attachments/assets/46bc9dc6-0ef8-43ec-983b-024c434ddf6d">


## Building/Compiling
If you wish to compile the jar yourself, simply clone the repo and run:    
`mvn clean package`       
If you want the macOS APP and DMG file run:    
`mvn clean package -P macos`     
Super Simple!!!

## Tools
This application uses 2 different tools for the processing of the jar files
1) [**SpecialSource**](https://github.com/md-5/SpecialSource) by md-5 = This tool remaps the Minecraft jar using Mojang's mappings. 
2) [**FernFlower**](https://github.com/PaperMC/patched-spigot-fernflower) = This tool is used to decompile the jar file (class files) into usable .java files.

## License
This tool shades in both [**SpecialSource**](https://github.com/md-5/SpecialSource) and [**FernFlower**](https://github.com/PaperMC/patched-spigot-fernflower),
please see their repos for the appropriate licenses. 
