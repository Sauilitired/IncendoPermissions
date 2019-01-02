package org.incendo.permission.bukkit;

import com.google.common.base.Preconditions;
import com.intellectualsites.commands.CommandResult;
import com.intellectualsites.commands.callers.CommandCaller;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.permission.commands.PermissionCommand;
import org.incendo.permission.config.Messages;
import org.jetbrains.annotations.NotNull;

import static com.intellectualsites.commands.CommandHandlingOutput.*;

/**
 * Bukkit command executor
 */
@RequiredArgsConstructor
final class BukkitCommandExecutor implements CommandExecutor {

    private final PermissionCommand permissionCommand;
    private final BukkitPlayerRegistry playerRegistry;

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command,
        @NotNull final String label, @NotNull final String[] args) {
        Preconditions.checkNotNull(sender, "sender");

        final CommandCaller caller;
        if (sender instanceof Player) {
            caller = playerRegistry.getPlayer((Player) sender);
        } else {
            caller = playerRegistry.getConsole();
        }

        // We need to turn the arguments into a single string
        final StringBuilder stringBuilder = new StringBuilder(label);
        for (final String arg : args) {
            stringBuilder.append(" ").append(arg);
        }

        final CommandResult result = permissionCommand.handle(caller, stringBuilder.toString());
        switch (result.getCommandResult()) {
            case NOT_PERMITTED: {
                caller.message(Messages.NOT_PERMITTED);
            } break;
            case CALLER_OF_WRONG_TYPE: {
                if (sender instanceof Player) {
                    caller.message(Messages.ONLY_CONSOLE);
                } else {
                    caller.message(Messages.ONLY_PLAYER);
                }
            } break;
            case ERROR: {
                caller.message(Messages.COMMAND_ERROR_OCCURRED);
            } break;
            case NOT_FOUND: {
                if (result.getClosestMatch() != null) {
                    caller.message(String.format(Messages.COMMAND_DID_YOU_MEAN,
                        result.getClosestMatch().getCommand()));
                } else {
                    caller.message(Messages.COMMAND_NOT_FOUND);
                }
            } break;
            case WRONG_USAGE: {
                caller.message(String.format(Messages.COMMAND_WRONG_USAGE,
                    result.getCommand().getUsage()));
            } break;
            case ARGUMENT_ERROR: {
                caller.message(Messages.COMMAND_ARGUMENT);
            } break;
            default: break;
        }


        // We handle usage messages ourselves
        return true;
    }

}