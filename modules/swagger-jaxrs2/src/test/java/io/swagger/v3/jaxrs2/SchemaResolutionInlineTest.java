package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.schemaResolution.SchemaResolutionResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class SchemaResolutionInlineTest {
    @Test
    public void testSchemaResolutionInline() {
        ModelConverters.reset();
        Reader reader = new Reader(new SwaggerConfiguration().openAPI(new OpenAPI()).schemaResolution(Schema.SchemaResolution.INLINE));
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
                                type: object
                                properties:
                                  property1:
                                    type: object
                                    properties:
                                      bar:
                                        type: string
                                    description: InlineSchemaFirst property 1
                                    nullable: true
                                    example: example
                                  property2:
                                    type: object
                                    properties:
                                      bar:
                                        type: string
                                    description: ' InlineSchemaFirst property 2'
                                    example: example 2
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
                                  type: object
                                  properties:
                                    bar:
                                      type: object
                                      properties:
                                        property1:
                                          type: object
                                          properties:
                                            bar:
                                              type: string
                                          description: property 1
                                        property2:
                                          type: object
                                          properties:
                                            bar:
                                              type: string
                                          description: property 2
                                          example: example
                                  description: InlineSchemaSecond property 1
                                  nullable: true
                                  example: examplesecond
                                property2:
                                  type: object
                                  properties:
                                    bar:
                                      type: string
                                  description: InlineSchemaSecond property 2
                                  example: InlineSchemaSecond example 2
                              description: InlineSchemaSecond API
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                type: object
                                properties:
                                  foo:
                                    type: string
                                  propertySecond1:
                                    type: object
                                    properties:
                                      bar:
                                        type: object
                                        properties:
                                          property1:
                                            type: object
                                            properties:
                                              bar:
                                                type: string
                                            description: property 1
                                          property2:
                                            type: object
                                            properties:
                                              bar:
                                                type: string
                                            description: property 2
                                            example: example
                                    description: InlineSchemaSecond property 1
                                    nullable: true
                                    example: examplesecond
                                  property2:
                                    type: object
                                    properties:
                                      bar:
                                        type: string
                                    description: InlineSchemaSecond property 2
                                    example: InlineSchemaSecond example 2
                components:
                  schemas:
                    InlineSchemaFirst:
                      type: object
                      properties:
                        property1:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: InlineSchemaFirst property 1
                          nullable: true
                          example: example
                        property2:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: ' InlineSchemaFirst property 2'
                          example: example 2
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
                          type: object
                          properties:
                            property1:
                              type: object
                              properties:
                                bar:
                                  type: string
                              description: property 1
                            property2:
                              type: object
                              properties:
                                bar:
                                  type: string
                              description: property 2
                              example: example
                      description: propertysecond
                      nullable: true
                      example: examplesecond
                    InlineSchemaPropertySimple:
                      type: object
                      properties:
                        bar:
                          type: string
                      description: property
                      example: example
                    InlineSchemaSecond:
                      type: object
                      properties:
                        foo:
                          type: string
                        propertySecond1:
                          type: object
                          properties:
                            bar:
                              type: object
                              properties:
                                property1:
                                  type: object
                                  properties:
                                    bar:
                                      type: string
                                  description: property 1
                                property2:
                                  type: object
                                  properties:
                                    bar:
                                      type: string
                                  description: property 2
                                  example: example
                          description: InlineSchemaSecond property 1
                          nullable: true
                          example: examplesecond
                        property2:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: InlineSchemaSecond property 2
                          example: InlineSchemaSecond example 2
                      description: InlineSchemaSecond API
                    InlineSchemaSimple:
                      type: object
                      properties:
                        property1:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: property 1
                        property2:
                          type: object
                          properties:
                            bar:
                              type: string
                          description: property 2
                          example: example
                
                """;
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        ModelConverters.reset();
    }
}
