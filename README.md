# Devilized Lunarians System

A reconstruction workspace for the Lunarians Mindustry mod, using the supplied **Lunarians 3.78.1** package as the current reference.

## Current baseline

- Mindustry compatibility metadata in the reference package: `minGameVersion: 154`.
- The latest package contains compiled Java classes and an Android DEX; it is not a complete source tree.
- The older `Lunarians-Fix-Source-v10` archive is a 2.6-era patch source and is not a drop-in source tree for 3.78.1.
- Clean source recovery and feature-permission integration are in progress.

## Requested feature policy

For a new player profile, the initial defaults are:

- Auto Moon: unlocked and enabled.
- Schematic UI: unlocked and enabled.
- Friendship/Hub friends: available by default.
- All other Beta Features: locked and disabled until the administrator grants access.

An administrator must be able to grant or revoke each feature independently. The player's controls must reflect the current permission, and a revoked feature must not run even when an old local setting is still enabled.

**Privacy note:** making the friendship feature available does not accept the Hub's privacy consent on the player's behalf. Profile, friends, and chat synchronization must remain behind the player's explicit consent.

## Development status

The first implementation step adds an isolated, dependency-free permission policy and automated checks. Integration with the Mindustry HUD, settings, and the Hub server contract is tracked separately; this repository does not claim that the complete game mod has already been rebuilt.
