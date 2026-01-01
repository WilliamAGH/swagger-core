package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.resources.EnumParameterResource;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.io.IOException;

public class EnumTest {

    @Test(description = "Test enum")
    public void testEnum() throws IOException {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(EnumParameterResource.class);
        SerializationMatchers.assertEqualsToYaml(openAPI, EXPECTED_YAML);
    }


    static final String EXPECTED_YAML = """
            openapi: 3.0.1
            paths:
              /task:
                get:
                  operationId: getTasks
                  parameters:
                  - name: guid
                    in: query
                    required: true
                    schema:
                      type: string
                  - name: tasktype
                    in: query
                    schema:
                      type: string
                      enum:
                      - A
                      - B
                  responses:
                    "200":
                      content:
                        application/json:
                          schema:
                            type: array
                            items:
                              $ref: "#/components/schemas/TaskDTO"
                    "404":
                      description: User not found
            components:
              schemas:
                TaskDTO:
                  type: object
            """;
}