package billschitt.throwableRopeCoilMod.entity;

import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockRope;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.projectile.EntityProjectile;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;

public class EntityThrowableRopeCoil extends EntityProjectile {
    private static final int MAX_ROPE_LENGTH = 4;

    public EntityThrowableRopeCoil(World world) {
        super(world);
        this.modelItem = Item.ammoSnowball;
    }

    public EntityThrowableRopeCoil(World world, EntityLiving owner) {
        super(world, owner);
        this.modelItem = Item.ammoSnowball;
    }

    @Override
    public void onHit(HitResult hitResult) {
        if (!this.world.isClientSide && hitResult.hitType == HitResult.HitType.TILE) {
            int x = hitResult.x;
            int y = hitResult.y;
            int z = hitResult.z;
            Side side = hitResult.side;
    
            int ropeCoilsToDrop = 0;
            int ropesToPlace = MAX_ROPE_LENGTH;
    
            // If we hit a rope block, move to the bottom of the rope pillar
            if (this.world.getBlockId(x, y, z) == Block.rope.id) {
                while (y > 0 && this.world.getBlockId(x, y - 1, z) == Block.rope.id) {
                    y--;
                }
                y--; // Move one block below the existing rope pillar
            } else {
                // Adjust the starting position based on the hit side
                switch (side) {
                    case NORTH: z--; break;
                    case SOUTH: z++; break;
                    case WEST:  x--; break;
                    case EAST:  x++; break;
                    case TOP:
                        ropeCoilsToDrop = MAX_ROPE_LENGTH;
                        ropesToPlace = 0;
                        break;
                    case BOTTOM: y--; break;
                    case NONE: break;
                }
            }
    
            // Place rope blocks downwards
            while (ropesToPlace > 0 && y > 0) {
                if (this.world.getBlockId(x, y, z) == 0) {
                    this.world.setBlock(x, y, z, Block.rope.id);
                    updateRopeConnections(x, y, z);
                    ropesToPlace--;
                    y--;
                } else if (this.world.getBlockId(x, y, z) == Block.rope.id) {
                    // We've hit another rope pillar, continue downwards
                    y--;
                } else {
                    // We've hit an obstruction, stop placing rope
                    ropeCoilsToDrop = ropesToPlace;
                    break;
                }
            }
    
            // Force a chunk update
            this.world.markBlocksDirty(x - 1, y, z - 1, x + 1, y + MAX_ROPE_LENGTH, z + 1);
    
            // Drop any unused rope coils
            if (ropeCoilsToDrop > 0) {
                dropRopeCoils(x, y + 1, z, ropeCoilsToDrop);
            }
        }
    
        this.remove();
    }
    
    private void dropRopeCoils(int x, int y, int z, int count) {
        for (int i = 0; i < count; i++) {
            ItemStack itemStack = new ItemStack(Item.rope, 1);
            EntityItem entityItem = new EntityItem(this.world, x + 0.5, y + 0.5, z + 0.5, itemStack);
            
            // Set minimal motion
            entityItem.xd = 0; 
            entityItem.yd = 0.05;  // Slight upward motion to prevent items from sinking into the ground
            entityItem.zd = 0; 
            
            // Set a small random offset for position to prevent items from stacking exactly
            entityItem.yd = 0.25;
            
            this.world.entityJoinedWorld(entityItem);
        }
    }

    private void updateRopeConnections(int x, int y, int z) {
        updateSingleRopeBlock(x, y, z);
        updateSingleRopeBlock(x + 1, y, z);
        updateSingleRopeBlock(x - 1, y, z);
        updateSingleRopeBlock(x, y, z + 1);
        updateSingleRopeBlock(x, y, z - 1);
        updateSingleRopeBlock(x, y + 1, z);
        updateSingleRopeBlock(x, y - 1, z);
    }

    private void updateSingleRopeBlock(int x, int y, int z) {
        if (this.world.getBlockId(x, y, z) == Block.rope.id) {
            BlockRope ropeBlock = (BlockRope)Block.rope;
            ropeBlock.setBlockBoundsBasedOnState(this.world, x, y, z);
            this.world.notifyBlockChange(x, y, z, Block.rope.id);
            
            // Force an update of surrounding blocks
            this.world.notifyBlocksOfNeighborChange(x, y, z, Block.rope.id);
        }
    }
}
