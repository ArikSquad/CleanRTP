package eu.mikart.cleanrtp.references.messages.placeholder;

import eu.mikart.cleanrtp.player.commands.RTPCommand;
import eu.mikart.cleanrtp.references.PermissionCheck;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.RTPWorld;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlaceholderAnalyzer {

    public static List<ComponentLike> arguments(@Nullable CommandSender sender, @Nullable Object info) {
        List<ComponentLike> args = new ArrayList<>();
        if (sender instanceof Player player) {
            args.add(Argument.target(player));
            addMiniPlaceholders(args);
        }
        add(args, sender, info);
        return args;
    }

    public static TagResolver tagResolver(@Nullable CommandSender sender, @Nullable Object info, Object... namedValues) {
        TagResolver.Builder builder = TagResolver.builder();
        if (sender instanceof Player) addMiniPlaceholders(builder);
        addTags(builder, sender, info);
        for (int i = 0; i + 1 < namedValues.length; i += 2) {
            tag(builder, String.valueOf(namedValues[i]), String.valueOf(namedValues[i + 1]));
        }
        return builder.build();
    }

    private static void add(List<ComponentLike> args, @Nullable CommandSender sender, @Nullable Object info) {
        if (info instanceof Collection<?> collection) {
            collection.forEach(object -> add(args, sender, object));
        } else if (info instanceof String value) {
            addStringAliases(args, value);
        } else if (info instanceof Player player) {
            addPlayer(args, player);
        } else if (info instanceof Location location) {
            addLocation(args, location);
        } else if (info instanceof Integer integer) {
            addIntegerAliases(args, integer);
        } else if (info instanceof Biome biome) {
            args.add(Argument.component(Placeholders.BIOME.key(), Component.translatable(biome)));
        } else if (info instanceof WorldPlayer worldPlayer) {
            addWorldPlayer(args, worldPlayer);
        } else if (info instanceof World world) {
            addWorld(args, world);
        } else if (info instanceof RTPCommand command) {
            addPermission(args, command.permission());
        } else if (info instanceof PermissionNode permissionNode) {
            addPermission(args, permissionNode);
        } else if (info instanceof RTPWorld rtpWorld) {
            args.add(Argument.numeric(Placeholders.PRICE.key(), rtpWorld.getPrice()));
        }
    }

    private static void addWorldPlayer(List<ComponentLike> args, WorldPlayer worldPlayer) {
        args.add(Argument.numeric(Placeholders.PRICE.key(), worldPlayer.getPrice()));
        addWorld(args, worldPlayer.getWorld());
        args.add(Argument.string(Placeholders.PERMISSION.key(),
                PermissionCheck.getAWorldText(worldPlayer.getPlayer(), worldPlayer.getWorld().getName()).string()));
        addPlayer(args, worldPlayer.getPlayer());
    }

    private static void addLocation(List<ComponentLike> args, Location location) {
        args.add(Argument.numeric(Placeholders.LOCATION_X.key(), location.getBlockX()));
        args.add(Argument.numeric(Placeholders.LOCATION_Y.key(), location.getBlockY()));
        args.add(Argument.numeric(Placeholders.LOCATION_Z.key(), location.getBlockZ()));
        addWorld(args, location.getWorld());
    }

    private static void addWorld(List<ComponentLike> args, @Nullable World world) {
        if (world != null) {
            args.add(Argument.string(Placeholders.WORLD.key(), world.getName()));
        }
    }

    private static void addPlayer(List<ComponentLike> args, Player player) {
        args.add(Argument.string(Placeholders.PLAYER_NAME.key(), player.getName()));
    }

    private static void addPermission(List<ComponentLike> args, PermissionCheck permission) {
        args.add(Argument.string(Placeholders.PERMISSION.key(), permission.getNode()));
    }

    private static void addStringAliases(List<ComponentLike> args, String value) {
        args.add(Argument.string(Placeholders.COMMAND.key(), value));
        args.add(Argument.string(Placeholders.PLAYER_NAME.key(), value));
        args.add(Argument.string(Placeholders.WORLD.key(), value));
        args.add(Argument.string(Placeholders.COOLDOWN.key(), value));
        args.add(Argument.string(Placeholders.CURRENTDVERSION.key(), value));
        args.add(Argument.string(Placeholders.NEWVERSION.key(), value));
        args.add(Argument.string("type", value));
        args.add(Argument.string("value", value));
    }

    private static void addIntegerAliases(List<ComponentLike> args, int value) {
        args.add(Argument.numeric(Placeholders.ATTEMPTS.key(), value));
        args.add(Argument.numeric(Placeholders.PRICE.key(), value));
        args.add(Argument.numeric(Placeholders.DELAY.key(), value));
        args.add(Argument.numeric(Placeholders.TIME.key(), value));
    }

    private static void addMiniPlaceholders(List<ComponentLike> args) {
        try {
            args.add(Argument.tagResolver(io.github.miniplaceholders.api.MiniPlaceholders.audienceGlobalPlaceholders()));
        } catch (NoClassDefFoundError ignored) {
            // MiniPlaceholders is optional.
        }
    }

    private static void addMiniPlaceholders(TagResolver.Builder builder) {
        try {
            builder.resolver(io.github.miniplaceholders.api.MiniPlaceholders.audienceGlobalPlaceholders());
        } catch (NoClassDefFoundError ignored) {
            // MiniPlaceholders is optional.
        }
    }

    private static void addTags(TagResolver.Builder builder, @Nullable CommandSender sender, @Nullable Object info) {
        for (ComponentLike argument : arguments(sender, info)) {
            Object value = argument.asComponent();
            value = value instanceof Component component ? component : Component.text(String.valueOf(value));
        }
        if (info instanceof Collection<?> collection) {
            collection.forEach(object -> addTags(builder, sender, object));
        } else if (info instanceof String value) {
            tagStringAliases(builder, value);
        } else if (info instanceof Player player) {
            tag(builder, Placeholders.PLAYER_NAME.key(), player.getName());
        } else if (info instanceof Location location) {
            tag(builder, Placeholders.LOCATION_X.key(), location.getBlockX());
            tag(builder, Placeholders.LOCATION_Y.key(), location.getBlockY());
            tag(builder, Placeholders.LOCATION_Z.key(), location.getBlockZ());
            if (location.getWorld() != null) tag(builder, Placeholders.WORLD.key(), location.getWorld().getName());
        } else if (info instanceof Integer integer) {
            tagIntegerAliases(builder, integer);
        } else if (info instanceof Biome biome) {
            tag(builder, Placeholders.BIOME.key(), biome.name());
        } else if (info instanceof WorldPlayer worldPlayer) {
            tag(builder, Placeholders.PRICE.key(), worldPlayer.getPrice());
            tag(builder, Placeholders.WORLD.key(), worldPlayer.getWorld().getName());
            tag(builder, Placeholders.PERMISSION.key(),
                    PermissionCheck.getAWorldText(worldPlayer.getPlayer(), worldPlayer.getWorld().getName()).string());
            tag(builder, Placeholders.PLAYER_NAME.key(), worldPlayer.getPlayer().getName());
        } else if (info instanceof World world) {
            tag(builder, Placeholders.WORLD.key(), world.getName());
        } else if (info instanceof RTPCommand command) {
            tag(builder, Placeholders.PERMISSION.key(), command.permission().getNode());
        } else if (info instanceof PermissionNode permissionNode) {
            tag(builder, Placeholders.PERMISSION.key(), permissionNode.getNode());
        } else if (info instanceof RTPWorld rtpWorld) {
            tag(builder, Placeholders.PRICE.key(), rtpWorld.getPrice());
        }
    }

    private static void tagStringAliases(TagResolver.Builder builder, String value) {
        tag(builder, Placeholders.COMMAND.key(), value);
        tag(builder, Placeholders.PLAYER_NAME.key(), value);
        tag(builder, Placeholders.WORLD.key(), value);
        tag(builder, Placeholders.COOLDOWN.key(), value);
        tag(builder, Placeholders.CURRENTDVERSION.key(), value);
        tag(builder, Placeholders.NEWVERSION.key(), value);
    }

    private static void tagIntegerAliases(TagResolver.Builder builder, int value) {
        tag(builder, Placeholders.ATTEMPTS.key(), value);
        tag(builder, Placeholders.PRICE.key(), value);
        tag(builder, Placeholders.DELAY.key(), value);
        tag(builder, Placeholders.TIME.key(), value);
    }

    private static void tag(TagResolver.Builder builder, String name, Object value) {
        builder.tag(name, Tag.selfClosingInserting(Component.text(String.valueOf(value))));
    }
}
