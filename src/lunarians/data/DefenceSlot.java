package lunarians.data;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/data/DefenceSlot.class */
public class DefenceSlot {
    public static final int UNSET = Integer.MIN_VALUE;
    public String unit = "";
    public int maxUnits = 0;
    public int x = Integer.MIN_VALUE;
    public int y = Integer.MIN_VALUE;
    public float radius = 12.0f;

    public boolean ready() {
        return (this.unit.isEmpty() || this.x == Integer.MIN_VALUE || this.y == Integer.MIN_VALUE || this.radius <= 0.0f) ? false : true;
    }

    public DefenceSlot copy() {
        DefenceSlot defenceSlot = new DefenceSlot();
        defenceSlot.unit = this.unit;
        defenceSlot.maxUnits = this.maxUnits;
        defenceSlot.x = this.x;
        defenceSlot.y = this.y;
        defenceSlot.radius = this.radius;
        return defenceSlot;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits + "|" + this.x + "|" + this.y + "|" + this.radius;
    }

    public static DefenceSlot deserialize(String str) {
        DefenceSlot defenceSlot = new DefenceSlot();
        if (str == null || str.isEmpty()) {
            return defenceSlot;
        }
        String[] split = str.split("\\|", -1);
        try {
            if (split.length > 0) {
                defenceSlot.unit = split[0];
            }
            if (split.length > 1 && !split[1].isEmpty()) {
                defenceSlot.maxUnits = Math.max(0, Integer.parseInt(split[1]));
            }
            if (split.length > 3) {
                defenceSlot.x = Integer.parseInt(split[2]);
                defenceSlot.y = Integer.parseInt(split[3]);
            }
            if (split.length > 4 && !split[4].isEmpty()) {
                defenceSlot.radius = Math.max(1.0f, Float.parseFloat(split[4]));
            }
        } catch (Exception e) {
        }
        return defenceSlot;
    }
}
