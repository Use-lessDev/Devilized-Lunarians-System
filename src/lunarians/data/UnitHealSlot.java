package lunarians.data;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/data/UnitHealSlot.class */
public class UnitHealSlot {
    public String unit = "";
    public int maxUnits = 0;

    public boolean ready() {
        return !this.unit.isEmpty();
    }

    public UnitHealSlot copy() {
        UnitHealSlot unitHealSlot = new UnitHealSlot();
        unitHealSlot.unit = this.unit;
        unitHealSlot.maxUnits = this.maxUnits;
        return unitHealSlot;
    }

    public String serialize() {
        return this.unit + "|" + this.maxUnits;
    }

    public static UnitHealSlot deserialize(String str) {
        UnitHealSlot unitHealSlot = new UnitHealSlot();
        if (str == null || str.isEmpty()) {
            return unitHealSlot;
        }
        String[] split = str.split("\\|", -1);
        try {
            if (split.length > 0) {
                unitHealSlot.unit = split[0];
            }
            if (split.length > 1 && !split[1].isEmpty()) {
                unitHealSlot.maxUnits = Math.max(0, Integer.parseInt(split[1]));
            }
        } catch (Exception e) {
        }
        return unitHealSlot;
    }
}
