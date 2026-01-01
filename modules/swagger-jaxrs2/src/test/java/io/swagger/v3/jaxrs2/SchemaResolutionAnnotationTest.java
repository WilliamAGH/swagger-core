package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.schemaResolution.SchemaResolutionAnnotatedResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

public class SchemaResolutionAnnotationTest {

    @Test
    public void testSchemaResolutionAnnotation() {
        ModelConverters.reset();
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()));
        OpenAPI openAPI = reader.read(SchemaResolutionAnnotatedResource.class);
        String yaml = """
                openapi: 3.0.1
                paths:
                  /test/inlineSchemaFirst:
                    get:
                      operationId: inlineSchemaFirst
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/InlineSchemaFirst"
                  /test/inlineSchemaSecond:
                    get:
                      operationId: inlineSchemaSecond
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              type: object
                              properties:
                                foo:
                                  type: string
                                propertySecond1:
                                  $ref: "#/components/schemas/InlineSchemaPropertySecond"
                                property2:
                                  $ref: "#/components/schemas/InlineSchemaPropertyFirst"
                              description: InlineSchemaSecond API
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/InlineSchemaSecond"
                components:
                  schemas:
                    InlineSchemaFirst:
                      type: object
                      properties:
                        property1:
                          description: InlineSchemaFirst property 1
                          nullable: true
                          allOf:
                          - $ref: "#/components/schemas/InlineSchemaPropertyFirst"
                        property2:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: property
                          example: example
                    InlineSchemaPropertyFirst:
                      type: object
                      properties:
                        bar:
                          type: string
                      description: property
                      example: example
                    InlineSchemaPropertySecond:
                      type: object
                      properties:
                        bar:
                          $ref: "#/components/schemas/InlineSchemaSimple"
                      description: propertysecond
                      nullable: true
                      example: examplesecond
                    InlineSchemaPropertySimple:
                      type: object
                      properties:
                        bar:
                          type: string
                      description: property
                    InlineSchemaSecond:
                      type: object
                      properties:
                        foo:
                          type: string
                        propertySecond1:
                          $ref: "#/components/schemas/InlineSchemaPropertySecond"
                        property2:
                          $ref: "#/components/schemas/InlineSchemaPropertyFirst"
                      description: InlineSchemaSecond API
                    InlineSchemaSimple:
                      type: object
                      properties:
                        property1:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: property
                        property2:
                          description: property 2
                          example: example
                          allOf:
                          - $ref: "#/components/schemas/InlineSchemaPropertySimple"
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }
}
