import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetUserTest {
    Response response;

    @BeforeSuite()
    public void setupTests(){
        RestAssured.baseURI = "https://reqres.in";

        response = given ()
                        .contentType(ContentType.JSON)
                        .when()
                        .get("/api/users/1")
                        .then()
                        .extract().response();
    }

    @Test
    public void HTTPStatusCode200() {
        assertThat("HTTP Status is 200", response.statusCode(), equalTo(HTTP_OK));
    }

    @Test
    public void returnedData() {
        // Verify Payload
        assertThat("Verifying ID is correct", response.jsonPath().getString("data.id"), equalTo("1"));
        assertThat("Verifying email is correct", response.jsonPath().getString("data.email"), equalTo("george.bluth@reqres.in"));
        assertThat("Verifying first_name is correct", response.jsonPath().getString("data.first_name"), equalTo("George"));
        assertThat("Verifying last_name is correct", response.jsonPath().getString("data.last_name"), equalTo("Bluth"));
        assertThat("Verifying avatar is correct", response.jsonPath().getString("data.avatar"), equalTo("https://reqres.in/img/faces/1-image.jpg"));
    }

    @Test
    public void getUserHappyPathAssertingInThen() {
        RestAssured.
                when()
                    .get("/api/users/1")
                .then()
                    .assertThat()
                        .statusCode(200)
                        .contentType("application/json; charset=utf-8")
                        .header("Server", "cloudflare")
                        .body("data.email", equalTo("george.bluth@reqres.in")) // data.email is using Groovy GPath
                        .body("size()", CoreMatchers.is(2)); // Number of items in the object
    }

}
