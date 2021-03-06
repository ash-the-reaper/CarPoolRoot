package rode1lift.ashwin.uomtrust.mu.rod1lift.DTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ashwin on 05-Jun-17.
 */

public class RequestObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private RequestDTO requestDTO;
	private List<ManageRequestDTO> manageRequestDTOList;
	private List<AccountDTO> accountDTOList;
	private List<CarDTO> carDTOList;
   
	public RequestDTO getRequestDTO() {
		return requestDTO;
	}
	public void setRequestDTO(RequestDTO requestDTO) {
		this.requestDTO = requestDTO;
	}
	public List<ManageRequestDTO> getManageRequestDTOList() {
		return manageRequestDTOList;
	}
	public void setManageRequestDTOList(List<ManageRequestDTO> manageRequestDTOList) {
		this.manageRequestDTOList = manageRequestDTOList;
	}
	public List<AccountDTO> getAccountDTOList() {
		return accountDTOList;
	}
	public void setAccountDTOList(List<AccountDTO> accountDTOList) {
		this.accountDTOList = accountDTOList;
	}

	public List<CarDTO> getCarDTO() {
		return carDTOList;
	}

	public void setCarDTO(List<CarDTO>  carDTOList) {
		this.carDTOList = carDTOList;
	}
}
