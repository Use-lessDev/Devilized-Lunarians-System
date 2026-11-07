package lunarians.data;

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
    return this.unit + "|" + this.unit;
  }
  
  public static UnitHealSlot deserialize(String paramString) {
    UnitHealSlot unitHealSlot = new UnitHealSlot();
    if (paramString == null || paramString.isEmpty())
      return unitHealSlot; 
    String[] arrayOfString = paramString.split("\\|", -1);
    try {
      if (arrayOfString.length > 0)
        unitHealSlot.unit = arrayOfString[0]; 
      if (arrayOfString.length > 1 && !arrayOfString[1].isEmpty())
        unitHealSlot.maxUnits = Math.max(0, Integer.parseInt(arrayOfString[1])); 
    } catch (Exception exception) {}
    return unitHealSlot;
  }
}
