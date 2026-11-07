package lunarians;

import arc.Events;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;

public class AutoDefence {
    public static boolean enabled = false;

    public static void init() {
        Events.run(EventType.Trigger.update, AutoDefence::update);
    }

    public static void update() {
        if (!enabled || Vars.state.isMenu()) return;
    }
}
