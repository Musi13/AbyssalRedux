package rs2.abyssalps.model.npcs.combat;

import java.util.LinkedList;
import java.util.Queue;

import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.items.defs.WeaponAnimations;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.impl.Chaos_Fanatic;
import rs2.abyssalps.model.npcs.combat.impl.Crazy_Archaeologist;
import rs2.abyssalps.model.npcs.combat.impl.Vetion;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class DamageQueue {

	private static boolean wearingAntiShield(Client player) {
		switch (player.playerEquipment[player.playerShield]) {
		case 11283:
		case 1540:
			return true;

		default:
			return false;
		}
	}

	private static int fireProtectionAmount(Client player) {
		int total = 0;
		if (player.playerEquipment[player.playerShield] == 1540
				|| player.playerEquipment[player.playerShield] == 11283) {
			total++;
		}
		if (player.prayerActive[16]) {
			total++;
		}
		if (System.currentTimeMillis() - player.lastAntiFire < 360000) {
			total++;
		}
		return total;
	}

	/**
	 * The queue containing all of the damage being dealt by the player
	 */
	private Queue<Damage> damageQueue = new LinkedList<>();

	NPC n;

	/**
	 * Adds a damage object to the end of the queued damage list
	 * 
	 * @param damage
	 *            the damage to be dealt
	 */
	public void add(Damage damage) {
		damageQueue.add(damage);
	}

	/**
	 * Creates a new class that will manage all damage dealt by the player
	 * 
	 * @param player
	 *            the player dealing the damage
	 */
	public DamageQueue(NPC n) {
		this.n = n;
	}

	public void execute() {
		if (damageQueue.size() <= 0) {
			return;
		}
		Damage damage;
		Queue<Damage> updatedQueue = new LinkedList<>();
		while ((damage = damageQueue.poll()) != null) {
			damage.decreaseTicks();
			if (damage.getTicks() == 1) {
				if (damage.getTarget() != null) {
					executeDamage(damage);
				}
			} else if (damage.getTicks() > 1) {
				updatedQueue.add(damage);
			}
		}
		damageQueue.addAll(updatedQueue);
	}

	public void executeDamage(Damage damage) {
		if (n.isDead || n.HP <= 0) {
			resetAttack();
			return;
		}
		if (damage.getTarget().playerLevel[3] <= 0 || damage.getTarget().isDead) {
			resetAttack();
			return;
		}

		if (Server.getMultiplayerSessionListener().inSession(
				damage.getTarget(), MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(damage.getTarget(),
					MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
		}

		if (damage.getTarget().attackTimer <= 3
				|| damage.getTarget().attackTimer == 0
				&& damage.getTarget().npcIndex == 0
				&& !damage.getTarget().castingMagic) { // block
			// animation
			damage.getTarget().startAnimation(
					WeaponAnimations.blockAnimation(damage.getTarget()));
		}

		if (damage.getTarget().playerIndex <= 0
				&& damage.getTarget().npcIndex <= 0) {
			if (damage.getTarget().autoRet == 1) {
				damage.getTarget().npcIndex = n.npcId;
			}
		}

		int damageAmount = damage.getDamage();

		if (damage.getCombatType() == CombatType.MELEE) {

			if (Misc.random(damage.getTarget().getCombat()
					.calculateMeleeDefence()) > Misc.random(n.attack)) {
				damageAmount = n.npcType == 1677 ? Misc.random(10) : 0;
			}

			if (damage.getTarget().prayerActive[18] && !damage.ignorePrayer()) {
				damageAmount = 0;
			}

			if (n.npcType == 6504) {
				damage.getTarget().freezeTimer = 10;
				damage.getTarget()
						.sendMessage(
								"Venenatis hurls her web at you, sticking you to the ground.");
			}
		}

		if (damage.getCombatType() == CombatType.RANGE) {

			if (Misc.random(damage.getTarget().getCombat()
					.calculateRangeDefence()) > Misc.random(n.attack)) {
				damageAmount = 0;
			}

			if (damage.getTarget().prayerActive[17] && !damage.ignorePrayer()) {
				damageAmount = 0;
			}
		}

		if (damage.getCombatType() == CombatType.MAGIC) {

			if (n.npcType == 319 && damage.getTarget().prayerActive[16]) {
				damageAmount = Misc.random(40);
			}

			if (Misc.random(damage.getTarget().getCombat().mageDef()) > Misc
					.random(n.attack)) {
				damageAmount = 0;
			}

			if (damage.getTarget().prayerActive[16] && !damage.ignorePrayer()) {
				if (n.npcType == 494) {
					damageAmount = Misc.random(10);
				} else {
					damageAmount = 0;
				}
			}

		}

		if (damage.getCombatType() == CombatType.RANGE) {

			if (damage.getTarget().prayerActive[16] && !damage.ignorePrayer()) {
				damageAmount = Misc.random(20);
			}

			if (Misc.random(damage.getTarget().getCombat().mageDef()) > Misc
					.random(n.attack)) {
				damageAmount = 0;
			}

		}

		if (damage.getCombatType() == CombatType.DRAGON_FIRE) {

			if (wearingAntiShield(damage.getTarget())) {
				damage.getTarget().sendMessage(
						"The shield absorbs most of the dragons' fire.");
			}

			if (fireProtectionAmount(damage.getTarget()) == 1) {
				damageAmount = Misc.random(10);
			} else if (fireProtectionAmount(damage.getTarget()) == 2) {
				damageAmount = Misc.random(5);
			} else if (fireProtectionAmount(damage.getTarget()) == 3) {
				damageAmount = 0;
			}

			if (Misc.random(damage.getTarget().getCombat().mageDef()) > Misc
					.random(n.attack)) {
				damageAmount = 0;
			}

		}

		if (damage.getCombatType() == CombatType.POISON_BREATH) {

			if (wearingAntiShield(damage.getTarget())) {
				damage.getTarget().sendMessage(
						"The shield absorbs most of the dragons' fire.");
			}

			if (fireProtectionAmount(damage.getTarget()) == 1) {
				damageAmount = Misc.random(10);
			} else if (fireProtectionAmount(damage.getTarget()) == 2) {
				damageAmount = Misc.random(5);
			} else if (fireProtectionAmount(damage.getTarget()) == 3) {
				damageAmount = 0;
			}

			if (Misc.random(damage.getTarget().getCombat().mageDef()) > Misc
					.random(n.attack)) {
				damageAmount = 0;
			}

			if (damage.getTarget().poisonDamage <= 0 && damageAmount > 0) {
				damage.getTarget().getPA().appendPoison(13);
			}
		}

		if (damage.getCombatType() == CombatType.SHOCK_BREATH) {

			if (wearingAntiShield(damage.getTarget())) {
				damage.getTarget().sendMessage(
						"The shield absorbs most of the dragons' fire.");
			}

			if (fireProtectionAmount(damage.getTarget()) == 1) {
				damageAmount = Misc.random(10);
			} else if (fireProtectionAmount(damage.getTarget()) == 2) {
				damageAmount = Misc.random(5);
			} else if (fireProtectionAmount(damage.getTarget()) == 3) {
				damageAmount = 0;
			}

			if (Misc.random(damage.getTarget().getCombat().mageDef()) > Misc
					.random(n.attack)) {
				damageAmount = 0;
			}

			int[] skills = { 0, 1, 2, 4, 6 };
			int skill = skills[(int) (Math.random() * skills.length)];
			if (damage.getTarget().playerLevel[skill] > 0 && damageAmount > 0) {
				damage.getTarget().playerLevel[skill] -= 1;
				if (damage.getTarget().playerLevel[skill] <= 0) {
					damage.getTarget().playerLevel[skill] = 0;
				}
			}
			damage.getTarget().getPA().refreshSkill(skill);
		}

		if (damage.getCombatType() == CombatType.ICE_BREATH) {

			if (wearingAntiShield(damage.getTarget())) {
				damage.getTarget().sendMessage(
						"The shield absorbs most of the dragons' fire.");
			}

			if (fireProtectionAmount(damage.getTarget()) == 1) {
				damageAmount = Misc.random(10);
			} else if (fireProtectionAmount(damage.getTarget()) == 2) {
				damageAmount = Misc.random(5);
			} else if (fireProtectionAmount(damage.getTarget()) == 3) {
				damageAmount = 0;
			}

			if (Misc.random(damage.getTarget().getCombat().mageDef()) > Misc
					.random(n.attack)) {
				damageAmount = 0;
			}

			if (damage.getTarget().freezeTimer <= 0 && damageAmount > 0) {
				damage.getTarget().freezeTimer = 20;
				damage.getTarget().sendMessage("You have been frozen!");
				damage.getTarget().stopMovement();
			}
		}

		if (n.endGfx > 0 && damageAmount > 0) {
			if (n.npcType == 2205 || n.npcType == 319) {
				damage.getTarget().gfx0(n.endGfx);
			} else {
				damage.getTarget().gfx100(n.endGfx);
			}
		}

		n.endGfx = 0;

		if (damageAmount > damage.getTarget().playerLevel[3]) {
			damageAmount = damage.getTarget().playerLevel[3];
		}

		if (damage.getTarget().vengOn) {
			damage.getTarget().getCombat()
					.appendVengeanceNPC(n.npcId, damageAmount);
		}

		damage.getTarget().getCombat().recoilNPC(damageAmount, n.npcId);

		handleSpecialAttack(n, damage, damageAmount);

		damage.getTarget().logoutDelay = System.currentTimeMillis();

		if (damage.getCombatType() == 4 || damage.getCombatType() == 8
				|| damage.getCombatType() == 10) {
			if ((damage.getTarget().getX() == Chaos_Fanatic.x
					&& damage.getTarget().getY() == Chaos_Fanatic.y
					|| damage.getTarget().getX() == Chaos_Fanatic.x1
					&& damage.getTarget().getY() == Chaos_Fanatic.y1 || damage
					.getTarget().getX() == Chaos_Fanatic.x2
					&& damage.getTarget().getY() == Chaos_Fanatic.y2
					&& damage.getCombatType() == 4)
					|| (damage.getTarget().getX() == Crazy_Archaeologist.x
							&& damage.getTarget().getY() == Crazy_Archaeologist.y
							|| damage.getTarget().getX() == Crazy_Archaeologist.x1
							&& damage.getTarget().getY() == Crazy_Archaeologist.y1 || damage
							.getTarget().getX() == Crazy_Archaeologist.x2
							&& damage.getTarget().getY() == Crazy_Archaeologist.y2
							&& damage.getCombatType() == 8)
					|| (damage.getTarget().getX() == Vetion.x
							&& damage.getTarget().getY() == Vetion.y
							|| damage.getTarget().getX() == Vetion.x1
							&& damage.getTarget().getY() == Vetion.y1 || damage
							.getTarget().getX() == Vetion.x2
							&& damage.getTarget().getY() == Vetion.y2
							&& damage.getCombatType() == 10)) {
				damage.getTarget().dealDamage(damageAmount);

				damage.getTarget().handleHitMask(damageAmount);
				damage.getTarget().getPA().refreshSkill(3);

				damage.getTarget().updateRequired = true;
				return;
			}
			if (n.npcType == 5862 && damage.getCombatType() > 300) {
				boolean hasSpectralSpiritShield = damage.getTarget().playerEquipment[damage
						.getTarget().playerShield] == 12821;
				int drain = 30;
				if (hasSpectralSpiritShield)
					drain /= 2;
				if (damage.getCombatType() == 301) {
					if (damage.getTarget().prayerActive[18]
							&& !damage.ignorePrayer()) // melee
						damageAmount = 0;
				} else if (damage.getCombatType() == 302) { // range
					if (damage.getTarget().prayerActive[17]
							&& !damage.ignorePrayer())
						damageAmount = 0;
				} else if (damage.getCombatType() == 303) { // mage
					if (damage.getTarget().prayerActive[16]
							&& !damage.ignorePrayer())
						damageAmount = 0;
				}
				if (damageAmount == 0)
					damage.getTarget().prayerPoint -= drain;
				damage.getTarget().dealDamage(damageAmount);
				damage.getTarget().handleHitMask(damageAmount);
			}
		} else {
			damage.getTarget().dealDamage(damageAmount);

			damage.getTarget().handleHitMask(damageAmount);
		}

		damage.getTarget().getPA().refreshSkill(3);

		damage.getTarget().updateRequired = true;
	}

	public void resetAttack() {
		n.killerId = -1;
		n.underAttack = false;
	}

	public void handleSpecialAttack(NPC n, Damage damage, int endDamage) {
		if (endDamage <= 0) {
			return;
		}
		switch (n.npcType) {
		case 2054:
			switch (damage.getCombatType()) {
			case CombatType.RANGE:
				int index = damage.getTarget().playerEquipment.length;
				if (damage.getTarget().playerEquipment[index] > 0) {
					damage.getTarget()
							.getItems()
							.removeItem(
									damage.getTarget().playerEquipment[index],
									index);
				}
				if (index == damage.getTarget().playerWeapon
						|| index == damage.getTarget().playerArrows) {
					damage.getTarget().getCombat().resetPlayerAttack();
				}
				damage.getTarget().sendMessage(
						"The chaos elemental removes some of your equipment.");
				break;

			case CombatType.MAGIC:
				Position randomPosition = teleportPosition(n, damage
						.getTarget().getX(), damage.getTarget().getY());
				damage.getTarget()
						.getPA()
						.movePlayer(randomPosition.getX(),
								randomPosition.getY(), 0);
				break;
			}
			break;
		}
	}

	public Position teleportPosition(NPC n, int otherX, int otherY) {
		int x = n.absX - otherX;
		int y = n.absY - otherY;
		if (x > 0) {
			otherX += 5;
		} else if (x < 0) {
			otherX -= 5;
		}
		if (y > 0) {
			otherY += 5;
		} else if (y < 0) {
			otherY -= 5;
		}
		return new Position(otherX, otherY);
	}
}
