package kr.piebin.piegun.skript.effect;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import kr.piebin.piegun.manager.GunTeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class GunTeamClear extends Effect {
    private Expression<Player> player;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Player user = this.player.getSingle(e);
        GunTeamManager.clearTeam(user);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Gun Team Clear Effect";
    }
}
