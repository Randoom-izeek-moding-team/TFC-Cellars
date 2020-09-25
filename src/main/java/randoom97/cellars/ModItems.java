package randoom97.cellars;

import net.dries007.tfc.api.types.Metal;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import randoom97.cellars.blocks.BlockIceBunker;
import randoom97.cellars.blocks.CellarBlock;
import randoom97.cellars.blocks.BasicElectricCooler;
import randoom97.cellars.blocks.CellarShelf;
import randoom97.cellars.blocks.IndustrialCellarBlock;
import randoom97.cellars.items.IceChunks;
import randoom97.cellars.items.IceSaw;
import randoom97.cellars.items.IceSawBlade;
import randoom97.cellars.items.ItemCellarDoor;

public class ModItems {

	@GameRegistry.ObjectHolder("cellars:item_cellar_door")
	public static ItemCellarDoor itemCellarDoor;
	
	@GameRegistry.ObjectHolder("cellars:ice_chunks")
	public static IceChunks iceChunks;

	// saws
	@GameRegistry.ObjectHolder("cellars:tfc_bismuth_bronze_ice_saw")
	public static IceSaw bismuthBronzeIceSaw;

	@GameRegistry.ObjectHolder("cellars:tfc_black_bronze_ice_saw")
	public static IceSaw blackBronzeIceSaw;

	@GameRegistry.ObjectHolder("cellars:tfc_blue_steel_ice_saw")
	public static IceSaw blueSteelIceSaw;

	@GameRegistry.ObjectHolder("cellars:tfc_bronze_ice_saw")
	public static IceSaw bronzeIceSaw;

	@GameRegistry.ObjectHolder("cellars:tfc_red_steel_ice_saw")
	public static IceSaw redSteelIceSaw;

	@GameRegistry.ObjectHolder("cellars:tfc_iron_ice_saw")
	public static IceSaw ironIceSaw;

	// saw blades
	@GameRegistry.ObjectHolder("cellars:tfc_bismuth_bronze_ice_saw_blade")
	public static IceSawBlade bismuthBronzeIceSawBlade;

	@GameRegistry.ObjectHolder("cellars:tfc_black_bronze_ice_saw_blade")
	public static IceSawBlade blackBronzeIceSawBlade;

	@GameRegistry.ObjectHolder("cellars:tfc_blue_steel_ice_saw_blade")
	public static IceSawBlade blueSteelIceSawBlade;

	@GameRegistry.ObjectHolder("cellars:tfc_bronze_ice_saw_blade")
	public static IceSawBlade bronzeIceSawBlade;

	@GameRegistry.ObjectHolder("cellars:tfc_red_steel_ice_saw_blade")
	public static IceSawBlade redSteelIceSawBlade;

	@GameRegistry.ObjectHolder("cellars:tfc_iron_ice_saw_blade")
	public static IceSawBlade ironIceSawBlade;

	public static void initModels() {
		itemCellarDoor.initModel();
		iceChunks.initModel();

		// ice saws
		bismuthBronzeIceSaw.initModel();
		blackBronzeIceSaw.initModel();
		blueSteelIceSaw.initModel();
		bronzeIceSaw.initModel();
		redSteelIceSaw.initModel();
		ironIceSaw.initModel();

		// ice saw blades
		bismuthBronzeIceSawBlade.initModel();
		blackBronzeIceSawBlade.initModel();
		blueSteelIceSawBlade.initModel();
		bronzeIceSawBlade.initModel();
		redSteelIceSawBlade.initModel();
		ironIceSawBlade.initModel();
	}

	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new ItemBlock(ModBlocks.cellarBlock).setRegistryName(CellarBlock.CELLAR_BLOCK));
		event.getRegistry().register(new ItemBlock(ModBlocks.industrialCellarBlock).setRegistryName(IndustrialCellarBlock.INDUSTRIAL_CELLAR_BLOCK));
		
		event.getRegistry().register(new ItemCellarDoor());
		event.getRegistry()
				.register(new ItemBlock(ModBlocks.blockIceBunker).setRegistryName(BlockIceBunker.BLOCK_ICE_BUNKER));
		event.getRegistry()
				.register(new ItemBlock(ModBlocks.basicElectricCooler).setRegistryName(BasicElectricCooler.BASIC_ELECTRIC_COOLER));
		event.getRegistry().register(new ItemBlock(ModBlocks.cellarShelf).setRegistryName(CellarShelf.CELLAR_SHELF));
		event.getRegistry().register(new IceChunks());

		// ice saws
		event.getRegistry().register(new IceSaw(Metal.BISMUTH_BRONZE));
		event.getRegistry().register(new IceSaw(Metal.BLACK_BRONZE));
		event.getRegistry().register(new IceSaw(Metal.BLUE_STEEL));
		event.getRegistry().register(new IceSaw(Metal.BRONZE));
		event.getRegistry().register(new IceSaw(Metal.RED_STEEL));
		event.getRegistry().register(new IceSaw(Metal.WROUGHT_IRON));

		// ice saw blades
		event.getRegistry().register(new IceSawBlade(Metal.BISMUTH_BRONZE));
		event.getRegistry().register(new IceSawBlade(Metal.BLACK_BRONZE));
		event.getRegistry().register(new IceSawBlade(Metal.BLUE_STEEL));
		event.getRegistry().register(new IceSawBlade(Metal.BRONZE));
		event.getRegistry().register(new IceSawBlade(Metal.RED_STEEL));
		event.getRegistry().register(new IceSawBlade(Metal.WROUGHT_IRON));

	}

}
