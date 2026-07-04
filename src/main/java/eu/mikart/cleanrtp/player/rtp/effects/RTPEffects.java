package eu.mikart.cleanrtp.player.rtp.effects;

import lombok.Getter;

public class RTPEffects {

    @Getter final RtpEffectParticles particles = new RtpEffectParticles();
    @Getter final RtpEffectPotions potions = new RtpEffectPotions();
    @Getter final RtpEffectSounds sounds = new RtpEffectSounds();
    @Getter final RtpEffectTitles titles = new RtpEffectTitles();

    //public HashMap<Player, List<CompletableFuture<Chunk>>> playerLoads = new HashMap<>();

    public void load() {
        particles.load();
        potions.load();
        sounds.load();
        titles.load();
    }

}
