package eu.mikart.cleanrtp.player.rtp.effects;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.HelperPlayer;
import eu.mikart.cleanrtp.versions.AsyncHandler;
import org.bukkit.entity.Player;
import org.bukkit.Registry;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RtpEffectPotions {

    private boolean potionEnabled;
    private final HashMap<PotionEffectType, Integer[]> potionEffects = new HashMap<>();
    private boolean invincibleEnabled;
    private int invincibleTime;

    void load() {
        potionEffects.clear();
        //Invincible
        invincibleEnabled = BetterRTP.getInstance().getSettings().getGeneral().getEffects().isInvicible();
        if (invincibleEnabled)
            invincibleTime = BetterRTP.getInstance().getSettings().getGeneral().getEffects().getInvicibleSeconds();

        //Potions
        potionEnabled =BetterRTP.getInstance().getSettings().getGeneral().getEffects().isPotions();
        if (potionEnabled) {
            List<String> list = BetterRTP.getInstance().getSettings().getGeneral().getEffects().getPotionsList();
            for (String p : list) {
                String[] ary = p.replaceAll(" ", "").split(":");
                String type = ary[0].trim();
                NamespacedKey key = NamespacedKey.fromString(type.toLowerCase(java.util.Locale.ROOT));
                PotionEffectType effect = key == null ? null : Registry.MOB_EFFECT.get(key);
                if (effect != null) {
                    try {
                        int duration = ary.length >= 2 ? Integer.parseInt(ary[1]) : 60;
                        int amplifier = ary.length >= 3 ? Integer.parseInt(ary[2]) : 1;
                        potionEffects.put(effect, new Integer[] {duration, amplifier});
                    } catch (NumberFormatException e) {
                        BetterRTP.getInstance().getLogger().info("The potion duration or amplifier `" + ary[1] + "` is not an integer. Effect was removed!");
                    }
                } else
                    BetterRTP.getInstance().getLogger().info("The potion effect `" + type + "` does not exist! " +
                            "Please fix or remove this potion effect! Try '/rtp info potion_effects' to get a list of valid effects!");
            }
        }
    }

    public void giveEffects(Player p) {
        AsyncHandler.syncAtEntity(p, () -> {
            if (invincibleEnabled)
                HelperPlayer.getData(p).setInvincibleEndTime(System.currentTimeMillis() + (invincibleTime * 1000L));
            if (potionEnabled) {
                List<PotionEffect> effects = new ArrayList<>();
                for (PotionEffectType e : potionEffects.keySet()) {
                    Integer[] mods = potionEffects.get(e);
                    int duration = mods[0];
                    int amplifier = mods[1];
                    effects.add(new PotionEffect(e, duration, amplifier, false, false));
                }
                p.addPotionEffects(effects);
            }
        });
    }

}
