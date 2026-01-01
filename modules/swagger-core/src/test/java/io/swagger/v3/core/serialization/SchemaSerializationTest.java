package io.swagger.v3.core.serialization;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.JsonSchema;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

public class SchemaSerializationTest {

    @Test
    public void serializeRefSchema3_1() {
        OpenAPI openAPI = new OpenAPI()
                .components(new Components()
                        .addSchemas("Pet", new JsonSchema()
                                .types(new HashSet<>(Arrays.asList("object")))
                                .format("whatever")
                                ._if(new JsonSchema().type("string").types(new HashSet<>(Arrays.asList("string"))))
                                .then(new JsonSchema().type("string").types(new HashSet<>(Arrays.asList("string"))))
                                ._else(new JsonSchema().type("string").types(new HashSet<>(Arrays.asList("string"))))
                                .minimum(BigDecimal.valueOf(1))
                                .addProperties("id", new JsonSchema().type("integer").types(new HashSet<>(Arrays.asList("integer"))))
                                .addProperties("name", new JsonSchema().type("string").types(new HashSet<>(Arrays.asList("string"))))
                                .addProperties("tag", new JsonSchema().type("string").types(new HashSet<>(Arrays.asList("string")))))
                        .addSchemas("AnotherPet", new JsonSchema()
                                .title("Another Pet")
                                .description("Another Pet for petstore referencing Pet schema")
                                .$ref("#/components/schemas/Pet")
                                .addProperties("category", new JsonSchema().types(new HashSet<>(Arrays.asList("string"))))
                                .addProperties("photoUrl", new JsonSchema().types(new HashSet<>(Arrays.asList("string"))))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.0.1
                components:
                  schemas:
                    Pet:
                      type: object
                      format: whatever
                      if:
                        type: string
                      then:
                        type: string
                      else:
                        type: string
                      minimum: 1
                      properties:
                        id:
                          type: integer
                        name:
                          type: string
                        tag:
                          type: string
                    AnotherPet:
                      $ref: "#/components/schemas/Pet"
                      description: Another Pet for petstore referencing Pet schema
                      properties:
                        category:
                          type: string
                        photoUrl:
                          type: string
                      title: Another Pet
                """);
        SerializationMatchers.assertEqualsToYaml(openAPI, """
                openapi: 3.0.1
                components:
                  schemas:
                    Pet:
                      minimum: 1
                      properties:
                        id:
                          type: integer
                        name:
                          type: string
                        tag:
                          type: string
                      format: whatever
                    AnotherPet:
                      $ref: "#/components/schemas/Pet"
                """);
    }
}
