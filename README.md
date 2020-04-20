# McDeob
**McDeob** is a re-mapping and deobfuscation tool for Minecraft server/client jars.   
This tool will download the appropriate Minecraft server/client jar along with the mappings for that version, 
then it will proceed to remap the jar and optionally decompile it. The decompile output will be a jar file.
Simply unzip that jar file to find all your remapped/decompiled Minecraft code.

## Running
Here is how to run **McDeob**:
1) Download the jar from [**Releases**](https://github.com/ShaneBeeStudios/McDeob/releases) and place it into an empty directory
2) CD into that directory
3) Run the command line include the jar name, minecraft version and either server or client, ex:
```
java -jar McDeob-1.0.jar 1.15.2 server
```
The process will start with downloading the appropriate files. After downloads are finished you will be asked
to hit enter to begin the remapping process.  
Once remapping has finished, you will be given the option to decompile the jar into .java files.   
The resulting output will be a jar file, which you can simply unzip to expose the .java files.

## Compiling
If you wish to compile this yourself, simply clone the repo and run `mvn clean package`.   
Super Simple!!!

## Tools
This application uses 2 different tools for the processing of the jar files
1) [**Reconstruct by LXGaming**](https://github.com/LXGaming/Reconstruct) = This tool remaps the Minecraft jar using Minecraft's mappings. 
2) [**Fernflower**](https://github.com/fesh0r/fernflower) = This tool is used to decompile the jar file (class files) into usable .java files.

## License
This tool shades in both [**Reconstruct by LXGaming**](https://github.com/LXGaming/Reconstruct) and [**Fernflower**](https://github.com/fesh0r/fernflower),
please see their repos for the appropriate licenses. 
