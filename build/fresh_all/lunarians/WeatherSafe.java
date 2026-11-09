/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  arc.Events
 *  mindustry.Vars
 *  mindustry.game.EventType$Trigger
 *  mindustry.gen.Groups
 */
package lunarians;

import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;

public class WeatherSafe {
    private static boolean registered;
    private static float timer;

    public static void init() {
        if (registered || Vars.headless) {
            return;
        }
        registered = true;
        Events.run((Object)EventType.Trigger.update, WeatherSafe::tick);
    }

    private static void tick() {
        if ((timer += 1.0f) < 120.0f) {
            return;
        }
        timer = 0.0f;
        try {
            if (Groups.weather == null) {
                return;
            }
            Groups.weather.each(weatherState -> {
                try {
                    if (weatherState == null) {
                        return;
                    }
                    if (weatherState.weather == null) {
                        weatherState.remove();
                    }
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            });
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

