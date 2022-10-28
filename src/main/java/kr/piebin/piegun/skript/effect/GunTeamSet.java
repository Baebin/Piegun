package kr.piebin.piegun.skript.effect;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import kr.piebin.piegun.manager.util.GunTeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class GunTeamSet extends Effect {
    private Expression<Player> player;
    private Expression<String> team;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        team = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        Player user = this.player.getSingle(e);
        String user_team = this.team.getSingle(e);
        
        GunTeamManager.setTeam(user, user_team);
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Gun Team Set Effect";
    }
}
