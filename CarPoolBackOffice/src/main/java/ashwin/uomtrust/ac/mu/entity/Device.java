package ashwin.uomtrust.ac.mu.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Device implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1657398929436188426L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long deviceId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accountId", nullable = true)
	private Account userAccount;
	
	private String deviceToken;

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Account getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(Account userAccount) {
		this.userAccount = userAccount;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
}
