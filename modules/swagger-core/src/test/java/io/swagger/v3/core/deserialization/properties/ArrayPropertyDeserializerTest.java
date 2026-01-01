package io.swagger.v3.core.deserialization.properties;

import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ArrayPropertyDeserializerTest {
    private static final String yaml =
            """
                  operationId: something
                  responses:
                    "200":
                      content:
                        '*/*':
                          examples:
                            simple:
                              value: Array example
                            more:
                              value: with two items
                          description: OK
                          schema:
                            type: array
                            minItems: 3
                            maxItems: 100
                            items:
                              type: string
            """;

    @Test(description = "it should includes the example in the arrayproperty")
    public void testArrayDeserialization() throws Exception {

        Operation operation = Yaml.mapper().readValue(yaml, Operation.class);
        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);

        MediaType media = response.getContent().get("*/*");
        Schema responseSchema = media.getSchema();
        assertTrue(media.getExamples().size() == 2);
        assertNotNull(responseSchema);
        assertTrue(responseSchema instanceof ArraySchema);

        ArraySchema mp = (ArraySchema) responseSchema;
        assertEquals(mp.getMinItems(), Integer.valueOf(3));
        assertEquals(mp.getMaxItems(), Integer.valueOf(100));
    }
}