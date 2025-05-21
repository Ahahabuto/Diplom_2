import api.DataGenerator;
import api.Specifications;
import api.UserApi;
import api.UserData;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserProfileChangeTest extends BaseForTests {
    private String secondUserAccessToken;

    @Before
    public void setUp() {
        super.setUp();
        user = DataGenerator.generateUser();
        registerUser(user);
    }

    @Test
    public void updateUserWithAuthorizationTest() {
        UserData updatedData = new UserData("luffy@grandline.ru", "Gear5", "Monkey D. Luffy");

        UserApi.updateUser(accessToken, updatedData)
                .then()
                .log().all()
                .body("success", equalTo(true))
                .body("user.email", equalTo(updatedData.getEmail()))
                .body("user.name", equalTo(updatedData.getName()));
    }

    @Test
    public void updateUserEmailWithAuthorizationTest() {
        String newEmail = "mugiwara@grandline.ru";
        UserData updatedData = new UserData(newEmail, user.getPassword(), user.getName());

        UserApi.updateUser(accessToken, updatedData)
                .then()
                .log().all()
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail));
    }

    @Test
    public void updateUserPasswordWithAuthorizationTest() {
        String newPassword = "HitoHitoNoMiModelNika";
        UserData updatedData = new UserData(user.getEmail(), newPassword, user.getName());

        UserApi.updateUser(accessToken, updatedData)
                .then()
                .log().all()
                .body("success", equalTo(true));

        UserApi.login((new UserData(user.getEmail(), newPassword, user.getName())))
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void updateUserNameWithAuthorizationTest() {
        String newName = "Monkey D. Luffy is a king";
        UserData updatedData = new UserData(user.getEmail(), user.getPassword(), newName);

        UserApi.updateUser(accessToken, updatedData)
                .then()
                .log().all()
                .body("success", equalTo(true))
                .body("user.name", equalTo(newName));
    }

    @Test
    public void updateUserWithoutAuthTest() {
        UserData updatedData = new UserData("luffy@grandline.ru", "Gear5", "Monkey D. Luffy");

        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec401Unauthorized());
        UserApi.updateUser(null, updatedData)
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    public void updateUserWithExistingEmailTest() {
        UserData secondUser = new UserData("zoro@onepiece.com", "susui", "Zoro");
        Response registerResponse = UserApi.register(secondUser);
        secondUserAccessToken = registerResponse.path("accessToken");

        UserData updateData = new UserData("zoro@onepiece.com", user.getPassword(), user.getName());

        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec403Forbidden());
        UserApi.updateUser(accessToken, updateData)
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

    @After
    public void tearDown() {
        if (secondUserAccessToken != null) {
            try {
                Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec202Accepted());
                UserApi.delete(secondUserAccessToken);
            } catch (Exception e) {
                System.out.println("Failed to delete the second user: " + e.getMessage());
            }
        }

    super.tearDown();

    }
}