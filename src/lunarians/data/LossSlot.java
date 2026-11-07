package lunarians.data;

public class LossSlot {
  public String item = "";
  
  public float perSec = 5.0F;
  
  public float delay = 1.0F;
  
  public String serialize() {
    return this.item + "|" + this.perSec + "|" + this.delay;
  }
  
  public static LossSlot deserialize(String paramString) {
    LossSlot lossSlot = new LossSlot();
    if (paramString == null || paramString.isEmpty())
      return lossSlot; 
    String[] arrayOfString = paramString.split("\\|", -1);
    try {
      if (arrayOfString.length > 0)
        lossSlot.item = arrayOfString[0]; 
      if (arrayOfString.length > 1)
        lossSlot.perSec = Math.max(0.5F, Math.min(300.0F, Float.parseFloat(arrayOfString[1]))); 
      if (arrayOfString.length > 2)
        lossSlot.delay = Math.max(1.0F, Math.min(60.0F, Float.parseFloat(arrayOfString[2]))); 
    } catch (NumberFormatException numberFormatException) {}
    return lossSlot;
  }
}
