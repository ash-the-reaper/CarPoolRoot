package ashwin.uomtrust.ac.mu.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ashwin.uomtrust.ac.mu.dto.AccountDTO;
import ashwin.uomtrust.ac.mu.dto.ManageRequestDTO;
import ashwin.uomtrust.ac.mu.dto.RequestDTO;
import ashwin.uomtrust.ac.mu.dto.RequestObject;
import ashwin.uomtrust.ac.mu.entity.Account;
import ashwin.uomtrust.ac.mu.entity.Car;
import ashwin.uomtrust.ac.mu.entity.ManageRequest;
import ashwin.uomtrust.ac.mu.entity.Request;
import ashwin.uomtrust.ac.mu.enums.RequestStatus;
import ashwin.uomtrust.ac.mu.repository.AccountRepository;
import ashwin.uomtrust.ac.mu.repository.ManageRequestRepository;
import ashwin.uomtrust.ac.mu.repository.RequestRepository;
import ashwin.uomtrust.ac.mu.utils.Utils;

@Service
public class ManageRequestServiceImp implements ManageRequestService{
	
	@Autowired
	private RequestRepository requestRepository;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private ManageRequestRepository manageRequestRepository;

	@Override
	public List<ManageRequest> getManageRequestByRequestId(Long requestId) {
		// TODO Auto-generated method stub
		return manageRequestRepository.getManageRequestByRequestId(requestId);
	}

	@Override
	public List<RequestObject> driverGetRequestList(RequestDTO requestDTO) {
		// TODO Auto-generated method stub
		List<ManageRequest> manageRequestList = manageRequestRepository.getDriverManageRequestByRequestStatus(requestDTO.getCarId(), requestDTO.getRequestStatus());
		
		List<RequestObject> requestObjectList = new ArrayList<>();
		
		for(ManageRequest m : manageRequestList){
			RequestObject requestObject = new RequestObject();
			
			RequestDTO newRequestDTO = new RequestDTO();
			newRequestDTO.setAccountId(m.getCar().getUserAccount().getAccountId());
			
			Request request = m.getRequest();
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(request.getEventDate().getTime());
			newRequestDTO.setEventDate(calendar.getTime());
			newRequestDTO.setPlaceFrom(request.getPlaceFrom());
			newRequestDTO.setPlaceTo(request.getPlaceTo());
			newRequestDTO.setRequestId(request.getRequestId());
			newRequestDTO.setRequestStatus(request.getRequestStatus());
			newRequestDTO.setPrice(request.getPrice());
			newRequestDTO.setSeatAvailable(request.getSeatAvailable());
			
			Car car = m.getCar();
			newRequestDTO.setCarId(car.getCarId());

			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(request.getDateCreated().getTime());
			newRequestDTO.setDateCreated(cal.getTime());
			
			cal.setTimeInMillis(request.getDateUpdated().getTime());
			newRequestDTO.setDateUpdated(cal.getTime());
			
			cal.setTimeInMillis(request.getEventDate().getTime());
			newRequestDTO.setEventDate(cal.getTime());
			
			newRequestDTO.setAccountId(request.getAccount().getAccountId());			
			
			
			Account a = m.getUserAccount();
			AccountDTO accountDTO = new AccountDTO();
			accountDTO.setAccountId(a.getAccountId());				
			accountDTO.setFirstName(a.getFirstName());
			accountDTO.setLastName(a.getLastName());
			accountDTO.setPhoneNum(a.getPhoneNum());
			Utils.getImageProfile(accountDTO);
			
			List<AccountDTO> accountDTOList = new ArrayList<>();
			accountDTOList.add(accountDTO);
			
			ManageRequestDTO manageRequestDTO = new ManageRequestDTO();
			manageRequestDTO.setAccountId(m.getUserAccount().getAccountId());
			manageRequestDTO.setCarId(m.getCar().getCarId());
			
			Calendar mCalendar = Calendar.getInstance();
			
			mCalendar.setTimeInMillis(m.getDateCreated().getTime());			
			manageRequestDTO.setDateCreated(mCalendar.getTime());
			
			mCalendar.setTimeInMillis(m.getDateUpdated().getTime());
			manageRequestDTO.setDateUpdated(mCalendar.getTime());
			
			manageRequestDTO.setSeatRequested(m.getSeatRequested());
			manageRequestDTO.setManageRequestId(m.getManageRequestId());
			manageRequestDTO.setRequestId(m.getRequest().getRequestId());
			manageRequestDTO.setRequestStatus(m.getRequestStatus());
			
			List<ManageRequestDTO> manageRequestDTOList = new ArrayList<>();
			manageRequestDTOList.add(manageRequestDTO);
			
			requestObject.setAccountDTOList(accountDTOList);
			requestObject.setRequestDTO(newRequestDTO);
			requestObject.setManageRequestDTOList(manageRequestDTOList);			
			requestObjectList.add(requestObject);
		}

		return requestObjectList;
	}
	
	@Override
	public Boolean driverDeleteClientRequest(Long manageRequestId) {
		// TODO Auto-generated method stub
		ManageRequest m = manageRequestRepository.findOne(manageRequestId);
		m.setRequestStatus(RequestStatus.DRIVER_REJECTED);
		manageRequestRepository.save(m);
		
		return true;
	}
	
	
	@Override
	public Boolean driverAcceptClientRequest(Long manageRequestId) {
		// TODO Auto-generated method stub
		ManageRequest m = manageRequestRepository.findOne(manageRequestId);
		m.setRequestStatus(RequestStatus.DRIVER_ACCEPTED);
		manageRequestRepository.save(m);
		
		return true;
	}
}
