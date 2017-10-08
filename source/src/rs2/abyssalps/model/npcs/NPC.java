package rs2.abyssalps.model.npcs;

import rs2.Server;
import rs2.abyssalps.content.minigame.zombies.ZombiesDefinitions;
import rs2.abyssalps.model.npcs.combat.DamageQueue;
import rs2.abyssalps.model.npcs.drop.Drops;
import rs2.abyssalps.model.player.PathFinder;
import rs2.abyssalps.model.player.Player;
import rs2.util.Misc;
import rs2.util.Stream;

public class NPC {

	public int npcId;
	public int npcType;
	public int absX, absY;
	public int walkX, walkY;
	public int heightLevel;
	public int makeX, makeY, maxHit, defence, attack, moveX, moveY, direction,
			walkingType;
	public int spawnX, spawnY;
	public int viewX, viewY;
	public int tileIndex;
	public long lastChat;
	public boolean visible = true;
	public boolean canAttack = true;
	public boolean respawnable = true;
	public boolean isSleeping = false;
	public int sleepCycle = 4;

	private int followId = 0;

	public int getFollowId() {
		return this.followId;
	}

	public void setFollowId(int id) {
		this.followId = id;
	}

	public void resetFollowing() {
		setFollowId(-1);
		facePlayer(-1);
	}

	public int getSize() {
		NPCSize size = NPCSize.forId(npcType);
		return size == null ? 1 : size.getSize();
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean canAttack() {
		return canAttack && isVisible();
	}

	public void setCanAttack(boolean canAttack) {
		this.canAttack = canAttack;
	}

	public boolean isRespawnable() {
		return respawnable;
	}

	public void setRespawnable(boolean respawnable) {
		this.respawnable = respawnable;
	}

	public boolean isSleeping() {
		return this.isSleeping;
	}

	public void setIsSleeping(boolean isSleeping) {
		this.isSleeping = isSleeping;
	}

	public void setOpponentCoordinates(int absX, int absY) {
		opponentX = absX;
		opponentY = absY;
	}

	/**
	 * Make an NPC walk somewhere
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */

	public void walkTo(int x, int y) {
		walkX = x;
		walkY = y;
		walkingType = 1337;
	}

	public boolean inGodwars() {
		if (heightLevel == 2 || heightLevel == 0 || heightLevel == 1) {
			if (absX >= 2800 && absX <= 2950 && absY >= 5200 && absY <= 5400) {
				return true;
			}
		}
		return false;
	}

	private int opponentX;
	private int opponentY;

	public int pathX;
	public int pathY;

	public boolean[] minionRespawn = new boolean[3];

	public int distanceTo(Player player) {
		return distanceTo(player.absX, player.absY);
	}

	public int distanceTo(NPC npc) {
		return distanceTo(npc.absX, npc.absY);
	}

	public int distanceTo(int otherX, int otherY) {
		int minDistance = (int) Math.hypot(otherX - absX, otherY - absY);
		for (int x = absX; x < absX + getSize() - 1; x++) {
			for (int y = absY; y < absY + getSize() - 1; y++) {
				int distance = (int) Math.hypot(otherX - x, otherY - y);
				if (distance < minDistance) {
					minDistance = distance;
				}
			}
		}
		return minDistance;
	}

	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */
	public int attackType, projectileId, endGfx, spawnedBy = -1, hitDelayTimer,
			HP, MaxHP, hitDiff, animNumber, actionTimer, enemyX, enemyY;
	public boolean applyDead, isDead, needRespawn, respawns;
	public boolean walkingHome, underAttack;
	public int freezeTimer, attackTimer, killerId, killedBy, oldIndex,
			underAttackBy;
	public long lastDamageTaken;
	public boolean randomWalk;
	public boolean dirUpdateRequired;
	public boolean animUpdateRequired;
	public boolean hitUpdateRequired;
	public boolean updateRequired;
	public boolean forcedChatRequired;
	public boolean faceToUpdateRequired;
	public int firstAttacker;
	public String forcedText;

	private DamageQueue damageQueue = new DamageQueue(this);

	public DamageQueue getDamage() {
		return damageQueue;
	}

	public NPC(int _npcId, int _npcType) {
		npcId = _npcId;
		npcType = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
	}

	public void updateNPCMovement(Stream str) {
		if (direction == -1) {

			if (updateRequired) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	/**
	 * Text update
	 **/

	public void forceChat(String text) {
		forcedText = text;
		forcedChatRequired = true;
		updateRequired = true;
	}

	/**
	 * Graphics
	 **/

	public int mask80var1 = 0;
	public int mask80var2 = 0;
	protected boolean mask80update = false;

	public void appendMask80Update(Stream str) {
		str.writeWord(mask80var1);
		str.writeDWord(mask80var2);
	}

	public void gfx100(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 6553600;
		mask80update = true;
		updateRequired = true;
	}

	public void gfx0(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 65536;
		mask80update = true;
		updateRequired = true;
	}

	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animNumber);
		str.writeByte(1);
	}

	public void startAnimation(int animId) {
		animNumber = animId;
		animUpdateRequired = true;
		updateRequired = true;
	}

	/**
	 *
	 * Face
	 *
	 **/

	public int FocusPointX = -1, FocusPointY = -1;
	public int face = 0;

	private void appendSetFocusDestination(Stream str) {
		str.writeWordBigEndian(FocusPointX);
		str.writeWordBigEndian(FocusPointY);
	}

	public void turnNpc(int i, int j) {
		FocusPointX = 2 * i + 1;
		FocusPointY = 2 * j + 1;
		updateRequired = true;

	}

	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}

	public void facePlayer(int player) {
		face = player + 32768;
		dirUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}

	public void appendNPCUpdateBlock(Stream str) {
		if (!updateRequired)
			return;
		int updateMask = 0;
		if (animUpdateRequired)
			updateMask |= 0x10;
		if (hitUpdateRequired2)
			updateMask |= 8;
		if (mask80update)
			updateMask |= 0x80;
		if (dirUpdateRequired)
			updateMask |= 0x20;
		if (forcedChatRequired)
			updateMask |= 1;
		if (hitUpdateRequired)
			updateMask |= 0x40;
		if (FocusPointX != -1)
			updateMask |= 4;

		str.writeByte(updateMask);

		if (animUpdateRequired)
			appendAnimUpdate(str);
		if (hitUpdateRequired2)
			appendHitUpdate2(str);
		if (mask80update)
			appendMask80Update(str);
		if (dirUpdateRequired)
			appendFaceEntity(str);
		if (forcedChatRequired) {
			str.writeString(forcedText);
		}
		if (hitUpdateRequired)
			appendHitUpdate(str);
		if (FocusPointX != -1)
			appendSetFocusDestination(str);

	}

	public void clearUpdateFlags() {
		updateRequired = false;
		forcedChatRequired = false;
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		animUpdateRequired = false;
		dirUpdateRequired = false;
		mask80update = false;
		forcedText = null;
		face = 0;
		moveX = 0;
		moveY = 0;
		direction = -1;
		FocusPointX = -1;
		FocusPointY = -1;
	}

	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if (dir == -1)
			return -1;
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void getNextNPCMovement(int i) {
		if (direction != -1) {
			return;
		}
		direction = getNextWalkingDirection();
	}

	public void appendHitUpdate(Stream str) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeByteC(hitDiff);
		if (hitDiff > 0) {
			str.writeByteS(1);
		} else {
			str.writeByteS(0);
		}
		str.writeByteS(Misc.getCurrentHP(HP, MaxHP, 100));
		str.writeByteC(100);
	}

	public int hitDiff2 = 0;
	public boolean hitUpdateRequired2 = false;

	public void appendHitUpdate2(Stream str) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeByteA(hitDiff2);
		if (hitDiff2 > 0) {
			str.writeByteC(1);
		} else {
			str.writeByteC(0);
		}
		str.writeByteA(HP);
		str.writeByte(MaxHP);
	}

	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}

	public void remove() {
		setVisible(false);
		if (NPCHandler.npcs[npcId] == this) {
			NPCHandler.npcs[npcId] = null;
		}
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public boolean inCorp() {
		if ((absX >= 2971 && absY <= 4398 && absX <= 3001 && absY >= 4365)) {
			return true;
		}
		return false;
	}

	public boolean inMulti() {
		if (npcType == 5535 || npcType == 494) {
			return true;
		}
		if (ZombiesDefinitions.inMinigame(absX, absY)) {
			return true;
		}
		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 1231 && absX <= 1249 && absY >= 1237 && absY <= 1256)
				|| (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)
				|| (absX >= 3216 && absY <= 10355 && absX <= 3247 && absY >= 10329)
				|| (absX >= 3684 && absY >= 5797 && absX <= 3708 && absY <= 5821)
				|| (absX >= 2800 && absX <= 2950 && absY >= 5200 && absY <= 5400)
				|| (absX >= 2971 && absY <= 4398 && absX <= 3001 && absY >= 4365)
				|| (absX >= 2800 && absX <= 2950 && absY >= 5200 && absY <= 5400)
				|| (absX >= 1479 && absY <= 3702 && absX <= 1558 && absY >= 3678)
				|| (absX >= 2250 && absY >= 3050 && absX <= 2290 && absY <= 3090)
				|| (absX <= 1249 && absY <= 1256 && absX >= 1231 && absY >= 1243)) {
			return true;
		}
		return false;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}
}
