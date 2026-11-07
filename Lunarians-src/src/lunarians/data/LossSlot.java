package lunarians.data;

/* JADX WARN: Classes with same name are omitted:
  classes.dex
 */
/* loaded from: Lunarians-v3_78_0-PC-Mobile (1).jar:lunarians/data/LossSlot.class */
public class LossSlot {
    public String item = "";
    public float perSec = 5.0f;
    public float delay = 1.0f;

    public String serialize() {
        return this.item + "|" + this.perSec + "|" + this.delay;
    }

    public static LossSlot deserialize(String str) {
        LossSlot lossSlot = new LossSlot();
        if (str == null || str.isEmpty()) {
            return lossSlot;
        }
        String[] split = str.split("\\|", -1);
        try {
            if (split.length > 0) {
                lossSlot.item = split[0];
            }
            if (split.length > 1) {
                lossSlot.perSec = Math.max(0.5f, Math.min(300.0f, Float.parseFloat(split[1])));
            }
            if (split.length > 2) {
                lossSlot.delay = Math.max(1.0f, Math.min(60.0f, Float.parseFloat(split[2])));
            }
        } catch (NumberFormatException e) {
        }
        return lossSlot;
    }
}
