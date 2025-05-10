import api.OrderApi;
import api.Specifications;
import api.UserData;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrderCreationTest extends BaseForTests{

    @Before
    public void setUp() {
        super.setUp();

        user = new UserData("kingofthe@pirates.com", "GomuGomuNo", "Nika");
        registerUser(user);
        getIngredients();
    }

    @Test
    public void createOrderWithAuthAndValidIngredients() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec200Ok());

        OrderApi.createOrder(accessToken, validIngredients)
                .then()
                .log().all()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithoutAuthWithValidIngredients() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec200Ok());

        OrderApi.createOrder(null, validIngredients)
                .then()
                .log().all()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithAuthWithoutIngredients() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec400BadReq());

        OrderApi.createOrder(accessToken, List.of())
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithoutAuthAndIngredients() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec400BadReq());

        OrderApi.createOrder(null, List.of())
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithInvalidIngredientsHash() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec500InternalServerError());

        OrderApi.createOrder(accessToken, invalidIngredients)
                .then()
                .log().all();
    } // ожидаемый результат: Status code: 500 Internal Server Error
      // фактический результат: Status code: 400 Bad Request
}