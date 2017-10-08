package rs2.abyssalps.model.player;

import rs2.Server;
import rs2.abyssalps.content.MaxCape;
import rs2.abyssalps.content.TotalTime;
import rs2.abyssalps.content.WildyTeleports;
import rs2.abyssalps.content.skill.slayer.SlayerConstants;
import rs2.util.Misc;

public class DialogueHandler {

	private Client c;

	public DialogueHandler(Client client) {
		this.c = client;
	}

	/**
	 * Handles all talking
	 * 
	 * @param dialogue
	 *            The dialogue you want to use
	 * @param npcId
	 *            The npc id that the chat will focus on during the chat
	 */
	public void sendDialogues(int dialogue, int npcId) {
		c.talkingNpc = npcId;
		switch (dialogue) {
		case 0:
			c.talkingNpc = -1;
			c.getPA().removeAllWindows();
			c.nextChat = 0;
			break;
		case 1:
			sendStatement("You found a hidden tunnel! Do you want to enter it?");
			c.dialogueAction = 1;
			c.nextChat = 2;
			break;
		case 2:
			sendOption2("Yea! I'm fearless!", "No way! That looks scary!");
			c.dialogueAction = 1;
			c.nextChat = 0;
			break;

		case 3:
			npcTalk("You currently have " + c.getPoints()[0]
					+ " Assault points", "Lanthus", 5721);
			c.nextChat = 0;
			break;

		case 4:
			npcTalk("You need a Ecumenical key to enter this chamber.",
					"AbyssalPS Guide", 3308);
			c.nextChat = 0;
			break;

		case 7:
			if (!c.setPin) {
				npcTalk("You are about to create a bank pin.",
						"AbyssalPS Banker", 394);
				c.nextChat = 8;
			} else {
				npcTalk("You already have a bank pin.", "AbyssalPS Banker", 394);
				c.nextChat = 0;
			}
			break;

		case 8:
			if (!c.setPin) {
				c.getBankPin().open();
				c.nextChat = 0;
			}
			break;

		case 9:
			c.getDH().sendOption2("Cosmetics", "Weapons / Gear");
			c.interfaceAction = 7;
			break;

		case 10:
			npcTalk("Hello " + c.playerName + " where would you like to go?",
					"Wizard", 1160);
			c.nextChat = 11;
			break;

		case 11:
			sendOption5("Scorpia", "Chaos Fanatic", "Venenatis",
					"Crazy archaeologist", "Next Page");
			c.interfaceAction = 8;
			break;

		case 12:
			WildyTeleports.startTeleport(c, c.wildyTeleport, 0);
			break;

		case 13:
			WildyTeleports.startTeleport(c, c.wildyTeleport, 1);
			break;

		case 18:
			if (SlayerConstants.hasTask(c)) {
				npcTalk("You have a task, would you like to reset it?",
						"Mazchna", 402);
				c.nextChat = 21;
				return;
			}
			npcTalk("What type of task would you like?", "Mazchna", 402);
			c.nextChat = 19;
			break;

		case 19:
			if (SlayerConstants.hasTask(c)) {
				npcTalk("You have a task, would you like to reset it?",
						"Mazchna", 402);
				c.nextChat = 21;
				return;
			}
			sendOption3("Easy Task", "Medium Task", "Hard Task");
			c.interfaceAction = 11;
			break;

		case 20:
			npcTalk("Your task is to kill " + c.getSlayerData()[1] + " "
					+ Server.npcHandler.getNpcListName(c.getSlayerData()[0])
					+ "s.", "Mazchna", 402);
			c.nextChat = 0;
			break;

		case 21:
			sendOption2("Yes", "No");
			c.interfaceAction = 12;
			break;

		case 24:
			npcTalk("You need "
					+ Misc.formatNumbers(SlayerConstants.TASK_RESET_FEE)
					+ " coins to do this.", "Mazchna", 402);
			c.nextChat = 0;
			break;

		case 25:
			npcTalk("Your slayer task has been reset.", "Mazchna", 402);
			c.nextChat = 0;
			break;

		case 26:
			sendOption2("Yes", "No");
			c.interfaceAction = 15;
			break;

		case 27:
			npcTalk("Beautiful day, isn't it?", "Hans", 3077);
			c.nextChat = 0;
			break;

		case 28:
			String days = c.getTotalTime().getTime()[3] > 1
					|| c.getTotalTime().getTime()[3] == 0 ? "days" : "day";
			String hours = c.getTotalTime().getTime()[2] > 1
					|| c.getTotalTime().getTime()[2] == 0 ? "hours" : "hour";
			String minutes = c.getTotalTime().getTime()[1] > 1
					|| c.getTotalTime().getTime()[1] == 0 ? "minutes"
					: "minute";
			npcTalk2("You've spent " + c.getTotalTime().getTime()[3] + " "
					+ days + ", " + c.getTotalTime().getTime()[2] + " " + hours
					+ ", " + c.getTotalTime().getTime()[1] + " " + minutes
					+ " in the world.",
					"since you arrived at " + TotalTime.getTime(c.playerName)
							+ ".", "Hans", 3077);
			c.nextChat = 0;
			break;

		case 29:
			npcTalk2("Would you like to buy the Max Cape?", "For a price of "
					+ Misc.formatNumbers(MaxCape.REQUIRED_GP) + " gp?", "Mac",
					6481);
			c.nextChat = 30;
			break;

		case 30:
			sendOption2("Yes", "No");
			c.interfaceAction = 16;
			c.nextChat = 0;
			break;

		case 31:
			npcTalk2("You don't have " + MaxCape.REQUIRED_MAX_SKILLS
					+ " maxed skills,", "which is required to buy this cape.",
					"Mac", 6481);
			c.nextChat = 0;
			break;

		case 32:
			npcTalk("You need " + Misc.formatNumbers(MaxCape.REQUIRED_GP)
					+ " gp to buy this cape.", "Mac", 6481);
			c.nextChat = 0;
			break;

		case 33:
			npcTalk("Thanks for your purchase.", "Mac", 6481);
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995),
					MaxCape.REQUIRED_GP);
			c.getItems().addItem(13280, 1);
			c.getItems().addItem(13281, 1);
			c.nextChat = 0;
			break;

		case 2900:
			sendStatement("Are you ready to visit the chest room?");
			c.nextChat = 3000;
			c.dialogueAction = 29;
			break;

		case 3000:
			sendOption2("Yes, I've killed all the other brothers!",
					"No, I still need to kill more brothers");
			c.nextChat = 0;
			break;
		}
	}

	/*
	 * Information Box
	 */

	public void sendStartInfo(String text, String text1, String text2,
			String text3, String title) {
		c.getPA().sendFrame126(title, 6180);
		c.getPA().sendFrame126(text, 6181);
		c.getPA().sendFrame126(text1, 6182);
		c.getPA().sendFrame126(text2, 6183);
		c.getPA().sendFrame126(text3, 6184);
		c.getPA().sendFrame164(6179);
	}

	/*
	 * Options
	 */

	private void sendOption(String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2470);
		c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126(s1, 2472);
		c.getPA().sendFrame126("Click here to continue", 2473);
		c.getPA().sendFrame164(13758);
	}

	public void sendOption2(String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame164(2459);
	}

	public void sendOption3(String text, String text2, String text3) {
		c.getPA().sendFrame126(text, 2471);
		c.getPA().sendFrame126(text2, 2472);
		c.getPA().sendFrame126(text3, 2473);
		c.getPA().sendFrame164(2469);
	}

	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame126("Select an Option", 2481);
		c.getPA().sendFrame126(s, 2482);
		c.getPA().sendFrame126(s1, 2483);
		c.getPA().sendFrame126(s2, 2484);
		c.getPA().sendFrame126(s3, 2485);
		c.getPA().sendFrame164(2480);
	}

	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendFrame126("Select an Option", 2493);
		c.getPA().sendFrame126(s, 2494);
		c.getPA().sendFrame126(s1, 2495);
		c.getPA().sendFrame126(s2, 2496);
		c.getPA().sendFrame126(s3, 2497);
		c.getPA().sendFrame126(s4, 2498);
		c.getPA().sendFrame164(2492);
	}

	public void sendItemChat1(String header, String one, int item, int zoom) {
		c.getPA().sendFrame246(4883, zoom, item);
		c.getPA().sendFrame126(header, 4884);
		c.getPA().sendFrame126(one, 4885);
		c.getPA().sendFrame164(4882);
	}

	public void sendItemChat2(String header, String one, String two, int item,
			int zoom) {
		c.getPA().sendFrame246(4888, zoom, item);
		c.getPA().sendFrame126(header, 4889);
		c.getPA().sendFrame126(one, 4890);
		c.getPA().sendFrame126(two, 4891);
		c.getPA().sendFrame164(4887);
	}

	public void sendItemChat3(String header, String one, String two,
			String three, int item, int zoom) {
		c.getPA().sendFrame246(4894, zoom, item);
		c.getPA().sendFrame126(header, 4895);
		c.getPA().sendFrame126(one, 4896);
		c.getPA().sendFrame126(two, 4897);
		c.getPA().sendFrame126(three, 4898);
		c.getPA().sendFrame164(4893);
	}

	public void sendItemChat4(String header, String one, String two,
			String three, String four, int item, int zoom) {
		c.getPA().sendFrame246(4901, zoom, item);
		c.getPA().sendFrame126(header, 4902);
		c.getPA().sendFrame126(one, 4903);
		c.getPA().sendFrame126(two, 4904);
		c.getPA().sendFrame126(three, 4905);
		c.getPA().sendFrame126(four, 4906);
		c.getPA().sendFrame164(4900);
	}

	/*
	 * Statements
	 */

	public void sendStatement(String s) { // 1 line click here to continue chat
											// box interface
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}

	/*
	 * Npc Chatting
	 */

	private void sendNpcChat1(String s) {

	}

	public void npcTalk(String text, String name, int chatNpc) {
		c.getPA().sendFrame200(4883, 591);
		c.getPA().sendFrame126(name, 4884);
		c.getPA().sendFrame126(text, 4885);
		c.getPA().sendFrame126("Click here to continue.", 4886);
		c.getPA().sendFrame75(chatNpc, 4883);
		c.getPA().sendFrame164(4882);
	}

	public void npcTalk2(String text, String text2, String name, int chatNpc) {
		c.getPA().sendFrame200(4888, 591);
		c.getPA().sendFrame126(name, 4889);
		c.getPA().sendFrame126(text, 4890);
		c.getPA().sendFrame126(text2, 4891);
		c.getPA().sendFrame75(chatNpc, 4888);
		c.getPA().sendFrame164(4887);
	}

	private void sendNpcChat4(String s, String s1, String s2, String s3,
			int ChatNpc, String name) {
		c.getPA().sendFrame200(4901, 591);
		c.getPA().sendFrame126(name, 4902);
		c.getPA().sendFrame126(s, 4903);
		c.getPA().sendFrame126(s1, 4904);
		c.getPA().sendFrame126(s2, 4905);
		c.getPA().sendFrame126(s3, 4906);
		c.getPA().sendFrame75(ChatNpc, 4901);
		c.getPA().sendFrame164(4900);
	}

	/*
	 * Player Chating Back
	 */

	private void sendPlayerChat1(String s) {
		c.getPA().sendFrame200(969, 591);
		c.getPA().sendFrame126(c.playerName, 970);
		c.getPA().sendFrame126(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendFrame164(968);
	}

	private void sendPlayerChat2(String s, String s1) {
		c.getPA().sendFrame200(974, 591);
		c.getPA().sendFrame126(c.playerName, 975);
		c.getPA().sendFrame126(s, 976);
		c.getPA().sendFrame126(s1, 977);
		c.getPA().sendFrame185(974);
		c.getPA().sendFrame164(973);
	}

	private void sendPlayerChat3(String s, String s1, String s2) {
		c.getPA().sendFrame200(980, 591);
		c.getPA().sendFrame126(c.playerName, 981);
		c.getPA().sendFrame126(s, 982);
		c.getPA().sendFrame126(s1, 983);
		c.getPA().sendFrame126(s2, 984);
		c.getPA().sendFrame185(980);
		c.getPA().sendFrame164(979);
	}

	private void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame200(987, 591);
		c.getPA().sendFrame126(c.playerName, 988);
		c.getPA().sendFrame126(s, 989);
		c.getPA().sendFrame126(s1, 990);
		c.getPA().sendFrame126(s2, 991);
		c.getPA().sendFrame126(s3, 992);
		c.getPA().sendFrame185(987);
		c.getPA().sendFrame164(986);
	}
}
