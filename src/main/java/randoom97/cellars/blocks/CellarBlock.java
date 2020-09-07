package randoom97.cellars.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import randoom97.cellars.Cellars;

public class CellarBlock extends Block{

	public static final ResourceLocation CELLAR_BLOCK = new ResourceLocation(Cellars.MODID, "cellar_block");
	
	public CellarBlock() {
		super(Material.WOOD);
		setRegistryName(CELLAR_BLOCK);
		setTranslationKey(Cellars.MODID + ".cellar_block");
		setHardness(3.0F);
		setCreativeTab(Cellars.tabCellars);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
}
