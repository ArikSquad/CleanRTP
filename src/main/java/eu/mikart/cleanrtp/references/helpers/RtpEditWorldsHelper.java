package eu.mikart.cleanrtp.references.helpers;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.types.CmdEdit;
import eu.mikart.cleanrtp.references.file.FileOther;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import eu.mikart.cleanrtp.references.messages.MessagesCore;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RtpEditWorldsHelper {

    public static void editCustomWorld(CommandSender sendi, CmdEdit.RTP_CMD_EDIT_SUB cmd, String world, String val) {
        String path = "CustomWorlds";
        if (editSingleMap(sendi, cmd, world, val, path, FileOther.Filetype.CONFIG))
            BetterRTP.getInstance().getRTP().loadWorlds();
    }

    public static void editLocation(CommandSender sendi, CmdEdit.RTP_CMD_EDIT_SUB cmd, String location, String val) {
        String path = "Locations";
        if (editSingleMap(sendi, cmd, location, val, path, FileOther.Filetype.LOCATIONS))
            BetterRTP.getInstance().getRTP().loadLocations();
    }

    private static boolean editSingleMap(CommandSender sendi, CmdEdit.RTP_CMD_EDIT_SUB cmd, String field, String val, String path, FileOther.Filetype file) {
        Object value;
        try {
            value = cmd.getResult(val);
        } catch (Exception e) {
            e.printStackTrace();
            MessagesCore.EDIT_ERROR.send(sendi);
            return false;
        }

        if (value == null) {
            MessagesCore.EDIT_ERROR.send(sendi);
            return false;
        }
        YamlConfiguration config = file.getConfig();

        List<Map<?, ?>> map = config.getMapList(path);
        boolean found = false;
        for (Map<?, ?> m : map) {
            if (m.keySet().toArray()[0].equals(field)) {
                found = true;
                for (Object map2 : m.values()) {
                    Map<Object, Object> values = (Map<Object, Object>) map2;
                    values.put(cmd.get(), value);
                    RtpMessage.sms(sendi,
                            MessagesCore.EDIT_SET.get(sendi, null)
                                    .replace("%type%", cmd.get())
                                    .replace("%value%", val));
                }
                break;
            }
        }
        if (!found) {
            Map<Object, Object> map2 = new HashMap<>();
            Map<Object, Object> values = new HashMap<>();
            values.put(cmd.get(), value);
            map2.put(field, values);
            map.add(map2);
        }

        config.set(path, map);

        try {
            config.save(file.getFile());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void editPermissionGroup(CommandSender sendi, CmdEdit.RTP_CMD_EDIT_SUB cmd, String group, String world, String val) {
        Object value;
        try {
            value = cmd.getResult(val);
        } catch (Exception e) {
            e.printStackTrace();
            MessagesCore.EDIT_ERROR.send(sendi);
            return;
        }

        if (value == null) {
            MessagesCore.EDIT_ERROR.send(sendi);
            return;
        }

        String path = "PermissionGroup.Groups";
        FileOther.Filetype file = FileOther.Filetype.CONFIG;
        YamlConfiguration config = file.getConfig();

        List<Map<?, ?>> map = config.getMapList(path);
        for (Map<?, ?> m : map)
            for (Map.Entry<?, ?> entry : m.entrySet()) {
                String _group = entry.getKey().toString();
                if (_group.equals(group)) {
                    BetterRTP.getInstance().getLogger().info("Group: " + group);
                    Object _value = entry.getValue();
                    for (Object worldList : ((ArrayList) _value)) {
                        BetterRTP.getInstance().getLogger().info("World: " + worldList.toString());
                        for (Object hash : ((HashMap) worldList).entrySet()) {
                            Map.Entry worldFields = (Map.Entry) hash;
                            BetterRTP.getInstance().getLogger().info("Hash: " + hash);
                            if (world.equals(worldFields.getKey().toString())) {
                                Map<Object, Object> values = (Map<Object, Object>) worldFields.getValue();
                                values.put(cmd.get(), value);
                                RtpMessage.sms(sendi,
                                        MessagesCore.EDIT_SET.get(sendi, null)
                                                .replace("%type%", cmd.get())
                                                .replace("%value%", val));
                            }
                        }
                    }
                }
            }
        /*if (!found) {
            Map<Object, Object> map2 = new HashMap<>();
            Map<Object, Object> values = new HashMap<>();
            values.put(cmd.get(), value);
            map2.put(world, values);
            map.add(map2);
        }*/
        //if (!found)
        //    return;

        config.set(path, map);

        try {
            config.save(file.getFile());
            BetterRTP.getInstance().getRTP().loadPermissionGroups();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editDefault(CommandSender sendi, CmdEdit.RTP_CMD_EDIT_SUB cmd, String val) {
        Object value;
        try {
            value = cmd.getResult(val);
        } catch (Exception e) {
            e.printStackTrace();
            MessagesCore.EDIT_ERROR.send(sendi);
            return;
        }

        if (value == null) {
            MessagesCore.EDIT_ERROR.send(sendi);
            return;
        }

        FileOther.Filetype file = FileOther.Filetype.CONFIG;
        YamlConfiguration config = file.getConfig();

        config.set("Default." + cmd.get(), value);

        try {
            config.save(file.getFile());
            BetterRTP.getInstance().getRTP().loadWorlds();
            RtpMessage.sms(sendi,
                    MessagesCore.EDIT_SET.get(sendi, null)
                            .replace("%type%", cmd.get())
                            .replace("%value%", val));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editWorldtype(CommandSender sendi, String world, String val) {
        //sendi.sendMessage("Editting worldtype for world " + world + " to " + val);
        WorldType type;
        try {
            type = WorldType.valueOf(val.toUpperCase());
        } catch (Exception e) {
            //e.printStackTrace();
            MessagesCore.EDIT_ERROR.send(sendi);
            return;
        }

        FileOther.Filetype file = FileOther.Filetype.CONFIG;
        YamlConfiguration config = file.getConfig();

        List<Map<?, ?>> world_map = config.getMapList("WorldType");
        List<Map<?, ?>> removeList = new ArrayList<>();
        for (Map<?, ?> m : world_map) {
            for (Map.Entry<?, ?> entry : m.entrySet()) {
                if (entry.getKey().equals(world))
                    removeList.add(m);
            }
        }
        for (Map<?, ?> o : removeList)
            world_map.remove(o);
        Map<String, String> newIndex = new HashMap<>();
        newIndex.put(world, type.name());
        world_map.add(newIndex);
        config.set("WorldType", world_map);

        try {
            config.save(file.getFile());
            BetterRTP.getInstance().getRTP().load();
            RtpMessage.sms(sendi,
                    MessagesCore.EDIT_SET.get(sendi, null)
                            .replace("%type%", CmdEdit.RTP_CMD_EDIT.WorldType.name())
                            .replace("%value%", val));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editOverride(CommandSender sendi, String world, String val) {

        FileOther.Filetype file = FileOther.Filetype.CONFIG;
        YamlConfiguration config = file.getConfig();

        List<Map<?, ?>> world_map = config.getMapList("Overrides");
        List<Map<?, ?>> removeList = new ArrayList<>();
        for (Map<?, ?> m : world_map) {
            for (Map.Entry<?, ?> entry : m.entrySet()) {
                if (entry.getKey().equals(world))
                    removeList.add(m);
            }
        }
        for (Map<?, ?> o : removeList)
            world_map.remove(o);
        if (!val.equals("REMOVE_OVERRIDE")) {
            Map<String, String> newIndex = new HashMap<>();
            newIndex.put(world, val);
            world_map.add(newIndex);
        } else {
            val = "(removed override)";
        }
        config.set("Overrides", world_map);

        try {
            config.save(file.getFile());
            BetterRTP.getInstance().getRTP().load();
            RtpMessage.sms(sendi,
                    MessagesCore.EDIT_SET.get(sendi, null)
                            .replace("%type%", CmdEdit.RTP_CMD_EDIT.OVERRIDE.name())
                            .replace("%value%", val));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void editBlacklisted(CommandSender sendi, String block, boolean add) {

        FileOther.Filetype file = FileOther.Filetype.CONFIG;
        YamlConfiguration config = file.getConfig();

        List<String> world_map = config.getStringList("BlacklistedBlocks");
        List<String> removeList = new ArrayList<>();
        for (String m : world_map) {
            if (m.equals(block)) {
                removeList.add(m);
            }
        }
        for (String o : removeList)
            world_map.remove(o);
        if (add) {
            world_map.add(block);
        } else {
            block = "(removed " + block + ")";
        }
        config.set("BlacklistedBlocks", world_map);

        try {
            config.save(file.getFile());
            BetterRTP.getInstance().getRTP().load();
            RtpMessage.sms(sendi,
                    MessagesCore.EDIT_SET.get(sendi, null)
                            .replace("%type%", CmdEdit.RTP_CMD_EDIT.BLACKLISTEDBLOCKS.name())
                            .replace("%value%", block));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
