package ashwin.uomtrust.ac.mu.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ashwin.uomtrust.ac.mu.dto.AccountDTO;
import ashwin.uomtrust.ac.mu.entity.Account;
import ashwin.uomtrust.ac.mu.enums.AccountRole;
import ashwin.uomtrust.ac.mu.repository.AccountRepository;
import ashwin.uomtrust.ac.mu.utils.Utils;
import scala.annotation.meta.setter;

@Service
public class AccountServiceImp implements AccountService{
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Account findByAccountId(Long accountId) {
		// TODO Auto-generated method stub
		return accountRepository.findByAccountId(accountId);
	}

	@Override
	public AccountDTO findByEmail(String email) {
		// TODO Auto-generated method stub
		Account account = accountRepository.findByEmail(email);
		AccountDTO accountDTO  = new AccountDTO();
		
		if(account != null && account.getAccountId() != null){
			accountDTO.setAccountId(account.getAccountId());
			accountDTO.setAccountRole(account.getAccountRole());
			accountDTO.setAccountStatus(account.getAccountStatus());
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(account.getDateCreated().getTime());
			accountDTO.setDateCreated(calendar.getTime());
			
			calendar.setTimeInMillis(account.getDateUpdated().getTime());
			accountDTO.setDateUpdated(calendar.getTime());
			
			accountDTO.setEmail(email);
			
			if(account.getGoogleId() != null)
				account.setGoogleId(accountDTO.getGoogleId());

			if(account.getFacebookId() != null)
				accountDTO.setFacebookId(account.getFacebookId());
			
			accountDTO.setFullName(account.getFullName());
			
			if(account.getPhoneNum() != null)
				accountDTO.setPhoneNum(account.getPhoneNum());
			
			if(account.getPassword() != null)
				accountDTO.setPassword(account.getPassword());
						
			Utils.getImageProfile(accountDTO);
		}
		
		return accountDTO;
	}

	@Override
	public Account saveAccount(Account account) {
		// TODO Auto-generated method stub
		return accountRepository.save(account);
	}

	@Override
	public AccountDTO saveAccount(AccountDTO accountDTO) {
		// TODO Auto-generated method stub
		Account account = accountRepository.findByAccountId(accountDTO.getAccountId());
		
		if(account == null)
			account = new Account();
		else
			account.setAccountId(accountDTO.getAccountId());
		
		account.setAccountRole(accountDTO.getAccountRole());
		account.setAccountStatus(accountDTO.getAccountStatus());
		
		Calendar calendar = Calendar.getInstance();
		
		if(accountDTO.getDateCreated() != null){
			calendar.setTimeInMillis(accountDTO.getDateCreated().getTime());
			account.setDateCreated(accountDTO.getDateCreated());
		}
		
		if(accountDTO.getDateUpdated() != null){
			calendar.setTimeInMillis(accountDTO.getDateUpdated().getTime());
			account.setDateUpdated(accountDTO.getDateUpdated());
		}
		
		if(accountDTO.getGoogleId() != null)
			account.setGoogleId(accountDTO.getGoogleId());
		
		account.setEmail(accountDTO.getEmail());
		
		if(accountDTO.getFacebookId() != null)
			account.setFacebookId(accountDTO.getFacebookId());
		
		account.setFullName(accountDTO.getFullName());
		
		if(accountDTO.getPhoneNum() != null)
			account.setPhoneNum(accountDTO.getPhoneNum());
		
		Account newAccount = accountRepository.save(account);
		
		accountDTO.setAccountId(newAccount.getAccountId());
		
		Utils.saveProfilePictureToServer(accountDTO);
		
		return accountDTO;
	}

}
