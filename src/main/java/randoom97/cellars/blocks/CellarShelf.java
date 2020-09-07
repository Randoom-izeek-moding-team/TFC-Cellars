package randoom97.cellars.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import randoom97.cellars.Cellars;
import randoom97.cellars.tiles.TileCellarShelf;

public class CellarShelf extends Block implements ITileEntityProvider {

public static final ResourceLocation CELLAR_SHELF = new ResourceLocation(Cellars.MODID, "cellar_shelf");
	
	public CellarShelf() {
		super(Material.WOOD);
		setRegistryName(CELLAR_SHELF);
		setTranslationKey(Cellars.MODID + ".cellar_shelf");
		setHardness(3.0F);
		setCreativeTab(Cellars.tabCellars);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {return false;}
	
	@Override
	public boolean isFullCube(IBlockState state) { return false; }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCellarShelf();
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileCellarShelf) {
			((TileCellarShelf) te).beforeBlockBreak();
			ItemStackHandler ish = (ItemStackHandler) te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			for(int i = 0; i < ish.getSlots(); i++) {
				if(ish.getStackInSlot(i) != null) {
					world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), ish.getStackInSlot(i)));
				}
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return true;
		}
		TileEntity te = world.getTileEntity(pos);
		if(!(te instanceof TileCellarShelf)) {
			return false;
		}
		player.openGui(Cellars.instance, 0,  world,  pos.getX(),  pos.getY(), pos.getZ());
		return true;
	}
	
}
