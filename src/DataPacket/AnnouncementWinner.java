package DataPacket;

import java.io.Serializable;

public class AnnouncementWinner extends DataPacket implements Serializable {
	public static String tag = "AnnouncementWinner";
	public String accountName;
	
	
	
	public AnnouncementWinner() {
		super(tag);
	}

	public AnnouncementWinner(String accountName) {
		super(tag);
		this.accountName = accountName;
	}
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	
	
}
