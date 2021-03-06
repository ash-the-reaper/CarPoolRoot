package rode1lift.ashwin.uomtrust.mu.rod1lift.ENUM;

import java.util.HashMap;
import java.util.Map;

public enum RequestStatus {
	REQUEST_PENDING(0),
	REQUEST_CANCEL(1),
	PASSENGER_ACCEPTED(2),
	PASSENGER_REJECTED(3),
	DRIVER_ACCEPTED(4),
	DRIVER_REJECTED(5),
	PAID(6),
	FULL(7);


	private int value ;
	private static final Map<Integer, RequestStatus> map = new HashMap<>();


	RequestStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	static {
		for (RequestStatus item : values()) {
			map.put(item.getValue(), item);
		}
	}

	public static RequestStatus valueFor(int ref) {
		return map.get(ref);
	}
}