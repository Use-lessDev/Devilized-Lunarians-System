package lunarians;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.Tiles;
import mindustry.world.blocks.environment.Floor;

public final class PathSafe {
  public static boolean isBadFor(Unit paramUnit, int paramInt1, int paramInt2) {
    Tiles tiles = Vars.world.tiles;
    boolean bool2 = tiles.in(paramInt1, paramInt2);
    boolean bool = true;
    if (!bool2)
      return bool; 
    World world = Vars.world;
    Tile tile = world.tile(paramInt1, paramInt2);
    if (tile == null)
      return bool; 
    boolean bool1 = paramUnit.isFlying();
    bool2 = false;
    world = null;
    if (bool1)
      return false; 
    UnitType unitType = paramUnit.type;
    if (unitType != null) {
      UnitType unitType1 = paramUnit.type;
      boolean bool4 = unitType1.naval;
      if (bool4)
        return tile.solid(); 
    } 
    boolean bool3 = tile.solid();
    if (bool3)
      return bool; 
    Floor floor = tile.floor();
    if (floor != null) {
      bool1 = floor.isLiquid;
      if (bool1) {
        float f = floor.drownTime;
        bool1 = false;
        unitType = null;
        int i = f cmp 0.0F;
        if (i > 0)
          return bool; 
      } 
    } 
    Building building = tile.build;
    if (building != null) {
      Block block = tile.build.block;
      if (block != null) {
        block = tile.build.block;
        bool3 = block.solid;
        if (bool3)
          return bool; 
      } 
    } 
    return false;
  }
  
  public static Vec2 safeNear(Unit paramUnit, float paramFloat1, float paramFloat2, float paramFloat3) {
    float f1 = 8.0F;
    float f2 = paramFloat1 / f1;
    int i = Mathf.floor(f2);
    float f3 = paramFloat2 / f1;
    int j = Mathf.floor(f3);
    boolean bool = isBadFor(paramUnit, i, j);
    float f4 = 4.0F;
    if (!bool) {
      Vec2 vec21 = new Vec2();
      paramFloat1 = i * f1 + f4;
      paramFloat2 = j * f1 + f4;
      this(paramFloat1, paramFloat2);
      return vec21;
    } 
    paramFloat3 /= f1;
    int k = Mathf.ceil(paramFloat3);
    bool = true;
    k = Math.max(bool, k);
    while (bool <= k) {
      int n;
      boolean bool1 = -bool;
      boolean bool2 = bool1;
      while (n <= bool) {
        int i1;
        boolean bool3 = bool1;
        while (true)
          i1 = bool3 + 1; 
        continue;
        n = bool2 + 1;
      } 
      int m = bool + 1;
    } 
    Vec2 vec2 = new Vec2();
    this(paramFloat1, paramFloat2);
    return vec2;
  }
}
