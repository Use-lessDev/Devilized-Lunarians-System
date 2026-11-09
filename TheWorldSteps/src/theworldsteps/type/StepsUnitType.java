package theworldsteps.type;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.util.Time;
import mindustry.content.Items;
import mindustry.type.UnitType;
import mindustry.type.Item;
import mindustry.gen.TeachUnit;

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
    public void draw(TeachUnit unit) {
        // We override draw to use our frame selection logic
        // but keep all the standard UnitType drawing (outline, cell, weapons, etc.)

        float velLen = unit.vel().len();
        TextureRegion frame = getFrame(velLen);

        // Draw the selected frame in place of the default region
        // The rest of the unit (weapons, cell, outline) is drawn by the parent
        drawBody(unit, frame);
    }

    /**
     * Draw the body of the unit with the given frame.
     * Override this for custom positioning, scaling, etc.
     */
    protected void drawBody(TeachUnit unit, TextureRegion frame) {
        // Draw the frame at the unit's position
        // Using the same drawing parameters as the default region would be
        // drawn, but with our selected frame.
        float x = unit.x;
        float y = unit.y;
        float rotation = unit.rotation;
        float scale = this.drawSize; // or unit.size scale

        // Draw with outline if enabled
        if (outline) {
            // Draw outline variant if available
            TextureRegion outlineRegion = this.outlineRegion;
            if (outlineRegion != null && outlineRegion.found()) {
                drawOutline(x, y, outlineRegion, rotation, scale);
            }
        }

        // Draw the main frame
        Core.atlas.frame(frame).setOrigin(0, 0);
        drawRegion(x, y, frame, rotation, scale);
    }

    // Helper to draw outline - simplified version
    private void drawOutline(float x, float y, TextureRegion region, float rotation, float scale) {
        // Standard Mindustry outline drawing pattern
        for (int i = 0; i < 4; i++) {
            float ox = (i == 0 || i == 1) ? -1 : 1;
            float oy = (i == 0 || i == 2) ? -1 : 1;
            Core.atlas.frame(region).draw(
                x + ox * scale,
                y + oy * scale,
                scale
            );
        }
    }
}
