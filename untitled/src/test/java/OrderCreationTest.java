import api.DataGenerator;
import api.OrderApi;
import api.Specifications;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrderCreationTest extends BaseForTests{

    @Before
    public void setUp() {
        super.setUp();

        user = DataGenerator.generateUser();
        registerUser(user);
        getIngredients();
    }

    @Test
    public void createOrderWithAuthAndValidIngredientsTest() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec200Ok());

        OrderApi.createOrder(accessToken, validIngredients)
                .then()
                .log().all()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithoutAuthWithValidIngredientsTest() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec200Ok());

        OrderApi.createOrder(null, validIngredients)
                .then()
                .log().all()
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrderWithAuthWithoutIngredientsTest() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec400BadReq());

        OrderApi.createOrder(accessToken, List.of())
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithoutAuthAndIngredientsTest() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec400BadReq());

        OrderApi.createOrder(null, List.of())
                .then()
                .log().all()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithInvalidIngredientsHashTest() {
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec500InternalServerError());

        OrderApi.createOrder(accessToken, invalidIngredients)
                .then()
                .log().all();
    } // ожидаемый результат: Status code: 500 Internal Server Error
      // фактический результат: Status code: 400 Bad Request
}