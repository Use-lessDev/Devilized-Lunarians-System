package lunarians.data;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/data/HealSlot.class */
public class HealSlot {
    public String unit = "";
    public int maxUnits = 0;

    public boolean ready() {
        return !this.unit.isEmpty();
    }

    public HealSlot copy() {
        HealSlot healSlot = new HealSlot();
        healSlot.unit = this.unit;
        healSlot.maxUnits = this.maxUnits;
        return healSlot;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits;
    }

    public static HealSlot deserialize(String str) {
        HealSlot healSlot = new HealSlot();
        if (str == null || str.isEmpty()) {
            return healSlot;
        }
        String[] split = str.split("\\|", -1);
        try {
            if (split.length > 0) {
                healSlot.unit = split[0];
            }
            if (split.length > 1 && !split[1].isEmpty()) {
                healSlot.maxUnits = Math.max(0, Integer.parseInt(split[1]));
            }
        } catch (Exception e) {
        }
        return healSlot;
    }
}
