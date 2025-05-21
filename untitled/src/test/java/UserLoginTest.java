import api.DataGenerator;
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
        user = DataGenerator.generateUser();
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
    public void loginWithInvalidEmailTest() {
        UserData invalidUser = new UserData(DataGenerator.generateEmail(), user.getPassword(), user.getName());

        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec401Unauthorized());
        UserApi.login(invalidUser)
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void loginWithInvalidPasswordTest() {
        UserData invalidUser = new UserData(user.getEmail(), DataGenerator.generatePassword(), user.getName());
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec401Unauthorized());
        UserApi.login(invalidUser)
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}