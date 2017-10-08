package rs2.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.common.IoSession;

import rs2.Config;
import rs2.Connection;
import rs2.abyssalps.model.player.Client;
import rs2.sanction.SanctionHandler;

public class HostList {

	private static HostList list = new HostList();

	public static HostList getHostList() {
		return list;
	}

	private Map<String, Integer> connections = new HashMap<String, Integer>();

	private static String connectedMac = "";

	public String getMacAddress(IoSession session) {
		try {
			try {
				InetAddress a = ((InetSocketAddress) session.getRemoteAddress())
						.getAddress().getLocalHost();

				NetworkInterface n = NetworkInterface.getByInetAddress(a);
				byte[] m = n.getHardwareAddress();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < m.length; i++) {
					sb.append(String.format("%02X%s", m[i],
							(i < m.length - 1) ? "-" : ""));
				}
				connectedMac = sb.toString();
			} catch (SocketException e) {
				e.printStackTrace();
			}
			return connectedMac;
		} catch (UnknownHostException e) {
			return null;
		}
	}

	public synchronized boolean add(IoSession session) {
		String addr = ((InetSocketAddress) session.getRemoteAddress())
				.getAddress().getHostAddress();
		Integer amt = connections.get(addr);
		if (amt == null) {
			amt = 1;
		} else {
			amt += 1;
		}
		if (amt > Config.IPS_ALLOWED || SanctionHandler.isIPBanned(addr)) {
			return false;
		} else {
			connections.put(addr, amt);
			return true;
		}
	}

	public synchronized void remove(IoSession session) {
		if (session.getAttribute("inList") != Boolean.TRUE) {
			return;
		}
		String addr = ((InetSocketAddress) session.getRemoteAddress())
				.getAddress().getHostAddress();
		Integer amt = connections.get(addr);
		if (amt == null) {
			return;
		}
		amt -= 1;
		if (amt <= 0) {
			connections.remove(addr);
		} else {
			connections.put(addr, amt);
		}
	}

}
