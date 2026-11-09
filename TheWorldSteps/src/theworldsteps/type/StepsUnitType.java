package theworldsteps.type;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.Time;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

/**
 * StepsUnitType - A custom unit type with frame-based idle/walk animations.
 *
 * Instead of a single static sprite + mechanical sway, this unit cycles through
 * separate idle and walk frame arrays based on whether it's moving or not.
 *
 * Features:
 *   - idleRegions / walkRegions: separate frame arrays
 *   - idleFrameInterval / walkFrameInterval: ticks per frame (lower = faster)
 *   - Automatic selection based on velocity (vel.len() > moveThreshold)
 *   - Uses Time.time for frame cycling
 *   - Full compatibility with outline, cell, weapons, engines, etc.
 */
public class StepsUnitType extends UnitType {

    /** Frames shown when the unit is stationary. */
    public TextureRegion[] idleRegions = {};

    /** Frames shown when the unit is moving. */
    public TextureRegion[] walkRegions = {};

    /** How many ticks between idle frame changes. Lower = faster idle animation. */
    public float idleFrameInterval = 6f;

    /** How many ticks between walk frame changes. Lower = faster walk animation. */
    public float walkFrameInterval = 3f;

    /** Velocity threshold above which the unit is considered "walking". */
    public float moveThreshold = 0.1f;

    public StepsUnitType(String name) {
        super(name);
    }

    /**
     * Pick the correct frame based on whether the unit is moving.
     * Called from drawBody() each frame.
     *
     * @param velocityLen the unit's current velocity magnitude
     * @return the TextureRegion to draw
     */
    public TextureRegion getFrame(float velocityLen) {
        boolean moving = velocityLen > moveThreshold;
        TextureRegion[] regions = moving ? walkRegions : idleRegions;
        float interval = moving ? walkFrameInterval : idleFrameInterval;

        if (regions.length == 0) {
            // Fallback: use the default region if no custom frames are set
            return this.region;
        }

        // Calculate frame index using game time
        int frameIndex = (int)((Time.time / interval) % regions.length);
        return regions[frameIndex];
    }

    @Override
    public void load() {
        super.load();
        
        // If no custom frames provided, use the default region as a single frame
        if (idleRegions.length == 0 && region != null && region.found()) {
            idleRegions = new TextureRegion[]{region};
        }
        if (walkRegions.length == 0 && region != null && region.found()) {
            walkRegions = new TextureRegion[]{region};
        }
    }

    @Override
    public void drawBody(Unit unit) {
        float velLen = unit.vel().len();
        TextureRegion frame = getFrame(velLen);
        
        if (frame != null && frame.found()) {
            Draw.rect(frame, unit.x, unit.y, unit.rotation);
        }
    }
}
