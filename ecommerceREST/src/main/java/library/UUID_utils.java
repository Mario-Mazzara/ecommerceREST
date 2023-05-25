package library;

import java.util.HexFormat;
import java.util.UUID;

public class UUID_utils {
	public static byte[] getRandomUUID() {
		return parseUUID(UUID.randomUUID().toString().replace("-", "").toUpperCase());
	}
	
	public static byte[] parseUUID (String Id) {
		return HexFormat.of().parseHex(Id);
	}
}

