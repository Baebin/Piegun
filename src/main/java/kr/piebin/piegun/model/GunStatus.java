package kr.piebin.piegun.model;

import kr.piebin.piegun.manager.GunUtilManager;

import java.util.HashMap;
import java.util.Map;

public class GunStatus {
    Map<String, Integer> ammoMap;
    Map<String, Long> fireTimeMap;

    Map<String, Boolean> fireStatusMap;
    Map<String, Boolean> zoomStatusMap;
    Map<String, Boolean> reloadStatusMap;
    Map<String, Boolean> actionBarStatusMap;

    public GunStatus() {
        ammoMap = new HashMap<>();
        fireTimeMap = new HashMap<>();

        fireStatusMap = new HashMap<>();
        zoomStatusMap = new HashMap<>();
        reloadStatusMap = new HashMap<>();
        actionBarStatusMap = new HashMap<>();
    }

    public int getAmmo(String gun) {
        if (!ammoMap.containsKey(gun)) return GunUtilManager.gunMap.get(gun).getAmmo();
        return ammoMap.get(gun);
    }

    public void setAmmo(String gun, int ammo) {
        if (ammo < 0) ammo = 0;
        if (ammo > GunUtilManager.gunMap.get(gun).getAmmo()) ammo = GunUtilManager.gunMap.get(gun).getAmmo();
        this.ammoMap.put(gun, ammo);
    }

    public long getFireTime(String gun) {
        if (!fireTimeMap.containsKey(gun)) return System.currentTimeMillis() - GunUtilManager.gunMap.get(gun).getDelay_fire();
        return fireTimeMap.get(gun);
    }

    public void setFireTime(String gun, Long time) {
        fireTimeMap.put(gun, time);
    }

    public boolean getFireStatus(String gun) {
        if (!fireStatusMap.containsKey(gun)) return false;
        return fireStatusMap.get(gun);
    }

    public void setFireStatus(String gun, boolean status) {
        fireStatusMap.put(gun, status);
    }

    public boolean getZoomStatus(String gun) {
        if (!zoomStatusMap.containsKey(gun)) return false;
        return zoomStatusMap.get(gun);
    }

    public void setReloadStatus(String gun, boolean status) {
        reloadStatusMap.put(gun, status);
    }

    public boolean getReloadStatus(String gun) {
        if (!reloadStatusMap.containsKey(gun)) return false;
        return reloadStatusMap.get(gun);
    }

    public void setZoomStatusMap(String gun, boolean status) {
        zoomStatusMap.put(gun, status);
    }

    public boolean getActionBarStatus(String gun) {
        if (!actionBarStatusMap.containsKey(gun)) return false;
        return actionBarStatusMap.get(gun);
    }

    public void setActionBarStatus(String gun, boolean status) {
        actionBarStatusMap.put(gun, status);
    }
}
