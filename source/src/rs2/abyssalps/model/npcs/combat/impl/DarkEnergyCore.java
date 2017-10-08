package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.npcs.combat.CombatType;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class DarkEnergyCore extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC npc) {
		int damage = Misc.random(13);
		if (damage > 0 && !client.prayerActive[18]) {
			for (NPC corpNpc : NPCHandler.getNpcsById(CorporealBeast.CORPOREAL_BEAST_ID)) {
				corpNpc.HP += damage;
				if (corpNpc.HP > corpNpc.MaxHP) {
					corpNpc.HP = corpNpc.MaxHP;
				}
			}
		}
		npc.getDamage().add(new Damage(client, damage, 2, CombatType.MELEE));
	}

}
