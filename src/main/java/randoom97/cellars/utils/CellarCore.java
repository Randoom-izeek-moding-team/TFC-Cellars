package randoom97.cellars.utils;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import randoom97.cellars.Cellars;
import randoom97.cellars.ModBlocks;

public class CellarCore extends TileEntity {
	private int unformed = 0;
	private int formedCellarBlocks = 1;
	private int formedInustrialBlock = 2;
	private Block[] validBlocks = { ModBlocks.cellarBlock, ModBlocks.industrialCellarBlock };
	private Block[] validDoors = { ModBlocks.cellarDoor, ModBlocks.industrialCellarDoor };
	private Block acceptibleDoors = null;
	private Block acceptibleWalls = null;
	public Block getDoorType() {
		return acceptibleDoors;
	}
	public BlockPos[] FindDoors(int[] size) {
		BlockPos[] doors = new BlockPos[2];
		for (int i = 0; i < size.length; i++) {
			// Cellars.logger.info(size[i]);
		}
		int y = 1;
		for (int x = -size[1] - 1; x <= size[3] + 1; x++) {
			for (int z = -size[2] - 1; z <= size[0] + 1; z++) {

				// ice bunker
				if (y == 0 && x == 0 && z == 0)
					continue;

				Block block = world.getBlockState(pos.add(x, y, z)).getBlock();
				// Cellars.logger.info(block);
				// door
				for (int i = 0; i < validDoors.length; i++) {
					if (y == 1 && block == validDoors[i]) {
						// door1 not null means another door was already found
						// Cellars.logger.info("Door found");
						if (doors[0] != null) {
							// Cellars.logger.info("Too many doors!");
							doors[0] = null;
							doors[1] = null;
							return doors;
						}

						doors[0] = pos.add(x, y, z);
						if (x == -size[1] - 1) {
							doors[1] = doors[0].add(-1, 0, 0);
						}
						if (x == size[3] + 1) {
							doors[1] = doors[0].add(1, 0, 0);
						}
						if (z == -size[2] - 1) {
							doors[1] = doors[0].add(0, 0, -1);
						}
						if (z == size[2] + 1) {
							doors[1] = doors[0].add(0, 0, 1);
						}
						continue;
					}

					// upper part of door
					if (y == 2 && block == validDoors[i] && doors[0] != null) {
						if (!pos.add(x, y, z).equals(doors[0].add(0, 1, 0))) {
							// Cellars.logger.info("Upper door doesn't match lower door!");
							doors[0] = null;
							doors[1] = null;
							return doors;
						}
						continue;
					}
				}
			}
		}
		return doors;
	}

	public int[] structureComplete() {

		BlockPos door1, door2;
		int[] size = new int[5];
		door1 = null;
		door2 = null;
		int[] cellarInfo = new int[7];
		cellarInfo[0] = unformed;
		// get size
		for (int direction = 0; direction < 4; direction++) {
			Block block = null;
			switch (direction) {
			case 0:
				block = world.getBlockState(pos.add(0, 0, 1)).getBlock();
				break;
			case 1:
				block = world.getBlockState(pos.add(-1, 0, 0)).getBlock();
				break;
			case 2:
				block = world.getBlockState(pos.add(0, 0, -1)).getBlock();
				break;
			case 3:
				block = world.getBlockState(pos.add(1, 0, 0)).getBlock();
				break;
			}

			// used to look for the type Of cellar block used and fail of the blocks around
			// the core dont match
			for (int i = 0; i < validBlocks.length; i++) {
				if (block == validBlocks[i] && acceptibleWalls == null) {
					acceptibleWalls = block;
					// Cellars.logger.info("found acceptible side");
					break;
				} else if (block != validBlocks[i] && acceptibleWalls == block) {
					break;
				} else if (acceptibleWalls != block && i == validBlocks.length - 1) {
					// Cellars.logger.info("sides dont match");
					return cellarInfo; // failed
				}
			}
			if (acceptibleWalls != null) {
				break;
			}

		}
		for (int i = 0; i < validBlocks.length; i++) {
			if (acceptibleWalls == validBlocks[i]) {
				acceptibleDoors = validDoors[i];
				break;
			}
		}
		if (acceptibleDoors == null) {
			Cellars.logger.info("this should not happen, how are you here");
		}
		for (int direction = 0; direction < 4; direction++) {
			for (int dist = 1; dist < 6; dist++) {
				if (dist == 5) {
					// Cellars.logger.info("Cellar too big!");
					return cellarInfo;
				}
				Block block = null;
				switch (direction) {
				case 0:
					block = world.getBlockState(pos.add(0, 1, dist)).getBlock();
					break;
				case 1:
					block = world.getBlockState(pos.add(-dist, 1, 0)).getBlock();
					break;
				case 2:
					block = world.getBlockState(pos.add(0, 1, -dist)).getBlock();
					break;
				case 3:
					block = world.getBlockState(pos.add(dist, 1, 0)).getBlock();
					break;
				}

				if (block == acceptibleWalls || block == acceptibleDoors) {
					size[direction] = dist - 1;
					break;
				}
			}
		}
		for (int dist = 2; dist <= 5; dist++) {
			if (dist == 5) {
				// Cellars.logger.info("Cellar too tall");
				return cellarInfo;
			}
			Block block = world.getBlockState(pos.add(0, dist, 0)).getBlock();
			if (block == acceptibleWalls) {
				size[4] = dist - 1;
				break;
			}
		}

		// check blocks and set entrance
		for (int y = 0; y <= size[4] + 1; y++) {
			for (int x = -size[1] - 1; x <= size[3] + 1; x++) {
				for (int z = -size[2] - 1; z <= size[0] + 1; z++) {

					// ice bunker
					if (y == 0 && x == 0 && z == 0)
						continue;

					Block block = world.getBlockState(pos.add(x, y, z)).getBlock();

					// skip the inside of the cellar
					if (y > 0 && y < size[4] + 1 && x > -size[1] - 1 && x < size[3] + 1 && z > -size[2] - 1
							&& z < size[0] + 1) {
						if (block == acceptibleWalls) {
							// Cellars.logger.info("Inside can't contain cellar block!");
							return cellarInfo;
						}
						continue;
					}

					// corners
					if ((x == -size[1] - 1 || x == size[3] + 1) && (z == -size[2] - 1 || z == size[0] + 1)) {
						if (block == acceptibleWalls) {
							continue;
						}
						// Cellars.logger.info("Corners invalid at " + pos.add(x, y, z));
						return cellarInfo;
					}

					// door
					if (y == 1 && block == acceptibleDoors) {
						// door1 not null means another door was already found
						if (door1 != null) {
							// Cellars.logger.info("Too many doors!");
							return cellarInfo;
						}

						door1 = pos.add(x, y, z);
						if (x == -size[1] - 1) {
							door2 = door1.add(-1, 0, 0);
						}
						if (x == size[3] + 1) {
							door2 = door1.add(1, 0, 0);
						}
						if (z == -size[2] - 1) {
							door2 = door1.add(0, 0, -1);
						}
						if (z == size[2] + 1) {
							door2 = door1.add(0, 0, 1);
						}
						continue;
					}

					// upper part of door
					if (y == 2 && block == acceptibleDoors && door1 != null) {
						if (!pos.add(x, y, z).equals(door1.add(0, 1, 0))) {
							// Cellars.logger.info("Upper door doesn't match lower door!");
							return cellarInfo;
						}
						continue;
					}

					// wall
					if (!(block == acceptibleWalls)) {
						// Cellars.logger.info("Cellar wall incorrect at " + pos.add(x, y, z));
						return cellarInfo;
					}
				}
			}
		}

		// no door found
		if (door1 == null) {
			// Cellars.logger.info("No door was found!");
			return cellarInfo;
		}

		// check the entrance
		Vec3i direction = door1.subtract(door2);
		boolean xAllign = direction.getX() != 0;
		for (int y = -1; y < 3; y++) {
			if (xAllign) {
				for (int z = -1; z <= 1; z++) {
					IBlockState state = world.getBlockState(door2.add(0, y, z));
					Block block = state.getBlock();
					if (z == 0 && (y == 0 || y == 1)) {
						if (!(block == acceptibleDoors)) {
							// Cellars.logger.info("Expected door2 but didn't get one");
							return cellarInfo;
						}
						continue;
					} else if (!(block == acceptibleWalls)) {
						// Cellars.logger.info("Edges of door wrong at " + door2.add(0, y, z));
						return cellarInfo;
					}
				}
			} else {
				for (int x = -1; x <= 1; x++) {
					Block block = world.getBlockState(door2.add(x, y, 0)).getBlock();
					if (x == 0 && (y == 0 || y == 1)) {
						if (!(block == acceptibleDoors)) {
							// Cellars.logger.info("Expected door2 but didn't get one");
							return cellarInfo;
						}
					} else if (!(block == acceptibleWalls)) {
						// Cellars.logger.info("Edges of door wrong at " + door2.add(x, y, 0));
						return cellarInfo;
					}
				}
			}
		}

		// door directions
		if (xAllign) {
			EnumFacing facing1 = world.getBlockState(door1).getValue(BlockDoor.FACING);
			if (facing1 != EnumFacing.EAST && facing1 != EnumFacing.WEST) {
				Cellars.logger.log(Level.DEBUG, "Door1 facing " + facing1);
				return cellarInfo;
			}
			EnumFacing facing2 = world.getBlockState(door2).getValue(BlockDoor.FACING);
			if (facing2 != EnumFacing.EAST && facing2 != EnumFacing.WEST) {
				Cellars.logger.log(Level.DEBUG, "Door2 facing " + facing2);
				return cellarInfo;
			}
		} else {
			EnumFacing facing1 = world.getBlockState(door1).getValue(BlockDoor.FACING);
			if (facing1 != EnumFacing.NORTH && facing1 != EnumFacing.SOUTH) {
				Cellars.logger.log(Level.DEBUG, "Door1 facing " + facing1);
				return cellarInfo;
			}
			EnumFacing facing2 = world.getBlockState(door2).getValue(BlockDoor.FACING);
			if (facing2 != EnumFacing.NORTH && facing2 != EnumFacing.SOUTH) {
				Cellars.logger.log(Level.DEBUG, "Door2 facing " + facing2);
				return cellarInfo;
			}
		}
		if (acceptibleWalls == validBlocks[0]) {
			cellarInfo[0] = formedCellarBlocks;
		} else if (acceptibleWalls == validBlocks[1]) {
			cellarInfo[0] = formedInustrialBlock;
		}
		for (int i = 0; i < size.length; i++) {
			cellarInfo[1 + i] = size[i];
		}
		// Cellars.logger.info("All is well");
		return cellarInfo;
	}
}
