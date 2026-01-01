package io.swagger.v3.core.deserialization.properties;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.testng.annotations.Test;

import static io.swagger.v3.core.util.TestUtils.normalizeLineEnds;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class MapPropertyDeserializerTest {
    private static final String json = """
            {
              "tags": [
                "store"
              ],
              "summary": "Returns pet inventories by status",
              "description": "Returns a map of status codes to quantities",
              "operationId": "getInventory",
              "produces": [
                "application/json"
              ],
              "parameters": [],
              "responses": {
                "200": {
                  "description": "successful operation",
                  "content": {
                    "*/*": {
                      "schema": {
                        "type": "object",
                        "x-foo": "vendor x",
                        "additionalProperties": {
                          "type": "integer",
                          "format": "int32"
                        }
                      }
                    }
                  }
                }
              },
              "security": [
                {
                  "api_key": []
                }
              ]
            }\
            """;

    private static final String jsonAdditionalPropertiesBoolean = """
            {
              "tags": [
                "store"
              ],
              "summary": "Returns pet inventories by status",
              "description": "Returns a map of status codes to quantities",
              "operationId": "getInventory",
              "produces": [
                "application/json"
              ],
              "parameters": [],
              "responses": {
                "200": {
                  "description": "successful operation",
                  "content": {
                    "*/*": {
                      "schema": {
                        "type": "object",
                        "x-foo": "vendor x",
                        "additionalProperties": false
                      }
                    }
                  }
                }
              },
              "security": [
                {
                  "api_key": []
                }
              ]
            }\
            """;

    private static final String jsonAdditionalPropertiesBooleanTrue = """
            {
              "tags": [
                "store"
              ],
              "summary": "Returns pet inventories by status",
              "description": "Returns a map of status codes to quantities",
              "operationId": "getInventory",
              "produces": [
                "application/json"
              ],
              "parameters": [],
              "responses": {
                "200": {
                  "description": "successful operation",
                  "content": {
                    "*/*": {
                      "schema": {
                        "type": "object",
                        "x-foo": "vendor x",
                        "additionalProperties": true
                      }
                    }
                  }
                }
              },
              "security": [
                {
                  "api_key": []
                }
              ]
            }\
            """;

    @Test(description = "it should deserialize a response per #1349")
    public void testMapDeserialization() throws Exception {

        Operation operation = Json.mapper().readValue(json, Operation.class);
        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);

        Schema responseSchema = response.getContent().get("*/*").getSchema();
        assertNotNull(responseSchema);
        assertTrue(responseSchema instanceof MapSchema);

        MapSchema mp = (MapSchema) responseSchema;
        assertTrue(mp.getAdditionalProperties() instanceof IntegerSchema);
    }

    @Test(description = "it should deserialize a boolean additionalProperties")
    public void testBooleanAdditionalPropertiesDeserialization() throws Exception {

        Operation operation = Json.mapper().readValue(jsonAdditionalPropertiesBoolean, Operation.class);
        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);

        Schema responseSchema = response.getContent().get("*/*").getSchema();
        assertNotNull(responseSchema);
        assertTrue(responseSchema instanceof ObjectSchema);

        assertTrue(responseSchema.getAdditionalProperties() instanceof Boolean);
        assertFalse((Boolean)responseSchema.getAdditionalProperties());

        operation = Json.mapper().readValue(jsonAdditionalPropertiesBooleanTrue, Operation.class);
        response = operation.getResponses().get("200");
        assertNotNull(response);

        responseSchema = response.getContent().get("*/*").getSchema();
        assertNotNull(responseSchema);
        assertTrue(responseSchema instanceof MapSchema);

        assertTrue(responseSchema.getAdditionalProperties() instanceof Boolean);
        assertTrue((Boolean)responseSchema.getAdditionalProperties());
    }

    @Test(description = "it should serialize a boolean additionalProperties")
    public void testBooleanAdditionalPropertiesSerialization() throws Exception {

        Operation operation = Json.mapper().readValue(json, Operation.class);
        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);

        Schema responseSchema = response.getContent().get("*/*").getSchema();

        Schema schema = new ObjectSchema().additionalProperties(true);
        assertEquals(normalizeLineEnds(Json.pretty(schema)), """
                {
                  "type" : "object",
                  "additionalProperties" : true
                }\
                """);

        schema = new ObjectSchema().additionalProperties(responseSchema);
        assertEquals(normalizeLineEnds(Json.pretty(schema)), """
                {
                  "type" : "object",
                  "additionalProperties" : {
                    "type" : "object",
                    "additionalProperties" : {
                      "type" : "integer",
                      "format" : "int32"
                    },
                    "x-foo" : "vendor x"
                  }
                }\
                """);
    }

    @Test(description = "vendor extensions should be included with object type")
    public void testMapDeserializationVendorExtensions() throws Exception {
        Operation operation = Json.mapper().readValue(json, Operation.class);
        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);

        Schema responseSchema = response.getContent().get("*/*").getSchema();
        assertNotNull(responseSchema);

        MapSchema mp = (MapSchema) responseSchema;
        assertTrue(mp.getExtensions().size() > 0);
        assertNotNull(mp.getExtensions().get("x-foo"));
        assertEquals(mp.getExtensions().get("x-foo"), "vendor x");
    }

    @Test(description = "it should read an example within an inlined schema")
    public void testIssue1261InlineSchemaExample() throws Exception {
        Operation operation = Yaml.mapper().readValue(
                """
                      responses:
                        "200":
                          content:
                            '*/*':
                              description: OK
                              schema:
                                type: object
                                properties:
                                  id:
                                    type: integer
                                    format: int32
                                  name:
                                    type: string
                                required: [id, name]
                                example: ok""", Operation.class);

        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);
        Schema schema = response.getContent().get("*/*").getSchema();
        Object example = schema.getExample();
        assertNotNull(example);
        assertTrue(example instanceof String);
        assertEquals(example, "ok");
    }
}