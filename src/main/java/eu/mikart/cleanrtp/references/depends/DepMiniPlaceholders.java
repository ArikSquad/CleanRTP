package eu.mikart.cleanrtp.references.depends;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.rtp.RTPSetupInformation;
import eu.mikart.cleanrtp.references.PermissionCheck;
import eu.mikart.cleanrtp.references.helpers.HelperDate;
import eu.mikart.cleanrtp.references.helpers.RtpCheckHelper;
import eu.mikart.cleanrtp.references.helpers.RtpHelper;
import eu.mikart.cleanrtp.references.player.HelperPlayer;
import eu.mikart.cleanrtp.references.player.playerdata.PlayerData;
import eu.mikart.cleanrtp.references.rtpinfo.worlds.WorldPlayer;
import io.github.miniplaceholders.api.Expansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DepMiniPlaceholders {
    private Expansion expansion;

    public void register() {
        try {
            expansion = Expansion.builder(BetterRTP.getInstance().getPluginMeta().getName().toLowerCase())
                    .author(BetterRTP.getInstance().getPluginMeta().getAuthors().getFirst())
                    .version(BetterRTP.getInstance().getPluginMeta().getVersion())
                    .audiencePlaceholder(Player.class, "count", (player, queue, ctx) ->
                            insert(String.valueOf(HelperPlayer.getData(player).getRtpCount())))
                    .audiencePlaceholder(Player.class, "cooldown", (player, queue, ctx) ->
                            insert(cooldown(HelperPlayer.getData(player), world(player, queue.hasNext() ? queue.pop().value() : null))))
                    .audiencePlaceholder(Player.class, "cooldowntime", (player, queue, ctx) ->
                            insert(cooldownTime(HelperPlayer.getData(player), world(player, queue.hasNext() ? queue.pop().value() : null))))
                    .audiencePlaceholder(Player.class, "canrtp", (player, queue, ctx) ->
                            insert(canRTPALL(player, world(player, queue.hasNext() ? queue.pop().value() : null))))
                    .audiencePlaceholder(Player.class, "canrtpcooldown", (player, queue, ctx) ->
                            insert(canRTP_cooldown(player, world(player, queue.hasNext() ? queue.pop().value() : null))))
                    .audiencePlaceholder(Player.class, "canrtpprice", (player, queue, ctx) ->
                            insert(canRTP_price(player, world(player, queue.hasNext() ? queue.pop().value() : null))))
                    .audiencePlaceholder(Player.class, "price", (player, queue, ctx) ->
                            insert(price(player, world(player, queue.hasNext() ? queue.pop().value() : null))))
                    .build();
            expansion.register();
        } catch (NoClassDefFoundError ignored) {
            expansion = null;
        }
    }

    public void unregister() {
        if (expansion != null && expansion.registered()) {
            expansion.unregister();
        }
    }

    private Tag insert(String value) {
        return Tag.selfClosingInserting(Component.text(value));
    }

    private String cooldown(PlayerData data, World world) {
        if (world == null) return "Invalid World";
        long lng = BetterRTP.getInstance().getCooldowns().locked(data.player) ? -1L :
                RtpCheckHelper.getCooldown(data.player, RtpHelper.getPlayerWorld(new RTPSetupInformation(world, data.player, data.player, true)));
        return HelperDate.total(lng);
    }

    private String cooldownTime(PlayerData data, World world) {
        if (world == null) return "Invalid World";
        RTPSetupInformation setupInfo = new RTPSetupInformation(RtpHelper.getActualWorld(data.player, world), data.player, data.player, true);
        WorldPlayer pWorld = RtpHelper.getPlayerWorld(setupInfo);
        Long cooldownTime = BetterRTP.getInstance().getCooldowns().locked(data.player) ? -1L :
                (RtpCheckHelper.applyCooldown(data.player) ? pWorld.getCooldown() * 1000L : 0L);
        return HelperDate.total(cooldownTime);
    }

    private String canRTPALL(Player player, World world) {
        if (world == null) return "Invalid World";
        world = RtpHelper.getActualWorld(player, world);
        if (!PermissionCheck.getAWorld(player, world.getName()))
            return BetterRTP.getInstance().getSettings().getPlaceholder_nopermission();
        RTPSetupInformation setupInformation = new RTPSetupInformation(world, player, player, true);
        WorldPlayer pWorld = RtpHelper.getPlayerWorld(setupInformation);
        if (RtpCheckHelper.isCoolingDown(player, pWorld))
            return BetterRTP.getInstance().getSettings().getPlaceholder_cooldown();
        if (!BetterRTP.getInstance().getEco().hasBalance(pWorld))
            return BetterRTP.getInstance().getSettings().getPlaceholder_balance();
        return BetterRTP.getInstance().getSettings().getPlaceholder_true();
    }

    private String canRTP_cooldown(Player player, World world) {
        if (world == null) return "Invalid World";
        world = RtpHelper.getActualWorld(player, world);
        RTPSetupInformation setupInformation = new RTPSetupInformation(world, player, player, true);
        WorldPlayer pWorld = RtpHelper.getPlayerWorld(setupInformation);
        if (RtpCheckHelper.isCoolingDown(player, pWorld))
            return BetterRTP.getInstance().getSettings().getPlaceholder_cooldown();
        return BetterRTP.getInstance().getSettings().getPlaceholder_true();
    }

    private String canRTP_price(Player player, World world) {
        if (world == null) return "Invalid World";
        world = RtpHelper.getActualWorld(player, world);
        RTPSetupInformation setupInformation = new RTPSetupInformation(world, player, player, true);
        WorldPlayer pWorld = RtpHelper.getPlayerWorld(setupInformation);
        if (!BetterRTP.getInstance().getEco().hasBalance(pWorld))
            return BetterRTP.getInstance().getSettings().getPlaceholder_balance();
        return BetterRTP.getInstance().getSettings().getPlaceholder_true();
    }

    private String price(Player player, World world) {
        if (world == null) return "Invalid World";
        world = RtpHelper.getActualWorld(player, world);
        RTPSetupInformation setupInformation = new RTPSetupInformation(world, player, player, true);
        WorldPlayer pWorld = RtpHelper.getPlayerWorld(setupInformation);
        return String.valueOf(pWorld.getPrice());
    }

    private World world(Player player, String worldName) {
        if (worldName == null || worldName.isBlank()) {
            return player.getWorld();
        }
        for (World world : Bukkit.getWorlds()) {
            if (worldName.equalsIgnoreCase(world.getName())) {
                return world;
            }
        }
        return null;
    }
}
