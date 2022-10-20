package kr.piebin.piegun.model;

import kr.piebin.piegun.manager.GunManager;

import java.util.HashMap;
import java.util.Map;

public class GunStatus {
    Map<String, Boolean> fireStatusMap;
    Map<String, Integer> ammoMap;
    Map<String, Boolean> zoomStatusMap;
    Map<String, Long> fireTimeMap;
    Map<String, Boolean> actionBarStatusMap;

    public GunStatus() {
        fireStatusMap = new HashMap<>();
        ammoMap = new HashMap<>();
        zoomStatusMap = new HashMap<>();
        fireTimeMap = new HashMap<>();
        actionBarStatusMap = new HashMap<>();
    }

    public boolean getFireStatus(String gun) {
        if (!fireStatusMap.containsKey(gun)) return false;
        return fireStatusMap.get(gun);
    }

    public void setFireStatus(String gun, boolean status) {
        fireStatusMap.put(gun, status);
    }

    public int getAmmo(String gun) {
        if (!ammoMap.containsKey(gun)) return GunManager.gunMap.get(gun).getAmmo();
        return ammoMap.get(gun);
    }

    public void setAmmo(String gun, int ammo) {
        if (ammo < 0) ammo = 0;
        if (ammo > GunManager.gunMap.get(gun).getAmmo()) ammo = GunManager.gunMap.get(gun).getAmmo();
        this.ammoMap.put(gun, ammo);
    }

    public boolean getZoomStatus(String gun) {
        if (!zoomStatusMap.containsKey(gun)) return false;
        return zoomStatusMap.get(gun);
    }

    public void setZoomStatusMap(String gun, boolean status) {
        zoomStatusMap.put(gun, status);
    }

    public long getFireTime(String gun) {
        if (!fireTimeMap.containsKey(gun)) return System.currentTimeMillis() - GunManager.gunMap.get(gun).getDelay_fire();
        return fireTimeMap.get(gun);
    }

    public void setFireTime(String gun, Long time) {
        fireTimeMap.put(gun, time);
    }

    public boolean getActionBarStatus(String gun) {
        if (!actionBarStatusMap.containsKey(gun)) return false;
        return actionBarStatusMap.get(gun);
    }

    public void setActionBarStatus(String gun, boolean status) {
        actionBarStatusMap.put(gun, status);
    }
}
