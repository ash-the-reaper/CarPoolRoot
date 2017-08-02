package ashwin.uomtrust.ac.mu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ashwin.uomtrust.ac.mu.dto.AccountDTO;
import ashwin.uomtrust.ac.mu.dto.CarDTO;
import ashwin.uomtrust.ac.mu.dto.DeviceDTO;
import ashwin.uomtrust.ac.mu.dto.MessageDTO;
import ashwin.uomtrust.ac.mu.entity.Account;
import ashwin.uomtrust.ac.mu.service.AccountService;
import ashwin.uomtrust.ac.mu.service.CarService;
import ashwin.uomtrust.ac.mu.service.DeviceService;
import ashwin.uomtrust.ac.mu.service.MessageService;

@RestController
@RequestMapping("/api/account")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(value = "/createAdmin", method = RequestMethod.POST)
	public Account createAdmin(@RequestBody Account account) {
		if(account != null && account.getEmail() !=null )
			return accountService.saveAccount(account);
		return null;
		
	}
	
	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(value = "/createAccount", method = RequestMethod.POST)
	public AccountDTO createAccount(@RequestBody AccountDTO accountDTO) {
		if(accountDTO != null && accountDTO.getEmail() !=null ){
			return accountService.saveAccount(accountDTO);
		}
		return null;
	}
	
	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(value = "/checkAccountViaEmail", method = RequestMethod.POST)
	public AccountDTO checkAccountViaEmail(@RequestBody Account account) {
		if(account != null && account.getEmail() !=null ){
			return accountService.findByEmail(account.getEmail());
		}
		return null;
	}
	
	
	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(value = "/driverCreateCar", method = RequestMethod.POST)
	public CarDTO driverCreateCar(@RequestBody CarDTO carDTO) {
		if(carDTO != null && carDTO.getCarId() !=null ){
			return carService.saveCar(carDTO);
		}
		return null;
	}
	
	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(value = "/driverFetchCarDetails", method = RequestMethod.POST)
	public CarDTO driverFetchCarDetails(@RequestBody CarDTO carDTO) {
		if(carDTO != null && carDTO.getAccountId() !=null ){
			return carService.findCarByAccountId(carDTO.getAccountId());
		}
		return null;
	}
	
	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(value = "/saveDeviceToken", method = RequestMethod.POST)
	public DeviceDTO saveDeviceToken(@RequestBody DeviceDTO deviceDTO) {
		if(deviceDTO != null && deviceDTO.getAccountId() !=null ){
			return deviceService.save(deviceDTO);
		}
		
		return null;
	}
	
	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(value = "/downloadMessage", method = RequestMethod.POST)
	public List<MessageDTO> downloadMessage(@RequestBody MessageDTO messageDTO) {
		if(messageDTO != null && messageDTO.getAccountId() !=null ){
			return messageService.getMessages(messageDTO);
		}
		
		return null;
	}
	
	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(value = "/saveMessage", method = RequestMethod.POST)
	public MessageDTO saveMessage(@RequestBody MessageDTO messageDTO) {
		if(messageDTO != null && messageDTO.getAccountId() != null ){
			return messageService.saveMessage(messageDTO);
		}
		
		return null;
	}

}
