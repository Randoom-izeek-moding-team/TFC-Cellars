package randoom97.cellars.config;

import net.minecraftforge.common.config.Config;
import randoom97.cellars.Cellars;

@Config(modid = Cellars.MODID, category = "cellars")
public class CellarsConfig {
	// basic Electric Cooler
	@Config.Comment(value = "The largest Basic cellar will draw this RF/tick, smaller one draw less (defaul:90)")
	@Config.RangeInt(min = 6)
	public static int BASIC_RFPerTick = 90;
	@Config.Comment(value = "temp the Basic cooler will turn off at, (depending on insulation it might not reach the temp at all)")
	@Config.RangeInt(min = 1)
	public static float BASIC_COOLER_TO_TEMP = -10F;
	@Config.Comment(value = "Rate at which ice is consumed when the electric cooler is at or above room temperature (20C).")
	@Config.RangeInt(min = 1)
	public static int ELECTRIC_ROOM_TEMP_TICK = 1;
	@Config.Comment(value = "Rate at which ice is consumed when the electric cooler is at freezing (0C).")
	@Config.RangeInt(min = 1)
	public static int ELECTRIC_FROZEN_TICK = 10;

	// ice bunker
	@Config.Comment(value = "Rate at which ice is consumed when the ice bunker is at or above room temperature (20C).")
	@Config.RangeInt(min = 1)
	public static int ROOM_TEMP_TICK = 40;

	@Config.Comment(value = "Rate at which ice is consumed when the ice bunker is at freezing (0C).")
	@Config.RangeInt(min = 1)
	public static int FROZEN_TICK = 2400;

	@Config.Comment(value = "Strength of the cellars Block insulation. Stabilizes to environment Temp/(1+Insulation)(default:3)")
	public static float BASICINSULATION = 3F;

	@Config.Comment(value = "Strength of the Industrial cellars Block  insulation. Stabilizes to environment Temp/(1+Insulation)(default:10)")
	public static float INDUSTRIALINSULATION = 10F;

	@Config.Comment(value = "How fast the temperature attempts to stabilize.")
	@Config.RangeDouble(max = 1.0)
	public static float RATE_OF_CHANGE = 1 / 20F;

	@Config.Comment(value = "How fast the temperature is effected by Cooling items(Snow and Ice Chunks).")
	@Config.RangeDouble(max = 1.0)
	public static float COOlING_ITEM_RATE = 1 / 20F;

	@Config.Comment(value = "How fast the temperature is effected basic powered cooler.")
	@Config.RangeDouble(max = 1.0)
	public static float COOlING_BASIC_ENERGY_RATE = 1 / 10F;

	@Config.Comment(value = "How many ticks between temperature changes.")
	public static int ICE_BUNKER_TICK_RATE = 20;

	// cellar shelf
	@Config.Comment(value = "Temperature before Cold modifier is applied to food.")
	public static float COLD_TEMP = 7.22F;

	@Config.Comment(value = "Temperature before Frozen modifier is applied to food.")
	public static float FROZEN_TEMP = 0.1F;

	@Config.Comment(value = "Temperature before Supper Cooled modifier is applied to food.")
	public static float SUPPER_COOLED_TEMP = 0.1F;

	// food modifiers
	@Config.Comment(value = "Modifier for cold food decay rate")
	@Config.RangeDouble(min = 0)
	public static float COLD_MODIFIER = 0.25F;

	@Config.Comment(value = "Modifier for frozen food decay rate")
	@Config.RangeDouble(min = 0)
	public static float FROZEN_MODIFIER = 0.1F;

	@Config.Comment(value = "Modifier for Supper Cooled food decay rate")
	@Config.RangeDouble(min = 0)
	public static float SUPPER_COOLED_MODIFIER = 0.01F;
}
