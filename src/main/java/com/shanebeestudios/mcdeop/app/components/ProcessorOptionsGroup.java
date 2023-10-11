package com.shanebeestudios.mcdeop.app.components;

import com.shanebeestudios.mcdeop.processor.ProcessorOptions;
import java.io.Serial;
import java.io.Serializable;
import javax.swing.*;
import lombok.Getter;

@Getter
public class ProcessorOptionsGroup implements Serializable {
    @Serial
    private static final long serialVersionUID = -2361182126374526544L;

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
