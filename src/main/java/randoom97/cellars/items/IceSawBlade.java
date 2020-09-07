package randoom97.cellars.items;

import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.items.ItemTFC;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import randoom97.cellars.Cellars;

public class IceSawBlade extends ItemTFC {

	public final ResourceLocation ICE_SAW_BLADE;
	
	public IceSawBlade(Metal metal) {
		super();
		
		ToolMaterial material = metal.getToolMetal();
		
		ICE_SAW_BLADE = new ResourceLocation(Cellars.MODID, material.name()+"_ice_saw_blade");
		
		setRegistryName(ICE_SAW_BLADE);
		setTranslationKey(Cellars.MODID + "."+material.name()+"_ice_saw_blade");
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
