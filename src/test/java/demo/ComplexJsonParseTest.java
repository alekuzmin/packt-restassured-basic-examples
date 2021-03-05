package demo;

import files.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

public class ComplexJsonParseTest {
    @Test
    public void checkComplexJson() {
        // dummy response
        JsonPath js = new JsonPath(Payload.CoursePrice());

        // 1. Print no of courses returned by API
        int count = js.getInt("courses.size()");
        System.out.println(count);

        // 2. Print Purchase Amount
        int totalAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println(totalAmount);

        // 3. Title of first course
        String firstTitle = js.get("courses[0].title"); // get default is string
        System.out.println(firstTitle);

        // 4. Print all the course titles and their respective Prices
        for(int i = 0; i < count; i++) {
            String name = js.get("courses[" + i + "].title");
            System.out.println(js.getString("courses[" + i + "].price"));
            System.out.println(name);
        }
        // 5. Print no of copies sold by RPA Course
        for(int i = 0; i < count; i++) {
            String courseTitle = js.get("courses[" + i + "].title");
            if (courseTitle.equals("RPA")) {
                int copies = js.get("courses["+i+"].copies");
                System.out.println(copies);
            }
        }
    }
}
