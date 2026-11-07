package lunarians.data;

public class UnitHealSlot {
  public int maxUnits = 0;
  
  public String unit = "";
  
  public static UnitHealSlot deserialize(String paramString) {
    UnitHealSlot unitHealSlot = new UnitHealSlot();
    this();
    if (paramString != null) {
      boolean bool = paramString.isEmpty();
      if (!bool) {
        String str = "\\|";
        byte b = -1;
        String[] arrayOfString = paramString.split(str, b);
        try {
          int i = arrayOfString.length;
          b = 0;
          if (i > 0) {
            str = arrayOfString[0];
            unitHealSlot.unit = str;
          } 
          i = arrayOfString.length;
          byte b1 = 1;
          if (i > b1) {
            str = arrayOfString[b1];
            boolean bool1 = str.isEmpty();
            if (!bool1) {
              String str1 = arrayOfString[b1];
              int j = Integer.parseInt(str1);
              j = Math.max(0, j);
              unitHealSlot.maxUnits = j;
            } 
          } 
        } catch (Exception exception) {}
      } 
    } 
    return unitHealSlot;
  }
  
  public UnitHealSlot copy() {
    UnitHealSlot unitHealSlot = new UnitHealSlot();
    this();
    String str = this.unit;
    unitHealSlot.unit = str;
    int i = this.maxUnits;
    unitHealSlot.maxUnits = i;
    return unitHealSlot;
  }
  
  public boolean ready() {
    return this.unit.isEmpty() ^ true;
  }
  
  public String serialize() {
    String str = this.unit;
    int i = this.maxUnits;
    StringBuilder stringBuilder = new StringBuilder();
    this();
    stringBuilder.append(str);
    stringBuilder.append("|");
    stringBuilder.append(i);
    return stringBuilder.toString();
  }
}
