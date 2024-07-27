package billschitt.throwableRopeCoilMod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.item.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import billschitt.throwableRopeCoilMod.client.render.RopeCoilRenderer;
import billschitt.throwableRopeCoilMod.entity.EntityThrowableRopeCoil;
import billschitt.throwableRopeCoilMod.item.ItemThrowableRopeCoil;

import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class throwableRopeCoilModInitialiser implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {

    public static final String MOD_ID = "throwableropecoilmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item THROWABLE_ROPE_COIL = new ItemBuilder(MOD_ID)
        .setIcon("minecraft:item/rope")
        .setStackSize(1)
        .build(new ItemThrowableRopeCoil("throwableRopeCoil", 20405));



    @Override
    public void onInitialize() {
        LOGGER.info("Throwable Rope Coil Mod initialized.");
        
        EntityHelper.createEntity(
            EntityThrowableRopeCoil.class,
            20406,
            "throwable_rope_coil",
            () -> new RopeCoilRenderer(THROWABLE_ROPE_COIL)
        );


    }
    
    @Override
    public void beforeGameStart() {
    }

	@Override
	public void afterGameStart() {
	}

    @Override
    public void initNamespaces() {
        RecipeBuilder.initNameSpace(MOD_ID);
        RecipeBuilder.getItemGroup(MOD_ID, "craftableThrowableRopeCoil");
    }

    @Override
    public void onRecipesReady() {

        RecipeBuilder.Shapeless(MOD_ID)
            .addInput(Item.rope)
            .addInput(Item.rope)
            .addInput(Item.rope)
            .addInput(Item.rope)
            .create("throwable_rope_coil", THROWABLE_ROPE_COIL.getDefaultStack());

        //System.out.println("Throwable Rope Coil Recipe created");
    }

}
