import api.OrderApi;
import api.Specifications;
import api.UserData;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserOrderTest extends BaseForTests{

    @Before
    public void setUp() {
        super.setUp();

        user = new UserData("luffy@gear5.com", "GomuGomuNo", "Luffy");
        registerUser(user);
        getIngredients();

        OrderApi.createOrder(accessToken, validIngredients)
                .then()
                .log().all();
    }

    @Test
    public void getOrdersWithAuth() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec200Ok());

        OrderApi.getUserOrders(accessToken)
                .then()
                .log().all()
                .body("success", equalTo(true))
                .body("orders", not(empty()))
                .body("orders[0].ingredients", not(empty()))
                .body("orders[0]._id", notNullValue())
                .body("orders[0].status", anyOf(equalTo("done"), equalTo("pending"), equalTo("created")))
                .body("orders[0].number", notNullValue())
                .body("orders[0].createdAt", notNullValue())
                .body("orders[0].updatedAt", notNullValue())
                .body("total", greaterThanOrEqualTo(1))
                .body("totalToday", greaterThanOrEqualTo(1));
    }

    @Test
    public void getOrderWithoutAuth() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec401Unauthorized());

        OrderApi.getUserOrders(null)
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}