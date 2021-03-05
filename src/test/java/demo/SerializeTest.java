package demo;

import io.restassured.RestAssured;
import org.testng.annotations.Test;
import pojo.AddPlace;
import pojo.Location;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class SerializeTest {
    @Test
    public void addMapSerialize() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        AddPlace p = new AddPlace();
        p.setAccuracy(50);
        p.setName("Rahul Shetty Academy");
        p.setPhone_number("(+91) 983 893 3937");
        p.setAddress("29, side layout, cohen 09");
        p.setTypes(Arrays.asList("shoe park", "shop"));
        p.setWebsite("http://rahulshettyacademy.com");
        p.setLanguage("French-IN");
        p.setLocation(new Location(-38.383494, 33.427362));

        String reponse = given().queryParam("key", "qaclick123").log().all()
                .body(p)
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).extract().response().asString();
        System.out.println(reponse);

    }
}
