package ashwin.uomtrust.ac.mu.enums;

import java.util.HashMap;
import java.util.Map;

public enum AccountRole {
	DRIVER(0),
	PASSENGER(1),
	ADMIN(2);
	
	private int value ;
	private static final Map<Integer, AccountRole> map = new HashMap<>();


	AccountRole(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	static {
		for (AccountRole item : values()) {
			map.put(item.getValue(), item);
		}
	}

	public static AccountRole valueFor(int ref) {
		return map.get(ref);
	}
}