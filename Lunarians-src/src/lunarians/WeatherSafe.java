package lunarians;

import arc.Events;
import arc.func.Cons;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.WeatherState;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/WeatherSafe.class */
public class WeatherSafe {
    private static boolean registered;
    private static float timer;

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.WeatherSafe$0, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass0 implements Runnable {
        @Override // java.lang.Runnable
        public final void run() {
            WeatherSafe.$r8$lambda$_on8y-lB7-jjrDzWunt2XGOQtCg();
        }
    }

    /* compiled from: D8$$SyntheticClass */
    /* renamed from: lunarians.WeatherSafe$1, reason: invalid class name */
    /* loaded from: classes.dex */
    public final /* synthetic */ class AnonymousClass1 implements Cons {
        public final void get(Object obj) {
            WeatherSafe.lambda$tick$0((WeatherState) obj);
        }
    }

    public static void init() {
        if (registered || Vars.headless) {
            return;
        }
        registered = true;
        Events.run(EventType.Trigger.update, WeatherSafe::tick);
    }

    private static void tick() {
        timer += 1.0f;
        if (timer < 120.0f) {
            return;
        }
        timer = 0.0f;
        try {
            if (Groups.weather == null) {
                return;
            }
            Groups.weather.each(weatherState -> {
                if (weatherState == null) {
                    return;
                }
                try {
                    if (weatherState.weather == null) {
                        weatherState.remove();
                    }
                } catch (Throwable th) {
                }
            });
        } catch (Throwable th) {
        }
    }
}
