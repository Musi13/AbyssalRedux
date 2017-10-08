package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.CombatType;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Green_Dragon extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		int damage = 0;
		int combatType = 0;
		if (Misc.random(2) == 0) {
			combatType = CombatType.DRAGON_FIRE;
		} else {
			combatType = CombatType.MELEE;
		}

		if (combatType == 0) {
			damage = Misc.random(12);
			n.startAnimation(91);
		} else {
			damage = Misc.random(65);
			n.gfx0(1);
			n.startAnimation(81);
		}

		n.getDamage().add(new Damage(client, damage, 2, combatType));
	}

}
