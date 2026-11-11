/*
 * Decompiled with CFR 0.152.
 */
package lunarians.data;

public class LossSlot {
    public String item = "";
    public float perSec = 5.0f;
    public float delay = 1.0f;

    public String serialize() {
        return this.item + "|" + this.perSec + "|" + this.delay;
    }

    public static LossSlot deserialize(String string) {
        LossSlot lossSlot = new LossSlot();
        if (string == null || string.isEmpty()) {
            return lossSlot;
        }
        String[] stringArray = string.split("\\|", -1);
        try {
            if (stringArray.length > 0) {
                lossSlot.item = stringArray[0];
            }
            if (stringArray.length > 1) {
                lossSlot.perSec = Math.max(0.5f, Math.min(300.0f, Float.parseFloat(stringArray[1])));
            }
            if (stringArray.length > 2) {
                lossSlot.delay = Math.max(1.0f, Math.min(60.0f, Float.parseFloat(stringArray[2])));
            }
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        return lossSlot;
    }
}

