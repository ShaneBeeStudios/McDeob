# McDeob

[![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/Timmi6790/McDeob?include_prereleases)](https://github.com/Timmi6790/McDeob/releases/latest)
[![GitHub issues](https://img.shields.io/github/issues/Timmi6790/McDeob)](https://github.com/Timmi6790/McDeob/issues)
![GitHub closed issues](https://img.shields.io/github/issues-closed/Timmi6790/McDeob)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/Timmi6790/McDeob)](https://github.com/Timmi6790/McDeob/pulls)
[![GitHub](https://img.shields.io/github/license/Timmi6790/McDeob)](https://github.com/Timmi6790/McDeob/blob/master/LICENSE)

**McDeob** is a re-mapping and deobfuscation tool for Minecraft server/client jars.   
This tool will download the appropriate Minecraft server/client jar along with the mappings for that version,
then it will proceed to remap the jar and optionally decompile it. Simply unzip that file to find all your
remapped/decompiled Minecraft code.

## Requirements

- Java 17+

## Fork Notice

I don't plan to PR this to the original repo, as I have adjusted the code for my personal needs.

### Fork Changes

* Add dynamic version fetching
* Add all versions that have the mappings available
* Add Remap and Zip options
* Restructure saving structure
* Replace Fernflower with Vineflower (Multithreaded support)
* Random bug fixes the original author missed

## Running

Details for installing and running **McDeob** can be found on the [**WIKI
**](https://github.com/ShaneBeeStudios/McDeob/wiki)

### Note:

The remapping process takes around 2 minutes (give or take). Whilst its remapping you will see the progress in the
app.   
The decompiling process takes about 3 minutes (give or take). Currently there is no progress in the app, so just wait
til the app says its completed.    
(These numbers are based on a test run on my Mac (MacbookPro with M1 chip), times may vary on other machines.)

### SUPER IMPORTANT NOTE

The files output by this app are for your own personal use only.   
The mappings used to decompile are provided by Mojang, but come with a hefty copyright from Microsoft.   
You can use these files personally however you see fit, but you are not permitted to distribute them, since they do
contain proprietery Minecraft code.    
That said, you may **NOT** upload the resulting files to something like GitHub.

## Preview

![](https://i.imgur.com/aXFtkaI.png)

## Compiling

If you wish to compile this yourself, simply clone the repo and run `mvn clean package`.   
Super Simple!!!

## Tools

This application uses 2 different tools for the processing of the jar files

1) [**Reconstruct by LXGaming**](https://github.com/LXGaming/Reconstruct) = This tool remaps the Minecraft jar using
Minecraft's mappings.
2) [**Vineflower**](https://github.com/Vineflower/vineflower) = This tool is used to decompile the jar file (class
files) into usable .java files.

## License

This tool shades in both [**Reconstruct by LXGaming**](https://github.com/LXGaming/Reconstruct) and
[**Vineflower**](https://github.com/Vineflower/vineflower), please see their repos for the appropriate licenses.
