package kr.piebin.piegun.model;

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

    // Resource
    int model_default;
    int model_zoom;

    // Sniper zoom
    boolean zoomEnabled;
    boolean zoomMotionEnabled;

    // Sound
    String sound_shoot;
    String sound_reload;
    String sound_empty;

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
        if (ry < 5) ry = 5;
        this.ry = ry;
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
}
