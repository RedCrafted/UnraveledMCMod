package me.totalfreedom.totalfreedommod.commands;

import me.totalfreedom.totalfreedommod.config.ConfigEntry;
import me.totalfreedom.totalfreedommod.config.MainConfig;
import me.totalfreedom.totalfreedommod.rank.PlayerRank;
import me.totalfreedom.totalfreedommod.util.FLog;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * See https://github.com/TotalFreedom/License - This file may not be edited or removed.
 */
@CommandPermissions(level = PlayerRank.NON_OP, source = SourceType.BOTH)
@CommandParameters(description = "Shows information about TotalFreedomMod or reloads it", usage = "/<command> [reload]")
public class Command_tfm extends FreedomCommand
{

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (args.length == 1)
        {
            if (!args[0].equals("reload"))
            {
                return false;
            }

            if (!plugin.al.isAdmin(sender))
            {
                noPerms();
                return true;
            }

            MainConfig.load();
            plugin.services.stop();
            plugin.services.start();

            final String message = String.format("%s v%s reloaded.",
                    TotalFreedomMod.pluginName,
                    TotalFreedomMod.pluginVersion);

            playerMsg(message);
            FLog.info(message);
            return true;
        }

        TotalFreedomMod.BuildProperties build = TotalFreedomMod.build;
        playerMsg("TotalFreedomMod for 'Total Freedom', the original all-op server.", ChatColor.GOLD);
        playerMsg("Running on " + ConfigEntry.SERVER_NAME.getString() + ".", ChatColor.GOLD);
        playerMsg("Created by Madgeek1450 and Prozza.", ChatColor.GOLD);
        playerMsg(String.format("Version "
                + ChatColor.BLUE + "%s.%s " + ChatColor.GOLD + "("
                + ChatColor.BLUE + "%s" + ChatColor.GOLD + ")",
                build.version,
                build.number,
                build.head), ChatColor.GOLD);
        playerMsg(String.format("Compiled "
                + ChatColor.BLUE + "%s" + ChatColor.GOLD + " by "
                + ChatColor.BLUE + "%s",
                build.date,
                build.author), ChatColor.GOLD);
        playerMsg("Visit " + ChatColor.AQUA + "http://github.com/TotalFreedom/TotalFreedomMod"
                + ChatColor.GREEN + " for more information.", ChatColor.GREEN);

        return true;
    }
}