package com.shanebeestudios.mcdeop.processor.remapper;

import com.shanebeestudios.mcdeop.processor.Cleanup;
import com.shanebeestudios.mcdeop.processor.ReconConfig;
import io.github.lxgaming.reconstruct.common.Reconstruct;
import io.github.lxgaming.reconstruct.common.manager.TransformerManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Set;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReconstructRemapper implements Remapper, Cleanup {
    /**
     * {@link Reconstruct} breaks if you run it multiple times because all the transformers are cached with the first config every initialized.
     * This is a nasty hack to clear the static fields in {@link TransformerManager} so that we can run it multiple times.
     */
    @SneakyThrows
    private void fixReconstruct() {
        log.debug("Fix Reconstruct");
        final Class<TransformerManager> affectedClass = TransformerManager.class;
        final Field[] declaredFields = affectedClass.getDeclaredFields();
        for (final Field field : declaredFields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            field.setAccessible(true);
            final Object fieldObject = field.get(affectedClass);
            if (fieldObject instanceof Set) {
                log.debug("Clear Set field in Reconstruct: {}", field.getName());
                ((Set<?>) fieldObject).clear();
            }
        }
    }

    @Override
    public void cleanup() {
        this.fixReconstruct();
    }

    @Override
    public void remap(final Path jarPath, final Path mappingsPath, final Path outputDir) {
        final ReconConfig config = new ReconConfig();

        config.setInputPath(jarPath.toAbsolutePath());
        config.setMappingPath(mappingsPath.toAbsolutePath());
        config.setOutputPath(outputDir.toAbsolutePath());

        final Reconstruct reconstruct = new Reconstruct(config);
        reconstruct.load();
    }
}
