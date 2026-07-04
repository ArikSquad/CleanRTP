package eu.mikart.cleanrtp.player.commands;

import eu.mikart.cleanrtp.references.PermissionCheck;

public record CommandMeta(String name, PermissionCheck permission) {
}
