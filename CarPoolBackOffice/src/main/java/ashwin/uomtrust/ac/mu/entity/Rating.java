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
public class Rating implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3893345518310273706L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ratingId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "requestId", nullable = true)
	private Request request;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accountId", nullable = true)
	private Account userAccount;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "carId", nullable = true)
	private Car car;

	public Integer getRatingId() {
		return ratingId;
	}

	public void setRatingId(Integer ratingId) {
		this.ratingId = ratingId;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Account getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(Account userAccount) {
		this.userAccount = userAccount;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
}