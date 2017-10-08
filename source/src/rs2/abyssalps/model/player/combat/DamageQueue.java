package rs2.abyssalps.model.player.combat;

import java.util.LinkedList;
import java.util.Queue;

import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.model.items.defs.WeaponAnimations;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.npcs.combat.impl.Vetion;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;

public class DamageQueue {

	/**
	 * The queue containing all of the damage being dealt by the player
	 */
	private Queue<Damage> damageQueue = new LinkedList<>();

	Client c;

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
	public DamageQueue(Client player) {
		this.c = player;
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
				if (c.getMinigame() != null) {
					if (c.getMinigame().onDamage(c, damage)) {
						continue;
					}
				}
				if (!damage.isPlayer()) {
					executeDamageNPC(damage.getTarget(), damage.getDamage(),
							damage.getCombatType());
				} else if (damage.isPlayer()) {
					executeDamagePlayer(damage.getTarget(), damage.getDamage(),
							damage.getCombatType());
				}
			} else if (damage.getTicks() > 1) {
				updatedQueue.add(damage);
			}
		}
		damageQueue.addAll(updatedQueue);
	}

	private void executeDamagePlayer(int i, int damage, int combatType) {
		if (PlayerHandler.players[i] == null) {
			c.faceUpdate(0);
			c.playerIndex = 0;
			return;
		}

		if (Server.getMultiplayerSessionListener().inSession(c,
				MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(c,
					MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
		}

		if (PlayerHandler.players[i].isDead
				|| PlayerHandler.players[i].playerLevel[3] <= 0
				|| c.playerLevel[3] <= 0) {
			c.faceUpdate(0);
			c.playerIndex = 0;
			return;
		}

		if (PlayerHandler.players[i].respawnTimer > 0) {
			c.faceUpdate(0);
			c.playerIndex = 0;
			return;
		}

		Client o = (Client) PlayerHandler.players[i];
		o.getPA().removeAllWindows();

		if (o.playerIndex <= 0 && o.npcIndex <= 0) {
			if (o.autoRet == 1) {
				o.playerIndex = c.playerId;
			}
		}

		if (o.attackTimer <= 3 || o.attackTimer == 0 && o.playerIndex == 0
				&& !c.castingMagic) { // block animation
			o.startAnimation(WeaponAnimations.blockAnimation(o));
		}

		if (!c.castingMagic && c.projectileStage > 0) {

			if (c.dbowSpec) {
				o.gfx100(1100);
				if (damage < 8)
					damage = 8;
				c.dbowSpec = false;
			}

			boolean dropArrows = true;

			for (int noArrowId : c.NO_ARROW_DROP) {
				if (c.lastWeaponUsed == noArrowId) {
					dropArrows = false;
					break;
				}
			}

			if (dropArrows) {
				c.getItems().dropArrowPlayer(i);
			}

		} else if (c.projectileStage > 0) {
			if (c.getCombat().getEndGfxHeight() == 100 && !c.magicFailed) {
				o.gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
			} else if (!c.magicFailed) {
				o.gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
			} else if (c.magicFailed) {
				o.gfx100(85);
			}
		}

		c.getCombat().applyRecoil(damage, i);

		if (o.vengOn)
			c.getCombat().appendVengeance(i, damage);

		o.logoutDelay = System.currentTimeMillis();
		o.underAttackBy = c.playerId;
		o.killerId = c.playerId;
		o.singleCombatDelay = System.currentTimeMillis();

		o.dealDamage(damage);
		o.damageTaken[c.playerId] += damage;
		c.totalPlayerDamageDealt += damage;
		o.handleHitMask(damage);
		o.getPA().refreshSkill(3);
		o.updateRequired = true;

		c.usingMagic = false;
		c.castingMagic = false;
		c.oldSpellId = 0;
		c.projectileStage = 0;
		c.lastWeaponUsed = 0;
		c.doubleHit = false;
		c.magicFailed = false;
		c.getPA().requestUpdates();
	}

	private void executeDamageNPC(int i, int damage, int combatType) {
		if (NPCHandler.npcs[i] == null) {
			c.npcIndex = 0;
			return;
		}
		if (NPCHandler.npcs[i].isDead || !NPCHandler.npcs[i].isVisible()) {
			c.npcIndex = 0;
			return;
		}
		NPCHandler.npcs[i].facePlayer(c.playerId);

		if (NPCHandler.npcs[i].underAttackBy > 0
				&& Server.npcHandler.getsPulled(i)) {
			NPCHandler.npcs[i].killerId = c.playerId;
		} else if (NPCHandler.npcs[i].underAttackBy < 0
				&& !Server.npcHandler.getsPulled(i)) {
			NPCHandler.npcs[i].killerId = c.playerId;
		}

		c.lastNpcAttacked = i;
		if (!c.castingMagic && c.projectileStage > 0) {
		} else if (c.projectileStage > 0) {
			if (c.getCombat().getEndGfxHeight() == 100 && !c.magicFailed) { // end
																			// GFX
				NPCHandler.npcs[i].gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
			} else if (!c.magicFailed) {
				NPCHandler.npcs[i].gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
			}

			if (c.magicFailed) {
				NPCHandler.npcs[i].gfx100(85);
			}
		}

		if (NPCHandler.npcs[i].npcType == 6611) {
			if (Vetion.minionsDead < 3 && Vetion.minionsSpawned) {
				damage = 0;
			}
		}

		NPCHandler.npcs[i].hitDiff = damage;
		NPCHandler.npcs[i].HP -= damage;
		c.totalDamageDealt += damage;
		NPCHandler.npcs[i].hitUpdateRequired = true;
		NPCHandler.npcs[i].updateRequired = true;
		c.magicFailed = false;
		c.usingMagic = false;
		c.castingMagic = false;
		c.oldSpellId = 0;
	}
}
