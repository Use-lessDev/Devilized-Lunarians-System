package theworldsteps.content;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import mindustry.content.Items;
import mindustry.type.Item;
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
            hitSize = 8f;
            buildTime = 60f * 2; // 2 seconds
            // The region will be loaded from sprite "units_baby-idle"
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
        if (idle0 == null || !idle0.found()) {
            Log.warn("[TheWorldSteps] Sprite 'units_baby-idle' not found, using fallback");
            idle0 = Core.atlas.find("error");
        }
        baby.idleRegions = new TextureRegion[]{idle0};

        // Load walk frames: baby-walking-2-x-idle.png (single frame for now)
        TextureRegion walk0 = Core.atlas.find("units_baby-walking-2-x-idle");
        if (walk0 == null || !walk0.found()) {
            Log.warn("[TheWorldSteps] Sprite 'units_baby-walking-2-x-idle' not found, using idle frame");
            walk0 = idle0;
        }
        baby.walkRegions = new TextureRegion[]{walk0};

        // Set the default region (used for fallback and outline)
        baby.region = idle0;

        // Set frame intervals (ticks per frame)
        baby.idleFrameInterval = 10f;  // 10 ticks between idle frames
        baby.walkFrameInterval = 4f;   // 4 ticks between walk frames

        // Set velocity threshold for walking
        baby.moveThreshold = 0.1f;

        // Set icons (required — without them the IconSelectDialog crashes)
        TextureRegion icon = Core.atlas.find("units_baby-icon");
        if (icon == null || !icon.found()) {
            Log.warn("[TheWorldSteps] Sprite 'units_baby-icon' not found, using idle frame");
            icon = idle0;
        }
        baby.uiIcon = icon;
        baby.fullIcon = icon;

        Log.info("[TheWorldSteps] Loaded baby sprites: idle=" + baby.idleRegions.length +
                 " frames, walk=" + baby.walkRegions.length + " frames");
    }
}
