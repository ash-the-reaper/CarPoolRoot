package ashwin.uomtrust.ac.mu.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ashwin.uomtrust.ac.mu.enums.AccountRole;
import ashwin.uomtrust.ac.mu.enums.AccountStatus;


@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long accountId;
	
	private Integer phoneNum;
	
	private String fullName;
	private String facebookId;
	private String googleId;

	
	@Column(length = 100, nullable = false, unique = true)
	private String email;
	
	private AccountRole accountRole;
	private AccountStatus accountStatus;
	
	private Date dateCreated;
	private Date dateUpdated;
	
	@Column(length = 100, nullable = true)
	private String password;
	
	public Account() {
	}

	@PrePersist
	public void prePersist() {
		setDateCreated(new Date());
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}		
	
	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public AccountRole getAccountRole() {
		return accountRole;
	}

	public void setAccountRole(AccountRole accountRole) {
		this.accountRole = accountRole;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", email=" + email + ", accountRole=" + accountRole +"]";
	}

	public Integer getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(Integer phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
