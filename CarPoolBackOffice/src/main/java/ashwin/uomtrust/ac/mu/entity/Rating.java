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
	private Long ratingId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "requestId", nullable = true)
	private Request request;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accountId", nullable = true)
	private Account account;
	
	private String comment;
	
	private Double rating;
	
	private Long raterId;

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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Long getRaterId() {
		return raterId;
	}

	public void setRaterId(Long raterId) {
		this.raterId = raterId;
	}
}
