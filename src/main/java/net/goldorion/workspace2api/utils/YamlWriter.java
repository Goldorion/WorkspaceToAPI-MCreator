package net.goldorion.workspace2api.utils;

import net.mcreator.io.FileIO;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.common.FlowStyle;
import org.snakeyaml.engine.v2.schema.FailsafeSchema;

import java.io.File;

public class YamlWriter {

    public static void writeObject(Object object, File destination) {
        Dump dump = new Dump(DumpSettings.builder().setSchema(new FailsafeSchema()).setDefaultFlowStyle(FlowStyle.BLOCK).setMultiLineFlow(true).build());
        FileIO.writeStringToFile(dump.dumpToString(object), destination);
    }
}
