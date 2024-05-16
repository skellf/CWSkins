package me.skellf.cwskins;

import me.skellf.cwskins.commands.skin.ClearSkinCommand;
import me.skellf.cwskins.commands.skin.CreateSkinCommand;
import me.skellf.cwskins.commands.skin.GiveSkinCommand;
import me.skellf.cwskins.listeners.ApplySkinListener;
import me.skellf.cwskins.listeners.DamageListener;
import me.skellf.cwskins.listeners.PreventSkinUse;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class CWSkins extends JavaPlugin {

    private static CWSkins instance;
    private static final Logger log = Logger.getLogger("CWSkins");

    @Override
    public void onEnable() {
        instance = this;

        log.info("Разработчик: " + getDescription().getAuthors() + " версия: " + getDescription().getVersion());
        this.getServer().getPluginManager().registerEvents(new ApplySkinListener(), this);
        this.getServer().getPluginManager().registerEvents(new DamageListener(), this);
        this.getServer().getPluginManager().registerEvents(new PreventSkinUse(), this);
        this.getCommand("clearskin").setExecutor(new ClearSkinCommand());
        this.getCommand("giveskin").setExecutor(new GiveSkinCommand());
        this.getCommand("createskin").setExecutor(new CreateSkinCommand());
        File skinsFolder = new File(getDataFolder(), "skins");
        if (!skinsFolder.exists()){
            skinsFolder.mkdirs();
            log.info("Папка со скинами создана!");
        }
    }

    @Override
    public void onDisable() {
        saveSkins();
        log.info("Сохраняем скины...");
    }

    private void saveSkins(){
        File skinsFolder = new File(getDataFolder(), "skins");
        File[] skinFiles = skinsFolder.listFiles();
        if (skinFiles == null){
            log.warning("В папке skins нету скинов!");
            return;
        }

        for (File skinFile : skinFiles){
            try {
                CustomSkin skin = CustomSkin.fromFile(skinFile);
                if (skin != null) {
                    skin.writeToFile(skinFile);
                }
            } catch (IOException e){
                log.warning("Произошла ошибка при сохранении скина: " + skinFile.getPath());
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the file of skin
     * @param skinName the name of skin
     * @return {@code new File}
     */
    public static File getSkinFile(String skinName){
        return new File(CWSkins.getInstance().getDataFolder() + "/skins/" + skinName + ".yml");
    }

    public static CWSkins getInstance(){
        return instance;
    }
}
