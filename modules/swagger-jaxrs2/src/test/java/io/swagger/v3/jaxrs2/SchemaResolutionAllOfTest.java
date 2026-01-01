package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.schemaResolution.SchemaResolutionResourceSimple;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class SchemaResolutionAllOfTest {

    @Test
    public void testSchemaResolutionAllOf() {
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.ALL_OF));
        OpenAPI openAPI = reader.read(SchemaResolutionResourceSimple.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/inlineSchemaFirst:
                    get:
                      operationId: inlineSchemaFirst
                      responses:
                        default:
                          description: InlineSchemaFirst Response API
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/InlineSchemaFirst"
                  /test/inlineSchemaSecond:
                    get:
                      operationId: inlineSchemaFirst_1
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              allOf:
                              - description: InlineSchemaSecond API
                              - $ref: "#/components/schemas/InlineSchemaFirst"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                components:
                  schemas:
                    InlineSchemaFirst:
                      type: object
                      properties:
                        property1:
                          $ref: "#/components/schemas/InlineSchemaPropertyFirst"
                    InlineSchemaPropertyFirst:
                      type: object
                      properties:
                        bar:
                          type: string
                      description: property
                      nullable: true
                      example: example
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }
}
