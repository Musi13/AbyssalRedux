package rs2.abyssalps.model.player.combat;

import rs2.Config;
import rs2.abyssalps.content.BlowPipe;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.Misc;

public class Experience {

	public static boolean usingSpear(Client player) {
		if (ItemDefinitions.forId(player.playerEquipment[3]).getName()
				.toLowerCase().endsWith("spear")) {
			return true;
		}
		return false;
	}

	private static boolean toxicWeapons(int id) {
		switch (id) {
		case 12904:
		case 12926:
			return true;
		}
		return false;
	}

	private static boolean toxicEquipment(int id) {
		switch (id) {
		case 12931:
		case 13199:
		case 13197:
			return true;
		}
		return false;
	}

	public static void calculateDamagePlayer(Client client, int index,
			int combatType, boolean doubleHit) {
		if (PlayerHandler.players[index] == null) {
			return;
		}
		Client otherClient = (Client) PlayerHandler.players[index];
		int damage = 0;
		int damage2 = 0;

		otherClient.logoutDelay = System.currentTimeMillis();
		otherClient.underAttackBy = client.playerId;
		otherClient.killerId = client.playerId;
		otherClient.singleCombatDelay = System.currentTimeMillis();
		if (client.killedBy != otherClient.playerId) {
			client.totalPlayerDamageDealt = 0;
		}
		client.killedBy = otherClient.playerId;
		if (toxicEquipment(otherClient.playerEquipment[otherClient.playerHat])) {
			if (otherClient.poisonDamage <= 0 && otherClient.venomDamage < 6
					&& Misc.random(3) == 1) {
				client.getPA().appendVenom(6);
			}
		}
		switch (combatType) {
		case 0:

			damage = Misc.random(client.getCombat().calculateMeleeMaxHit());

			if (doubleHit) {
				damage2 = Misc
						.random(client.getCombat().calculateMeleeMaxHit());
			}

			boolean veracsEffect = false;
			boolean guthansEffect = false;

			if (client.getPA().fullVeracs()) {
				if (Misc.random(4) == 1) {
					veracsEffect = true;
				}
			}

			if (client.getPA().fullGuthans()) {
				if (Misc.random(4) == 1) {
					guthansEffect = true;
				}
			}

			if (otherClient.prayerActive[18] && !veracsEffect) {
				damage = (int) damage * 60 / 100;
				if (doubleHit) {
					damage2 = (int) damage2 * 60 / 100;
				}
			}

			if (otherClient.playerEquipment[otherClient.playerShield] == 12817) {
				if (Misc.random(100) <= 70 && damage > 0) {
					otherClient.gfx0(321);
					damage = (int) (damage * .25);
				}
			}

			if (Misc.random(otherClient.getCombat().calculateMeleeDefence()) > Misc
					.random(client.getCombat().calculateMeleeAttack())
					&& !veracsEffect) {
				damage = 0;
				client.bonusAttack = 0;
			} else if (client.playerEquipment[client.playerWeapon] == 5698
					&& otherClient.poisonDamage <= 0 && Misc.random(3) == 1) {
				otherClient.getPA().appendPoison(13);
				client.bonusAttack += damage / 3;
			} else {
				client.bonusAttack += damage / 3;
			}

			if (doubleHit) {
				if (Misc.random(otherClient.getCombat().calculateMeleeDefence()) > Misc
						.random(client.getCombat().calculateMeleeAttack())) {
					damage2 = 0;
				}
			}

			if (damage > 0 && guthansEffect) {
				client.playerLevel[3] += damage;
				if (client.playerLevel[3] > client
						.getLevelForXP(client.playerXP[3]))
					client.playerLevel[3] = client
							.getLevelForXP(client.playerXP[3]);
				client.getPA().refreshSkill(3);
				otherClient.gfx0(398);
			}

			if (damage > otherClient.playerLevel[3]) {
				damage = otherClient.playerLevel[3];
			}

			if (doubleHit) {
				if (damage2 > otherClient.playerLevel[3] - damage) {
					damage2 = otherClient.playerLevel[3] - damage;
				}
			}

			switch (client.specEffect) {
			case 1: // dragon scimmy special
				if (damage > 0) {
					if (otherClient.prayerActive[16]
							|| otherClient.prayerActive[17]
							|| otherClient.prayerActive[18]) {
						otherClient.headIcon = -1;
						otherClient.getPA().sendFrame36(client.PRAYER_GLOW[16],
								0);
						otherClient.getPA().sendFrame36(client.PRAYER_GLOW[17],
								0);
						otherClient.getPA().sendFrame36(client.PRAYER_GLOW[18],
								0);
					}
					otherClient.sendMessage("You have been injured!");
					otherClient.stopPrayerDelay = System.currentTimeMillis();
					otherClient.prayerActive[16] = false;
					otherClient.prayerActive[17] = false;
					otherClient.prayerActive[18] = false;
					otherClient.getPA().requestUpdates();
				}
				break;
			case 2:
				if (damage > 0) {
					if (otherClient.freezeTimer <= 0) {
						otherClient.freezeTimer = 30;
						otherClient.stopMovement();
						otherClient.gfx0(369);
						otherClient.sendMessage("You have been frozen!");
						client.sendMessage("You freeze your enemy.");
					}
				}
				break;
			case 3:
				int[] skills = { 0, 1, 2, 4, 6 };
				if (damage > 0) {
					client.playerLevel[0] -= damage;
					client.playerLevel[1] -= (damage - 10);
					client.playerLevel[2] -= (damage - 20);
					client.playerLevel[4] -= (damage - 30);
					client.playerLevel[6] -= (damage - 40);
				}
				for (int i = 0; i < skills.length; i++) {
					if (client.playerLevel[skills[i]] <= 0) {
						client.playerLevel[skills[i]] = 0;
					}
					client.getPA().refreshSkill(skills[i]);
				}
				break;
			case 4:
				if (damage > 0) {
					if (client.playerLevel[3] + damage > client
							.getLevelForXP(client.playerXP[3]))
						if (client.playerLevel[3] > client
								.getLevelForXP(client.playerXP[3]))
							;
						else
							client.playerLevel[3] = client
									.getLevelForXP(client.playerXP[3]);
					else
						client.playerLevel[3] += damage;
					client.getPA().refreshSkill(3);
				}
				break;

			case 5:
				int[] skills1 = { 0, 1, 4, 6 };
				if (damage > 0) {
					for (int i = 0; i < skills1.length; i++) {
						if (client.playerLevel[skills1[i]] > 0) {
							client.playerLevel[skills1[i]] -= damage * .10;
						}
						if (client.playerLevel[skills1[i]] <= 0) {
							client.playerLevel[skills1[i]] = 0;
						}
						client.getPA().refreshSkill(skills1[i]);
					}
				}
				break;
			}
			client.specEffect = 0;

			client.getCombat().applySmite(index, damage);

			if (doubleHit) {
				client.getCombat().applySmite(index, damage2);
			}
			if (otherClient.isConstitutionLocked()) {
				damage = 0;
				damage2 = 0;
			}
			boolean pidHit = false;

			client.getDamage().add(
					new Damage(index, damage, 0, 2, true, pidHit));
			if (doubleHit) {
				client.getDamage().add(
						new Damage(index, damage2, 0, 2, true, pidHit));
			}
			addExperience(client, doubleHit ? damage + damage2 : damage,
					combatType);
			break;

		case 1:
			damage = BlowPipe.usingBlowPipe(client) ? Misc.random(BlowPipe
					.getBlowpipeMaxHit(client)) : Misc.random(client
					.getCombat().rangeMaxHit());

			if (doubleHit) {
				damage2 = Misc.random(client.getCombat().rangeMaxHit());
			}

			boolean ignoreDef = false;
			if (Misc.random(4) == 1 && client.lastArrowUsed == 9243) {
				ignoreDef = true;
				otherClient.gfx0(758);
			}

			if (damage > 0 && Misc.random(5) == 1
					&& client.lastArrowUsed == 9244) {
				damage *= 1.45;
				otherClient.gfx0(756);
			}

			if (otherClient.prayerActive[17]) {
				damage = (int) damage * 60 / 100;
				if (doubleHit) {
					damage2 = (int) damage * 60 / 100;
				}
			}

			if (otherClient.playerEquipment[otherClient.playerShield] == 12817) {
				if (Misc.random(100) <= 70 && damage > 0) {
					otherClient.gfx0(321);
					damage = (int) (damage * .25);
				}
			}

			if (Misc.random(10 + otherClient.getCombat()
					.calculateRangeDefence()) > Misc.random(10 + client
					.getCombat().calculateRangeAttack())
					&& !ignoreDef) {
				damage = 0;
			}

			if (doubleHit) {
				if (Misc.random(10 + otherClient.getCombat()
						.calculateRangeDefence()) > Misc.random(10 + client
						.getCombat().calculateRangeAttack())) {
					damage2 = 0;
				}
			}

			if (damage > otherClient.playerLevel[3]) {
				damage = otherClient.playerLevel[3];
			}

			if (doubleHit) {
				if (damage2 > otherClient.playerLevel[3] - damage) {
					damage2 = otherClient.playerLevel[3] - damage;
				}
			}

			client.getDamage().add(
					new Damage(index, damage, 1, (int) getDelay(client,
							otherClient, client.getCombat().getStartDelay(),
							client.getCombat().usingDbow() ? client.getCombat()
									.getProjectileSpeed() - 10 : client
									.getCombat().getProjectileSpeed()), true,
							false));

			if (doubleHit) {
				client.getDamage().add(
						new Damage(index, damage2, 1, (int) getDelay(client,
								otherClient,
								client.getCombat().getStartDelay(), client
										.getCombat().usingDbow() ? client
										.getCombat().getProjectileSpeed() - 10
										: client.getCombat()
												.getProjectileSpeed() + 10),
								true, false));
			}
			addExperience(client, doubleHit ? damage + damage2 : damage, 1);
			break;

		case 2:
			damage = client.usingSpecial && client.playerEquipment[3] == 11838 ? Misc
					.random(16) : Misc
					.random(client.MAGIC_SPELLS[client.oldSpellId][6]);
			if (client.getCombat().godSpells()) {
				if (System.currentTimeMillis() - client.godSpellDelay < Config.GOD_SPELL_CHARGE) {
					damage += 10;
				}
			}

			if (otherClient.prayerActive[16]) {
				damage = (int) damage * 60 / 100;
			}

			if (otherClient.playerEquipment[otherClient.playerShield] == 12817) {
				if (Misc.random(100) <= 70 && damage > 0) {
					otherClient.gfx0(321);
					damage = (int) (damage * .25);
				}
			}

			if (Misc.random(otherClient.getCombat().mageDef()) > Misc
					.random(client.getCombat().mageAtk())) {
				client.magicFailed = true;
				damage = 0;
			}

			if (client.magicFailed) {
				damage = 0;
			}

			if (damage > otherClient.playerLevel[3]) {
				damage = otherClient.playerLevel[3];
			}

			if (!client.magicFailed && damage > 0) {
				if (System.currentTimeMillis() - otherClient.reduceStat > 35000) {
					otherClient.reduceStat = System.currentTimeMillis();
					switch (client.MAGIC_SPELLS[client.oldSpellId][0]) {
					case 12987:
					case 13011:
					case 12999:
					case 13023:
						otherClient.playerLevel[0] -= ((otherClient.getPA()
								.getLevelForXP(otherClient.playerXP[0]) * 10) / 100);
						break;
					}
				}
			}

			int freezeDelay = client.getCombat().getFreezeTime();// freeze time
			if (freezeDelay > 0 && otherClient.freezeTimer <= -3
					&& !client.magicFailed && damage > 0) {
				otherClient.freezeTimer = freezeDelay;
				otherClient.resetWalkingQueue();
				otherClient.sendMessage("You have been frozen!");
			}
			if (!client.magicFailed) {
				switch (client.MAGIC_SPELLS[client.oldSpellId][0]) {

				case 12445: // teleblock
					if (System.currentTimeMillis() - otherClient.teleBlockDelay > otherClient.teleBlockLength) {
						otherClient.teleBlockDelay = System.currentTimeMillis();
						otherClient.sendMessage("You have been teleblocked.");
						if (otherClient.prayerActive[16]
								&& System.currentTimeMillis()
										- otherClient.protMageDelay > 1500)
							otherClient.teleBlockLength = 150000;
						else
							otherClient.teleBlockLength = 300000;
					}
					break;

				case 12901:
				case 12919: // blood spells
				case 12911:
				case 12929:
					int heal = (int) (damage / 4);
					if (client.playerLevel[3] + heal > client.getPA()
							.getLevelForXP(client.playerXP[3])) {
						client.playerLevel[3] = client.getPA().getLevelForXP(
								client.playerXP[3]);
					} else {
						client.playerLevel[3] += heal;
					}
					client.getPA().refreshSkill(3);
					break;

				case 1153:
					otherClient.playerLevel[0] -= ((otherClient.getPA()
							.getLevelForXP(otherClient.playerXP[0]) * 5) / 100);
					otherClient
							.sendMessage("Your attack level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System
							.currentTimeMillis();
					otherClient.getPA().refreshSkill(0);
					break;

				case 1157:
					otherClient.playerLevel[2] -= ((otherClient.getPA()
							.getLevelForXP(otherClient.playerXP[2]) * 5) / 100);
					otherClient
							.sendMessage("Your strength level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System
							.currentTimeMillis();
					otherClient.getPA().refreshSkill(2);
					break;

				case 1161:
					otherClient.playerLevel[1] -= ((otherClient.getPA()
							.getLevelForXP(otherClient.playerXP[1]) * 5) / 100);
					otherClient
							.sendMessage("Your defence level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System
							.currentTimeMillis();
					otherClient.getPA().refreshSkill(1);
					break;

				case 1542:
					otherClient.playerLevel[1] -= ((otherClient.getPA()
							.getLevelForXP(otherClient.playerXP[1]) * 10) / 100);
					otherClient
							.sendMessage("Your defence level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System
							.currentTimeMillis();
					otherClient.getPA().refreshSkill(1);
					break;

				case 1543:
					otherClient.playerLevel[2] -= ((otherClient.getPA()
							.getLevelForXP(otherClient.playerXP[2]) * 10) / 100);
					otherClient
							.sendMessage("Your strength level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System
							.currentTimeMillis();
					otherClient.getPA().refreshSkill(2);
					break;

				case 1562:
					otherClient.playerLevel[0] -= ((otherClient.getPA()
							.getLevelForXP(otherClient.playerXP[0]) * 10) / 100);
					otherClient
							.sendMessage("Your attack level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System
							.currentTimeMillis();
					otherClient.getPA().refreshSkill(0);
					break;
				}
			}
			client.getDamage().add(
					new Damage(index, damage, 2,
							(int) getDelay(client, otherClient, client
									.getCombat().getStartDelay(), 98), true,
							false));
			addExperience(client, damage, 2);
			break;
		}
	}

	public static void calculateDamageNPC(Client client, int index,
			int combatType, boolean doubleHit) {
		int damage = 0;
		int damage2 = 0;
		if (NPCHandler.npcs[index] == null) {
			return;
		}
		NPC n = (NPC) NPCHandler.npcs[index];
		n.underAttack = true;
		n.setFollowId(client.getId());
		client.killingNpcIndex = client.npcIndex;
		client.lastNpcAttacked = index;
		switch (combatType) {
		case 0:
			damage = Misc.random(client.getCombat().calculateMeleeMaxHit());

			if (doubleHit) {
				damage2 = Misc
						.random(client.getCombat().calculateMeleeMaxHit());
			}

			if (n.npcType == 319 && !usingSpear(client)) {
				damage = (int) (damage * .50);
			}

			if (damage > n.HP) {
				damage = n.HP;
			}

			if (damage2 > n.HP - damage) {
				damage2 = n.HP - damage;
			}

			boolean fullVeracsEffect = client.getPA().fullVeracs()
					&& Misc.random(3) == 1;

			if (!fullVeracsEffect) {
				if (Misc.random(n.defence) > 10 + Misc.random(client
						.getCombat().calculateMeleeAttack())) {
					damage = 0;
				} else if (n.npcType == 2266 || n.npcType == 2267) {
					damage = 0;
					damage2 = 0;
				}
			}

			if (doubleHit) {
				if (Misc.random(n.defence) > 10 + Misc.random(client
						.getCombat().calculateMeleeAttack())) {
					damage2 = 0;
				}
			}

			boolean guthansEffect = false;
			if (client.getPA().fullGuthans()) {
				if (Misc.random(3) == 1) {
					guthansEffect = true;
				}
			}

			if (damage > 0 || damage2 > 0) {
				if (n.npcType >= 3777 && n.npcType <= 3780) {
					client.pcDamage += damage + damage2;
				}
			}

			if (damage > 0 && guthansEffect) {
				n.gfx0(398);
				client.playerLevel[3] += damage;
				if (client.playerLevel[3] > client
						.getLevelForXP(client.playerXP[3])) {
					client.playerLevel[3] = client
							.getLevelForXP(client.playerXP[3]);
				}
				client.getPA().refreshSkill(3);
			}

			switch (client.specEffect) {
			case 4:
				if (damage > 0) {
					if (client.playerLevel[3] + damage > client
							.getLevelForXP(client.playerXP[3]))
						if (client.playerLevel[3] > client
								.getLevelForXP(client.playerXP[3]))
							;
						else
							client.playerLevel[3] = client
									.getLevelForXP(client.playerXP[3]);
					else
						client.playerLevel[3] += damage;
					client.getPA().refreshSkill(3);
				}
				break;
			}
			client.getDamage().add(
					new Damage(index, damage, 0, 2, false, false));
			if (doubleHit) {
				client.getDamage().add(
						new Damage(index, damage2, 0, 3, false, false));
			}
			addExperience(client, doubleHit ? damage2 + damage : damage, 0);
			break;

		case 1:
			damage = BlowPipe.usingBlowPipe(client) ? Misc.random(BlowPipe
					.getBlowpipeMaxHit(client)) : Misc.random(client
					.getCombat().rangeMaxHit());

			if (doubleHit) {
				damage2 = Misc.random(client.getCombat().rangeMaxHit());
			}

			boolean ignoreDef = false;

			if (Misc.random(5) == 1
					&& client.playerEquipment[client.playerArrows] == 9243) {
				ignoreDef = true;
				n.gfx0(758);
			}

			if (Misc.random(n.defence) > Misc.random(10 + client.getCombat()
					.calculateRangeAttack()) && !ignoreDef) {
				damage = 0;
			} else if (n.npcType == 2265 || n.npcType == 2267 && !ignoreDef) {
				damage = 0;
			}

			if (Misc.random(4) == 1
					&& client.playerEquipment[client.playerArrows] == 9242
					&& damage > 0) {
				n.gfx0(754);
				damage = n.HP / 5;
				client.handleHitMask(client.playerLevel[3] / 10);
				client.dealDamage(client.playerLevel[3] / 10);
				client.gfx0(754);
			}

			if (damage > 0 && Misc.random(5) == 1
					&& client.playerEquipment[client.playerArrows] == 9244) {
				damage *= 1.45;
				n.gfx0(756);
			}

			if (n.npcType == 319 && !usingSpear(client)) {
				damage = (int) (damage * .50);
			}

			if (damage > n.HP) {
				damage = n.HP;
			}

			if (doubleHit) {
				if (damage2 > n.HP - damage) {
					damage2 = n.HP - damage;
				}
			}

			if (damage > 0) {
				if (n.npcType >= 3777 && n.npcType <= 3780) {
					client.pcDamage += damage;
				}
			}

			boolean dropArrows = true;

			for (int noArrowId : client.NO_ARROW_DROP) {
				if (client.lastWeaponUsed == noArrowId) {
					dropArrows = false;
					break;
				}
			}

			if (dropArrows) {
				client.getItems().dropArrowNpc();
			}

			client.getDamage().add(
					new Damage(index, damage, 1, (int) getDelay(client, n, 53,
							client.getCombat().getProjectileSpeed()), false,
							false));
			if (doubleHit) {
				client.getDamage().add(
						new Damage(index, damage2, 1,
								(int) getDelay(client, n, 53, client
										.getCombat().getProjectileSpeed()) + 1,
								false, false));
			}
			addExperience(client, damage, 1);
			break;

		case 2:
			damage = Misc.random(client.MAGIC_SPELLS[client.oldSpellId][6]);
			if (client.getCombat().godSpells()) {
				if (System.currentTimeMillis() - client.godSpellDelay < Config.GOD_SPELL_CHARGE) {
					damage += Misc.random(10);
				}
			}

			client.magicFailed = false;

			int bonusAttack = client.getCombat().getBonusAttack(index);

			if (Misc.random(n.defence) > 10
					+ Misc.random(client.getCombat().mageAtk()) + bonusAttack) {
				damage = 0;
				client.magicFailed = true;
			} else if (n.npcType == 2265 || n.npcType == 2266) {
				damage = 0;
				client.magicFailed = true;
			}

			if (n.npcType == 319 && !usingSpear(client)) {
				damage = (int) (damage * .50);
			}

			if (damage > n.HP) {
				damage = n.HP;
			}

			if (damage > 0) {
				if (n.npcType >= 3777 && n.npcType <= 3780) {
					client.pcDamage += damage;
				}
			}
			if (!client.magicFailed) {
				int freezeDelay = client.getCombat().getFreezeTime();// freeze
				if (freezeDelay > 0 && n.freezeTimer == 0) {
					n.freezeTimer = freezeDelay;
				}
				switch (client.MAGIC_SPELLS[client.oldSpellId][0]) {
				case 12901:
				case 12919: // blood spells
				case 12911:
				case 12929:
					int heal = Misc.random(damage / 2);
					if (client.playerLevel[3] + heal >= client.getPA()
							.getLevelForXP(client.playerXP[3])) {
						client.playerLevel[3] = client.getPA().getLevelForXP(
								client.playerXP[3]);
					} else {
						client.playerLevel[3] += heal;
					}
					client.getPA().refreshSkill(3);
					break;
				}

			}
			client.getDamage().add(
					new Damage(index, damage, 2, (int) getDelay(client, n, 50,
							78), false, false));
			addExperience(client, damage, 2);
			break;

		}
	}

	private static void addExperience(Client client, int damage, int combatType) {
		switch (combatType) {
		case 0:
			if (client.fightMode == 3) {
				client.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3),
						0);
				client.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3),
						1);
				client.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3),
						2);
				client.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3),
						3);
				client.getPA().refreshSkill(0);
				client.getPA().refreshSkill(1);
				client.getPA().refreshSkill(2);
				client.getPA().refreshSkill(3);
			} else {
				client.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE),
						client.fightMode);
				client.getPA().addSkillXP((damage * Config.MELEE_EXP_RATE / 3),
						3);
				client.getPA().refreshSkill(client.fightMode);
				client.getPA().refreshSkill(3);
			}
			break;

		case 1:
			if (client.fightMode == 3) {
				client.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3),
						4);
				client.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3),
						1);
				client.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3),
						3);
				client.getPA().refreshSkill(1);
				client.getPA().refreshSkill(3);
				client.getPA().refreshSkill(4);
			} else {
				client.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE), 4);
				client.getPA().addSkillXP((damage * Config.RANGE_EXP_RATE / 3),
						3);
				client.getPA().refreshSkill(3);
				client.getPA().refreshSkill(4);
			}
			break;

		case 2:
			client.getPA().addSkillXP(
					(client.MAGIC_SPELLS[client.oldSpellId][7] + damage
							* Config.MAGIC_EXP_RATE), 6);
			client.getPA().addSkillXP(
					(client.MAGIC_SPELLS[client.oldSpellId][7] + damage
							* Config.MAGIC_EXP_RATE / 3), 3);
			client.getPA().refreshSkill(3);
			client.getPA().refreshSkill(6);
			break;
		}
	}

	public static double getDelay(Client attacker, Client victim, int delay,
			int speed) {
		/** The distance between the entities. */
		int distance = attacker.distanceToPoint(victim.getX(), victim.getY());

		/** The speed at which the projectile is traveling. */
		int projectileSpeed = delay + speed + distance * 5;

		/** The delay of the hit. */
		double hitDelay = projectileSpeed * .02857;

		/** Returns the hit delay. */
		return hitDelay;
	}

	public static double getDelay(Client attacker, NPC victim, int delay,
			int speed) {
		/** The distance between the entities. */
		int distance = attacker.distanceToPoint(victim.getX(), victim.getY());

		/** The speed at which the projectile is traveling. */
		int projectileSpeed = delay + speed + distance * 5;

		/** The delay of the hit. */
		double hitDelay = projectileSpeed * .02857;

		/** Returns the hit delay. */
		return hitDelay;
	}
}
