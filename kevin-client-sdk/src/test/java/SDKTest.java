import com.kevin.kevinapiclientsdk.client.KevinApiClient;
import com.kevin.kevinapiclientsdk.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Projectname: kevinapi-backend
 * @Filename: SDKTest
 * @Author: skw
 */
@Slf4j
public class SDKTest {

	@Test
	public void testSign(){

		User user = new User();
		user.setUsername("skw");
		KevinApiClient kevinApiClient = new KevinApiClient("2061a7e1a4c6f3899848360cd16d7356", "80648946907b614c5630880833349460");

		String skw = kevinApiClient.getUsernameByPost(user);
		System.out.println(skw);

	}

}
