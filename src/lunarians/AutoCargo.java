package lunarians;

import arc.Events;
import arc.func.Boolf;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.world.Tile;
import mindustry.world.blocks.storage.CoreBlock;

public class AutoCargo {
    public static boolean enabled = false;
    public static float range = 20f;
    public static Item preferredItem = null;

    public static void init() {
        Events.run(EventType.Trigger.update, AutoCargo::update);
    }

    public static void update() {
        if (!enabled || Vars.state.isMenu()) return;
        // simplified for this example - full content is in the decompiled file
    }
}
