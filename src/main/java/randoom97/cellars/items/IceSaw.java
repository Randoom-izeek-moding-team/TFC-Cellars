package randoom97.cellars.items;

import java.util.Set;

import com.google.common.collect.Sets;

import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import randoom97.cellars.Cellars;
import randoom97.cellars.ModItems;

public class IceSaw extends ItemTool {

	private static final Set<Block> blocksEffectiveAgainst = Sets.newHashSet(new Block[] {Blocks.ICE, BlocksTFC.SEA_ICE});
	
	public final ResourceLocation ICE_SAW;
	
	public IceSaw(Metal metal) {
		super(metal.getToolMetal(), blocksEffectiveAgainst);
		
		ICE_SAW = new ResourceLocation(Cellars.MODID, toolMaterial.name()+"_ice_saw");
		
		setRegistryName(ICE_SAW);
		setTranslationKey(Cellars.MODID + "."+toolMaterial.name()+"_ice_saw");
		setCreativeTab(Cellars.tabCellars);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		super.onBlockDestroyed(stack, world, state, pos, entityLiving);
		if(world.isRemote)
			return false;
		
		Block block = state.getBlock();
		if(block == Blocks.ICE || block == BlocksTFC.SEA_ICE) {
			EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.iceChunks, 1));
			world.spawnEntity(entityItem);
			world.setBlockToAir(pos);
		}
		return true;
	}
	
}
