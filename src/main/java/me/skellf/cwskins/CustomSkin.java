package me.skellf.cwskins;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSkin {
    private final String itemName;
    private final List<String> lore;
    private final Material material;
    private final int customModelData;
    public static final NamespacedKey CUSTOM_SKIN_KEY = new NamespacedKey(CWSkins.getInstance(), "custom-skin");
    public static final NamespacedKey APPLIED_SKIN_KEY = new NamespacedKey(CWSkins.getInstance(), "applied-skin");
    public static final NamespacedKey CLEAR_SKIN_KEY = new NamespacedKey(CWSkins.getInstance(), "clear-skin");

    public String getItemName() {
        return itemName;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    /**
     * The constructor
     * @param itemName skin's item name
     * @param lore skin's item lore
     * @param material skin's item material
     * @param customModelData skin's item model data
     */
    public CustomSkin(String itemName, List<String> lore, Material material, int customModelData) {
        this.itemName = itemName;
        this.lore = lore;
        this.material = material;
        this.customModelData = customModelData;
    }

    /**
     * The reader of skin's file
     * @param file skin's file
     * @return CustomSkin class
     * @throws IOException exception
     */
    public static CustomSkin fromFile(File file) throws IOException{
        Yaml yaml = new Yaml();
        try (FileReader reader = new FileReader(file)){
            Map<String, Object> yamlData = yaml.load(reader);
            if (yamlData != null) {
                String itemName = (String) yamlData.get("itemName");
                List<String> lore = (List<String>) yamlData.get("lore");
                Material material = Material.matchMaterial((String) yamlData.get("material"));
                int customModelData = (int) yamlData.get("customModelData");
                return new CustomSkin(itemName, lore, material, customModelData);
            }
        }

        return null;
    }

    /**
     * The writer to file
     * @param file skin's empty file
     * @throws IOException exception
     */
    public void writeToFile(File file) throws IOException{
        Yaml yaml = new Yaml();
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        yaml = new Yaml(options);
        try (FileWriter writer = new FileWriter(file)){
            yaml.dump(getDataMap(), writer);
        }
    }

    private Map<String, Object> getDataMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("itemName", itemName);
        data.put("lore", lore);
        data.put("material", material.name());
        data.put("customModelData", customModelData);
        return data;
    }
}
