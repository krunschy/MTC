package org.game.HelperClassTests;

import java.util.List;
import org.game.services.JsonUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonUtilsTest {
    //teste das zerlegen eines objects
    @Test
    void getfromJSON() {
        String json = "{\"Username\":\"altenhof\", \"Password\":\"markus\"}";
        String user = JsonUtils.getValueFromJSON(json, "Username");
        String password = JsonUtils.getValueFromJSON(json, "Password");
        assertEquals("altenhof",user);
        assertEquals("markus",password);
    }
    //teste das zerlegen eines object arrays
    @Test
    void ArrayToJson() {
        String jsonarray = "[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}, {\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50.0}, {\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Name\":\"WaterSpell\", \"Damage\": 20.0}, {\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Name\":\"Ork\", \"Damage\": 45.0}, {\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"Name\":\"FireSpell\",    \"Damage\": 25.0}]";
        List<String> objects = JsonUtils.getIndividualJsonObjects(jsonarray);
        assertEquals("[{\"Id\":\"845f0dc7-37d0-426e-994e-43fc3ac83c08\", \"Name\":\"WaterGoblin\", \"Damage\": 10.0}",objects.get(0));
        assertEquals(" {\"Id\":\"99f8f8dc-e25e-4a95-aa2c-782823f36e2a\", \"Name\":\"Dragon\", \"Damage\": 50.0}",objects.get(1));
        assertEquals(" {\"Id\":\"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Name\":\"WaterSpell\", \"Damage\": 20.0}",objects.get(2));
        assertEquals(" {\"Id\":\"1cb6ab86-bdb2-47e5-b6e4-68c5ab389334\", \"Name\":\"Ork\", \"Damage\": 45.0}",objects.get(3));
        assertEquals(" {\"Id\":\"dfdd758f-649c-40f9-ba3a-8657f4b3439f\", \"Name\":\"FireSpell\",    \"Damage\": 25.0}]",objects.get(4));
    }
}
