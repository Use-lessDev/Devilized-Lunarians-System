package lunarians;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.gen.Unit;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

public final class PathSafe {
  public static boolean isBadFor(Unit paramUnit, int paramInt1, int paramInt2) {
    if (!Vars.world.tiles.in(paramInt1, paramInt2))
      return true; 
    Tile tile = Vars.world.tile(paramInt1, paramInt2);
    if (tile == null)
      return true; 
    if (paramUnit.isFlying())
      return false; 
    if (paramUnit.type != null && paramUnit.type.naval)
      return tile.solid(); 
    if (tile.solid())
      return true; 
    Floor floor = tile.floor();
    if (floor != null && floor.isLiquid && floor.drownTime > 0.0F)
      return true; 
    if (tile.build != null && tile.build.block != null && tile.build.block.solid)
      return true; 
    return false;
  }
  
  public static Vec2 safeNear(Unit paramUnit, float paramFloat1, float paramFloat2, float paramFloat3) {
    int i = Mathf.floor(paramFloat1 / 8.0F);
    int j = Mathf.floor(paramFloat2 / 8.0F);
    if (!isBadFor(paramUnit, i, j))
      return new Vec2(i * 8.0F + 4.0F, j * 8.0F + 4.0F); 
    int k = Math.max(1, Mathf.ceil(paramFloat3 / 8.0F));
    for (byte b = 1; b <= k; b++) {
      for (byte b1 = -b; b1 <= b; b1++) {
        for (byte b2 = -b; b2 <= b; b2++) {
          if (Math.abs(b1) == b || Math.abs(b2) == b) {
            int m = i + b1, n = j + b2;
            if (!isBadFor(paramUnit, m, n))
              return new Vec2(m * 8.0F + 4.0F, n * 8.0F + 4.0F); 
          } 
        } 
      } 
    } 
    return new Vec2(paramFloat1, paramFloat2);
  }
}
