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
then it will proceed to remap the jar and optionally decompile it. The decompile output will be a jar file.
Simply unzip that jar file to find all your remapped/decompiled Minecraft code.

## Running
Details for installing and running **McDeob** can be found on the [**WIKI**](https://github.com/ShaneBeeStudios/McDeob/wiki)

### Note:
The remapping process takes around 5 minutes (give or take). Whilst its remapping you will see the progress in the app.   
The decompiling process on the other hand takes about 12 minutes. Currently there is no progress in the app, so just wait til the app says its completed. 

### SUPER IMPORTANT NOTE
The files output by this app are for your own personal use only.   
The mappings used to decompile are provided by Mojang, but come with a hefty copyright from Microsoft.   
You can use these files personally however you see fit, but you are not permitted to distribute them, since they do contain proprietery Minecraft code.    
That said, you may **NOT** upload the resulting files to something like GitHub.

## Preview
![](https://i.imgur.com/eOSuIGr.png)

## Compiling
If you wish to compile this yourself, simply clone the repo and run `mvn clean package`.   
Super Simple!!!

## Tools
This application uses 2 different tools for the processing of the jar files
1) [**Reconstruct by LXGaming**](https://github.com/LXGaming/Reconstruct) = This tool remaps the Minecraft jar using Minecraft's mappings. 
2) [**Fernflower**](https://github.com/fesh0r/fernflower) = This tool is used to decompile the jar file (class files) into usable .java files.

## License
This tool shades in both [**Reconstruct by LXGaming**](https://github.com/LXGaming/Reconstruct) (we are using a modified fork of which you can find [**HERE**](https://github.com/ShaneBeeStudios/Reconstruct)) and [**Fernflower**](https://github.com/fesh0r/fernflower),
please see their repos for the appropriate licenses. 
