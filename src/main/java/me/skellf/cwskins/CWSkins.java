package me.skellf.cwskins;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.skellf.cwskins.commands.CommandDispatcher;
import me.skellf.cwskins.commands.tabcomplete.SkinTabCompleter;
import me.skellf.cwskins.listeners.ApplySkinListener;
import me.skellf.cwskins.listeners.DamageListener;
import me.skellf.cwskins.listeners.PreventSkinUse;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class CWSkins extends SimplePlugin {

    private Map<String, String> messages;
    private Gson gson;
    private static final Logger log = Logger.getLogger("CWSkins");

    @Override
    public void onPluginStart() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();

        File skinsFolder = new File(getDataFolder(), "skins");
        if (!skinsFolder.exists()){
            skinsFolder.mkdirs();
            log.info("Folder with skins is created!");
        }

        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
        this.reloadConfig();
        this.createMessagesFile();
        this.loadMessages();
        log.info("Developer: " + getDescription().getAuthors() + ", version: " + getDescription().getVersion());
        this.getServer().getPluginManager().registerEvents(new ApplySkinListener(), this);
        this.getServer().getPluginManager().registerEvents(new DamageListener(), this);
        this.getServer().getPluginManager().registerEvents(new PreventSkinUse(), this);
        this.getCommand("cwskins").setExecutor(new CommandDispatcher(this));
        this.getCommand("cwskins").setTabCompleter(new SkinTabCompleter());
    }

    @Override
    public void onPluginStop() {
        saveSkins();
        log.info("Saving skins...");
    }

    private void createMessagesFile() {
        File file = new File(getDataFolder(), "messages.json");
        try {
            Map<String, String> oldMessages = new HashMap<>();
            if (file.exists()) {
                try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                    Type type = new TypeToken<Map<String, String>>() {}.getType();
                    oldMessages = gson.fromJson(reader, type);
                }
            }

            try (InputStream in = getResource("messages.json");
                 FileOutputStream out = new FileOutputStream(file)) {
                if (in == null) {
                    log.severe("messages.json not found in plugin's file.");
                    return;
                }
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }

            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                Type type = new TypeToken<Map<String, String>>() {}.getType();
                messages = gson.fromJson(reader, type);
                messages.putAll(oldMessages);
            }

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                gson.toJson(messages, writer);
            }
        } catch (IOException e) {
            log.severe("Failed to create or rewrite messages.");
            e.printStackTrace();
        }
    }

    public void loadMessages() {
        File file = new File(getDataFolder(), "messages.json");
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            messages = gson.fromJson(reader, type);
            getLogger().info("Messages are successfully loaded!");
        } catch (Exception e) {
            getLogger().severe("Failed to load messages.json");
            e.printStackTrace();
        }
    }

    public String getMessage(String key, Object... args){
        String message = messages.getOrDefault(key, "Message not found");
        return String.format(message, args);
    }

    private void saveSkins(){
        File skinsFolder = new File(getDataFolder(), "skins");
        File[] skinFiles = skinsFolder.listFiles();
        if (skinFiles == null){
            log.warning("There are no skins in the skins folder!");
            return;
        }

        for (File skinFile : skinFiles){
            try {
                CustomSkin skin = CustomSkin.fromFile(skinFile);
                if (skin != null) {
                    skin.writeToFile(skinFile);
                }
            } catch (IOException e){
                log.warning("An error occurred when saving skin: " + skinFile.getPath());
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the file of skin
     * @param skinName the name of skin
     * @return Skin file
     */
    public static File getSkinFile(String skinName){
        return new File(CWSkins.getInstance().getDataFolder() + "/skins/" + skinName + ".yml");
    }

    public static CWSkins getInstance(){
        return (CWSkins) SimplePlugin.getInstance();
    }
}
