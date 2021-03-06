package ashwin.uomtrust.ac.mu.enums;

import java.util.HashMap;
import java.util.Map;

public enum Gender {
	MALE(0),
	FEMALE(1);
	
	private int value ;
	private static final Map<Integer, Gender> map = new HashMap<>();


	Gender(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	static {
		for (Gender item : values()) {
			map.put(item.getValue(), item);
		}
	}

	public static Gender valueFor(int ref) {
		return map.get(ref);
	}
}