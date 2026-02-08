package dev.xpple.seedmapper.buildscript;

import org.apache.tools.ant.taskdefs.condition.Os;
import org.gradle.api.tasks.Exec;

public abstract class CreateJavaBindingsTask extends Exec {

    private static final String EXTENSION = Os.isFamily(Os.FAMILY_WINDOWS) ? ".bat" : "";

    {
        // always run task
        this.getOutputs().upToDateWhen(_ -> false);

        this.setWorkingDir(this.getProject().getRootDir());
        this.setStandardOutput(System.out);
        this.environment("LIBCLANG_PATH", this.getProject().file("jextract/build/jextract/runtime/lib").getAbsolutePath());
        // generate wrapper header for `Generator` to de-anonymise inner struct
        /*
        this.commandLine("rm src/main/c/cubiomes/generator_wrapper.h");
        this.commandLine("echo \"#include \"generator.h\"\n" + //
                                "STRUCT(Generator)\n" + //
                                "{\n" + //
                                "    int mc;\n" + //
                                "    int dim;\n" + //
                                "    uint32_t flags;\n" + //
                                "    uint64_t seed;\n" + //
                                "    uint64_t sha;\n" + //
                                "\n" + //
                                "    union OverworldGenerator {\n" + //
                                "        struct LegacyLayersGenerator {\n" + //
                                "            LayerStack ls;\n" + //
                                "            Layer xlayer[5];\n" + //
                                "            Layer *entry;\n" + //
                                "        } lg;\n" + //
                                "        struct ModernNoiseGenerator {\n" + //
                                "            BiomeNoise bn;\n" + //
                                "        } ng;\n" + //
                                "        struct BetaGenerator {\n" + //
                                "            BiomeNoiseBeta bnb;\n" + //
                                "        } bg;\n" + //
                                "    } og;\n" + //
                                "    NetherNoise nn;\n" + //
                                "    EndNoise en;\n" + //
                                "};\" > src/main/c/cubiomes/generator_wrapper.h");
        */
        this.commandLine("./jextract/build/jextract/bin/jextract" + EXTENSION, "-DGENERATOR_H_", "--include-dir", "src/main/c/cubiomes", "--output", "src/main/java", "--use-system-load-library", "--target-package", "com.github.cubiomes", "--header-class-name", "Cubiomes", "@src/main/c/cubiomes/includes.txt", "biomenoise.h", "biomes.h", "finders.h", "generator_wrapper.h", "layers.h", "biomenoise.h", "biomes.h", "noise.h", "terrainnoise.h", "rng.h", "util.h", "quadbase.h", "xrms.h", "loot/items.h", "loot/logging.h", "loot/loot_functions.h", "loot/loot_table_context.h", "loot/loot_table_parser.h", "loot/loot_tables.h", "loot/mc_loot.h");
    }
}
