package ashwin.uomtrust.ac.mu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ashwin.uomtrust.ac.mu.dto.RatingDTO;
import ashwin.uomtrust.ac.mu.entity.Account;
import ashwin.uomtrust.ac.mu.entity.Request;
import ashwin.uomtrust.ac.mu.entity.Rating;
import ashwin.uomtrust.ac.mu.repository.AccountRepository;
import ashwin.uomtrust.ac.mu.repository.CarRepository;
import ashwin.uomtrust.ac.mu.repository.RequestRepository;
import ashwin.uomtrust.ac.mu.repository.RatingRepository;

@Service
public class RatingServiceImp implements RatingService{
	
	@Autowired
	private RequestRepository requestRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private RatingRepository ratingRepository;
	
	@Autowired
	private CarRepository carRepository;

	@Override
	public void save(RatingDTO ratingDTO) {
		// TODO Auto-generated method stub
		Rating rating = new Rating();
		
		if(ratingDTO.getRatingId() != null)
			rating.setRatingId(ratingDTO.getRatingId());
		
		Account account;
		if(ratingDTO.getCarId() != null)
			account = carRepository.getCarById(ratingDTO.getCarId()).getUserAccount();
		else
			account = accountRepository.findByAccountId(ratingDTO.getAccountId());
		
		rating.setAccount(account);
		
		rating.setComment(ratingDTO.getComment());
		
		Request request = requestRepository.getRequestById(ratingDTO.getRequestId());
		rating.setRequest(request);
		
		rating.setRating(ratingDTO.getRating());
		
		rating.setRaterId(ratingDTO.getRaterId());
		
		ratingRepository.save(rating);	
	}

	@Override
	public Double getDriverRating(Long accountId) {
		// TODO Auto-generated method stub
		return ratingRepository.getRating(accountId);
	}
}
