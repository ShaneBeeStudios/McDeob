package com.shanebeestudios.mcdeop.processor.decompiler;

import java.nio.file.Path;

public interface Decompiler {
    void decompile(Path jarPath, Path outputDir);
}
