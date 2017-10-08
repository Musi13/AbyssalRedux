package rs2.abyssalps.content.minigame.zulrah;

import rs2.abyssalps.model.npcs.NPC;
import rs2.util.CycledState;
import rs2.util.Misc;

public class ZulrahRotations {

	public static CycledState[] getRotationStates(NPC zulrah) {
		int random = Misc.random(3);
		if (random == 0) {
			return new CycledState[] {
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.RED_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_SOUTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.RED_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_EAST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_SOUTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_SOUTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_EAST_POSITION) };
		} else if (random == 1) {
			return new CycledState[] {
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.RED_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_EAST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_SOUTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.RED_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_WEST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_SOUTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_EAST_POSITION), };
		} else if (random == 2) {
			return new CycledState[] {
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_WEST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.RED_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_EAST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_SOUTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_WEST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_EAST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_WEST_POSITION) };
		} else {
			return new CycledState[] {
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_WEST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_SOUTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_EAST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.RED_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_WEST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_SOUTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_EAST_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.BLUE_ZULRAH_ID,
							ZulrahConstants.ZULRAH_NORTH_POSITION),
					new ZulrahTransformation(zulrah, ZulrahConstants.GREEN_ZULRAH_ID,
							ZulrahConstants.ZULRAH_WEST_POSITION), };
		}
	}

}