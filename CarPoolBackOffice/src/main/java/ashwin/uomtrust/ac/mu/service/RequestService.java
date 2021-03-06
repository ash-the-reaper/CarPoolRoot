package ashwin.uomtrust.ac.mu.service;

import java.util.List;

import ashwin.uomtrust.ac.mu.dto.RequestDTO;
import ashwin.uomtrust.ac.mu.dto.RequestObject;
import ashwin.uomtrust.ac.mu.entity.Account;
import ashwin.uomtrust.ac.mu.entity.Request;

public interface RequestService {

	public RequestDTO save(RequestDTO requestDTO);
	public Boolean driverDeletePendingRequest(Long requestId);

	public List<RequestObject> driverGetPendingRequestList(RequestDTO requestDTO);
	public List<RequestObject> driverGetHistoryList(Account account);
	public List<RequestObject> passengerGetNewList(RequestDTO requestDTO);
	
	//admin
	public int getTotalTripsCreatedToday();
	public int getTotalTripsForToday();
	public int getTotalTrips();

}
