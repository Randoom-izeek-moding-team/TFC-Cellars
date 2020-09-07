package randoom97.cellars.registry;

import net.dries007.tfc.api.capability.food.FoodTrait;
import randoom97.cellars.config.CellarsConfig;

public class CellarFoodTrait {
	public static FoodTrait COLD;
	public static FoodTrait FROZEN;
	
	public static void init() {
		COLD = new FoodTrait("cellar_cold", CellarsConfig.COLD_MODIFIER);
		FROZEN = new FoodTrait("cellar_frozen", CellarsConfig.FROZEN_MODIFIER);
	}
}
