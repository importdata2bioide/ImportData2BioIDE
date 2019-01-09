import java.util.HashMap;
import java.util.Map;

public class TestMap {
	
	public static void main(String[] args) {
		Map<String,String> users = new HashMap<>();
		users.put("1", "");
		System.out.println(users.isEmpty());
	}

}
