package lunarians.data;

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
    return this.unit + "|" + this.unit;
  }
  
  public static HealSlot deserialize(String paramString) {
    HealSlot healSlot = new HealSlot();
    if (paramString == null || paramString.isEmpty())
      return healSlot; 
    String[] arrayOfString = paramString.split("\\|", -1);
    try {
      if (arrayOfString.length > 0)
        healSlot.unit = arrayOfString[0]; 
      if (arrayOfString.length > 1 && !arrayOfString[1].isEmpty())
        healSlot.maxUnits = Math.max(0, Integer.parseInt(arrayOfString[1])); 
    } catch (Exception exception) {}
    return healSlot;
  }
}
