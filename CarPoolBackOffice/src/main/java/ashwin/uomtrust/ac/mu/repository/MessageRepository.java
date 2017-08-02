package ashwin.uomtrust.ac.mu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ashwin.uomtrust.ac.mu.entity.Message;



@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {	
	
	@Query("select m from Message m join m.account a where a.accountId =:accountId or m.otherUserId =:accountId")
	public List<Message> getMessageByAccountId(@Param("accountId") Long accountId);
	
}
