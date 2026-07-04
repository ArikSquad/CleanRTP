package eu.mikart.cleanrtp.player.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import eu.mikart.cleanrtp.BetterRTP;
import eu.mikart.cleanrtp.player.commands.types.CmdTeleport;
import eu.mikart.cleanrtp.references.PermissionNode;
import eu.mikart.cleanrtp.player.events.custom.RtpCommandEvent;
import eu.mikart.cleanrtp.player.events.custom.RtpCommandAfterEvent;
import eu.mikart.cleanrtp.references.messages.RtpMessage;
import eu.mikart.cleanrtp.references.messages.MessagesCore;

public class Commands {

    private final BetterRTP pl;
    public List<RTPCommand> commands = new ArrayList<>();

    public Commands(BetterRTP pl) {
        this.pl = pl;
    }

    public void load() {
        commands.clear();
        for (RTPCommandType cmd : RTPCommandType.values())
           registerCommand(cmd.getCmd(), false);
    }

    public void registerPaperCommands() {
        pl.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = io.papermc.paper.command.brigadier.Commands
                    .literal("betterrtp")
                    .requires(source -> PermissionNode.USE.check(source.getSender()))
                    .executes(context -> execute(context, "betterrtp", new String[0]));

            for (RTPCommand command : commands) {
                root.then(subcommand(command));
            }

            event.registrar().register(
                    root.build(),
                    "Randomly teleport to a location",
                    List.of("brtp", "rtp", "randomtp", "wild", "wildtp"));
        });
    }

    private LiteralArgumentBuilder<CommandSourceStack> subcommand(RTPCommand command) {
        String name = command.getName().toLowerCase(Locale.ROOT);
        return io.papermc.paper.command.brigadier.Commands
                .literal(name)
                .requires(source -> command.permission().check(source.getSender()))
                .executes(context -> execute(context, "betterrtp", new String[] { name }))
                .then(io.papermc.paper.command.brigadier.Commands
                        .argument("arguments", StringArgumentType.greedyString())
                        .suggests((context, builder) -> {
                            CommandSender sender = context.getSource().getSender();
                            String remainingInput = builder.getRemaining();
                            String[] args = arguments(name, remainingInput);
                            List<String> suggestions = command.tabComplete(sender, args);
                            if (suggestions != null) {
                                int lastSpace = remainingInput.lastIndexOf(' ');
                                int tokenStart = lastSpace < 0 ? 0 : lastSpace + 1;
                                String remaining = remainingInput.substring(tokenStart).toLowerCase(Locale.ROOT);
                                var tokenBuilder = builder.createOffset(builder.getStart() + tokenStart);
                                for (String suggestion : suggestions) {
                                    if (suggestion.toLowerCase(Locale.ROOT).startsWith(remaining)) {
                                        tokenBuilder.suggest(suggestion);
                                    }
                                }
                                return tokenBuilder.buildFuture();
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> execute(
                                context,
                                "betterrtp",
                                arguments(name, StringArgumentType.getString(context, "arguments")))));
    }

    private int execute(CommandContext<CommandSourceStack> context, String label, String[] args) {
        try {
            commandExecuted(context.getSource().getSender(), label, args);
        } catch (NullPointerException e) {
            e.printStackTrace();
            RtpMessage.sms(context.getSource().getSender(), "&cERROR &7Seems like your Administrator did not update their language file!");
        }
        return Command.SINGLE_SUCCESS;
    }

    private String[] arguments(String command, String remaining) {
        if (remaining == null || remaining.isBlank()) {
            return new String[] { command };
        }

        List<String> args = new ArrayList<>();
        args.add(command);
        args.addAll(Arrays.asList(remaining.trim().split("\\s+")));
        return args.toArray(String[]::new);
    }

    public void registerCommand(RTPCommand cmd, boolean forced) {
        if (!cmd.isDebugOnly() || pl.getSettings().isDebug() || forced) //If debug only, can it be enabled?
            commands.add(cmd);
    }

    public void commandExecuted(CommandSender sendi, String label, String[] args) {
        if (PermissionNode.USE.check(sendi)) {
            if (args != null && args.length > 0) {
                for (RTPCommand cmd : commands) {
                    if (cmd.getName().equalsIgnoreCase(args[0])) {
                        if (cmd.permission().check(sendi)) {
                            RtpCommandEvent event = new RtpCommandEvent(sendi, cmd);
                            //Command Event
                            Bukkit.getServer().getPluginManager().callEvent(event);
                            if (!event.isCancelled()) {
                                BetterRTP.debug(sendi.getName() + " executed: /" + label + " " + String.join(" ", args));
                                cmd.execute(sendi, label, args);
                                Bukkit.getServer().getPluginManager().callEvent(new RtpCommandAfterEvent(sendi, cmd));
                            }
                        } else
                            MessagesCore.NOPERMISSION.send(sendi, cmd);
                        return;
                    }
                }
                MessagesCore.INVALID.send(sendi, label);
            } else {
                RtpCommandEvent event = new RtpCommandEvent(sendi, new CmdTeleport());
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled())
                    event.getCmd().execute(sendi, label, args);
            }
        } else
            MessagesCore.NOPERMISSION.send(sendi, PermissionNode.USE);
    }

    public List<String> onTabComplete(CommandSender sendi, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            for (RTPCommand cmd : commands) {
                if (cmd.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    if (cmd.permission().check(sendi))
                        list.add(cmd.getName().toLowerCase());
            }
        } else if (args.length > 1) {
            for (RTPCommand cmd : commands) {
                if (cmd.getName().equalsIgnoreCase(args[0]))
                    if (cmd.permission().check(sendi)) {
                        List<String> _cmdlist = cmd.tabComplete(sendi, args);
                        if (_cmdlist != null)
                            list.addAll(_cmdlist);
                    }
            }
        }
        return list;
    }
}
