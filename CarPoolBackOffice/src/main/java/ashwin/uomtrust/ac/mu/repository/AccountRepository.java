package ashwin.uomtrust.ac.mu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ashwin.uomtrust.ac.mu.entity.Account;



@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
		
		public Account findByAccountId(Long accountId);
		public Account findByEmail(String email);
		
}