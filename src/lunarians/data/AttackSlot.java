package lunarians.data;

public class AttackSlot {
  public static final int UNSET = -2147483648;
  
  public String unit = "";
  
  public int maxUnits = 0;
  
  public int x = Integer.MIN_VALUE;
  
  public int y = Integer.MIN_VALUE;
  
  public float zoneRadius = 0.0F;
  
  public int standoffTiles = 0;
  
  public boolean ready() {
    return !this.unit.isEmpty();
  }
  
  public boolean hasZone() {
    return (this.x != Integer.MIN_VALUE && this.y != Integer.MIN_VALUE && this.zoneRadius > 0.0F);
  }
  
  public AttackSlot copy() {
    AttackSlot attackSlot = new AttackSlot();
    attackSlot.unit = this.unit;
    attackSlot.maxUnits = this.maxUnits;
    attackSlot.x = this.x;
    attackSlot.y = this.y;
    attackSlot.zoneRadius = this.zoneRadius;
    attackSlot.standoffTiles = this.standoffTiles;
    return attackSlot;
  }
  
  public String serialize() {
    return this.unit + "|" + this.maxUnits + "|" + this.x + "|" + this.y + "|" + this.zoneRadius + "|" + this.standoffTiles;
  }
  
  public static AttackSlot deserialize(String paramString) {
    AttackSlot attackSlot = new AttackSlot();
    if (paramString == null || paramString.isEmpty())
      return attackSlot; 
    String[] arrayOfString = paramString.split("\\|", -1);
    try {
      if (arrayOfString.length > 0)
        attackSlot.unit = arrayOfString[0]; 
      if (arrayOfString.length > 1 && !arrayOfString[1].isEmpty())
        attackSlot.maxUnits = Math.max(0, Integer.parseInt(arrayOfString[1])); 
      if (arrayOfString.length > 3) {
        attackSlot.x = Integer.parseInt(arrayOfString[2]);
        attackSlot.y = Integer.parseInt(arrayOfString[3]);
      } 
      if (arrayOfString.length > 4 && !arrayOfString[4].isEmpty())
        attackSlot.zoneRadius = Math.max(0.0F, Float.parseFloat(arrayOfString[4])); 
      if (arrayOfString.length > 5 && !arrayOfString[5].isEmpty())
        attackSlot.standoffTiles = Math.max(0, Integer.parseInt(arrayOfString[5])); 
    } catch (Exception exception) {}
    return attackSlot;
  }
}
