import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

public class GetUsersTest {
    Response response;

    @BeforeSuite()
    public void setupTests(){
        RestAssured.baseURI = "https://reqres.in";

        response = given ()
                        .contentType(ContentType.JSON)
                        .queryParam("page", "1")
                        .queryParam("per_page", "2")
                        .when()
                        .get("/api/users")
                        .then()
                        .extract().response();
    }

    @Test
    public void HTTPStatusCode200() {
        assertThat("HTTP Status is 200", response.statusCode(), equalTo(HTTP_OK));
    }

    @Test
    public void responseHeaders() {
        assertThat("Response Header Content-Type", response.getHeader("Content-Type"), equalTo("application/json; charset=utf-8"));
    }

    @Test
    public void pagingMetaData() {
        // Verify Payload
        assertThat("Verifying page", response.jsonPath().getInt("page"), equalTo(1));
        assertThat("Verifying per_page", response.jsonPath().getInt("per_page"), equalTo(2));
        assertThat("Verifying total", response.jsonPath().getInt("total"), equalTo(12));
        assertThat("Verifying total_pages", response.jsonPath().getInt("total_pages"), equalTo(6));
    }

    @Test
    public void returnedData() {
        // Use the jsonPath to populate a List, then use the List methods to assert
        final List<String> data = response.jsonPath().get("data.email");
        assertThat("Only two users should be returned", data.size(), equalTo(2));

        assertThat("has emails", data, hasItems("george.bluth@reqres.in", "janet.weaver@reqres.in"));
        assertThat("Verifying email, first entry", response.jsonPath().getString("data[0].email"), equalTo("george.bluth@reqres.in"));
        assertThat("Verifying first_name, second entry", response.jsonPath().getString("data[1].first_name"), equalTo("Janet"));
    }
}
