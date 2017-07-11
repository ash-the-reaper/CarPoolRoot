package ashwin.uomtrust.ac.mu.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ashwin.uomtrust.ac.mu.dto.RequestDTO;
import ashwin.uomtrust.ac.mu.entity.Account;
import ashwin.uomtrust.ac.mu.entity.ManageRequest;
import ashwin.uomtrust.ac.mu.entity.Request;
import ashwin.uomtrust.ac.mu.enums.RequestStatus;
import ashwin.uomtrust.ac.mu.repository.AccountRepository;
import ashwin.uomtrust.ac.mu.repository.RequestRepository;

@Service
public class RequestServiceImp implements RequestService{
	
	@Autowired
	private RequestRepository requestRepository;
	
	@Autowired
	private AccountRepository accountRepository;


	@Override
	public RequestDTO save(RequestDTO requestDTO) {
		Account account = accountRepository.findOne(requestDTO.getAccountId());
		
		Calendar calendar = Calendar.getInstance();
		
		Request request = new Request();
		
		if(requestDTO.getRequestId() != null && requestDTO.getRequestId()>0)
			request.setRequestId(requestDTO.getRequestId());
		
		request.setAccount(account);
		
		if(requestDTO.getDateCreated() != null){
			calendar.setTimeInMillis(requestDTO.getDateCreated().getTime());
			request.setDateCreated(calendar.getTime());
		}
		
		if(requestDTO.getDateUpdated() != null){
			calendar.setTimeInMillis(requestDTO.getDateUpdated().getTime());
			request.setDateUpdated(calendar.getTime());	
		}
		
		calendar.setTimeInMillis(requestDTO.getEventDate().getTime());
		request.setEventDate(calendar.getTime());
		
		request.setPlaceFrom(requestDTO.getPlaceFrom());
		request.setPlaceTo(requestDTO.getPlaceTo());
		request.setRequestStatus(requestDTO.getRequestStatus());
		
		Request newRequest = requestRepository.save(request);
		
		RequestDTO newRequestDTO = requestDTO;
		newRequestDTO.setRequestId(newRequest.getRequestId());
		
		return newRequestDTO;
	}

	/*@Override
	public Boolean delete(Integer request_id) {
		// TODO Auto-generated method stub
		requestRepository.deleteRequestById(request_id);
		Request request = requestRepository.getRequestById(request_id);
		Boolean result = false;
		
		if(request == null || request.getRequest_id() == null)
			result = true;
		
		return result;
	}
*/
	/*@Override
	public List<RequestDTO> getRequestByUserIdAndRequestStatus(RequestDTO requestDTO) {
		// TODO Auto-generated method stub
		List<Request> requestList = requestRepository.getRequestByUserIdAndRequestStatus(requestDTO.getAccountId(), requestDTO.getRequestStatus().getValue());
		
		List<RequestDTO> requestDTOs = new ArrayList<>();
		
		for(Request request : requestList){
			RequestDTO newRequestDTO = new RequestDTO();
			newRequestDTO.setAccountId(request.getAccount().getAccountId());
			newRequestDTO.setEventDateTime(request.getEvent_date_time().getTime());
			newRequestDTO.setPlaceFrom(request.getPlace_from());
			newRequestDTO.setPlaceTo(request.getPlace_to());
			newRequestDTO.setRequestId(request.getRequest_id());
			newRequestDTO.setDetails(request.getDetails());
			newRequestDTO.setRequestStatus(RequestStatus.valueFor(request.getRequest_status()));
			
			requestDTOs.add(newRequestDTO);
		}
		
		return requestDTOs;
	}*/
	
	/*@Override
	public List<RequestDTO> getOtherRequestByUserIdAndRequestStatus(RequestDTO requestDTO) {
		// TODO Auto-generated method stub
		
		return manageRequestService.getManageRequestByStatusForUser(requestDTO.getRequestStatus().getValue(), requestDTO.getAccountId());
	}
	*/
	
	/*@Override
	public List<RequestDTO> getPendingRequestListTaxi(RequestDTO requestDTO) {
		// TODO Auto-generated method stub
		String taxiAddress = accountRepository.findByAccountId(requestDTO.getAccountId()).getAddress();
		
		List<Request> requestList = requestRepository.getRequestByStatusForTaxi(requestDTO.getRequestStatus().getValue());
		Account account = accountRepository.findByAccountId(requestDTO.getAccountId());
		CarDetails carDetails = carDetailsRepository.getCarByAccountId(account.getAccountId());
		
		List<ManageRequest> manageRequestList = manageRequestRepository.getManageRequestForTaxi(carDetails.getCarId());
		
		
		List<Request> sameAddressRequestList = new ArrayList<>();
		if(requestList != null && requestList.size() >0){
			for(Request r : requestList){
				if(r.getPlace_from().equalsIgnoreCase(taxiAddress))
					sameAddressRequestList.add(r);
			}
		}
		
		
		List<Request> filteredRequestList = new ArrayList<>();
		if(manageRequestList != null && manageRequestList.size() >0 && sameAddressRequestList != null && sameAddressRequestList.size()>0){
			for(ManageRequest m : manageRequestList ){
				boolean found = false;
				Request newRequest = new Request();
				
				for(Request request : sameAddressRequestList){
					if(m.getRequest().getRequest_id().equals(request.getRequest_id())){
						newRequest = request;
						found = true;
					}
				}
				
				if(!found){
					filteredRequestList.add(newRequest);
				}
			}
		}
		
		if(manageRequestList == null || manageRequestList.size() <1)
			filteredRequestList = sameAddressRequestList;
		
		List<RequestDTO> finalRequestList = new ArrayList<>();
		for(Request request : filteredRequestList){
			RequestDTO newRequestDTO = new RequestDTO();
			newRequestDTO.setAccountId(request.getAccount().getAccountId());
			newRequestDTO.setEventDateTime(request.getEvent_date_time().getTime());
			newRequestDTO.setPlaceFrom(request.getPlace_from());
			newRequestDTO.setPlaceTo(request.getPlace_to());
			newRequestDTO.setRequestId(request.getRequest_id());
			newRequestDTO.setDetails(request.getDetails());
			newRequestDTO.setRequestStatus(RequestStatus.valueFor(request.getRequest_status()));
			
			finalRequestList.add(newRequestDTO);
		}
		
		return finalRequestList;
	}
*/
	
	
	/*@Override
	public RequestDTO acceptOrRejectRequestTaxi(RequestDTO requestDTO) {
		// TODO Auto-generated method stub
		Request request = requestRepository.getRequestById(requestDTO.getRequestId());
		CarDetails carDetails = carDetailsRepository.getCarById(requestDTO.getCarId());
		
		ManageRequest manageRequest = manageRequestRepository.getManageRequestForTaxiByRequestId(requestDTO.getRequestId(), carDetails);
		
		if(manageRequest == null || manageRequest.getManage_request_id() == null)
			manageRequest= new ManageRequest();
		
		manageRequest.setCarDetails(carDetails);
		manageRequest.setRequest(request);
		
		if(requestDTO.getPrice() != null)
			manageRequest.setPrice(requestDTO.getPrice());
		
		manageRequest.setUserAccount(request.getAccount());
		
		manageRequest.setDate_created(new Date());
		manageRequest.setDate_updated(new Date());
		manageRequest.setPrice(requestDTO.getPrice());
		manageRequest.setRequest_status(requestDTO.getRequestStatus().getValue());
		
		manageRequestRepository.save(manageRequest);
		
		return requestDTO;
	}
*/
	/*@Override
	public RequestDTO acceptOrRejectRequestUser(RequestDTO requestDTO) {
		// TODO Auto-generated method stub
		Request request = requestRepository.getRequestById(requestDTO.getRequestId());
		CarDetails carDetails = carDetailsRepository.getCarById(requestDTO.getCarId());
		
		ManageRequest manageRequest = manageRequestRepository.getManageRequestForTaxiByRequestId(requestDTO.getRequestId(), carDetails);
		
		if(manageRequest == null || manageRequest.getManage_request_id() == null)
			manageRequest= new ManageRequest();
		
		manageRequest.setCarDetails(carDetails);
		manageRequest.setRequest(request);
		
		if(requestDTO.getPrice() != null)
			manageRequest.setPrice(requestDTO.getPrice());
		
		manageRequest.setUserAccount(request.getAccount());
		
		manageRequest.setDate_created(new Date());
		manageRequest.setDate_updated(new Date());
		manageRequest.setPrice(requestDTO.getPrice());
		manageRequest.setRequest_status(requestDTO.getRequestStatus().getValue());
		
		manageRequestRepository.save(manageRequest);
		
		if(requestDTO.getRequestStatus() == RequestStatus.CLIENT_ACCEPTED){
			request.setRequest_status(RequestStatus.CLIENT_ACCEPTED.getValue());
			requestRepository.save(request);
		}
		
		return requestDTO;
	}*/

}