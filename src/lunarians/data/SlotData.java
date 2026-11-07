package lunarians.data;

public class SlotData {
  public String schematicBase64 = null;
  
  public String iconName = null;
  
  public boolean hasSchematic() {
    return (this.schematicBase64 != null && !this.schematicBase64.isEmpty());
  }
  
  public void clear() {
    this.schematicBase64 = null;
    this.iconName = null;
  }
  
  public void set(String paramString1, String paramString2) {
    this.schematicBase64 = paramString1;
    this.iconName = paramString2;
  }
  
  public String serialize() {
    return ((this.schematicBase64 == null) ? "" : this.schematicBase64) + "|||" + ((this.iconName != null) ? this.iconName : "");
  }
  
  public static SlotData deserialize(String paramString) {
    SlotData slotData = new SlotData();
    if (paramString == null || paramString.isEmpty())
      return slotData; 
    String[] arrayOfString = paramString.split("\\|\\|\\|", 2);
    if (arrayOfString.length > 0 && !arrayOfString[0].isEmpty())
      slotData.schematicBase64 = arrayOfString[0]; 
    if (arrayOfString.length > 1 && !arrayOfString[1].isEmpty())
      slotData.iconName = arrayOfString[1]; 
    return slotData;
  }
}
