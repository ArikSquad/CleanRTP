package eu.mikart.cleanrtp.commands;

import eu.mikart.cleanrtp.references.PermissionCheck;

public record CommandMeta(String name, PermissionCheck permission) {
}
