package rs2.net.packet;

import rs2.abyssalps.model.player.Client;
import rs2.net.packet.impl.AttackPlayer;
import rs2.net.packet.impl.Bank10;
import rs2.net.packet.impl.Bank5;
import rs2.net.packet.impl.BankAll;
import rs2.net.packet.impl.BankAllButOne;
import rs2.net.packet.impl.BankModifiableX;
import rs2.net.packet.impl.BankX1;
import rs2.net.packet.impl.BankX2;
import rs2.net.packet.impl.ChallengePlayer;
import rs2.net.packet.impl.ChangeAppearance;
import rs2.net.packet.impl.ChangeRegions;
import rs2.net.packet.impl.Chat;
import rs2.net.packet.impl.ClanChat;
import rs2.net.packet.impl.ClickItem;
import rs2.net.packet.impl.ClickNPC;
import rs2.net.packet.impl.ClickObject;
import rs2.net.packet.impl.ClickingButtons;
import rs2.net.packet.impl.ClickingInGame;
import rs2.net.packet.impl.ClickingStuff;
import rs2.net.packet.impl.Commands;
import rs2.net.packet.impl.Dialogue;
import rs2.net.packet.impl.DropItem;
import rs2.net.packet.impl.FollowPlayer;
import rs2.net.packet.impl.IdleLogout;
import rs2.net.packet.impl.InputField;
import rs2.net.packet.impl.ItemClick2;
import rs2.net.packet.impl.ItemClick3;
import rs2.net.packet.impl.ItemOnGroundItem;
import rs2.net.packet.impl.ItemOnItem;
import rs2.net.packet.impl.ItemOnNpc;
import rs2.net.packet.impl.ItemOnObject;
import rs2.net.packet.impl.MagicOnFloorItems;
import rs2.net.packet.impl.MagicOnItems;
import rs2.net.packet.impl.MoveItems;
import rs2.net.packet.impl.PickupItem;
import rs2.net.packet.impl.PrivateMessaging;
import rs2.net.packet.impl.RemoveItem;
import rs2.net.packet.impl.SilentPacket;
import rs2.net.packet.impl.Trade;
import rs2.net.packet.impl.Walking;
import rs2.net.packet.impl.WearItem;
import rs2.net.packet.impl.action.InterfaceAction;
import rs2.net.packet.impl.action.JoinChat;
import rs2.net.packet.impl.action.ReceiveString;

public class PacketHandler {

	private static PacketType packetId[] = new PacketType[256];

	static {

		SilentPacket u = new SilentPacket();
		packetId[3] = u;
		packetId[202] = u;
		packetId[77] = u;
		packetId[86] = u;
		packetId[78] = u;
		packetId[36] = u;
		packetId[226] = u;
		packetId[246] = u;
		packetId[148] = u;
		packetId[183] = u;
		packetId[230] = u;
		packetId[136] = u;
		packetId[189] = u;
		packetId[152] = u;
		packetId[200] = u;
		packetId[85] = u;
		packetId[165] = u;
		packetId[238] = u;
		packetId[150] = u;
		packetId[14] = u;
		packetId[18] = u;
		packetId[142] = new InputField();
		packetId[40] = new Dialogue();
		ClickObject co = new ClickObject();
		packetId[132] = co;
		packetId[252] = co;
		packetId[70] = co;
		packetId[57] = new ItemOnNpc();
		ClickNPC cn = new ClickNPC();
		packetId[72] = cn;
		packetId[131] = cn;
		packetId[155] = cn;
		packetId[17] = cn;
		packetId[21] = cn;
		packetId[16] = new ItemClick2();
		packetId[75] = new ItemClick3();
		packetId[122] = new ClickItem();
		packetId[241] = new ClickingInGame();
		packetId[4] = new Chat();
		packetId[236] = new PickupItem();
		packetId[87] = new DropItem();
		packetId[185] = new ClickingButtons();
		packetId[130] = new ClickingStuff();
		packetId[103] = new Commands();
		packetId[214] = new MoveItems();
		packetId[237] = new MagicOnItems();
		packetId[181] = new MagicOnFloorItems();
		packetId[202] = new IdleLogout();
		AttackPlayer ap = new AttackPlayer();
		packetId[73] = ap;
		packetId[249] = ap;
		packetId[128] = new ChallengePlayer();
		packetId[139] = new Trade();
		packetId[39] = new FollowPlayer();
		packetId[41] = new WearItem();
		packetId[145] = new RemoveItem();
		packetId[117] = new Bank5();
		packetId[43] = new Bank10();
		packetId[129] = new BankAll();
		packetId[101] = new ChangeAppearance();
		PrivateMessaging pm = new PrivateMessaging();
		packetId[188] = pm;
		packetId[126] = pm;
		packetId[215] = pm;
		packetId[59] = pm;
		packetId[95] = pm;
		packetId[133] = pm;
		packetId[74] = pm;
		packetId[135] = new BankX1();
		packetId[208] = new BankX2();
		packetId[140] = new BankAllButOne();
		packetId[141] = new BankModifiableX();
		Walking w = new Walking();
		packetId[98] = w;
		packetId[164] = w;
		packetId[248] = w;
		packetId[53] = new ItemOnItem();
		packetId[192] = new ItemOnObject();
		packetId[25] = new ItemOnGroundItem();
		ChangeRegions cr = new ChangeRegions();
		packetId[121] = cr;
		packetId[210] = cr;
		packetId[60] = new JoinChat();
		packetId[127] = new ReceiveString();
		packetId[213] = new InterfaceAction();
	}

	public static void processPacket(Client c, int packetType, int packetSize) {
		if (packetType == -1) { // this
			return;
		}
		PacketType p = packetId[packetType];
		if (p != null) {
			try {
				p.processPacket(c, packetType, packetSize);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(c.playerName
					+ " is sending invalid PacketType: " + packetType
					+ ". PacketSize: " + packetSize);
			c.disconnected = true;
		}
	}

}
