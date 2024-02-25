package org.game.HelperClassTests;
import java.util.List;
import java.util.ArrayList;
import org.game.DataBaseListString;
import org.game.httpserver.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataBaseListStringTest {
    @Test
    void CreateGetSet() {
        DataBaseListString test = new DataBaseListString();
        test.setHttp(HttpStatus.OK);
        List<String> stringList = new ArrayList<>();
        // Adding elements to the list
        stringList.add("Apple");
        stringList.add("Banana");
        stringList.add("Orange");
        stringList.add("Grapes");
        test.setValue(stringList);
        assertEquals(stringList, test.getValue());
        assertEquals(HttpStatus.OK, test.getHttp());
    }
}
