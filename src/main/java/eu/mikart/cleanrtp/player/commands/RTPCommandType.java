package eu.mikart.cleanrtp.player.commands;

import eu.mikart.cleanrtp.player.commands.types.*;
import lombok.Getter;

@Getter
public enum RTPCommandType {
    BIOME(new CmdBiome()),
    EDIT(new CmdEdit()),
    HELP(new CmdHelp()),
    INFO(new CmdInfo()),
    LOCATION(new CmdLocation()),
    PLAYER(new CmdPlayer()),
    PLAYERSUDO(new CmdPlayerSudo()),
    QUEUE(new CmdQueue(), true),
    RELOAD(new CmdReload()),
    //SETTINGS(new CmdSettings(), true),
    TEST(new CmdTest(), true),
    VERSION(new CmdVersion()),
    WORLD(new CmdWorld()),
    LOGGER(new CmdLogger(), true),
    ;

    private final RTPCommand cmd;
    private boolean debugOnly = false;

    RTPCommandType(RTPCommand cmd) {
        this.cmd = cmd;
    }

    RTPCommandType(RTPCommand cmd, boolean debugOnly) {
        this.cmd = cmd;
        this.debugOnly = debugOnly;
    }

}
