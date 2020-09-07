package randoom97.cellars.config;

import net.minecraftforge.common.config.Config;
import randoom97.cellars.Cellars;

@Config(modid = Cellars.MODID, category = "cellars")
public class CellarsConfig {
	
	// ice bunker
	@Config.Comment(value = "Rate at which ice is consumed when the ice bunker is at or above room temperature (20C).")
	@Config.RangeInt(min = 1)
	public static int ROOM_TEMP_TICK = 40;
	
	@Config.Comment(value = "Rate at which ice is consumed when the ice bunker is at freezing (0C).")
	@Config.RangeInt(min = 1)
	public static int FROZEN_TICK = 2400;
	
	@Config.Comment(value = "Strength of the cellars insulation. Stabilizes to environment Temp/(1+Insulation).")
	public static float INSULATION = 3F;
	
	@Config.Comment(value = "How fast the temperature attempts to stabilize.")
	@Config.RangeDouble(max = 1.0)
	public static float RATE_OF_CHANGE = 1 / 20F;
	
	@Config.Comment(value = "How many ticks between temperature changes.")
	public static int ICE_BUNKER_TICK_RATE = 20;
	
	
	
	// cellar shelf
	@Config.Comment(value = "Temperature before Cold modifier is applied to food.")
	public static float COLD_TEMP = 7.22F;
	
	@Config.Comment(value = "Temperature before Frozen modifier is applied to food.")
	public static float FROZEN_TEMP = 0.1F;
	
	
	
	// food modifiers
	@Config.Comment(value = "Modifier for cold food decay rate")
	@Config.RangeDouble(min = 0)
	public static float COLD_MODIFIER = 0.25F;
	
	@Config.Comment(value = "Modifier for frozen food decay rate")
	@Config.RangeDouble(min = 0)
	public static float FROZEN_MODIFIER = 0.1F;
}
