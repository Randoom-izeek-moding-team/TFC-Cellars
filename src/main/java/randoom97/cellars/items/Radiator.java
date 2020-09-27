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

public class Radiator extends ItemTFC {

	public static final ResourceLocation RADIATOR = new ResourceLocation(Cellars.MODID, "radiator");
	
	public Radiator() {
		super();
		
		setRegistryName(RADIATOR);
		setTranslationKey(Cellars.MODID + ".radiator");
		setCreativeTab(Cellars.tabCellars);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	public Size getSize(ItemStack arg0) {
		return Size.LARGE;
	}

	@Override
	public Weight getWeight(ItemStack arg0) {
		return Weight.HEAVY;
	}

}
