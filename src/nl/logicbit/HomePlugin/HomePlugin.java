package nl.logicbit.HomePlugin;

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.plugin.OnEnable;
import org.granitemc.granite.api.plugin.Plugin;
import org.granitemc.granite.api.plugin.PluginContainer;

/**
 * @author Matthijs Logemann
 * @version 1.0
 * @since 26-10-14
 */

@Plugin(name = "Home", id = "homeplugin", version = "0.1")
public class HomePlugin {
    static PluginContainer plugin;

    public HomePlugin(){
        plugin = Granite.getPluginContainer(this);
        plugin.registerCommandHandler(new HomeCommand());
        //plugin.registerEventHandler(this);


    }

    @OnEnable
    public void onEnable(PluginContainer p){
        HomeCommand.checkConfig();
    }

    public static String formatInfo(String message){
        return message;
    }

    public static String formatError(String message){
        return message;
    }

    public static String formatAlert(String message){
        return message;
    }

    public static PluginContainer getPlugin(){
        return plugin;
    }

}
