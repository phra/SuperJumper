/**
 * @author phra
 *
 */


public class PaccoWelcome extends Pacco implements PROTOCOL_CONSTANTS {
	private String nick;

	public PaccoWelcome(String nick) {
		super(PROTOCOL_CONSTANTS.PACKET_WELCOME);
		this.setNick(nick);
		this.setData(nick.getBytes());
		this.setSize(nick.getBytes().length);
	}

	public PaccoWelcome (Pacco pkt) throws ProtocolException {
		super(pkt.getType(),pkt.getData(),pkt.getSize());
		if (this.getType() != PROTOCOL_CONSTANTS.PACKET_WELCOME) {
			throw new ProtocolException("TYPE SBAGLIATO.");
		}
		this.setNick(new String(this.getData()));
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

}
