import api.Specifications;
import api.UserApi;
import api.UserData;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserLoginTest extends BaseForTests {

    @Before
    public void setUp() {
        super.setUp();
        user = new UserData("luffy@gear5.com", "GomuGomuNo", "Luffy");
        registerUser(user);
    }

    @Test
    public void successfulLoginTest() {
        UserApi.login(user)
                .then()
                .log().all()
                .body("success", equalTo(true))
                .body("accessToken", containsString("Bearer "))
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Test
    public void loginWithInvalidCredentialsTest() {
        UserData invalidUser = new UserData("wrongemail@.com", "password", "Luffy");

        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec401Unauthorized());
        UserApi.login(invalidUser)
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}