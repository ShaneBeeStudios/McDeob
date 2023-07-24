package com.shanebeestudios.mcdeop.processor;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProcessorOptions {
    @Builder.Default
    private boolean remap = true;

    @Builder.Default
    private boolean decompile = true;

    @Builder.Default
    private boolean zipDecompileOutput = true;
}
