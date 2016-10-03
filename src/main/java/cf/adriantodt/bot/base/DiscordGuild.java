/*
 * This class was created by <AdrianTodt>. It's distributed as
 * part of the DavidBot. Get the Source Code in github:
 * https://github.com/adriantodt/David
 *
 * DavidBot is Open Source and distributed under the
 * GNU Lesser General Public License v2.1:
 * https://github.com/adriantodt/David/blob/master/LICENSE
 *
 * File Created @ [12/09/16 07:38]
 */

package cf.adriantodt.bot.base;

import cf.adriantodt.bot.Bot;
import cf.adriantodt.bot.base.cmd.ITranslatable;
import cf.adriantodt.bot.base.cmd.UserCommand;
import cf.adriantodt.bot.persistence.DataManager;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.*;

public class DiscordGuild implements ITranslatable {
	public static final DiscordGuild PM, GLOBAL;
	public static final String[] DEFAULT_PREFIXES = {"&", "!"};
	public static List<DiscordGuild> all = new ArrayList<>();

	static {
		PM = new DiscordGuild();
		GLOBAL = new DiscordGuild();
		PM.id = "-2";
		GLOBAL.id = "-1";
		PM.name = "PM";
		GLOBAL.name = "GLOBAL";
		PM.guild = null;
		GLOBAL.guild = null;
	}

	public Guild guild = null;
	public Map<String, Long> userPerms = new HashMap<>();
	public Map<String, UserCommand> commands = new HashMap<>();
	public Map<String, Boolean> flags = new HashMap<>();
	public String id = "-1", name = "", defaultLanguage = "en_US";
	public List<String> cmdPrefixes = new ArrayList<>(Arrays.asList(DEFAULT_PREFIXES));

	public DiscordGuild() {
		all.add(this);
		userPerms.put("default", Permissions.BASE_USER);
		flags.put("cleanup", true);
	}

	public static DiscordGuild fromDiscord(Guild guild) {
		if (guild == null) return PM;
		DiscordGuild g = null;
		for (DiscordGuild gs : all)
			if (guild.getId().equals(gs.id)) {
				g = gs;
				break;
			}

		if (g == null) g = new DiscordGuild();
		if (g.id.equals("-1")) g.id = guild.getId();
		if (g.guild == null) g.guild = guild;
		if (g.name.isEmpty()) g.name = getAvailable(guild.getName());

		return g;
	}

	public static DiscordGuild fromId(String id) {
		for (DiscordGuild g : all) {
			if (g.id.equals(id)) return g;
		}
		return null;
	}

	public static DiscordGuild fromName(String name) {
		for (DiscordGuild g : all) {
			if (g.name.equals(name)) return g;
		}
		return null;
	}

	public static DiscordGuild fromDiscord(MessageReceivedEvent event) {
		return fromDiscord(event.getGuild());
	}

	private static String getAvailable(String name) {
		name = name.replace(" ", "_").replace(":", "");
		if (fromName(name) == null) return name;
		for (int i = 2; i < 1000; i++) {
			if (fromName(name + i) == null) return name + i;
		}
		throw new RuntimeException("What. the. fuck.");
	}

	@Override
	public String toString(String language) {
		return I18n.getLocalized("guild.guild", language) + ": " + name + (guild != null && !name.equals(guild.getName()) ? " (" + guild.getName() + ")" : "")
			+ "\n - " + I18n.getLocalized("guild.admin", language) + ": " + (guild == null ? Bot.API.getUserById(DataManager.configs.owner).getUsername() : guild.getOwner().getUsername())
			+ "\n - " + I18n.getLocalized("guild.cmds", language) + ": " + commands.size()
			+ "\n - " + I18n.getLocalized("guild.channels", language) + ": " + (guild == null ? (this == PM ? Bot.API.getPrivateChannels().size() : Bot.API.getTextChannels().size() + Bot.API.getPrivateChannels().size()) : guild.getTextChannels().size())
			+ "\n - " + I18n.getLocalized("guild.users", language) + ": " + (guild == null ? (this == PM ? Bot.API.getPrivateChannels().size() : Bot.API.getUsers().size()) : guild.getUsers().size())
			+ "\n - ID: " + id;
	}
}
