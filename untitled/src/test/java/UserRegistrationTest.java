import api.DataGenerator;
import api.Specifications;
import api.UserApi;
import api.UserData;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserRegistrationTest extends BaseForTests {

    @Before
    public void setUp() {
        super.setUp();
        user = DataGenerator.generateUser();
    }

    @Test
    public void successfulUserRegistrationTest() {
        accessToken = UserApi.register(user)
                    .then()
                    .log().all()
                    .body("success", equalTo(true))
                    .body("user.email", equalTo(user.getEmail()))
                    .body("user.name", equalTo(user.getName()))
                    .body("accessToken", notNullValue())
                    .body("refreshToken", notNullValue())
                    .extract().path("accessToken");
    }

    @Test
    public void duplicateUserRegistrationTest() {
        accessToken = UserApi.register(user)
                .then()
                .log().all()
                .extract().path("accessToken");

        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec403Forbidden());
        UserApi.register(user)
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void registrationWithoutEmailTest() {
        UserData invalid = new UserData("", DataGenerator.generatePassword(), DataGenerator.generateName());

        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec403Forbidden());
        UserApi.register(invalid)
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void registrationWithoutPasswordTest() {
        UserData invalid = new UserData(DataGenerator.generateEmail(), "", DataGenerator.generateName());

        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec403Forbidden());
        UserApi.register(invalid)
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    public void registrationWithoutNameTest() {
        UserData invalid = new UserData(DataGenerator.generateEmail(), DataGenerator.generatePassword(), "");

        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec403Forbidden());
        UserApi.register(invalid)
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}