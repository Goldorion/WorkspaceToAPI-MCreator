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
    private static final String[] SUPPORTED_GENERATORS = { "forge-1.19.4", "forge-1.20,1", "forge-1.20.4" };

    public GeneratePluginAction(ActionRegistry actionRegistry) {
        super(actionRegistry, L10N.t("action.workspace2api.generate_plugin"), e -> {
            Workspace workspace = actionRegistry.getMCreator().getWorkspace();
            String modID = workspace.getWorkspaceSettings().getModID();
            String modName = workspace.getWorkspaceSettings().getModName().replace(" ", "") + "/";
            String javaModName = workspace.getWorkspaceSettings().getJavaModName();
            File destFolder = new File(FileUtils.getTempDirectory(), "workspace2plugin" + new Random().nextInt(1000000) + "/");
            try {
                destFolder.mkdirs();
                destFolder.createNewFile();
                System.out.println(destFolder.getPath());
            } catch (IOException ex) {
                Workspace2API.LOG.error("Error while creating the temp directory. Stopping the generation of the plugin.", ex);
                return;
            }

            // Generate advancement entries
            List<String> advancementsDL = new ArrayList<>();
            Map<String, String> advancementsMappings = new HashMap<>();

            workspace.getModElements().forEach(me -> {
                if (me.getType() == ModElementType.ADVANCEMENT) {
                    advancementsDL.add(modName + me.getName());
                    advancementsMappings.put(modName + me.getName(),  modID + ":" + me.getRegistryName());
                }
            });
            if (!advancementsDL.isEmpty()) {
                YamlWriter.writeObject(advancementsDL, new File(destFolder, "datalists/advancements.yaml"));
                Arrays.stream(SUPPORTED_GENERATORS).forEach(gen ->  YamlWriter.writeObject(advancementsMappings, new File(destFolder, gen + "/mappings/advancements.yaml")));
            }

            // Generate biomes entries
            List<String> biomesDL = new ArrayList<>();
            Map<String, String> biomesMappings = new HashMap<>();

            workspace.getModElements().forEach(me -> {
                if (me.getType() == ModElementType.BIOME) {
                    biomesDL.add(modName + me.getName());
                    biomesMappings.put(modName + me.getName(),  modID + ":" + me.getRegistryName());
                }
            });
            if (!biomesDL.isEmpty()) {
                YamlWriter.writeObject(biomesDL, new File(destFolder, "datalists/biomes.yaml"));
                Arrays.stream(SUPPORTED_GENERATORS).forEach(gen ->  YamlWriter.writeObject(biomesMappings, new File(destFolder, gen + "/mappings/biomes.yaml")));
            }

            // Generate potion effects entries
            List<String> effectsDL = new ArrayList<>();
            Map<String, String> effectsMappings = new HashMap<>();

            workspace.getModElements().forEach(me -> {
                if (me.getType() == ModElementType.POTIONEFFECT) {
                    effectsDL.add(modName + me.getName());
                    effectsMappings.put(modName + me.getName(),  javaModName + "Effects." + me.getRegistryName().toUpperCase(Locale.ENGLISH) + ".get()");
                }
            });
            if (!effectsDL.isEmpty()) {
                YamlWriter.writeObject(effectsDL, new File(destFolder, "datalists/effects.yaml"));
                Arrays.stream(SUPPORTED_GENERATORS).forEach(gen ->  YamlWriter.writeObject(effectsMappings, new File(destFolder, gen + "/mappings/effects.yaml")));
            }

            // Generate damage sources entries
            List<String> damagesDL = new ArrayList<>();
            Map<String, String> damagesMappings = new HashMap<>();

            workspace.getModElements().forEach(me -> {
                if (me.getType() == ModElementType.DAMAGETYPE) {
                    damagesDL.add(modName + me.getName());
                    damagesMappings.put(modName + me.getName(),  modID + ":" + me.getRegistryName());
                }
            });
            if (!damagesDL.isEmpty()) {
                YamlWriter.writeObject(damagesDL, new File(destFolder, "datalists/damagesources.yaml"));
                Arrays.stream(SUPPORTED_GENERATORS).forEach(gen ->  YamlWriter.writeObject(damagesMappings, new File(destFolder, gen + "/mappings/damagesources.yaml")));
            }

            // Generate enchantments entries
            List<String> enchantmentsDL = new ArrayList<>();
            Map<String, String> enchantmentsMappings = new HashMap<>();

            workspace.getModElements().forEach(me -> {
                if (me.getType() == ModElementType.ENCHANTMENT) {
                    enchantmentsDL.add(modName + me.getName());
                    enchantmentsMappings.put(modName + me.getName(),  javaModName + "Enchantments." + me.getRegistryName().toUpperCase(Locale.ENGLISH) + ".get()");
                }
            });
            if (!enchantmentsDL.isEmpty()) {
                YamlWriter.writeObject(enchantmentsDL, new File(destFolder, "datalists/enchantments.yaml"));
                Arrays.stream(SUPPORTED_GENERATORS).forEach(gen ->  YamlWriter.writeObject(enchantmentsMappings, new File(destFolder, gen + "/mappings/enchantments.yaml")));
            }


        });
    }
}