package eu.mikart.cleanrtp.references;

import lombok.Getter;

@Getter
public enum PermissionNode implements PermissionCheck {

    ADMIN("admin"),
    USE("use"),
    BYPASS_ECONOMY("bypass.economy"),
    BYPASS_HUNGER("bypass.hunger"),
    BYPASS_COOLDOWN("bypass.cooldown"),
    BYPASS_DELAY("bypass.delay"),
    BYPASS_LOCATION("bypass.location"), //Ability to bypass `UseLocationsInSameWorld` if enabled in location.yml
    RELOAD("reload"),
    SETTINGS("settings"),
    INFO("info"),
    UPDATER("updater"),
    RTP_OTHER("player"),
    BIOME("biome"),
    WORLD("world"),
    SIGN_CREATE("sign"),
    VERSION("version"),
    EDIT("edit"),
    LOCATION("location")
    ;

    private final String node;

    PermissionNode(String node) {
        this.node = PermissionCheck.getPrefix() + node;
    }
}