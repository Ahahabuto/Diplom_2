import api.OrderApi;
import api.Specifications;
import api.UserApi;
import api.UserData;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;

import java.util.List;

public class BaseForTests {
    protected UserData user;
    protected String accessToken;
    protected String refreshToken;
    protected List<String> validIngredients;
    protected List<String> invalidIngredients;

    @Before
    public void setUp() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec200Ok());
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec202Accepted());
            UserApi.delete(accessToken)
                    .then()
                    .log().all();
        }
    }

    protected void registerUser(UserData userData) {
        Response response = UserApi.register(userData)
                .then()
                .extract().response();
        this.user = userData;
        this.accessToken = response.path("accessToken");
        this.refreshToken = response.path("refreshToken");
    }

    protected void getIngredients() {
        Response ingredientsResponse = OrderApi.getIngredients();
        validIngredients = ingredientsResponse.path("data._id");
        invalidIngredients = List.of("invalidHash1", "invalidHash2");
    }
}