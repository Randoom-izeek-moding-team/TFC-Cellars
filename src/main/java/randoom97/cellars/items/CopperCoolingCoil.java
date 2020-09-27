package randoom97.cellars.items;

import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.objects.items.ItemTFC;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import randoom97.cellars.Cellars;

public class CopperCoolingCoil extends ItemTFC {

	public static final ResourceLocation COPPER_COOLING_COIL = new ResourceLocation(Cellars.MODID, "copper_cooling_coil");
	
	public CopperCoolingCoil() {
		super();
		
		setRegistryName(COPPER_COOLING_COIL);
		setTranslationKey(Cellars.MODID + ".copper_cooling_coil");
		setCreativeTab(Cellars.tabCellars);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	public Size getSize(ItemStack arg0) {
		return Size.NORMAL;
	}

	@Override
	public Weight getWeight(ItemStack arg0) {
		return Weight.MEDIUM;
	}

}
