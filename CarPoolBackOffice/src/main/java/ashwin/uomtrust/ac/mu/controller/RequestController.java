package ashwin.uomtrust.ac.mu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ashwin.uomtrust.ac.mu.dto.RequestDTO;
import ashwin.uomtrust.ac.mu.dto.RequestObject;
import ashwin.uomtrust.ac.mu.service.AccountService;
import ashwin.uomtrust.ac.mu.service.CarService;
import ashwin.uomtrust.ac.mu.service.ManageRequestService;
import ashwin.uomtrust.ac.mu.service.RequestService;

@RestController
@RequestMapping("/api/request")
public class RequestController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private ManageRequestService manageRequestService;
	
	
	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping(value = "/driverCreateUpdateRequest", method = RequestMethod.POST)
	public RequestDTO driverCreateUpdateRequest(@RequestBody RequestDTO requestDTO) {    	
		return requestService.save(requestDTO);
	}
	
	@CrossOrigin(origins = "http://localhost:8081")
   	@RequestMapping(value = "/driverGetPendingRequestList", method = RequestMethod.POST)
   	public List<RequestObject> driverGetPendingRequestList(@RequestBody RequestDTO requestDTO) {    	    	
    	return requestService.driverGetPendingRequestList(requestDTO);
   	}

	@CrossOrigin(origins = "http://localhost:8081")
   	@RequestMapping(value = "/driverDeletePendingRequest", method = RequestMethod.POST)
   	public Boolean driverDeletePendingRequest(@RequestBody RequestDTO requestDTO) {    	    	
    	return requestService.driverDeletePendingRequest(requestDTO.getRequestId());
   	}
	
	@CrossOrigin(origins = "http://localhost:8081")
   	@RequestMapping(value = "/driverDeleteClientRequest", method = RequestMethod.POST)
   	public Boolean driverDeleteClientRequest(@RequestBody RequestDTO requestDTO) {    	
    	return manageRequestService.driverDeleteClientRequest(requestDTO.getManageRequestId());
   	}
	
	@CrossOrigin(origins = "http://localhost:8081")
   	@RequestMapping(value = "/driverAcceptClientRequest", method = RequestMethod.POST)
   	public Boolean driverAcceptClientRequest(@RequestBody RequestDTO requestDTO) {    	
    	return manageRequestService.driverAcceptClientRequest(requestDTO.getManageRequestId());
   	}
	
	@CrossOrigin(origins = "http://localhost:8081")
   	@RequestMapping(value = "/driverGetOtherRequestList", method = RequestMethod.POST)
   	public List<RequestObject> driverGetOtherRequestList(@RequestBody RequestDTO requestDTO) {    	    	
    	return manageRequestService.driverGetRequestList(requestDTO);
   	}
}
