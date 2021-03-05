package demo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;
import pojo.AddPlace;
import pojo.Location;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class SpecBuilderTest {
    @Test
    public void addMapSerialize() {
        // Build object
        AddPlace p = new AddPlace();
        p.setAccuracy(50);
        p.setName("Rahul Shetty Academy");
        p.setPhone_number("(+91) 983 893 3937");
        p.setAddress("29, side layout, cohen 09");
        p.setTypes(Arrays.asList("shoe park", "shop"));
        p.setWebsite("http://rahulshettyacademy.com");
        p.setLanguage("French-IN");
        p.setLocation(new Location(-38.383494, 33.427362));

        // Create Spec Builder
        RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key", "qaclick123")
                .setContentType(ContentType.JSON).build();
        ResponseSpecification resSpec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

        // Send the request with Spec builder
        RequestSpecification request = given().spec(req)
                .body(p);
        Response response = request.when().post("maps/api/place/add/json")
        .then().spec(resSpec).extract().response();

        String responseString = response.asString();
        System.out.println(responseString);
    }
}
