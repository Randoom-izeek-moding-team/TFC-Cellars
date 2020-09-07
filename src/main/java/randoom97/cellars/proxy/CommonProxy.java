package randoom97.cellars.proxy;

import net.dries007.tfc.api.recipes.anvil.AnvilRecipe;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.dries007.tfc.types.DefaultMetals;
import net.dries007.tfc.util.OreDictionaryHelper;
import net.dries007.tfc.util.forge.ForgeRule;
import net.dries007.tfc.util.skills.SmithingSkill;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import randoom97.cellars.Cellars;
import randoom97.cellars.ModBlocks;
import randoom97.cellars.ModItems;

@Mod.EventBusSubscriber
public class CommonProxy {
	public void preInit(FMLPreInitializationEvent e) {
	}
	
	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Cellars.instance,  new GuiHandler());
	}
	
	public void postInit(FMLPostInitializationEvent e) {
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		ModBlocks.registerBlocks(event);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		ModItems.registerItems(event);
	}
	
	@SubscribeEvent
	public static void registerAnvilRecipes(RegistryEvent.Register<AnvilRecipe> event) {
		event.getRegistry().register(new AnvilRecipe(
				new ResourceLocation(Cellars.MODID,"tfc_bismuth_bronze_ice_saw_blade"),
				IIngredient.of(OreDictionaryHelper.toString(new Object[] {"ingot","double", DefaultMetals.BISMUTH_BRONZE.getPath()})),
				new ItemStack(ModItems.bismuthBronzeIceSawBlade), Metal.BISMUTH_BRONZE.getTier(), SmithingSkill.Type.TOOLS,
				ForgeRule.DRAW_LAST, ForgeRule.UPSET_SECOND_LAST, ForgeRule.HIT_THIRD_LAST
				));
		event.getRegistry().register(new AnvilRecipe(
				new ResourceLocation(Cellars.MODID,"tfc_black_bronze_ice_saw_blade"),
				IIngredient.of(OreDictionaryHelper.toString(new Object[] {"ingot","double", DefaultMetals.BLACK_BRONZE.getPath()})),
				new ItemStack(ModItems.blackBronzeIceSawBlade), Metal.BLACK_BRONZE.getTier(), SmithingSkill.Type.TOOLS,
				ForgeRule.DRAW_LAST, ForgeRule.UPSET_SECOND_LAST, ForgeRule.HIT_THIRD_LAST
				));
		event.getRegistry().register(new AnvilRecipe(
				new ResourceLocation(Cellars.MODID,"tfc_blue_steel_ice_saw_blade"),
				IIngredient.of(OreDictionaryHelper.toString(new Object[] {"ingot","double", DefaultMetals.BLUE_STEEL.getPath()})),
				new ItemStack(ModItems.blueSteelIceSawBlade), Metal.BLUE_STEEL.getTier(), SmithingSkill.Type.TOOLS,
				ForgeRule.DRAW_LAST, ForgeRule.UPSET_SECOND_LAST, ForgeRule.HIT_THIRD_LAST
				));
		event.getRegistry().register(new AnvilRecipe(
				new ResourceLocation(Cellars.MODID,"tfc_bronze_ice_saw_blade"),
				IIngredient.of(OreDictionaryHelper.toString(new Object[] {"ingot","double", DefaultMetals.BRONZE.getPath()})),
				new ItemStack(ModItems.bronzeIceSawBlade), Metal.BRONZE.getTier(), SmithingSkill.Type.TOOLS,
				ForgeRule.DRAW_LAST, ForgeRule.UPSET_SECOND_LAST, ForgeRule.HIT_THIRD_LAST
				));
		event.getRegistry().register(new AnvilRecipe(
				new ResourceLocation(Cellars.MODID,"tfc_red_steel_ice_saw_blade"),
				IIngredient.of(OreDictionaryHelper.toString(new Object[] {"ingot","double", DefaultMetals.RED_STEEL.getPath()})),
				new ItemStack(ModItems.redSteelIceSawBlade), Metal.RED_STEEL.getTier(), SmithingSkill.Type.TOOLS,
				ForgeRule.DRAW_LAST, ForgeRule.UPSET_SECOND_LAST, ForgeRule.HIT_THIRD_LAST
				));
		event.getRegistry().register(new AnvilRecipe(
				new ResourceLocation(Cellars.MODID,"tfc_iron_ice_saw_blade"),
				IIngredient.of(OreDictionaryHelper.toString(new Object[] {"ingot","double", DefaultMetals.WROUGHT_IRON.getPath()})),
				new ItemStack(ModItems.ironIceSawBlade), Metal.WROUGHT_IRON.getTier(), SmithingSkill.Type.TOOLS,
				ForgeRule.DRAW_LAST, ForgeRule.UPSET_SECOND_LAST, ForgeRule.HIT_THIRD_LAST
				));
	}
}
