package billschitt.throwableRopeCoilMod.mixin;

import net.minecraft.core.block.BlockRope;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.block.entity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockRope.class, remap = false)
public class BlockRopeMixin extends Block {

    public BlockRopeMixin(String key, int id, Material material) {
        super(key, id, material);
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, Side side, int meta, EntityPlayer player, Item item) {
        if (player != null && player.isSneaking()) {
            removeConnectedRope(world, x, y, z, player);
            System.out.println("Attempting to remove rope");
        } else {
            super.onBlockDestroyedByPlayer(world, x, y, z, side, meta, player, item);
        }
    }

    @Inject(method = "getBreakResult", at = @At("HEAD"), cancellable = true)
    private void onGetBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity, CallbackInfoReturnable<ItemStack[]> cir) {
        if (dropCause == EnumDropCause.PROPER_TOOL) {
            EntityPlayer player = world.getClosestPlayer((double)x + 0.5, (double)y + 0.5, (double)z + 0.5, 5.0);
            if (player != null && player.isSneaking()) {
                cir.setReturnValue(new ItemStack[0]); // Return empty array when player is sneaking
            }
        }
    }

    private void removeConnectedRope(World world, int x, int y, int z, EntityPlayer player) {
        if (player.gamemode != Gamemode.creative) {
            addRopeToInventoryOrDrop(player, world, x, y, z);
        }
        
        while (world.getBlockId(x, y - 1, z) == Block.rope.id) {
            y--;
            world.setBlockWithNotify(x, y, z, 0);
            if (player.gamemode != Gamemode.creative) {
                addRopeToInventoryOrDrop(player, world, x, y, z);
            }
            System.out.println("Removed rope");
        }
    }

    private void addRopeToInventoryOrDrop(EntityPlayer player, World world, int x, int y, int z) {
        ItemStack ropeItem = new ItemStack(Item.rope);
        player.inventory.insertItem(ropeItem, true);
        if (ropeItem.stackSize > 0) {
            // If the stack size is still greater than 0, it means not all items were inserted
            world.dropItem(x, y, z, ropeItem);
        }
    }
}