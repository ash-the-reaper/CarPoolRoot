package ashwin.uomtrust.ac.mu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ashwin.uomtrust.ac.mu.entity.TripRating;



@Repository
public interface TripRatingRepository extends JpaRepository<TripRating, Long> {	
	
	@Query("select avg(t.rating) from TripRating t join t.userAccount a where a.accountId =:accountId")
	public Double getDriverRating(@Param("accountId") Long accountId);	
	
}
