package billschitt.throwableRopeCoilMod.client.render;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import org.lwjgl.opengl.GL11;

public class RopeCoilRenderer extends EntityRenderer<Entity> {
    private IconCoordinate itemIconIndex;

    public RopeCoilRenderer(Item item) {
        this(((ItemModel)ItemModelDispatcher.getInstance().getDispatch(item)).getIcon(null, item.getDefaultStack()));
    }

    public RopeCoilRenderer(IconCoordinate i) {
        this.itemIconIndex = i;
    }

    @Override
    public void doRender(Tessellator tessellator, Entity entity, double x, double y, double z, float yaw, float partialTick) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glEnable(32826);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.itemIconIndex.parentAtlas.bindTexture();
        float f2 = (float)this.itemIconIndex.getIconUMin();
        float f3 = (float)this.itemIconIndex.getIconUMax();
        float f4 = (float)this.itemIconIndex.getIconVMin();
        float f5 = (float)this.itemIconIndex.getIconVMax();
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        GL11.glRotatef(180.0F - this.renderDispatcher.viewLerpYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderDispatcher.viewLerpPitch, 1.0F, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV((double)(0.0F - f7), (double)(0.0F - f8), 0.0, (double)f2, (double)f5);
        tessellator.addVertexWithUV((double)(f6 - f7), (double)(0.0F - f8), 0.0, (double)f3, (double)f5);
        tessellator.addVertexWithUV((double)(f6 - f7), (double)(1.0F - f8), 0.0, (double)f3, (double)f4);
        tessellator.addVertexWithUV((double)(0.0F - f7), (double)(1.0F - f8), 0.0, (double)f2, (double)f4);
        tessellator.draw();
        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }
}