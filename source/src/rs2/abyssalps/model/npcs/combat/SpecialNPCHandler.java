package rs2.abyssalps.model.npcs.combat;

import java.util.HashMap;

import rs2.abyssalps.content.minigame.zulrah.ZulrahConstants;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.impl.Ahrims;
import rs2.abyssalps.model.npcs.combat.impl.Battlemage;
import rs2.abyssalps.model.npcs.combat.impl.Callisto;
import rs2.abyssalps.model.npcs.combat.impl.Cerberus;
import rs2.abyssalps.model.npcs.combat.impl.Chaos_Elemental;
import rs2.abyssalps.model.npcs.combat.impl.Chaos_Fanatic;
import rs2.abyssalps.model.npcs.combat.impl.Commander_Zilyana;
import rs2.abyssalps.model.npcs.combat.impl.CorporealBeast;
import rs2.abyssalps.model.npcs.combat.impl.Crazy_Archaeologist;
import rs2.abyssalps.model.npcs.combat.impl.Dagannoth_Prime;
import rs2.abyssalps.model.npcs.combat.impl.Dagannoth_Supreme;
import rs2.abyssalps.model.npcs.combat.impl.DarkEnergyCore;
import rs2.abyssalps.model.npcs.combat.impl.Elf_Warrior;
import rs2.abyssalps.model.npcs.combat.impl.Enormous_Tentacle;
import rs2.abyssalps.model.npcs.combat.impl.Flockleader_Gerin;
import rs2.abyssalps.model.npcs.combat.impl.General_Graardor;
import rs2.abyssalps.model.npcs.combat.impl.Green_Dragon;
import rs2.abyssalps.model.npcs.combat.impl.Growler;
import rs2.abyssalps.model.npcs.combat.impl.Karils;
import rs2.abyssalps.model.npcs.combat.impl.KetZek;
import rs2.abyssalps.model.npcs.combat.impl.King_Black_Dragon;
import rs2.abyssalps.model.npcs.combat.impl.Kraken;
import rs2.abyssalps.model.npcs.combat.impl.Kree_Arra;
import rs2.abyssalps.model.npcs.combat.impl.Lizardman_Shaman;
import rs2.abyssalps.model.npcs.combat.impl.Magic_Spinolyp;
import rs2.abyssalps.model.npcs.combat.impl.Ranged_Spinolyp;
import rs2.abyssalps.model.npcs.combat.impl.Scorpia;
import rs2.abyssalps.model.npcs.combat.impl.Scorpia_Offspring;
import rs2.abyssalps.model.npcs.combat.impl.Sergeant_Grimspike;
import rs2.abyssalps.model.npcs.combat.impl.Sergeant_Steelwill;
import rs2.abyssalps.model.npcs.combat.impl.TokXil;
import rs2.abyssalps.model.npcs.combat.impl.TzTok_Jad;
import rs2.abyssalps.model.npcs.combat.impl.Venenatis;
import rs2.abyssalps.model.npcs.combat.impl.Vetion;
import rs2.abyssalps.model.npcs.combat.impl.Wingman_Skree;
import rs2.abyssalps.model.npcs.combat.impl.ZulrahBlue;
import rs2.abyssalps.model.npcs.combat.impl.ZulrahGreen;
import rs2.abyssalps.model.npcs.combat.impl.ZulrahRed;
import rs2.abyssalps.model.player.Client;

public abstract class SpecialNPCHandler {

	public static SpecialNPCHandler forId(int npcType) {
		return specialMap.get(npcType);
	}

	public static double getDelay(NPC n, Client client, int delay, int speed) {
		/** The distance between the entities. */
		int distance = n.distanceTo(client);

		/** The speed at which the projectile is traveling. */
		int projectileSpeed = delay + speed + distance * 5;

		/** The delay of the hit. */
		double hitDelay = projectileSpeed * .02857;

		/** Returns the hit delay. */
		return hitDelay;
	}

	private static HashMap<Integer, SpecialNPCHandler> specialMap = new HashMap<Integer, SpecialNPCHandler>();

	static {
		Green_Dragon greenDragon = new Green_Dragon();
		for (int i = 260; i < 265; i++) {
			specialMap.put(i, greenDragon);
		}
		specialMap.put(270, greenDragon);
		specialMap.put(259, greenDragon);
		specialMap.put(265, greenDragon);
		specialMap.put(5862, new Cerberus());
		specialMap.put(6619, new Chaos_Fanatic());
		specialMap.put(239, new King_Black_Dragon());
		specialMap.put(CorporealBeast.CORPOREAL_BEAST_ID, new CorporealBeast());
		specialMap.put(CorporealBeast.DARK_CORE_ID, new DarkEnergyCore());
		specialMap.put(2215, new General_Graardor());
		specialMap.put(2218, new Sergeant_Grimspike());
		specialMap.put(2217, new Sergeant_Steelwill());
		specialMap.put(6618, new Crazy_Archaeologist());
		Vetion vetion = new Vetion();
		specialMap.put(6611, vetion);
		specialMap.put(6612, vetion);
		specialMap.put(3127, new TzTok_Jad());
		specialMap.put(2265, new Dagannoth_Supreme());
		specialMap.put(2266, new Dagannoth_Prime());
		specialMap.put(5947, new Magic_Spinolyp());
		specialMap.put(5961, new Ranged_Spinolyp());
		specialMap.put(5535, new Enormous_Tentacle());
		specialMap.put(494, new Kraken());
		specialMap.put(3428, new Elf_Warrior());
		specialMap.put(3164, new Flockleader_Gerin());
		specialMap.put(3163, new Wingman_Skree());
		specialMap.put(3162, new Kree_Arra());
		specialMap.put(2205, new Commander_Zilyana());
		Battlemage battleMage = new Battlemage();
		for (int index = 1610; index < 1613; index++) {
			specialMap.put(1610, battleMage);
		}
		specialMap.put(1675, new Karils());
		specialMap.put(1672, new Ahrims());
		specialMap.put(3125, new KetZek());
		specialMap.put(3121, new TokXil());
		specialMap.put(6615, new Scorpia());
		specialMap.put(6616, new Scorpia_Offspring());
		specialMap.put(2207, new Growler());
		specialMap.put(6609, new Callisto());
		specialMap.put(6766, new Lizardman_Shaman());
		specialMap.put(ZulrahConstants.GREEN_ZULRAH_ID, new ZulrahGreen());
		specialMap.put(ZulrahConstants.BLUE_ZULRAH_ID, new ZulrahBlue());
		specialMap.put(ZulrahConstants.RED_ZULRAH_ID, new ZulrahRed());
		specialMap.put(6504, new Venenatis());
		specialMap.put(2054, new Chaos_Elemental());

	}

	public abstract void execute(Client client, NPC n);

	public void onDeath(Client client, NPC npc) {

	}

}
