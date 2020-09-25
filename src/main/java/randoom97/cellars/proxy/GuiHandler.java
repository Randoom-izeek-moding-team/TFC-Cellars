package randoom97.cellars.proxy;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import randoom97.cellars.containers.ContainerBasicElectricCooler;
import randoom97.cellars.containers.ContainerCellarShelf;
import randoom97.cellars.containers.ContainerIceBunker;
import randoom97.cellars.guis.GuiBasicElectricCooler;
import randoom97.cellars.guis.GuiCellarShelf;
import randoom97.cellars.guis.GuiIceBunker;
import randoom97.cellars.tiles.TileBasicElectricCooler;
import randoom97.cellars.tiles.TileCellarShelf;
import randoom97.cellars.tiles.TileIceBunker;

public class GuiHandler implements IGuiHandler {

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileIceBunker) {
			return new ContainerIceBunker(player.inventory, (TileIceBunker) te);
		} else if (te instanceof TileCellarShelf) {
			return new ContainerCellarShelf(player.inventory, (TileCellarShelf) te);
		} else if (te instanceof TileBasicElectricCooler) {
			return new ContainerBasicElectricCooler(player.inventory, (TileBasicElectricCooler) te);
		}
		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileIceBunker) {
			TileIceBunker tileIceBunker = (TileIceBunker) te;
			return new GuiIceBunker(tileIceBunker, new ContainerIceBunker(player.inventory, tileIceBunker));
		} else if (te instanceof TileCellarShelf) {
			TileCellarShelf tileCellarShelf = (TileCellarShelf) te;
			return new GuiCellarShelf(tileCellarShelf, new ContainerCellarShelf(player.inventory, tileCellarShelf));
		} else if (te instanceof TileBasicElectricCooler) {
			TileBasicElectricCooler tileBasicElectricCooler = (TileBasicElectricCooler) te;
			return new GuiBasicElectricCooler(tileBasicElectricCooler, new ContainerBasicElectricCooler(player.inventory, tileBasicElectricCooler));
		}

		return null;
	}

}
