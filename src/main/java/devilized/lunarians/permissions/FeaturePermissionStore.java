package devilized.lunarians.permissions;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holds administrator overrides for one player.
 *
 * A missing override means "use the default policy". A true override grants
 * access; a false override revokes access. This class does not authenticate
 * administrators or persist data: those responsibilities belong to the
 * server-side integration.
 */
public final class FeaturePermissionStore {
    private final EnumMap<BetaFeature, Boolean> overrides =
        new EnumMap<BetaFeature, Boolean>(BetaFeature.class);

    /** Returns whether the player is allowed to access this feature. */
    public synchronized boolean isUnlocked(BetaFeature feature) {
        requireFeature(feature);
        Boolean override = overrides.get(feature);
        return override != null ? override.booleanValue() : feature.defaultUnlocked();
    }

    /** Returns whether the player's local setting may turn this feature on. */
    public synchronized boolean mayEnable(BetaFeature feature) {
        return isUnlocked(feature);
    }

    /** Runtime guard: a revoked feature never runs, even if locally enabled. */
    public synchronized boolean mayRun(BetaFeature feature, boolean localSettingEnabled) {
        return isUnlocked(feature) && localSettingEnabled;
    }

    /** Grants one feature to this player. */
    public synchronized void grant(BetaFeature feature) {
        requireFeature(feature);
        overrides.put(feature, Boolean.TRUE);
    }

    /** Revokes one feature from this player. */
    public synchronized void revoke(BetaFeature feature) {
        requireFeature(feature);
        overrides.put(feature, Boolean.FALSE);
    }

    /** Removes the administrator override and restores the default policy. */
    public synchronized void clearOverride(BetaFeature feature) {
        requireFeature(feature);
        overrides.remove(feature);
    }

    public synchronized boolean hasOverride(BetaFeature feature) {
        requireFeature(feature);
        return overrides.containsKey(feature);
    }

    /**
     * Replaces the complete override snapshot received from the server.
     * Missing and unknown keys are ignored; missing known keys use defaults.
     */
    public synchronized void replaceOverrides(Map<String, Boolean> serverSnapshot) {
        overrides.clear();
        if (serverSnapshot == null || serverSnapshot.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Boolean> entry : serverSnapshot.entrySet()) {
            BetaFeature feature = BetaFeature.fromPermissionKey(entry.getKey());
            Boolean value = entry.getValue();
            if (feature != null && value != null) {
                overrides.put(feature, value);
            }
        }
    }

    /** Returns an immutable key/value snapshot suitable for server sync. */
    public synchronized Map<String, Boolean> exportOverrides() {
        Map<String, Boolean> result = new LinkedHashMap<String, Boolean>();
        for (BetaFeature feature : BetaFeature.values()) {
            Boolean value = overrides.get(feature);
            if (value != null) {
                result.put(feature.permissionKey(), value);
            }
        }
        return Collections.unmodifiableMap(result);
    }

    /** Resets this player's explicit overrides to the new-player policy. */
    public synchronized void resetToDefaults() {
        overrides.clear();
    }

    private static void requireFeature(BetaFeature feature) {
        if (feature == null) {
            throw new IllegalArgumentException("feature must not be null");
        }
    }
}
