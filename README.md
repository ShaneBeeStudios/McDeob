# McDeob
**McDeob** is a re-mapping and deobfuscation tool for Minecraft server/client jars.   
This tool will download the appropriate Minecraft server/client jar along with the mappings for that version, 
then it will proceed to remap the jar and optionally decompile it. The decompile output will be a jar file.
Simply unzip that jar file to find all your remapped/decompiled Minecraft code.

## Running
Here is how to run **McDeob**:
1) Download the jar from [**Releases**](https://github.com/ShaneBeeStudios/McDeob/releases) and place it wherever you want to run it. Like an applications folder, or your desktop.
2) Run the app.
3) Choose your options like server/client, Minecraft version and whether or not to decompile after remapping.
4) Click start. All files will be put into your user's home directory in a new directory called "McDeop"
5) Enjoy!
### Note:
The remapping process takes around 5 minutes (give or take). Whilst its remapping you will see the progress in the app.   
The decompiling process on the other hand takes about 12 minutes. Currently there is no progress in the app, so just wait til the app says its completed. 

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
