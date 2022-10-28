package kr.piebin.piegun.skript.expression;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import kr.piebin.piegun.manager.util.GunTeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class GunTeamGet extends SimpleExpression<String> {
    private Expression<Player> player;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.player = (Expression<Player>)exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Gun Team";
    }

    @Nullable
    @Override
    protected String[] get(Event e) {
        Player user = this.player.getSingle(e);

        return new String[] { GunTeamManager.getTeam(user) };
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
