package rs2.abyssalps.content.skill.fletching;

import java.util.HashMap;
import java.util.Map;

public enum StringDefinitions {

	SHORTBOW(50, 841, 5, 5, 6678),
	LONGBOW(48, 839, 10, 10,6684),
	OAK_SHORTBOW(54,843,20,16, 6679),
	OAK_LONGBOW(56,845, 25,25,6685),
	WILLOW_SHORTBOW(60,849,35,33,6680),
	WILLOW_LONGBOW(58,847,40,41,6686),
	MAPLE_SHORTBOW(64,853,50,50,6681),
	MAPLE_LONGBOW(62,851,55,58,6687),
	YEW_SHORTBOW(68,857,65,67,6682),
	YEW_LONGBOW(66,855,70,75,6688),
	MAGIC_SHORTBOW(72,861,80,83,6683),
	MAGIC_LONGBOW(70,859,85,91,6689);

	private final int unstrungId;
	private final int strungId;
	private final int levelRequired;
	private final int experience;
	private final int animationId;
	
	private StringDefinitions(int unstrungId, int strungId, int levelRequired, int experience, int animationId) {
		this.unstrungId = unstrungId;
		this.strungId = strungId;
		this.levelRequired = levelRequired;
		this.experience = experience;
		this.animationId = animationId;
	}

	public int getUnstrungId() {
		return unstrungId;
	}

	public int getStrungId() {
		return strungId;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getExperience() {
		return experience;
	}
	
	public int getAnimationId() {
		return animationId;
	}
	
	private static Map<Integer, StringDefinitions> definitions = new HashMap<>();
	
	public static StringDefinitions getDefinition(int unstrungId) {
		return definitions.get(unstrungId);
	}
	
	static {
		for (StringDefinitions definition : StringDefinitions.values()) {
			definitions.put(definition.getUnstrungId(), definition);
		}
	}
	
}