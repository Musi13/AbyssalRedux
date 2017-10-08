package rs2.abyssalps.content.pets;

import java.util.HashMap;
import java.util.Map;

public enum PetDefinitions {

	ABYSSAL_ORPHAN(13262, 5883),
	BABY_MOLE(12646, 5780),
	CALLISTO_CUB(13178, 497),
	HELLPUPPY(13247, 964),
	KALPHITE_PRINCESS(12647, 6637),
	CHAOS_ELEMENTAL(11995, 5907),
	DAGANNOTH_PRIME(12644, 6627),
	DAGANNOTH_REX(12645, 6630),
	DAGANNOTH_SUPREME(12643, 6626),
	DARK_CORE(12816, 388), 
	GENERAL_GRAARDOR(12650, 6632),
	KRIL_TSUTSAROTH(12652, 6634),
	KRAKEN(12655, 6640),
	KREEARRA(12649, 6631),
	PENANCE_PET(12703, 6642),
	SMOKE_DEVIL(12648, 6639), 
	ZILYANA(12651,6633),
	SNAKELING(12921, 2127), 
	PRINCE_BLACK_DRAGON(12653, 6636),
	SCORPIAS_OFFSPRING(13181, 5547),
	TZREK_JAD(13225, 5892),
	VENENATIS_SPIDERLING(13177, 5557),
	VETION_PURPLE(13179, 5536),
	VETION_ORANGE(13180, 5537),
	KALPHITE_PRINCESS1(12647, 6638),
    KALPHITE_PRINCESS2(12654, 6637),
    HELL_CAT(7582, 1625),
    CLOCKWORK_CAT(7771, 2782),
    CAT(1561, 6662),
    HERON(13320, 6715),
    ROCK_GOLEM(13321, 6716),
    BEAVER(13322, 6717),
    BABY_CHINCHOMPA(13323, 6718),
    CHOMPY_CHICK(13071, 4001),
    PET_ROCK(3695, 6657);

	private int itemId;
	private int npcId;

	private PetDefinitions(int itemId, int npcId) {
		this.itemId = itemId;
		this.npcId = npcId;
	}

	public int getItemId() {
		return itemId;
	}

	public int getNpcId() {
		return npcId;
	}
	
	private static Map<Integer, PetDefinitions> npcToPetMap = new HashMap<>();
	private static Map<Integer, PetDefinitions> itemToPetMap = new HashMap<>();

	public static PetDefinitions getByNpcId(int npcId) {
		return npcToPetMap.get(npcId);
	}

	public static PetDefinitions getByItemId(int itemId) {
		return itemToPetMap.get(itemId);
	}

	static {
		for (PetDefinitions def : values()) {
			npcToPetMap.put(def.getNpcId(), def);
			itemToPetMap.put(def.getItemId(), def);
		}
	}
	
}