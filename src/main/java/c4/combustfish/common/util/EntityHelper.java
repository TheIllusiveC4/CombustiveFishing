package c4.combustfish.common.util;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.client.FMLClientHandler;

public class EntityHelper {

    public static EntityPlayer initClient() {
        return FMLClientHandler.instance().getClientPlayerEntity();
    }
}
