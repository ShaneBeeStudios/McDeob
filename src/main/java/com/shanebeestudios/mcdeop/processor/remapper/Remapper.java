package com.shanebeestudios.mcdeop.processor.remapper;

import java.nio.file.Path;

public interface Remapper {
    void remap(Path jarPath, Path mappingsPath, Path outputDir);
}
