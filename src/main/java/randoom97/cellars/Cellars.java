package randoom97.cellars;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import randoom97.cellars.proxy.CommonProxy;
import randoom97.cellars.registry.CellarFoodTrait;

@Mod(modid = Cellars.MODID, name = Cellars.MODNAME, version = Cellars.MODVERSION, dependencies = "required-after:forge@[14.23.5.2838,)", useMetadata = true )
public class Cellars {
	
	public static final String MODID = "cellars";
	public static final String MODNAME = "TFC Cellars Addon";
	public static final String MODVERSION = "0.0.1";
	
	@SidedProxy(clientSide = "randoom97.cellars.proxy.ClientProxy", serverSide = "randoom97.cellars.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	public static CreativeTabs tabCellars = new CreativeTabs("Cellars") {
		@Override
		public ItemStack createIcon() { return new ItemStack(ModBlocks.cellarBlock); }
	};
	
	@Mod.Instance
	public static Cellars instance;
	
	public static Logger logger;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);;
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
		CellarFoodTrait.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}
}
