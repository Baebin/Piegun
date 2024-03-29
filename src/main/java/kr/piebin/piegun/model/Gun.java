package kr.piebin.piegun.model;

import java.util.ArrayList;
import java.util.List;

public class Gun {
    String name;
    String item;

    float damage;
    int ammo;

    boolean auto;

    int burst;
    int spread;
    int distance;

    float knockback;

    // Time (ms)
    int delay_fire;
    int delay_reload;

    // Rebound
    float rx;
    float ry;

    // Particle;
    String particle;

    // Resource
    int model_default;
    int model_zoom;
    int model_zoom_fire;
    List<Integer> model_reload;
    //List<Integer> model_ammo;


    // Sniper zoom
    boolean zoomEnabled;
    boolean zoomMotionEnabled;

    // Sound
    String sound_shoot;
    String sound_reload;
    String sound_empty;
    String sound_auto_changed;

    String sound_hit;
    String sound_headshot;

    public String getName() {
        return name;
    }

    public Gun setName(String name) {
        this.name = name;
        return this;
    }

    public String getItem() {
        return item;
    }

    public Gun setItem(String item) {
        this.item = item;
        return this;
    }

    public float getDamage() {
        return damage;
    }

    public Gun setDamage(float damage) {
        if (damage < 0) damage = 0;
        this.damage = damage;
        return this;
    }

    public int getAmmo() {
        return ammo;
    }

    public Gun setAmmo(int ammo) {
        if (ammo < 0) ammo = 0;
        this.ammo = ammo;
        return this;
    }

    public boolean isAuto() {
        return auto;
    }

    public Gun setAuto(boolean auto) {
        this.auto = auto;
        return this;
    }

    public int getBurst() {
        return burst;
    }

    public Gun setBurst(int burst) {
        if (burst < 1) burst = 1;
        this.burst = burst;
        return this;
    }

    public int getSpread() {
        return spread;
    }

    public Gun setSpread(int spread) {
        if (spread < 1) spread = 1;
        this.spread = spread;
        return this;
    }

    public int getDistance() {
        return distance;
    }

    public Gun setDistance(int distance) {
        if (distance < 1) distance = 1;
        this.distance = distance;
        return this;
    }

    public float getKnockback() {
        return knockback;
    }

    public Gun setKnockback(float knockback) {
        this.knockback = knockback;
        return this;
    }

    public int getDelay_fire() {
        return delay_fire;
    }

    public Gun setDelay_fire(int delay_fire) {
        if (delay_fire < 10 ) delay_fire = 10;
        this.delay_fire = delay_fire;
        return this;
    }

    public int getDelay_reload() {
        return delay_reload;
    }

    public Gun setDelay_reload(int delay_reload) {
        if (delay_reload < 0 ) delay_reload = 0;
        this.delay_reload = delay_reload;
        return this;
    }

    public float getRx() {
        return rx;
    }

    public Gun setRx(float rx) {
        if (rx < 0) rx = 0;
        this.rx = rx;
        return this;
    }

    public float getRy() {
        return ry;
    }

    public Gun setRy(float ry) {
        this.ry = ry;
        return this;
    }

    public String getParticle() {
        return particle;
    }

    public Gun setParticle(String particle) {
        this.particle = particle;
        return this;
    }

    public int getModel_default() {
        return model_default;
    }

    public Gun setModel_default(int model_default) {
        if (model_default < 0) model_default = 0;
        this.model_default = model_default;
        return this;
    }

    public int getModel_zoom() {
        return model_zoom;
    }

    public Gun setModel_zoom(int model_zoom) {
        if (model_zoom < 0) model_zoom = 0;
        this.model_zoom = model_zoom;
        return this;
    }

    public int getModel_zoom_fire() {
        return model_zoom_fire;
    }

    public Gun setModel_zoom_fire(int model_zoom_fire) {
        if (model_zoom_fire < 0) model_zoom_fire = 0;
        this.model_zoom_fire = model_zoom_fire;
        return this;
    }

    public List<Integer> getModel_reload() {
        if (model_reload == null) return new ArrayList<>();
        return model_reload;
    }

    public Gun setModel_reload(List<Integer> model_reload) {
        this.model_reload = model_reload;
        return this;
    }

    /*
    public List<Integer> getModel_ammo() {
        if (model_ammo == null) return new ArrayList<>();
        return model_ammo;
    }

    public Gun setModel_ammo(List<Integer> model_ammo) {
        this.model_ammo = model_ammo;
        return this;
    }
     */

    public boolean isZoomEnabled() {
        return zoomEnabled;
    }

    public Gun setZoomEnabled(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
        return this;
    }

    public boolean isZoomMotionEnabled() {
        return zoomMotionEnabled;
    }

    public Gun setZoomMotionEnabled(boolean zoomMotionEnabled) {
        this.zoomMotionEnabled = zoomMotionEnabled;
        return this;
    }

    public String getSound_shoot() {
        return sound_shoot;
    }

    public Gun setSound_shoot(String sound_shoot) {
        this.sound_shoot = sound_shoot;
        return this;
    }

    public String getSound_reload() {
        return sound_reload;
    }

    public Gun setSound_reload(String sound_reload) {
        this.sound_reload = sound_reload;
        return this;
    }

    public String getSound_empty() {
        return sound_empty;
    }

    public Gun setSound_empty(String sound_empty) {
        this.sound_empty = sound_empty;
        return this;
    }

    public String getSound_auto_changed() {
        return sound_auto_changed;
    }

    public Gun setSound_auto_changed(String sound_auto_changed) {
        this.sound_auto_changed = sound_auto_changed;
        return this;
    }

    public String getSound_hit() {
        return sound_hit;
    }

    public Gun setSound_hit(String sound_hit) {
        this.sound_hit = sound_hit;
        return this;
    }

    public String getSound_headshot() {
        return sound_headshot;
    }

    public Gun setSound_headshot(String sound_headshot) {
        this.sound_headshot = sound_headshot;
        return this;
    }
}
