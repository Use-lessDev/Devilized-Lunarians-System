package devilized.lunarians.permissions;

import java.util.LinkedHashMap;
import java.util.Map;

/** Lightweight executable tests; no external test framework is required. */
public final class FeaturePermissionStoreTest {
    public static void main(String[] args) {
        testNewPlayerDefaults();
        testGrantAndRevoke();
        testClearingOverrideRestoresDefaults();
        testServerSnapshot();
        System.out.println("FeaturePermissionStore: all tests passed");
    }

    private static void testNewPlayerDefaults() {
        FeaturePermissionStore permissions = new FeaturePermissionStore();
        check(permissions.isUnlocked(BetaFeature.AUTO_MOON), "Auto Moon starts unlocked");
        check(permissions.isUnlocked(BetaFeature.SCHEMATIC_UI), "Schematic UI starts unlocked");
        check(permissions.isUnlocked(BetaFeature.FRIENDSHIP), "Friendship starts available");
        check(BetaFeature.AUTO_MOON.defaultEnabled(), "Auto Moon starts enabled");
        check(BetaFeature.SCHEMATIC_UI.defaultEnabled(), "Schematic UI starts enabled");

        for (BetaFeature feature : BetaFeature.values()) {
            if (feature != BetaFeature.AUTO_MOON
                && feature != BetaFeature.SCHEMATIC_UI
                && feature != BetaFeature.FRIENDSHIP) {
                check(!permissions.isUnlocked(feature), feature + " starts locked");
                check(!feature.defaultEnabled(), feature + " starts disabled");
            }
        }
    }

    private static void testGrantAndRevoke() {
        FeaturePermissionStore permissions = new FeaturePermissionStore();
        check(!permissions.mayRun(BetaFeature.PERFECT_DRILLS, true),
            "a locked feature must not run from an old local setting");

        permissions.grant(BetaFeature.PERFECT_DRILLS);
        check(permissions.mayEnable(BetaFeature.PERFECT_DRILLS), "grant unlocks the feature");
        check(permissions.mayRun(BetaFeature.PERFECT_DRILLS, true), "granted feature may run");
        check(!permissions.mayRun(BetaFeature.PERFECT_DRILLS, false),
            "a grant does not force the player's local setting on");

        permissions.revoke(BetaFeature.PERFECT_DRILLS);
        check(!permissions.mayEnable(BetaFeature.PERFECT_DRILLS), "revoke locks the feature");
        check(!permissions.mayRun(BetaFeature.PERFECT_DRILLS, true),
            "revocation blocks a feature even when its local setting is on");
    }

    private static void testClearingOverrideRestoresDefaults() {
        FeaturePermissionStore permissions = new FeaturePermissionStore();
        permissions.revoke(BetaFeature.AUTO_MOON);
        check(!permissions.isUnlocked(BetaFeature.AUTO_MOON), "explicit revoke takes priority");
        permissions.clearOverride(BetaFeature.AUTO_MOON);
        check(permissions.isUnlocked(BetaFeature.AUTO_MOON), "clearing restores default access");
    }

    private static void testServerSnapshot() {
        FeaturePermissionStore permissions = new FeaturePermissionStore();
        Map<String, Boolean> snapshot = new LinkedHashMap<String, Boolean>();
        snapshot.put("enableAutoMoon", Boolean.FALSE);
        snapshot.put("drillPerfect", Boolean.TRUE);
        snapshot.put("unknown-feature", Boolean.TRUE);
        permissions.replaceOverrides(snapshot);

        check(!permissions.isUnlocked(BetaFeature.AUTO_MOON), "server can revoke a default feature");
        check(permissions.isUnlocked(BetaFeature.PERFECT_DRILLS), "server can grant a beta feature");
        check(permissions.isUnlocked(BetaFeature.SCHEMATIC_UI), "missing keys use defaults");
        check(permissions.exportOverrides().size() == 2, "only recognized overrides are exported");

        permissions.replaceOverrides(null);
        check(permissions.isUnlocked(BetaFeature.AUTO_MOON), "empty snapshot clears overrides");
        check(!permissions.isUnlocked(BetaFeature.PERFECT_DRILLS), "other features return to defaults");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
