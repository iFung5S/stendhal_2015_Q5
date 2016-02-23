package games.stendhal.server.entity.creature;

import static org.junit.Assert.*;
import games.stendhal.client.entity.Player;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.maps.MockStendlRPWorld;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import utilities.RPClass.BabyDragonTestHelper;

public class CatAllowedFoodTest 
{
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		BabyDragonTestHelper.generateRPClasses();
		MockStendlRPWorld.get();
	}

	@Test
	public void checkCatCannotEatLionfish()
	{
		// Create a list
		List<String> foodName;
		
		// Create new cat object
		Cat cat = new Cat();
		
		foodName = cat.getFoodNames();
			
		assertFalse(foodName.contains("red lionfish"));
	}
}