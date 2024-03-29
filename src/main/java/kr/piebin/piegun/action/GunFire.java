package kr.piebin.piegun.action;

import kr.piebin.piegun.Piegun;
import kr.piebin.piegun.manager.util.GunTeamManager;
import kr.piebin.piegun.manager.util.PacketManager;
import kr.piebin.piegun.manager.util.SoundManager;
import kr.piebin.piegun.manager.weapon.GunFireManager;
import kr.piebin.piegun.manager.weapon.GunUtilManager;
import kr.piebin.piegun.model.Gun;
import kr.piebin.piegun.model.GunStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GunFire {
    final int delay = 220;
    final float distancePerBlock = 0.4f;

    List<Boolean> ISSTOPPED = new ArrayList<>();
    boolean isAuto;

    int spread = 1;

    float damage = 0;
    float distance = 0;
    float knockback = 0;

    String particle;

    Gun gun;
    GunStatus status;

    Player player;
    ItemStack item;
    String weapon;

    public GunFire(Player player, ItemStack item, String weapon) {
        this.player = player;
        this.item = item;
        this.weapon = weapon;
    }

    public void fire() {
        fire(player, item, weapon);
    }

    private void fire(Player player, ItemStack item, String weapon) {
        if (player == null || item == null || weapon == null) return;

        gun = GunUtilManager.gunMap.get(weapon);

        status = GunFireManager.getStatus(player);
        if (status.getReloadStatus(weapon)) {
            new GunReload(player, item, weapon).stopReload();
        }

        Bukkit.getScheduler().runTaskAsynchronously(Piegun.getInstance(), () -> {
            if (!status.getActionBarStatus(weapon)) PacketManager.showActionBar(player, weapon, item);

            if (status.getFireStatus(weapon)) {
                return;
            }

            if (System.currentTimeMillis() - status.getFireTime(weapon) < gun.getDelay_fire()) {
                return;
            }

            status.setFireStatus(weapon, true);
            GunFireManager.saveStatus(player, status);

            isAuto = status.getAutoStatus(weapon);

            spread = gun.getSpread();

            damage = gun.getDamage();
            distance = gun.getDistance();
            knockback = gun.getKnockback();

            particle = gun.getParticle();

            int delay_fire = gun.getDelay_fire();
            long start_time = System.currentTimeMillis();

            int burst = gun.getBurst();
            int ammo, canFire;
            do {
                status = GunFireManager.getStatus(player);

                if (System.currentTimeMillis() - status.getFireTime(weapon) >= delay_fire) {
                    status.setFireTime(weapon, System.currentTimeMillis());

                    ammo = status.getAmmo(weapon);
                    if (burst > ammo) canFire = ammo;
                    else canFire = burst;

                    for (int i = 0; i < canFire; i++) {
                        status.setAmmo(weapon, --ammo);

                        PacketManager.showBullet(player, weapon);

                        fireProjectile(player.getEyeLocation(), burst);
                    }

                    if (ammo == 0) SoundManager.playEmptySound(player, gun);
                    else {
                        showDust();
                        model_init();
                    }

                    GunFireManager.saveStatus(player, status);

                    if (delay - (System.currentTimeMillis() - start_time) - delay_fire <= 0) {
                        break;
                    } else {
                        try {
                            Thread.sleep(delay_fire);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!gun.isAuto()) break;
                        if (!isAuto) break;
                    }
                }
            } while (System.currentTimeMillis() - start_time <= delay);

            status.setFireStatus(weapon, false);
            GunFireManager.saveStatus(player, status);
        });
    }

    private void fireProjectile(Location location, int burst) {
        if (burst == 1) {
            rebound();
            fireProjectileSpread(location);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Piegun.getInstance(), () -> {
            Location location_new = location;

            for (int i = 0; i < burst; i++) {
                fireProjectileSpread(location_new);
                location_new = rebound(new Location(
                        location.getWorld(), location.getX(), location.getY(), location.getZ(),
                        location.getYaw(), location.getPitch()));

                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fireProjectileSpread(Location location) {
        model_init();

        if (spread > 1) {
            float yaw;
            float pitch;

            int multiply = -1;

            Random random = new Random();
            for (int i = 0; i < spread; i++) {
                if (random.nextInt(2) == 0) multiply *= -1;
                yaw = location.getYaw() + (random.nextInt(30) + 1) * multiply;

                if (random.nextInt(2) == 0) multiply *= -1;
                pitch = location.getPitch() - (random.nextInt(30) + 1) * multiply;

                fireProjectile(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), yaw, pitch));
            }
        } else {
            fireProjectile(location);
        }
    }

    private void fireProjectile(Location location) {
        SoundManager.playFireSound(player, gun);

        Location location_pre = location.getBlock().getLocation();

        // Sniper
        if (gun.isZoomMotionEnabled() && !status.getZoomStatus(weapon)) {
            Random random = new Random();

            int multiply = -1;

            if (random.nextInt(2) == 0) multiply *= -1;
            location.setYaw(location.getYaw() + (random.nextInt(15) + 1) * multiply);

            if (random.nextInt(2) == 0) multiply *= -1;
            location.setPitch(location.getPitch() + (random.nextInt(15) + 1) * multiply);
        }

        Vector vector = location.getDirection();

        double x = vector.getX() * distancePerBlock;
        double y = vector.getY() * distancePerBlock;
        double z = vector.getZ() * distancePerBlock;

        int index = ISSTOPPED.size();
        ISSTOPPED.add(false);

        Bukkit.getScheduler().runTaskAsynchronously(Piegun.getInstance(), () -> {
            int i = 0;
            while (location.distance(location_pre) <= distance) {
                location.add(x, y, z);

                if (ISSTOPPED.get(index)) return;
                if (i++ > 2) {
                    PacketManager.spawnParticle(location, particle);
                }

                if (!PacketManager.isPassable(location.getBlock())) return;

                checkEntityAndAttack(new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), 0, 0), index);
            }
        });
    }

    private void checkEntityAndAttack(Location location, int index) {
        Bukkit.getScheduler().runTask(Piegun.getInstance(), () -> {
            for (Entity e : location.getWorld().getNearbyEntities(location ,0.2, 0.2, 0.2)) {
                if (ISSTOPPED.get(index)) return;

                if (e instanceof LivingEntity && e != player) {
                    boolean isHeadshot = false;
                    if (((LivingEntity) e).getEyeLocation().distance(location) <= 0.5) isHeadshot = true;
                    if (e instanceof Player) {
                        if (!GunTeamManager.checkTeam((Player) e, player)) {
                            attack((LivingEntity) e, index, isHeadshot);
                        }
                    } else {
                        attack((LivingEntity) e, index, isHeadshot);
                        return;
                    }
                }
            }
        });
    }

    private void attack(LivingEntity e, int index, boolean isHeadshot) {
        Vector vector_knockback = player.getLocation().getDirection();
        vector_knockback.multiply(knockback);

        e.setVelocity(vector_knockback);

        ISSTOPPED.set(index, true);

        damage(e, isHeadshot);
    }

    private void damage(LivingEntity e, boolean isHeadshot) {
        PacketManager.sendPacketPlayOutEntityStatus(player, e);

        double health = e.getHealth() - damage;
        if (isHeadshot) {
            health -= damage;
            SoundManager.playHitSound(player, gun);
        } else {
            SoundManager.playHeadshotSound(player, gun);
        }
        if (health <= 0) {
            e.damage(100000, player);
            return;
        }

        e.setHealth(health);
        e.damage(0, player);
    }

    private void rebound() {
        rebound(player.getLocation());
    }

    private Location rebound(Location location) {
        float yaw_rebound = gun.getRx();
        float pitch_rebound = gun.getRy();

        if (status.getZoomStatus(weapon)) {
            yaw_rebound /= 2;
            pitch_rebound /= 2;
        }

        // 50% : L&R Rebound
        if (new Random().nextInt(10) < 5) {
            yaw_rebound *= -1;
        }

        yaw_rebound = location.getYaw() + yaw_rebound;
        pitch_rebound = location.getPitch() - pitch_rebound;

        location.setYaw(yaw_rebound);
        location.setPitch(pitch_rebound);

        PacketManager.sendPacketPlayOutPosition(player, yaw_rebound, pitch_rebound);

        return location;
    }

    private void showDust() {
        Vector vector = player.getLocation().getDirection();
        Location location = player.getEyeLocation();

        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            double weight_x = (random.nextInt(300)+100)/100;
            double weight_y = (random.nextInt(300)+100)/100;
            double weight_z = (random.nextInt(300)+100)/100;

            double x = vector.getX() * weight_x;
            double y = vector.getY() * weight_y;
            double z = vector.getZ() * weight_z;

            PacketManager.spawnParticleDust(player,
                    new Location(player.getWorld(), location.getX()+x, location.getY()+y, location.getZ()+z, 0, 0));
        }
    }

    private void model_init() {
        Bukkit.getScheduler().runTaskAsynchronously(Piegun.getInstance(), () -> {
            if (!status.getZoomStatus(weapon)) return;

            int model_zoom = gun.getModel_zoom();
            int model_zoom_fire = gun.getModel_zoom_fire();

            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(model_zoom_fire);
            item.setItemMeta(meta);

            try {
                int delay = gun.getDelay_fire();
                if (delay > 30) delay = 30;
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            meta.setCustomModelData(model_zoom);
            item.setItemMeta(meta);
        });
    }
}
