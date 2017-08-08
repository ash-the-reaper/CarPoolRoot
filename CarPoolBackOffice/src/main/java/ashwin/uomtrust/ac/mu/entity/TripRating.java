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
public class TripRating implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3893345518310273706L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ratingId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "requestId", nullable = true)
	private Request request;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accountId", nullable = true)
	private Account userAccount;
	
	private String comment;
	
	private float rating;

	public Long getRatingId() {
		return ratingId;
	}

	public void setRatingId(Long ratingId) {
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}
}
