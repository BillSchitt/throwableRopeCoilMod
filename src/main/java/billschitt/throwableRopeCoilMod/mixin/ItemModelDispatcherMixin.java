package billschitt.throwableRopeCoilMod.mixin;

import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelDispatcher.class)
public class ItemModelDispatcherMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectCustomRopeTexture(CallbackInfo ci) {
        ItemModelDispatcher dispatcher = (ItemModelDispatcher) (Object) this;
        ItemModelStandard ropeModel = new ItemModelStandard(Item.rope, "minecraft");
        IconCoordinate customRopeTexture = TextureRegistry.getTexture("throwableropecoilmod:item/rope_singular");
        ropeModel.icon = customRopeTexture;
        dispatcher.addDispatch(ropeModel);
    }
}