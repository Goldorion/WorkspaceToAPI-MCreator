package net.goldorion.workspace2api;

import net.goldorion.workspace2api.utils.YamlWriter;
import net.mcreator.element.ModElementType;
import net.mcreator.ui.action.ActionRegistry;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.init.L10N;
import net.mcreator.workspace.Workspace;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class GeneratePluginAction extends BasicAction {
    private static final String[] SUPPORTED_GENERATORS = {"datapack-1.19.4", "datapack-1.20.1", "forge-1.19.4", "forge-1.20,1", "forge-1.20.4"};
    private static final File FOLDER = new File(FileUtils.getTempDirectory(), "workspace2plugin" + new Random().nextInt(1000000) + "/");

    public GeneratePluginAction(ActionRegistry actionRegistry) {
        super(actionRegistry, L10N.t("action.workspace2api.generate_plugin"), e -> {
            Workspace workspace = actionRegistry.getMCreator().getWorkspace();
            String modID = workspace.getWorkspaceSettings().getModID();
            String modName = workspace.getWorkspaceSettings().getModName().replace(" ", "") + "/";
            String javaModName = workspace.getWorkspaceSettings().getJavaModName();
            try {
                FOLDER.mkdirs();
                FOLDER.createNewFile();
                System.out.println(FOLDER.getPath());
            } catch (IOException ex) {
                Workspace2API.LOG.error("Error while creating the temp directory. Stopping the generation of the plugin.", ex);
                return;
            }

            generateJSONElement(workspace, ModElementType.ADVANCEMENT, "advancements", modName, modID);
            generateJSONElement(workspace, ModElementType.BIOME, "biomes", modName, modID);
            generateJavaElement(workspace, ModElementType.POTIONEFFECT, "Effects", "effects", modName, javaModName);
            generateJSONElement(workspace, ModElementType.DAMAGETYPE, "damagesources", modName, modID);
            generateJavaElement(workspace, ModElementType.ENCHANTMENT, "Enchantments", "enchantments", modName, javaModName);
            generateJavaElement(workspace, ModElementType.POTION, "Potions", "potions", modName, javaModName);

        });
    }

    private static void generateJSONElement(Workspace workspace, ModElementType<?> met, String fileName, String modName, String modID) {
        List<String> dataList = new ArrayList<>();
        Map<String, String> mappings = new HashMap<>();

        workspace.getModElements().forEach(me -> {
            if (me.getType() == met) {
                dataList.add(modName + me.getName());
                mappings.put(modName + me.getName(), modID + ":" + me.getRegistryName());
            }
        });
        if (!dataList.isEmpty()) {
            YamlWriter.writeObject(dataList, new File(FOLDER, "datalists/" + fileName + ".yaml"));
            Arrays.stream(SUPPORTED_GENERATORS).filter(name -> name.startsWith("datapack")).forEach(name ->
                    YamlWriter.writeObject(mappings, new File(FOLDER, name + "/mappings/" + fileName + ".yaml")));
        }
    }

    private static void generateJavaElement(Workspace workspace, ModElementType<?> met, String className, String fileName, String modName, String javaModName) {
        List<String> dataList = new ArrayList<>();
        Map<String, String> mappings = new HashMap<>();

        workspace.getModElements().forEach(me -> {
            if (me.getType() == met) {
                dataList.add(modName + me.getName());
                mappings.put(modName + me.getName(), javaModName + className + "." + me.getRegistryName().toUpperCase(Locale.ENGLISH) + ".get()");
            }
        });
        if (!dataList.isEmpty()) {
            YamlWriter.writeObject(dataList, new File(FOLDER, "datalists/" + fileName + ".yaml"));
            Arrays.stream(SUPPORTED_GENERATORS).filter(name -> !name.startsWith("datapack")).forEach(gen -> YamlWriter.writeObject(mappings, new File(FOLDER, gen + "/mappings/" + fileName + ".yaml")));
        }
    }
}