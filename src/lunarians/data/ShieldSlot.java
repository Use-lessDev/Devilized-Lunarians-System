package lunarians.data;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/data/ShieldSlot.class */
public class ShieldSlot {
    public String unit = "";
    public int maxUnits = 0;
    public int followId = -1;
    public String followLabel = "";

    public boolean ready() {
        return !this.unit.isEmpty() && this.followId >= 0;
    }

    public ShieldSlot copy() {
        ShieldSlot shieldSlot = new ShieldSlot();
        shieldSlot.unit = this.unit;
        shieldSlot.maxUnits = this.maxUnits;
        shieldSlot.followId = this.followId;
        shieldSlot.followLabel = this.followLabel;
        return shieldSlot;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits + "|" + this.followId + "|" + this.followLabel;
    }

    public static ShieldSlot deserialize(String str) {
        ShieldSlot shieldSlot = new ShieldSlot();
        if (str == null || str.isEmpty()) {
            return shieldSlot;
        }
        String[] split = str.split("\\|", -1);
        try {
            if (split.length > 0) {
                shieldSlot.unit = split[0];
            }
            if (split.length > 1 && !split[1].isEmpty()) {
                shieldSlot.maxUnits = Math.max(0, Integer.parseInt(split[1]));
            }
            if (split.length > 2 && !split[2].isEmpty()) {
                try {
                    shieldSlot.followId = Integer.parseInt(split[2]);
                } catch (NumberFormatException e) {
                    shieldSlot.followId = -1;
                    shieldSlot.followLabel = split[2];
                }
            }
            if (split.length > 3) {
                shieldSlot.followLabel = split[3];
            }
        } catch (Exception e2) {
        }
        return shieldSlot;
    }
}
