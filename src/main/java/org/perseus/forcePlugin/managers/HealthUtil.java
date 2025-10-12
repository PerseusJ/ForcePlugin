package org.perseus.forcePlugin.managers;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;

public final class HealthUtil {

	private HealthUtil() {}

	@SuppressWarnings("deprecation")
	public static double getMaxHealth(LivingEntity entity) {
		AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
		if (attribute != null) {
			return attribute.getValue();
		}
		try {
			return entity.getMaxHealth();
		} catch (NoSuchMethodError ignored) {
			return 20.0; // sensible default
		}
	}
}


