package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.schemaResolution.SchemaResolutionResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class SchemaResolutionAllOfRefTest {

    @Test
    public void testSchemaResolutionAllOfRef() {
        ModelConverters.reset();
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.ALL_OF_REF));
        OpenAPI openAPI = reader.read(SchemaResolutionResource.class);
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
                              description: InlineSchemaSecond API
                              allOf:
                              - $ref: "#/components/schemas/InlineSchemaSecond"
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
                          description: ' InlineSchemaFirst property 2'
                          example: example 2
                          allOf:
                          - $ref: "#/components/schemas/InlineSchemaPropertyFirst"
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
                          description: InlineSchemaSecond property 1
                          nullable: true
                          allOf:
                          - $ref: "#/components/schemas/InlineSchemaPropertySecond"
                        property2:
                          description: InlineSchemaSecond property 2
                          example: InlineSchemaSecond example 2
                          allOf:
                          - $ref: "#/components/schemas/InlineSchemaPropertyFirst"
                    InlineSchemaSimple:
                      type: object
                      properties:
                        property1:
                          description: property 1
                          allOf:
                          - $ref: "#/components/schemas/InlineSchemaPropertySimple"
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
