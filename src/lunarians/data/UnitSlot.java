package lunarians.data;

import arc.struct.Seq;

public class UnitSlot {
  public String unit = "";
  
  public String command = "";
  
  public final Seq<String> items = new Seq();
  
  public int maxUnits = 0;
  
  public boolean ready() {
    return !this.unit.isEmpty();
  }
  
  public UnitSlot copy() {
    UnitSlot unitSlot = new UnitSlot();
    unitSlot.unit = this.unit;
    unitSlot.command = this.command;
    unitSlot.maxUnits = this.maxUnits;
    unitSlot.items.addAll(this.items);
    return unitSlot;
  }
  
  public String serialize() {
    StringBuilder stringBuilder = new StringBuilder();
    for (byte b = 0; b < this.items.size; ) {
      if (b > 0)
        stringBuilder.append(','); 
      stringBuilder.append((String)this.items.get(b));
      b++;
    } 
    return this.unit + "|" + this.unit + "|" + this.command + "|" + String.valueOf(stringBuilder);
  }
  
  public static UnitSlot deserialize(String paramString) {
    UnitSlot unitSlot = new UnitSlot();
    if (paramString == null || paramString.isEmpty())
      return unitSlot; 
    String[] arrayOfString = paramString.split("\\|", -1);
    try {
      if (arrayOfString.length > 0)
        unitSlot.unit = arrayOfString[0]; 
      if (arrayOfString.length > 1)
        unitSlot.command = arrayOfString[1]; 
      if (arrayOfString.length > 2 && !arrayOfString[2].isEmpty())
        for (String str : arrayOfString[2].split(",")) {
          if (!str.isEmpty())
            unitSlot.items.add(str); 
        }  
      if (arrayOfString.length > 3 && !arrayOfString[3].isEmpty())
        unitSlot.maxUnits = Math.max(0, Integer.parseInt(arrayOfString[3])); 
    } catch (Exception exception) {}
    return unitSlot;
  }
}
