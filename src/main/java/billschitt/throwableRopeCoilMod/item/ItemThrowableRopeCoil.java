package billschitt.throwableRopeCoilMod.item;

import java.util.Random;

import billschitt.throwableRopeCoilMod.entity.EntityThrowableRopeCoil;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;

public class ItemThrowableRopeCoil extends Item {
    public ItemThrowableRopeCoil(String name, int id) {
        super(name, id);
        this.setMaxStackSize(16);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (!world.isClientSide) {
            world.playSoundAtEntity(entityplayer, entityplayer, "random.bow", 0.5F, 0.4F / (new Random().nextFloat() * 0.4F + 0.8F));
            EntityThrowableRopeCoil ropeCoil = new EntityThrowableRopeCoil(world, entityplayer);
            
            float pitch = entityplayer.xRot;
            float yaw = entityplayer.yRot;
            float f = 0.4F;
            double motionX = -MathHelper.sin(yaw / 180.0F * (float)Math.PI) * MathHelper.cos(pitch / 180.0F * (float)Math.PI) * f;
            double motionZ = MathHelper.cos(yaw / 180.0F * (float)Math.PI) * MathHelper.cos(pitch / 180.0F * (float)Math.PI) * f;
            double motionY = -MathHelper.sin(pitch / 180.0F * (float)Math.PI) * f;
            
            ropeCoil.setHeading(motionX, motionY, motionZ, 1.5F, 1.0F);
            world.entityJoinedWorld(ropeCoil);
            itemstack.consumeItem(entityplayer);
        }
        return itemstack;
    }
}