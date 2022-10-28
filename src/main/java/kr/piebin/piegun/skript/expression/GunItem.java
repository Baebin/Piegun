package kr.piebin.piegun.skript.expression;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import kr.piebin.piegun.manager.weapon.GunUtilManager;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class GunItem extends SimpleExpression<ItemStack> {
    private Expression<String> weapon;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        this.weapon = (Expression<String>)exprs[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Gun Item";
    }

    @Nullable
    @Override
    protected ItemStack[] get(Event e) {
        String gun = this.weapon.getSingle(e);
        for (String key : GunUtilManager.gunMap.keySet()) {
            if (key.replaceAll("§\\w", "").equalsIgnoreCase(gun)) {
                return new ItemStack[] { GunUtilManager.getItem(key) };
            }
        }
        return new ItemStack[] { new ItemStack(Material.AIR) };
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }
}
