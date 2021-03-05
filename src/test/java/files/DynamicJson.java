package files;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJson {
    @Test
    public void addBook() {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given().log().all().header("Content-Type", "application/json")
                .body(Payload.AddBook("fdfdf", "fdfd"))
                .when().post("Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js = ReUsableMethod.rawToJson(response);
        String id = js.get("ID");
        System.out.println(id);
    }

    @Test(dataProvider = "BooksData")
    public void addBookNew(String isbn, String aisle) {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given().log().all().header("Content-Type", "application/json")
                .body(Payload.AddBook(isbn, aisle))
                .when().post("Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js = ReUsableMethod.rawToJson(response);
        String id = js.get("ID");
        System.out.println(id);
    }

    @DataProvider(name="BooksData")
    public Object[][] getData() {
        // array = collect of elemnets
        // multi dimension array = collect of arrays
        return new Object[][] {{"ojoij", "94467"}, {"ooo", "8098"}, {"fdfd", "3838"}};
    }
}
