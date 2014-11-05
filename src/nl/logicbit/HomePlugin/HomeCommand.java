package nl.logicbit.HomePlugin;

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.chat.ChatColor;
import org.granitemc.granite.api.chat.ChatComponentBuilder;
import org.granitemc.granite.api.command.Command;
import org.granitemc.granite.api.command.CommandInfo;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.world.GraniteWorld;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Matthijs Logemann
 * @version 1.0
 * @since 26-10-14
 */
public class HomeCommand {
    static HashMap<UUID, SerializableLocation> homes;
//static CfgFile config;
    private static File f;
    public static void checkConfig(){
        Granite.getLogger().info("Checking home file");
        f = new File("plugins/Home/homes.conf");
        if (f.exists() && !f.isDirectory()) {
            try {
                FileInputStream fin = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fin);
                homes = (HashMap<UUID,SerializableLocation>) ois.readObject();
                ois.close();

            }catch (EOFException e) {
                try {
                    homes = new HashMap<UUID, SerializableLocation>();
                    FileOutputStream fout = new FileOutputStream(f);
                    ObjectOutputStream oos = new ObjectOutputStream(fout);
                    oos.writeObject(homes);
                    oos.close();

                }catch (Exception e2){
                    // TODO something really bad happened! I cant write the file! :O

                    Granite.getLogger().error("[Homes] Oops, something went wrong! The \"homes.conf\" file is not writable!");

                    Granite.getLogger().error("[Home] Disabling plugin because the \"homes.conf\" file is not writable.");

                    HomePlugin.getPlugin().disable();
                }
            }catch(Exception e){
                Granite.getLogger().error("[Homes] Oops, something went wrong! The \"homes.conf\" file is not readable!");
                Granite.getLogger().error("[Home] Please remove the \"homes.conf\", because it is corrupt");
                e.printStackTrace();
                // TODO something really bad happened! I cant read the file! :O
            }
        }else{

            try {
                f.createNewFile();
                homes = new HashMap<UUID, SerializableLocation>();
                FileOutputStream fout = new FileOutputStream(f);
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(homes);
                oos.close();
            } catch (IOException e) {
                // TODO something really bad happened! I cant create the file! :O
                Granite.getLogger().error("[Homes] Oops, something went wrong! The \"homes.conf\" file is not writable!");

                Granite.getLogger().error("[Home] Disabling plugin because the \"homes.conf\" file is not writable.");

                HomePlugin.getPlugin().disable();
                e.printStackTrace();
            }

        }
    }
    @Command(name = "Home", info = "Teleports you to your home", aliases = {"h"})
    public void onCommandHome(CommandInfo info) {
        if (!(info.getCommandSender() instanceof Player)) {
            info.getCommandSender().sendMessage(new ChatComponentBuilder().color(ChatColor.RED).text("Only in-game players can use this command!").build());
            return;
        }

        Player player = (Player) info.getCommandSender();
//        info.getCommandSender().sendMessage(new ChatComponentBuilder().text("Couldn't tp you!").color(ChatColor.RED).build());

            try {

                //config.load(f);
                String path = "Homes." + player.getName() + ".Home";

//                Granite.getLogger().error(config.get(path));
//                int x = config.getInteger(path + ".x");
//                int y = config.getInteger(path + ".y");
//                int z = config.getInteger(path + ".z");
                //float pitch = (float) config.getDouble(path + ".pitch");
                //float yaw = (float) config.getInteger(path + ".yaw");
                SerializableLocation playerLoc = homes.get(player.getUniqueID());
                if (playerLoc == null){
                    info.getCommandSender().sendMessage(new ChatComponentBuilder().color(ChatColor.RED).text("You do not have a home set! Please set one using ").color(ChatColor.WHITE).text("/sethome").build());
                    return;
                }
                double x = playerLoc.getX();
                double y = playerLoc.getY();
                double z = playerLoc.getZ();

                float pitch = playerLoc.getPitch();
                float yaw = playerLoc.getYaw();
                Location home = new Location(player.getWorld(), x,y + 1,z, yaw, pitch);

                info.getCommandSender().sendMessage(new ChatComponentBuilder().color(ChatColor.GREEN).text("Teleporting...").build());

                player.setLocation(home);

        }catch (Exception e){
                player.sendMessage(new ChatComponentBuilder().color(ChatColor.RED).text("Something went wrong! Could not teleport you to your home.").build());
                Granite.getLogger().error("[Homes] Oops, something went wrong!");
                e.printStackTrace();
            }

    }

    @Command(name = "SetHome", info = "Sets your home", aliases = {})
    public void onCommandSetHome(CommandInfo info) {
        if (homes == null) homes = new HashMap<>();
        if (!(info.getCommandSender() instanceof Player)) {
            info.getCommandSender().sendMessage(new ChatComponentBuilder().color(ChatColor.RED).text("Only in-game players can use this command!").build());
            return;
        }

        Player player = (Player) info.getCommandSender();
        Location loc = player.getLocation();
//        if (!loc.getWorld().equals(Granite.get.getWorlds().get(0))) {
//            player.sendMessage("§7Home can only be set in main world.");
//            return;
//        }

//        String path = "Homes." + player.getName() + ".Home";
//        config.addDefault(path + ".x", (int)Math.round(loc.getX()));
//        config.addDefault(path + ".y", (int)Math.round(loc.getY()));
//        config.addDefault(path + ".z", (int)Math.round(loc.getZ()));
//        config.addDefault(path + ".yaw", loc.getYaw());
//        config.addDefault(path + ".pitch", loc.getPitch());
Location playerLoc = player.getLocation();
        homes.put(player.getUniqueID(), new SerializableLocation(playerLoc.getX(), playerLoc.getY(),playerLoc.getZ(),playerLoc.getYaw(),playerLoc.getPitch()));

        Granite.getLogger().debug(Granite.getPluginContainerByClass(HomePlugin.class).getDataDirectory());
        try {
            FileOutputStream fout = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(homes);
            oos.close();
        } catch (IOException e) {
            player.sendMessage(new ChatComponentBuilder().color(ChatColor.RED).text("Something went wrong! Could not set your home.").build());

            Granite.getLogger().error("[Homes] Oops, something went wrong! The \"homes.conf\" file is not writable!");

            Granite.getLogger().error("[Home] Disabling plugin because the \"homes.conf\" file is not writable.");

            HomePlugin.getPlugin().disable();
        }
//            try {
//                config.write();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

       // CfgObject obj = (CfgObject) CfgValue.read(getClass().getResourceAsStream("/config/homes.conf"));
        //assertEquals(CfgValue.read(obj.writeToString()), obj);
        //player.setSpawnPoint(loc, true);
        player.sendMessage(new ChatComponentBuilder().color(ChatColor.BLUE).text("Home set at " +
                (int)Math.round(loc.getX()) + ", " +
                (int)Math.round(loc.getY()) + ", " +
                (int)Math.round(loc.getZ())).build());
    }

    @Command(name = "spawn", info = "Teleports you back to spawn", aliases = {""})
    public void onSpawnCommand(CommandInfo info){
        if (info.getCommandSender() instanceof Player){
            Player p = (Player)info.getCommandSender();

            GraniteWorld world = (GraniteWorld) p.getLocation().getWorld();
//            Granite.getLogger().error(world.getDimension());
            Location spawn = p.getLocation().getWorld().getSpawnBlock().getLocation();
            Location loc = new Location(spawn.getWorld(), (double) spawn.getX() + 0.5D, (double) spawn.getY() + 1, (double) spawn.getZ() + 0.5D, spawn.getYaw() , spawn.getPitch());
            p.setLocation(loc);
        }
    }

}