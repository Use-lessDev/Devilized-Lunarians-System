package lunarians.data;

public class MoonSlot {
  public static final int UNSET = -2147483648;
  
  public String unit = "";
  
  public int maxUnits = 0;
  
  public float radius = 8.0F;
  
  public float speed = 1.0F;
  
  public int x = Integer.MIN_VALUE, y = Integer.MIN_VALUE;
  
  public int followId = -1;
  
  public String followLabel = "";
  
  public MoonSlot copy() {
    MoonSlot moonSlot = new MoonSlot();
    moonSlot.unit = this.unit;
    moonSlot.maxUnits = this.maxUnits;
    moonSlot.radius = this.radius;
    moonSlot.speed = this.speed;
    moonSlot.x = this.x;
    moonSlot.y = this.y;
    moonSlot.followId = this.followId;
    moonSlot.followLabel = this.followLabel;
    return moonSlot;
  }
  
  public boolean ready() {
    return (!this.unit.isEmpty() && this.radius > 0.0F);
  }
  
  public boolean hasTileCenter() {
    return (this.x != Integer.MIN_VALUE && this.y != Integer.MIN_VALUE);
  }
  
  public boolean hasUnitCenter() {
    return (this.followId >= 0);
  }
  
  public String serialize() {
    return this.unit + "|" + this.unit + "|" + this.maxUnits + "|" + this.radius + "|" + this.x + "|" + this.y + "|" + this.followId + "|" + this.followLabel;
  }
  
  public static MoonSlot deserialize(String paramString) {
    MoonSlot moonSlot = new MoonSlot();
    if (paramString == null || paramString.isEmpty())
      return moonSlot; 
    String[] arrayOfString = paramString.split("\\|", -1);
    try {
      if (arrayOfString.length > 0)
        moonSlot.unit = arrayOfString[0]; 
      if (arrayOfString.length > 1 && !arrayOfString[1].isEmpty())
        moonSlot.maxUnits = Math.max(0, Integer.parseInt(arrayOfString[1])); 
      if (arrayOfString.length > 2 && !arrayOfString[2].isEmpty())
        moonSlot.radius = Math.max(1.0F, Float.parseFloat(arrayOfString[2])); 
      if (arrayOfString.length > 4) {
        moonSlot.x = Integer.parseInt(arrayOfString[3]);
        moonSlot.y = Integer.parseInt(arrayOfString[4]);
      } 
      if (arrayOfString.length > 5 && !arrayOfString[5].isEmpty())
        try {
          moonSlot.followId = Integer.parseInt(arrayOfString[5]);
        } catch (NumberFormatException numberFormatException) {
          moonSlot.followId = -1;
        }  
      if (arrayOfString.length > 6)
        moonSlot.followLabel = arrayOfString[6]; 
      if (arrayOfString.length > 7 && !arrayOfString[7].isEmpty())
        moonSlot.speed = Math.max(0.25F, Math.min(4.0F, Float.parseFloat(arrayOfString[7]))); 
    } catch (Exception exception) {}
    return moonSlot;
  }
}
