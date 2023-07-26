package com.shanebeestudios.mcdeop.app.components;

import com.shanebeestudios.mcdeop.processor.ProcessorOptions;
import javax.swing.*;
import lombok.Getter;

@Getter
public class ProcessorOptionsGroup {
    private final JCheckBox remap;
    private final JCheckBox decompile;
    private final JCheckBox zipDecompileOutput;

    public ProcessorOptionsGroup() {
        this.remap = new JCheckBox("Remap");
        this.remap.setSelected(true);

        this.decompile = new JCheckBox("Decompile");
        this.decompile.setSelected(false);

        this.zipDecompileOutput = new JCheckBox("Zip decompiled code");
        this.zipDecompileOutput.setSelected(false);
    }

    public ProcessorOptions getProcessorOptions() {
        return ProcessorOptions.builder()
                .remap(this.remap.isSelected())
                .decompile(this.decompile.isSelected())
                .zipDecompileOutput(this.zipDecompileOutput.isSelected())
                .build();
    }
}
