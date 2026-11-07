package lunarians.data;

public class ShieldSlot {
  public String unit = "";
  
  public int maxUnits = 0;
  
  public int followId = -1;
  
  public String followLabel = "";
  
  public boolean ready() {
    return (!this.unit.isEmpty() && this.followId >= 0);
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
    return this.unit + "|" + this.unit + "|" + this.maxUnits + "|" + this.followId;
  }
  
  public static ShieldSlot deserialize(String paramString) {
    ShieldSlot shieldSlot = new ShieldSlot();
    if (paramString == null || paramString.isEmpty())
      return shieldSlot; 
    String[] arrayOfString = paramString.split("\\|", -1);
    try {
      if (arrayOfString.length > 0)
        shieldSlot.unit = arrayOfString[0]; 
      if (arrayOfString.length > 1 && !arrayOfString[1].isEmpty())
        shieldSlot.maxUnits = Math.max(0, Integer.parseInt(arrayOfString[1])); 
      if (arrayOfString.length > 2 && !arrayOfString[2].isEmpty())
        try {
          shieldSlot.followId = Integer.parseInt(arrayOfString[2]);
        } catch (NumberFormatException numberFormatException) {
          shieldSlot.followId = -1;
          shieldSlot.followLabel = arrayOfString[2];
        }  
      if (arrayOfString.length > 3)
        shieldSlot.followLabel = arrayOfString[3]; 
    } catch (Exception exception) {}
    return shieldSlot;
  }
}
