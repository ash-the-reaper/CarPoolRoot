package ashwin.uomtrust.ac.mu.service;

import java.util.List;

import ashwin.uomtrust.ac.mu.dto.ManageRequestDTO;
import ashwin.uomtrust.ac.mu.dto.RequestDTO;
import ashwin.uomtrust.ac.mu.dto.RequestObject;
import ashwin.uomtrust.ac.mu.entity.ManageRequest;

public interface ManageRequestService {

	public List<ManageRequest> getManageRequestByRequestId(Long requestId);
	public List<RequestObject> driverGetUserAcceptedRequestList(RequestDTO requestDTO);
	public Boolean driverDeleteClientRequest(Long manageRequestId);
	public Boolean driverAcceptClientRequest(Long manageRequestId);
	public Boolean passengerDeleteRequest(RequestDTO requestDTO);
	public boolean passengerAcceptRequest(ManageRequestDTO manageRequestDTO);
	public List<RequestObject> passengerGetPendingList(RequestDTO requestDTO);
	public List<RequestObject> passengerGetAcceptedRequest(RequestDTO requestDTO);
	public List<RequestObject> passengerGetHistoryList(RequestDTO requestDTO);
	public boolean passengerPayRequest(ManageRequestDTO manageRequestDTO);
	
}
