package top.theillusivec4.combustivefishing.common.registry;

import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;
import top.theillusivec4.combustivefishing.CombustiveFishing;

@ObjectHolder(value = CombustiveFishing.MODID)
public class CombustiveFishingItems {

  @ObjectHolder(RegistryReference.COMBUSTIVE_COD_BUCKET)
  public static final Item COMBUSTIVE_COD_BUCKET;

  @ObjectHolder(RegistryReference.COMBUSTIVE_COD)
  public static final Item COMBUSTIVE_COD;

  @ObjectHolder(RegistryReference.COOLED_COD)
  public static final Item COOLED_COD;

  @ObjectHolder(RegistryReference.BONE_FISH)
  public static final Item BONE_FISH;

  @ObjectHolder(RegistryReference.SWORDFISH_BILL)
  public static final Item SWORDFISH_BILL;

  @ObjectHolder(RegistryReference.COOLED_BILL)
  public static final Item COOLED_BILL;

  static {
    COMBUSTIVE_COD_BUCKET = null;
    COMBUSTIVE_COD = null;
    COOLED_COD = null;
    BONE_FISH = null;
    SWORDFISH_BILL = null;
    COOLED_BILL = null;
  }
}
