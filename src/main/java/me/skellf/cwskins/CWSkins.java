package me.skellf.cwskins;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.skellf.cwskins.commands.CommandDispatcher;
import me.skellf.cwskins.commands.tabcomplete.SkinTabCompleter;
import me.skellf.cwskins.listeners.ApplySkinListener;
import me.skellf.cwskins.listeners.restrictions.DamageListener;
import me.skellf.cwskins.listeners.restrictions.PreventAnvilUse;
import me.skellf.cwskins.listeners.restrictions.PreventEnchantSkin;
import me.skellf.cwskins.listeners.restrictions.PreventSkinUse;
import me.skellf.cwskins.util.VersionChecker;
import org.bukkit.Bukkit;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class CWSkins extends SimplePlugin {

    private Map<String, String> messages;
    private static final String VERSION = "v3.3.2";
    private Gson gson;
    private static final Logger log = Logger.getLogger("CWSkins");

    @Override
    public void onPluginStart() {
        log.info("\n" + " _______           _______  _       _________ _        _______ \n" +
                "(  ____ \\|\\     /|(  ____ \\| \\    /\\\\__   __/( (    /|(  ____ \\\n" +
                "| (    \\/| )   ( || (    \\/|  \\  / /   ) (   |  \\  ( || (    \\/\n" +
                "| |      | | _ | || (_____ |  (_/ /    | |   |   \\ | || (_____ \n" +
                "| |      | |( )| |(_____  )|   _ (     | |   | (\\ \\) |(_____  )\n" +
                "| |      | || || |      ) ||  ( \\ \\    | |   | | \\   |      ) |\n" +
                "| (____/\\| () () |/\\____) ||  /  \\ \\___) (___| )  \\  |/\\____) |\n" +
                "(_______/(_______)\\_______)|_/    \\/\\_______/|/    )_)\\_______)" +
                "\n");

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();

        String mcVersion = Bukkit.getMinecraftVersion();

        String[] versionParts = mcVersion.split("\\.");

        String versionNumber = versionParts[0] + versionParts[1] + (versionParts.length > 2 ? versionParts[2] : "0");

        if (Integer.parseInt(versionNumber) < 116) {
            getLogger().severe("Running unsupported Minecraft version!");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        File skinsFolder = new File(getDataFolder(), "skins");
        if (!skinsFolder.exists()){
            skinsFolder.mkdirs();
            log.info("Folder with skins is created!");
        }

        log.info("Developer: skellf, version: " + VERSION);

        this.saveDefaultConfig();
        this.saveConfig();

        if (getConfig().getBoolean("check-for-updates")){
            checkForUpdates();
        }

        this.createMessagesFile();
        this.loadMessages();
        this.getServer().getPluginManager().registerEvents(new ApplySkinListener(), this);
        if (getConfig().getBoolean("restrictions.prevent-skin-from-using")) {
            this.getServer().getPluginManager().registerEvents(new PreventSkinUse(), this);
        }
        if (getConfig().getBoolean("restrictions.prevent-skin-from-enchanting")) {
            this.getServer().getPluginManager().registerEvents(new PreventEnchantSkin(), this);
        }
        if (getConfig().getBoolean("restrictions.prevent-skin-from-anvil")) {
            this.getServer().getPluginManager().registerEvents(new PreventAnvilUse(), this);
        }
        if (getConfig().getBoolean("restrictions.prevent-damage-from-skin")) {
            this.getServer().getPluginManager().registerEvents(new DamageListener(), this);
        }
        this.getCommand("cwskins").setExecutor(new CommandDispatcher());
        this.getCommand("cwskins").setTabCompleter(new SkinTabCompleter());
    }

    @Override
    public void onPluginStop() {
        saveSkins();
        log.info("Saving skins...");
        log.info("Goodbye!");
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

    private void checkForUpdates(){
        new Thread(() -> {
            try {
                VersionChecker.ReleaseInfo releaseInfo = VersionChecker.getLatestVersion("skellf", "CWSkins");
                String latestVersion = releaseInfo.getVersion();
                String downloadUrl = releaseInfo.getDownloadUrl();

                if (!VERSION.equals(latestVersion)) {
                    log.warning("A new version of the plugin is available: " + latestVersion + ". You are running version: " + VERSION);
                    log.warning("Get new version here: " + downloadUrl);
                } else {
                    log.info("You are running the latest version of the plugin.");
                }
            } catch (Exception e) {
                log.warning("Failed to check for updates: " + e.getLocalizedMessage());
            }
        }).start();
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

    public static List<CustomSkin> getAllSkins() {
        List<CustomSkin> skins = new ArrayList<>();
        File skinsFolder = new File(CWSkins.getInstance().getDataFolder(), "skins");
        if (skinsFolder.exists() && skinsFolder.isDirectory()) {
            for (File skinFile : skinsFolder.listFiles()) {
                try {
                    CustomSkin skin = CustomSkin.fromFile(skinFile);
                    if (skin != null) {
                        skins.add(skin);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return skins;
    }
}
