package theworldsteps.content;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import theworldsteps.type.StepsUnitType;

/**
 * StepsUnits - Registers all custom StepsUnitType units.
 *
 * Currently defines:
 *   - baby: a basic test unit with idle/walk frames
 */
public class StepsUnits {

    public static StepsUnitType baby;

    public static void init() {
        Log.info("[TheWorldSteps] Registering units...");

        baby = new StepsUnitType("baby") {{
            description = "A tiny step-based unit with frame animations.";
            health = 100f;
            speed = 1.2f;
        }};

        Log.info("[TheWorldSteps] Registered: baby");
    }

    public static void loadContent() {
        // Safety: ensure baby exists even if init() wasn't called first
        if (baby == null) {
            Log.warn("[TheWorldSteps] baby was null in loadContent, calling init() first");
            init();
        }

        Log.info("[TheWorldSteps] Loading sprites for baby...");

        // Load idle frames: baby-idle.png (single frame for now)
        TextureRegion idle0 = Core.atlas.find("units_baby-idle");
        baby.idleRegions = new TextureRegion[]{idle0};

        // Load walk frames: baby-walking-2-x-idle.png (single frame for now)
        TextureRegion walk0 = Core.atlas.find("units_baby-walking-2-x-idle");
        baby.walkRegions = new TextureRegion[]{walk0};

        // Set frame intervals (ticks per frame)
        baby.idleFrameInterval = 10f;  // 10 ticks between idle frames
        baby.walkFrameInterval = 4f;   // 4 ticks between walk frames

        // Set velocity threshold for walking
        baby.moveThreshold = 0.1f;

        Log.info("[TheWorldSteps] Loaded baby sprites: idle=" + baby.idleRegions.length +
                 " frames, walk=" + baby.walkRegions.length + " frames");
    }
}
