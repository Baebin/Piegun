package kr.piebin.piegun.skript.expression;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import kr.piebin.piegun.manager.GunTeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class GunTeamCheck extends SimpleExpression<Boolean> {
    private Expression<Player> p1;
    private Expression<Player> p2;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.p1 = (Expression<Player>)exprs[0];
        this.p2 = (Expression<Player>)exprs[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Gun Team Check";
    }

    @Nullable
    @Override
    protected Boolean[] get(Event e) {
        Player user_1 = this.p1.getSingle(e);
        Player user_2 = this.p2.getSingle(e);

        return new Boolean[] { GunTeamManager.checkTeam(user_1, user_2) };
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }
}
