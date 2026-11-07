package lunarians.data;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/data/MoonSlot.class */
public class MoonSlot {
    public static final int UNSET = Integer.MIN_VALUE;
    public String unit = "";
    public int maxUnits = 0;
    public float radius = 8.0f;
    public float speed = 1.0f;
    public int x = Integer.MIN_VALUE;
    public int y = Integer.MIN_VALUE;
    public int followId = -1;
    public String followLabel = "";

    public MoonSlot copy() {
        MoonSlot moonSlot = new MoonSlot();
        moonSlot.unit = this.unit;
        moonSlot.maxUnits = this.maxUnits;
        moonSlot.radius = this.radius;
        moonSlot.speed = this.speed;
        moonSlot.x = this.x;
        moonSlot.y = this.y;
        moonSlot.followId = this.followId;
        moonSlot.followLabel = this.followLabel;
        return moonSlot;
    }

    public boolean ready() {
        return !this.unit.isEmpty() && this.radius > 0.0f;
    }

    public boolean hasTileCenter() {
        return (this.x == Integer.MIN_VALUE || this.y == Integer.MIN_VALUE) ? false : true;
    }

    public boolean hasUnitCenter() {
        return this.followId >= 0;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits + "|" + this.radius + "|" + this.x + "|" + this.y + "|" + this.followId + "|" + this.followLabel + "|" + this.speed;
    }

    public static MoonSlot deserialize(String str) {
        MoonSlot moonSlot = new MoonSlot();
        if (str == null || str.isEmpty()) {
            return moonSlot;
        }
        String[] split = str.split("\\|", -1);
        try {
            if (split.length > 0) {
                moonSlot.unit = split[0];
            }
            if (split.length > 1 && !split[1].isEmpty()) {
                moonSlot.maxUnits = Math.max(0, Integer.parseInt(split[1]));
            }
            if (split.length > 2 && !split[2].isEmpty()) {
                moonSlot.radius = Math.max(1.0f, Float.parseFloat(split[2]));
            }
            if (split.length > 4) {
                moonSlot.x = Integer.parseInt(split[3]);
                moonSlot.y = Integer.parseInt(split[4]);
            }
            if (split.length > 5 && !split[5].isEmpty()) {
                try {
                    moonSlot.followId = Integer.parseInt(split[5]);
                } catch (NumberFormatException e) {
                    moonSlot.followId = -1;
                }
            }
            if (split.length > 6) {
                moonSlot.followLabel = split[6];
            }
            if (split.length > 7 && !split[7].isEmpty()) {
                moonSlot.speed = Math.max(0.25f, Math.min(4.0f, Float.parseFloat(split[7])));
            }
        } catch (Exception e2) {
        }
        return moonSlot;
    }
}
