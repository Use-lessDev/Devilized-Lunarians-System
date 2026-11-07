package devilized.lunarians.permissions;

/**
 * Beta features exposed by the Lunarians client.
 *
 * The keys intentionally match the setting names used by the 3.78.1 reference
 * package where possible. Features that are not represented by a single
 * LunariansSettings boolean use their own stable keys.
 */
public enum BetaFeature {
    AUTO_MOON("enableAutoMoon", true, true),
    SCHEMATIC_UI("showSchematicPanel", true, true),
    FRIENDSHIP("hub.friends", true, true),

    ENEMY_CORE_ALERT("enableEnemyCoreAlert", false, false),
    AUTO_FILL("enableAutoFill", false, false),
    FREE_CAM("enableFreeCam", false, false),
    PRODUCTION_ALERT("enableProductionAlert", false, false),
    CAM_FOLLOW("enableCamFollow", false, false),
    RESOURCE_LOSS_WARN("enableLossWarn", false, false),
    AUTO_UNIT("enableAutoUnit", false, false),
    AUTO_CARGO("enableAutoCargo", false, false),
    AUTO_CONTAINER("enableAutoContainer", false, false),
    AUTO_DEFENCE("enableAutoDefence", false, false),
    AUTO_SHIELD("enableAutoShield", false, false),
    AUTO_HEAL("enableAutoHeal", false, false),
    AUTO_ATTACK("enableAutoAttack", false, false),
    AUTO_UNIT_HEAL("enableAutoUnitHeal", false, false),
    AUTO_CONNECT_CONVEYOR("enableAutoConnectConveyor", false, false),
    AUTO_DRILL("enableAutoDrill", false, false),
    AUTO_DRILL_CONVEYOR("enableAutoDrillConveyor", false, false),
    AUTO_DOCK("dockAuto", false, false),
    PVP_MODE("pvpMode", false, false),
    PERFECT_DRILLS("drillPerfect", false, false),
    ORDERED_ECONOMY("autoEconomy", false, false),
    MINER_UNITS("minerUnits", false, false);

    private final String permissionKey;
    private final boolean defaultUnlocked;
    private final boolean defaultEnabled;

    BetaFeature(String permissionKey, boolean defaultUnlocked, boolean defaultEnabled) {
        this.permissionKey = permissionKey;
        this.defaultUnlocked = defaultUnlocked;
        this.defaultEnabled = defaultEnabled;
    }

    public String permissionKey() {
        return permissionKey;
    }

    public boolean defaultUnlocked() {
        return defaultUnlocked;
    }

    /** Initial local setting for a brand-new player profile. */
    public boolean defaultEnabled() {
        return defaultEnabled;
    }

    public static BetaFeature fromPermissionKey(String key) {
        if (key == null) {
            return null;
        }
        for (BetaFeature feature : values()) {
            if (feature.permissionKey.equals(key)) {
                return feature;
            }
        }
        return null;
    }
}
