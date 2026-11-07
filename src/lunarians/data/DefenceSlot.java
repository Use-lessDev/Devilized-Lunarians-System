package lunarians.data;

public class DefenceSlot {
  public static final int UNSET = -2147483648;
  
  public String unit = "";
  
  public int maxUnits = 0;
  
  public int x = Integer.MIN_VALUE;
  
  public int y = Integer.MIN_VALUE;
  
  public float radius = 12.0F;
  
  public boolean ready() {
    return (!this.unit.isEmpty() && this.x != Integer.MIN_VALUE && this.y != Integer.MIN_VALUE && this.radius > 0.0F);
  }
  
  public DefenceSlot copy() {
    DefenceSlot defenceSlot = new DefenceSlot();
    defenceSlot.unit = this.unit;
    defenceSlot.maxUnits = this.maxUnits;
    defenceSlot.x = this.x;
    defenceSlot.y = this.y;
    defenceSlot.radius = this.radius;
    return defenceSlot;
  }
  
  public String serialize() {
    return this.unit + "|" + this.unit + "|" + this.maxUnits + "|" + this.x + "|" + this.y;
  }
  
  public static DefenceSlot deserialize(String paramString) {
    DefenceSlot defenceSlot = new DefenceSlot();
    if (paramString == null || paramString.isEmpty())
      return defenceSlot; 
    String[] arrayOfString = paramString.split("\\|", -1);
    try {
      if (arrayOfString.length > 0)
        defenceSlot.unit = arrayOfString[0]; 
      if (arrayOfString.length > 1 && !arrayOfString[1].isEmpty())
        defenceSlot.maxUnits = Math.max(0, Integer.parseInt(arrayOfString[1])); 
      if (arrayOfString.length > 3) {
        defenceSlot.x = Integer.parseInt(arrayOfString[2]);
        defenceSlot.y = Integer.parseInt(arrayOfString[3]);
      } 
      if (arrayOfString.length > 4 && !arrayOfString[4].isEmpty())
        defenceSlot.radius = Math.max(1.0F, Float.parseFloat(arrayOfString[4])); 
    } catch (Exception exception) {}
    return defenceSlot;
  }
}
