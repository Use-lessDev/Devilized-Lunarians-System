# Beta Feature Permissions

## Permission states

Each feature has a default access rule plus an optional administrator override:

- **Default**: use the new-player policy.
- **Granted**: the player may enable the feature.
- **Revoked**: the player may not enable or run the feature.

A grant does not force a feature on; it allows the player to use its own on/off setting. A revocation takes priority over that local setting.

## New-player defaults

| Feature | Permission | Initial setting |
|---|---:|---:|
| Auto Moon | Granted | On |
| Schematic UI | Granted | On |
| Friendship / Hub friends | Available | On (feature availability only) |
| All other Beta Features | Not granted | Off |

The BetaFeature enum contains the keys used by the reference Lunarians package and adds separate keys for Schematic UI and the friends feature.

## Integration contract

1. The server authenticates the administrator before accepting permission changes.
2. The server stores overrides per player and feature, not as one global boolean for every player.
3. The client receives the authoritative permission snapshot during Hub sync.
4. The HUD disables or marks locked controls when isUnlocked(feature) is false.
5. Each feature's runtime guard checks mayRun(feature, localSetting) before doing work.
6. Revoking a feature updates the snapshot, disables the control, and prevents the feature from running immediately.
7. Clearing an override returns that feature to the new-player default.

The permission core in this repository is deliberately independent of Mindustry/Arc so its rules can be tested without game binaries. The server endpoint and HUD wiring are not part of this first permission-core step.
