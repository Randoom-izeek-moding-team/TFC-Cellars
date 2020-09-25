package randoom97.cellars;

import randoom97.cellars.blocks.BasicElectricCooler;
import randoom97.cellars.blocks.BlockIceBunker;
import randoom97.cellars.blocks.CellarBlock;
import randoom97.cellars.blocks.CellarShelf;
import randoom97.cellars.blocks.IndustrialCellarBlock;
import randoom97.cellars.door.CellarDoor;
import randoom97.cellars.tiles.TileCellarShelf;
import randoom97.cellars.tiles.TileIceBunker;
import randoom97.cellars.tiles.TileBasicElectricCooler;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	@GameRegistry.ObjectHolder("cellars:cellar_block")
	public static CellarBlock cellarBlock;
	
	@GameRegistry.ObjectHolder("cellars:industrial_cellar_block")
	public static IndustrialCellarBlock industrialCellarBlock;
	
	@GameRegistry.ObjectHolder("cellars:cellar_door")
	public static CellarDoor cellarDoor;
	
	@GameRegistry.ObjectHolder("cellars:block_ice_bunker")
	public static BlockIceBunker blockIceBunker;
	
	@GameRegistry.ObjectHolder("cellars:basic_electric_cooler")
	public static BasicElectricCooler basicElectricCooler;
	
	@GameRegistry.ObjectHolder("cellars:cellar_shelf")
	public static CellarShelf cellarShelf;
	
	public static void initModels() {
		cellarBlock.initModel();
		industrialCellarBlock.initModel();
		basicElectricCooler.initModel();
		//cellarDoor.initModel();
		blockIceBunker.initModel();
		cellarShelf.initModel();
	}
	
	@SuppressWarnings("deprecation")
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new CellarBlock());
		event.getRegistry().register(new IndustrialCellarBlock());
		event.getRegistry().register(new CellarDoor());
		event.getRegistry().register(new BlockIceBunker());
		event.getRegistry().register(new BasicElectricCooler());
		event.getRegistry().register(new CellarShelf());
		GameRegistry.registerTileEntity(TileIceBunker.class, Cellars.MODID + "_ice_bunker");
		GameRegistry.registerTileEntity(TileCellarShelf.class, Cellars.MODID+"_cellar_shelf");
		GameRegistry.registerTileEntity(TileBasicElectricCooler.class, Cellars.MODID+"_basic_electric_cooler");
	}
	
}
