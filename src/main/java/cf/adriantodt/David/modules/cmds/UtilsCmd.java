/*
 * This class was created by <AdrianTodt>. It's distributed as
 * part of the DavidBot. Get the Source Code in github:
 * https://github.com/adriantodt/David
 *
 * DavidBot is Open Source and distributed under the
 * GNU Lesser General Public License v2.1:
 * https://github.com/adriantodt/David/blob/master/LICENSE
 *
 * File Created @ [07/11/16 21:22]
 */

package cf.adriantodt.David.modules.cmds;

import cf.adriantodt.David.commands.base.Commands;
import cf.adriantodt.David.commands.base.ICommand;
import cf.adriantodt.David.loader.Module;
import cf.adriantodt.David.loader.Module.Command;
import cf.adriantodt.David.loader.Module.Type;
import cf.adriantodt.utils.HTML2Discord;

@Module(name = "cmds.utils", type = Type.STATIC)
public class UtilsCmd {
	@Command("utils")
	private static ICommand utils() {
		return Commands.buildTree()
			.addCommand("convert", Commands.buildTree()
				.addCommand("html2md", Commands.buildSimple("utils.convert.html2md.usage")
					.setAction(event -> event.awaitTyping().sendMessage(HTML2Discord.toDiscordFormat(event.getArgs())).queue())
					.build()
				)
				.addCommand("md2text", Commands.buildSimple("utils.convert.md2text.usage")
					.setAction(event -> event.awaitTyping().sendMessage(HTML2Discord.toPlainText(event.getArgs())).queue())
					.build()
				)
				.addCommand("html2text", Commands.buildSimple("utils.convert.html2text.usage")
					.setAction(event -> event.awaitTyping().sendMessage(HTML2Discord.toPlainText(HTML2Discord.toDiscordFormat(event.getArgs()))).queue())
					.build()
				)
				.build()
			)
			.addCommand("shorten", Commands.buildSimple("utils.shorten.usage")
				.setAction(event -> {
					String r;
					if (event.getArgs(0).length == 1) {
						r = FeedingUtil.shorten(event.getArgs());
					} else if (event.getArgs(0).length == 2) {
						r = FeedingUtil.shorten(event.getArg(2, 0), event.getArg(2, 1));
					} else {
						event.awaitTyping().getAnswers().invalidargs().queue();
						return;
					}
					event.awaitTyping().getAnswers().bool(!r.contains("Error:"), ": " + r).queue();
				})
				.build()
			)
			.addCommand("hashcode", Commands.buildSimple("utils.hashcode.usage")
				.setAction(event -> event.awaitTyping().sendMessage("**HashCode**: " + event.getArgs().hashCode()).queue())
				.build()
			)
			.build();
	}
}
