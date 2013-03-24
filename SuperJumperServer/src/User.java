
public class User {
	
	private String nick;
	private Info info;
	protected FullDuplexBuffer buffer;
	
	public User(String nick){
		this.setNick(nick);
		this.info = new Info();
	}
	
	public User(String nick, float id){
		this.setNick(nick);
		this.info = new Info(id);
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public float getID(){
		return this.info.getID();
	}

	
	public class Info {
		private float ID;
		
		public Info(float id) {
			this.ID = id;
		}
		
		Info() {
			this.ID = 0;
		}
		
		public float getID(){
			return this.ID;
		}
	}

}
