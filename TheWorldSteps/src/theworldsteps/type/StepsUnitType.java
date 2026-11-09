package theworldsteps.type;

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
     * Called from draw() each frame.
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
    public void draw(Unit unit) {
        // Save the original region
        TextureRegion original = this.region;

        // Swap in the correct frame based on velocity
        this.region = getFrame(unit.vel().len());

        // Let the parent class draw everything (outline, cell, weapons, engines, etc.)
        super.draw(unit);

        // Restore the original region
        this.region = original;
    }
}
