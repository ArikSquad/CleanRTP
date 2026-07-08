package eu.mikart.cleanrtp.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.Ignore;
import eu.mikart.cleanrtp.player.commands.EditCommandSetting;
import eu.mikart.cleanrtp.player.rtp.RtpShape;
import eu.mikart.cleanrtp.references.settings.SoftDepends;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// this is the worst code in this project and I ADDED THIS, which is kinda cringe.
@Getter
@Configuration
@SuppressWarnings({"FieldMayBeFinal", "unused"})
public final class Settings {
    public static final String FILE_NAME = "config.yml";
    private GeneralSettings general = new GeneralSettings();
    private DefaultWorldSettings defaultWorld = new DefaultWorldSettings();
    private List<String> blacklistedBlocks = new ArrayList<>(List.of(
        "stationary_water",
        "stationary_lava",
        "water",
        "flowing_water",
        "lava",
        "flowing_lava",
        "cactus",
        "leaves",
        "leaves_2",
        "air",
        "void_air",
        "bedrock",
        "oak_leaves",
        "jungle_leaves",
        "kelp"
    ));
    private List<String> disabledWorlds = new ArrayList<>(List.of("prison", "creative"));
    private boolean isCustomWorldsEnabled = false;
    private List<Map<String, WorldOverrideSettings>> customWorlds = defaultCustomWorlds();
    private List<Map<String, String>> overrides = defaultOverrides();
    private List<Map<String, String>> worldType = defaultWorldTypes();
    private PermissionGroupSettings permissionGroup = defaultPermissionGroups();

    @Ignore
    private final SoftDepends depends = new SoftDepends();
    @Ignore
    private boolean protocolLibSounds;
    @Ignore
    private boolean locationEnabled;
    @Ignore
    private boolean useLocationIfAvailable;
    @Ignore
    private boolean locationNeedPermission;
    @Ignore
    private boolean useLocationsInSameWorld;
    @Ignore
    private String placeholder_true;
    @Ignore
    private String placeholder_nopermission;
    @Ignore
    private String placeholder_cooldown;
    @Ignore
    private String placeholder_balance;
    @Ignore
    private String placeholder_timeDays;
    @Ignore
    private String placeholder_timeHours;
    @Ignore
    private String placeholder_timeMinutes;
    @Ignore
    private String placeholder_timeSeconds;
    @Ignore
    private String placeholder_timeZero;
    @Ignore
    private String placeholder_timeInf;
    @Ignore
    private String placeholder_timeSeparator_middle;
    @Ignore
    private String placeholder_timeSeparator_last;

    public Optional<WorldOverrideSettings> findCustomWorld(String world) {
        return findSingletonMapValue(customWorlds, world);
    }

    public WorldOverrideSettings getOrCreateCustomWorld(String world) {
        return findCustomWorld(world).orElseGet(() -> {
            WorldOverrideSettings settings = new WorldOverrideSettings();
            customWorlds.add(singletonMap(world, settings));
            return settings;
        });
    }

    public List<String> getCustomWorldNames() {
        return customWorlds.stream()
            .flatMap(map -> map.keySet().stream())
            .toList();
    }

    public Map<String, WorldOverrideSettings> getCustomWorldsByWorld() {
        Map<String, WorldOverrideSettings> flattened = new LinkedHashMap<>();
        for (Map<String, WorldOverrideSettings> map : customWorlds) {
            flattened.putAll(map);
        }
        return flattened;
    }

    public Map<String, String> getOverridesByWorld() {
        return flattenStringMap(overrides);
    }

    public Map<String, String> getWorldTypesByWorld() {
        return flattenStringMap(worldType);
    }

    public List<PermissionGroupDefinition> getPermissionGroupDefinitions() {
        List<PermissionGroupDefinition> definitions = new ArrayList<>();
        for (Map<String, List<Map<String, WorldOverrideSettings>>> groupMap : permissionGroup.groups) {
            for (Map.Entry<String, List<Map<String, WorldOverrideSettings>>> entry : groupMap.entrySet()) {
                definitions.add(new PermissionGroupDefinition(entry.getKey(), entry.getValue()));
            }
        }
        return definitions;
    }

    public Optional<WorldOverrideSettings> findPermissionGroupWorld(String group, String world) {
        for (PermissionGroupDefinition definition : getPermissionGroupDefinitions()) {
            if (!definition.name().equals(group)) continue;
            return findSingletonMapValue(definition.worlds(), world);
        }
        return Optional.empty();
    }

    public void setDefaultValue(EditCommandSetting cmd, Object value) {
        defaultWorld.set(cmd, value);
        ConfigProvider.saveSettings();
    }

    public void setCustomWorldValue(String world, EditCommandSetting cmd, Object value) {
        getOrCreateCustomWorld(world).set(cmd, value);
        ConfigProvider.saveSettings();
    }

    public boolean setPermissionGroupValue(String group, String world, EditCommandSetting cmd, Object value) {
        Optional<WorldOverrideSettings> settings = findPermissionGroupWorld(group, world);
        settings.ifPresent(worldSettings -> worldSettings.set(cmd, value));
        if (settings.isPresent()) ConfigProvider.saveSettings();
        return settings.isPresent();
    }

    public void setLocationValue(String location, EditCommandSetting cmd, Object value) {
        locations.entries.computeIfAbsent(location, ignored -> new LocationEntry()).set(cmd, value);
        ConfigProvider.saveSettings();
    }

    public boolean isProtocolLibSounds() {
        return general.effects.protocolLibSound;
    }

    public boolean isLocationEnabled() {
        return locations.enabled;
    }

    public boolean isLocationNeedPermission() {
        return locations.requirePermission;
    }

    public boolean isUseLocationIfAvailable() {
        return locations.useLocationIfAvailable;
    }

    public boolean isUseLocationsInSameWorld() {
        return locations.useLocationsInSameWorld;
    }

    public void setWorldType(String world, String type) {
        removeSingletonMap(worldType, world);
        worldType.add(singletonMap(world, type));
        ConfigProvider.saveSettings();
    }

    public void setOverride(String world, String target) {
        removeSingletonMap(overrides, world);
        if (!"REMOVE_OVERRIDE".equals(target)) {
            overrides.add(singletonMap(world, target));
        }
        ConfigProvider.saveSettings();
    }

    public void setBlacklistedBlock(String block, boolean add) {
        blacklistedBlocks.removeIf(existing -> existing.equals(block));
        if (add) blacklistedBlocks.add(block);
        ConfigProvider.saveSettings();
    }

    private static String formatName(String fieldName) {
        return switch (fieldName) {
            case "languageFile" -> "Language-File";
            case "general" -> "Settings";
            case "defaultWorld" -> "Default";
            case "blacklistedBlocks" -> "BlacklistedBlocks";
            case "disabledWorlds" -> "DisabledWorlds";
            case "customWorlds" -> "CustomWorlds";
            case "worldType" -> "WorldType";
            case "permissionGroup" -> "PermissionGroup";
            case "rtpOnFirstJoin" -> "RtpOnFirstJoin";
            case "setAsRespawn" -> "SetAsRespawn";
            case "preloadRadius" -> "PreloadRadius";
            case "maxAttempts" -> "MaxAttempts";
            case "statusMessages" -> "StatusMessages";
            case "disableUpdater" -> "DisableUpdater";
            case "huskTowns" -> "HuskTowns";
            case "huskClaims" -> "HuskClaims";
            case "logToConsole" -> "LogToConsole";
            case "useWorldBorder" -> "UseWorldBorder";
            case "maxRadius" -> "MaxRadius";
            case "minRadius" -> "MinRadius";
            case "centerX" -> "CenterX";
            case "centerZ" -> "CenterZ";
            case "maxY" -> "MaxY";
            case "minY" -> "MinY";
            case "rtpOnDeath" -> "RTPOnDeath";
            case "perWorld" -> "PerWorld";
            case "cancelOnMove" -> "CancelOnMove";
            case "cancelOnDamage" -> "CancelOnDamage";
            default -> Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        };
    }

    private static List<Map<String, WorldOverrideSettings>> defaultCustomWorlds() {
        List<Map<String, WorldOverrideSettings>> worlds = new ArrayList<>();
        worlds.add(singletonMap("custom_world_1", WorldOverrideSettings.of(
            false, 1000, 100, 0, 0, 50, "square", 320, 0, 60L, null, null
        )));
        worlds.add(singletonMap("other_custom_world", WorldOverrideSettings.of(
            null, 10000, 150, 123, -123, 0, "circle", null, null, null,
            List.of("desert", "forest"), true
        )));
        return worlds;
    }

    private static List<Map<String, String>> defaultOverrides() {
        return new ArrayList<>(List.of(
            singletonMap("master_world", "world"),
            singletonMap("creative_world", "world")
        ));
    }

    private static List<Map<String, String>> defaultWorldTypes() {
        return new ArrayList<>(List.of(
            singletonMap("world", "NORMAL"),
            singletonMap("world_nether", "NETHER"),
            singletonMap("world_the_end", "NORMAL")
        ));
    }

    private static PermissionGroupSettings defaultPermissionGroups() {
        PermissionGroupSettings settings = new PermissionGroupSettings();
        settings.groups.add(singletonMap("vip", new ArrayList<>(List.of(
            singletonMap("Build_World", WorldOverrideSettings.of(
                null, 10000, 1000, null, null, 100, null, null, null, 500L, null, null, 0
            )),
            singletonMap("Survival_World", WorldOverrideSettings.of(
                false, 5000, 1000, 10, 10, 10, null, 320, 0, null, null, null, 10
            ))
        ))));
        settings.groups.add(singletonMap("vip2", new ArrayList<>(List.of(
            singletonMap("Build_World", WorldOverrideSettings.of(
                null, 25000, 10000, null, null, 15, null, null, null, 1500L, null, null
            ))
        ))));
        return settings;
    }

    private static <T> Map<String, T> singletonMap(String key, T value) {
        Map<String, T> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    private static <T> Optional<T> findSingletonMapValue(List<Map<String, T>> maps, String key) {
        for (Map<String, T> map : maps) {
            if (map.containsKey(key)) return Optional.ofNullable(map.get(key));
        }
        return Optional.empty();
    }

    private static <T> void removeSingletonMap(List<Map<String, T>> maps, String key) {
        maps.removeIf(map -> map.containsKey(key));
    }

    private static Map<String, String> flattenStringMap(List<Map<String, String>> maps) {
        Map<String, String> flattened = new LinkedHashMap<>();
        for (Map<String, String> map : maps) {
            flattened.putAll(map);
        }
        return flattened;
    }

    public LocationsConfig locations = new LocationsConfig();

    @Configuration
    public static final class LocationsConfig {

        @Comment("Enable the locations feature")
        public boolean enabled = false;

        @Comment("Require players to have `betterrtp.location.<world_name>`")
        public boolean requirePermission = false;

        @Comment("Will choose a location upon `/rtp` if location(s) is available in the world")
        public boolean useLocationIfAvailable = true;

        @Comment({
            "Will only choose locations in same world rtp'ing in",
            "",
            "If use-locations-in-same-world is set to true, use `betterrtp.bypass.location`",
            "to allow rtp'ing to locations in other worlds"
        })
        public boolean useLocationsInSameWorld = true;

        public Map<String, LocationEntry> entries = new LinkedHashMap<>() {{
            put("main-loc", new LocationEntry());
        }};
    }

    @Configuration
    public static final class LocationEntry {

        public String world = "world_name";

        public int centerX = 100;
        public int centerZ = 150;

        @Comment("Optional")
        public int maxRadius = 100;

        @Comment("Optional")
        public int minRadius = 5;

        @Comment("Optional")
        public String shape = "square";

        @Comment("Optional")
        public int minY = 0;

        @Comment("Optional")
        public int maxY = 320;

        @Comment("Optional")
        public int cooldown = 300;

        @Comment("Optional")
        public boolean useWorldBorder = false;

        @Comment("Optional")
        public List<String> biomes = new ArrayList<>();

        @Comment("Optional")
        public float price = 0;

        public void set(EditCommandSetting cmd, Object value) {
            switch (cmd) {
                case CENTER_X -> centerX = (int) value;
                case CENTER_Z -> centerZ = (int) value;
                case MAXRAD -> maxRadius = (int) value;
                case MINRAD -> minRadius = (int) value;
                case MAXY -> maxY = (int) value;
                case MINY -> minY = (int) value;
                case PRICE -> price = ((Number) value).floatValue();
                case SHAPE -> shape = (String) value;
                case USEWORLDBORDER -> useWorldBorder = (boolean) value;
            }
        }
    }

    @Getter
    @Configuration
    public static final class GeneralSettings {
        private RespectSettings respect = new RespectSettings();
        @Comment("Output to console some debugging info")
        private boolean debug = false;
        @Comment("Amount of chunks to preload around a safe location")
        private int preloadRadius = 5;
        @Comment("Maximum amount of tries before CleanRTP gives up and sends a NotSafeMessage")
        private int maxAttempts = 32;
        private FirstJoinSettings rtpOnFirstJoin = new FirstJoinSettings();
        private CooldownSettings cooldown = new CooldownSettings();
        private DelaySettings delay = new DelaySettings();
        private QueueSettings queue = new QueueSettings();
        private boolean statusMessages = false;
        private boolean disableUpdater = false;
        private LoggerSettings logger = new LoggerSettings();
        private Effects effects = new Effects();
        private Economy economy = new Economy();
        public Titles titles = new Titles();

        @Configuration
        public static final class Titles {

            @Comment("Enable the titles effect feature")
            public boolean enabled = true;

            @Comment({
                "All support %player% %x% %y% and %z% placeholders",
                "Only triggers when there is no delay"
            })
            public TitleMessage noDelay = new TitleMessage(
                "&6Teleporting...",
                "&8please wait",
                true
            );

            @Comment("Allow the teleport success message in chat")
            public TitleMessage teleport = new TitleMessage(
                "&6Teleported!",
                "&fx=%x% y=%y% z=%z%",
                true
            );

            @Comment("Allow the teleport delay message in chat")
            public TitleMessage delay = new TitleMessage(
                "",
                "&fTeleporting in %time% seconds...",
                true
            );

            @Comment("Allow the cancelled message in chat too")
            public TitleMessage cancelled = new TitleMessage(
                "&eYou moved...",
                "&cRtp was cancelled!",
                true
            );

            @Comment("Allow the loading message in chat")
            public TitleMessage loading = new TitleMessage(
                "",
                "&7loading chunks... please wait",
                true
            );

            public TitleMessage failed = new TitleMessage(
                "",
                "&cFailed! No safe spots located",
                true
            );
        }

        @Configuration
        public static final class TitleMessage {

            public String title = "";
            public String subtitle = "";
            public boolean sendMessage = true;

            private TitleMessage() {
            }

            public TitleMessage(String title, String subtitle, boolean sendMessage) {
                this.title = title;
                this.subtitle = subtitle;
                this.sendMessage = sendMessage;
            }
        }
    }

    @Getter
    @Configuration
    public static final class Economy {
        private boolean enabled = false;
        private float price = 500;
    }

    @Getter
    @Configuration
    public static final class Effects {
        private boolean invicible = false;
        private int invicibleSeconds = 5;

        private boolean potions = false;
        @Comment("Format <potion_name>:[duration_ticks]:[amplifier] #Default duration=60, amplifier=1")
        private List<String> potionsList = List.of(
            "Blindness:60:1",
            "Invisibility:60:1"
        );

        private boolean particles = false;
        private List<String> particlesList = List.of(
            "EXPLOSION_NORMAL",
            "CRIT"
        );
        // ????
        private String particleShape = "EXPLODE";

        private boolean sounds = true;
        private boolean protocolLibSound = false;
        private String delaySound = "entity_tnt_primed";
        private String successSound = "entity_generic_explode";
    }

    @Getter
    @Configuration
    public static final class RespectSettings {
        @Comment("Respect Lands areas (https://www.spigotmc.org/resources/lands.53313/)")
        private boolean lands = false;
        @Comment("Respect HuskClaims areas (https://www.spigotmc.org/resources/huskclaims-1-17-1-21-modern-golden-shovel-land-claiming-fully-cross-server-compatible.114467/)")
        private boolean huskClaims = false;
        @Comment("Respect HuskTowns areas (https://www.spigotmc.org/resources/husktowns.92672/)")
        private boolean huskTowns = false;
    }

    @Getter
    @Configuration
    public static final class FirstJoinSettings {
        private boolean enabled = false;
        private String world = "world";
        private boolean setAsRespawn = false;
    }

    @Getter
    @Configuration
    public static final class CooldownSettings {
        private boolean enabled = true;
        private int lockAfter = 0;
        private int time = 600;
        private boolean perWorld = false;
    }

    @Getter
    @Configuration
    public static final class DelaySettings {
        private boolean enabled = true;
        private int time = 5;
        private boolean cancelOnMove = true;
        private boolean cancelOnDamage = false;
    }

    @Getter
    @Configuration
    public static final class QueueSettings {
        private boolean enabled = true;
    }

    @Getter
    @Configuration
    public static final class LoggerSettings {
        private boolean enabled = true;
        private boolean logToConsole = false;
        private String format = "yyyy-MM-dd HH:mm:ss";
    }

    @Getter
    @Setter
    @Configuration
    public static final class DefaultWorldSettings {
        private boolean useWorldBorder = false;
        private List<String> biomes = new ArrayList<>();
        private int maxRadius = 1000;
        private int minRadius = 10;
        private int centerX = 0;
        private int centerZ = 0;
        private String shape = "square";
        private int maxY = 320;
        private int minY = 0;
        private boolean rtpOnDeath = false;

        public void set(EditCommandSetting cmd, Object value) {
            switch (cmd) {
                case CENTER_X -> centerX = (int) value;
                case CENTER_Z -> centerZ = (int) value;
                case MAXRAD -> maxRadius = (int) value;
                case MINRAD -> minRadius = (int) value;
                case MAXY -> maxY = (int) value;
                case MINY -> minY = (int) value;
                case SHAPE -> shape = (String) value;
                case USEWORLDBORDER -> useWorldBorder = (boolean) value;
                case PRICE -> {
                }
            }
        }
    }

    @Getter
    @Setter
    @Configuration
    public static final class WorldOverrideSettings {
        private Boolean useWorldBorder;
        private Integer maxRadius;
        private Integer minRadius;
        private Integer centerX;
        private Integer centerZ;
        private Integer price;
        private String shape;
        private Integer maxY;
        private Integer minY;
        private Long cooldown;
        private List<String> biomes;
        private Boolean rtpOnDeath;
        private Integer priority;

        public static WorldOverrideSettings of(
            Boolean useWorldBorder,
            Integer maxRadius,
            Integer minRadius,
            Integer centerX,
            Integer centerZ,
            Integer price,
            String shape,
            Integer maxY,
            Integer minY,
            Long cooldown,
            List<String> biomes,
            Boolean rtpOnDeath
        ) {
            return of(useWorldBorder, maxRadius, minRadius, centerX, centerZ, price, shape, maxY, minY, cooldown, biomes, rtpOnDeath, null);
        }

        public static WorldOverrideSettings of(
            Boolean useWorldBorder,
            Integer maxRadius,
            Integer minRadius,
            Integer centerX,
            Integer centerZ,
            Integer price,
            String shape,
            Integer maxY,
            Integer minY,
            Long cooldown,
            List<String> biomes,
            Boolean rtpOnDeath,
            Integer priority
        ) {
            WorldOverrideSettings settings = new WorldOverrideSettings();
            settings.useWorldBorder = useWorldBorder;
            settings.maxRadius = maxRadius;
            settings.minRadius = minRadius;
            settings.centerX = centerX;
            settings.centerZ = centerZ;
            settings.price = price;
            settings.shape = shape;
            settings.maxY = maxY;
            settings.minY = minY;
            settings.cooldown = cooldown;
            settings.biomes = biomes == null ? null : new ArrayList<>(biomes);
            settings.rtpOnDeath = rtpOnDeath;
            settings.priority = priority;
            return settings;
        }

        public void set(EditCommandSetting cmd, Object value) {
            switch (cmd) {
                case CENTER_X -> centerX = (int) value;
                case CENTER_Z -> centerZ = (int) value;
                case MAXRAD -> maxRadius = (int) value;
                case MINRAD -> minRadius = (int) value;
                case MAXY -> maxY = (int) value;
                case MINY -> minY = (int) value;
                case PRICE -> price = (int) value;
                case SHAPE -> shape = (String) value;
                case USEWORLDBORDER -> useWorldBorder = (boolean) value;
            }
        }

        public void applyTo(eu.mikart.cleanrtp.references.rtpinfo.worlds.RtpWorldDefaulted target, boolean includePrice) {
            if (useWorldBorder != null) target.setUseWorldBorder(useWorldBorder);
            if (centerX != null) target.setCenterX(centerX);
            if (centerZ != null) target.setCenterZ(centerZ);
            if (maxRadius != null) target.setMaxRadius(maxRadius);
            if (minRadius != null) target.setMinRadius(minRadius);
            if (includePrice && price != null) target.setPrice(price);
            if (shape != null) {
                try {
                    target.setShape(RtpShape.valueOf(shape.toUpperCase()));
                } catch (IllegalArgumentException ignored) {
                }
            }
            if (maxY != null) target.setMaxY(maxY);
            if (minY != null) target.setMinY(minY);
            if (cooldown != null) target.setCooldown(cooldown);
            if (biomes != null) target.setBiomes(new ArrayList<>(biomes));
        }
    }

    @Getter
    @Configuration
    public static final class PermissionGroupSettings {
        private boolean enabled = false;
        private List<Map<String, List<Map<String, WorldOverrideSettings>>>> groups = new ArrayList<>();
    }

    public record PermissionGroupDefinition(String name, List<Map<String, WorldOverrideSettings>> worlds) {
    }
}
