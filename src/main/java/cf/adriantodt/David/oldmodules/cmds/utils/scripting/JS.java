/*
 * This class was created by <AdrianTodt>. It's distributed as
 * part of the DavidBot. Get the Source Code in github:
 * https://github.com/adriantodt/David
 *
 * DavidBot is Open Source and distributed under the
 * GNU Lesser General Public License v2.1:
 * https://github.com/adriantodt/David/blob/master/LICENSE
 *
 * File Created @ [07/11/16 20:36]
 */

package cf.adriantodt.David.oldmodules.cmds.utils.scripting;

import cf.adriantodt.David.commands.base.CommandEvent;
import cf.adriantodt.David.modules.db.I18nModule;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JS {
	private static final ScriptEngine engine, unsafeEngine;
	public static final Evaluator JS_EVALUATOR = JS::eval, JS_UNSAFE_EVALUATOR = JS::unsafeEval;

	static {

		Evaluator.EVALUATOR_REGISTER.put("JS", JS_EVALUATOR);
		Evaluator.EVALUATOR_REGISTER.put("JS_UNSAFE", JS_UNSAFE_EVALUATOR);
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		unsafeEngine = new ScriptEngineManager().getEngineByName("nashorn");
		try {
			engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util);");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	public static void eval(CommandEvent event) {
		try {
			engine.put("event", event);
			Object out = engine.eval(
				"(function() {" +
					"with (imports) {" +
					event.getArgs() +
					"}" +
					"})();");
			event.awaitTyping().getAnswers().send(out == null ? I18nModule.getLocalized("bot.eval.noOut", event) : out.toString()).queue();
		} catch (ScriptException e) {
			event.awaitTyping().getAnswers().exception(e).queue();
		}
	}

	public static void unsafeEval(CommandEvent event) {
		try {
			engine.put("event", event);
			Object out = engine.eval(
				"(function() {" +
					"with (imports) {" +
					event.getArgs() +
					"}" +
					"})();");
			event.awaitTyping().getAnswers().send(out == null ? I18nModule.getLocalized("eval.noOut", event) : out.toString()).queue();
		} catch (ScriptException e) {
			event.awaitTyping().getAnswers().exception(e).queue();
		}
	}
}
