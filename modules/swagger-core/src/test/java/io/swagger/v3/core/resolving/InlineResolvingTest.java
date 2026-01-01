package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

public class InlineResolvingTest extends SwaggerTestBase{

    @Test
    public void testInlineResolving() {

        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(false).schemaResolution(io.swagger.v3.oas.models.media.Schema.SchemaResolution.INLINE);
        final ModelConverterContextImpl c = new ModelConverterContextImpl(modelResolver);
        // ModelConverters c = ModelConverters.getInstance(false, io.swagger.v3.oas.models.media.Schema.SchemaResolution.INLINE);
        c.resolve(new AnnotatedType(InlineSchemaFirst.class));

        String expectedYaml = """
                InlineSchemaFirst:
                  type: object
                  properties:
                    property1:
                      type: object
                      description: InlineSchemaFirst property 1
                      nullable: true
                      example: example
                    property2:
                      type: object
                      description: ' InlineSchemaFirst property 2'
                      example: example 2
                InlineSchemaPropertyFirst:
                  type: object
                  description: property
                  example: example
                """;

        SerializationMatchers.assertEqualsToYaml(c.getDefinedModels(), expectedYaml);
        // stringSchemaMap = c.readAll(InlineSchemaSecond.class);
        c.resolve(new AnnotatedType(InlineSchemaSecond.class));
        expectedYaml = """
                InlineSchemaFirst:
                  type: object
                  properties:
                    property1:
                      type: object
                      description: InlineSchemaFirst property 1
                      nullable: true
                      example: example
                    property2:
                      type: object
                      description: ' InlineSchemaFirst property 2'
                      example: example 2
                InlineSchemaPropertyFirst:
                  type: object
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
                          description: property 1
                        property2:
                          type: object
                          description: property 2
                          example: example
                  description: propertysecond
                  nullable: true
                  example: examplesecond
                InlineSchemaPropertySimple:
                  type: object
                  description: property
                  example: example
                InlineSchemaSecond:
                  type: object
                  properties:
                    propertySecond1:
                      type: object
                      properties:
                        bar:
                          type: object
                          properties:
                            property1:
                              type: object
                              description: property 1
                            property2:
                              type: object
                              description: property 2
                              example: example
                      description: InlineSchemaSecond property 1
                      nullable: true
                      example: examplesecond
                    property2:
                      type: object
                      description: InlineSchemaSecond property 2
                      example: InlineSchemaSecond example 2
                InlineSchemaSimple:
                  type: object
                  properties:
                    property1:
                      type: object
                      description: property 1
                    property2:
                      type: object
                      description: property 2
                      example: example
                """;
        SerializationMatchers.assertEqualsToYaml(c.getDefinedModels(), expectedYaml);
    }

    static class InlineSchemaFirst {

        // public String foo;

        @Schema(description = "InlineSchemaFirst property 1", nullable = true)
        public InlineSchemaPropertyFirst property1;


        private InlineSchemaPropertyFirst property2;

        @Schema(description = " InlineSchemaFirst property 2", example = "example 2")
        public InlineSchemaPropertyFirst getProperty2() {
            return null;
        }
    }

    static class InlineSchemaSecond {

        // public String foo;

        @Schema(description = "InlineSchemaSecond property 1", nullable = true)
        public InlineSchemaPropertySecond propertySecond1;


        private InlineSchemaPropertyFirst property2;

        @Schema(description = "InlineSchemaSecond property 2", example = "InlineSchemaSecond example 2")
        public InlineSchemaPropertyFirst getProperty2() {
            return null;
        }
    }

    @Schema(description = "property", example = "example")
    static class InlineSchemaPropertyFirst {
        // public String bar;
    }

    @Schema(description = "propertysecond", example = "examplesecond")
    static class InlineSchemaPropertySecond {
        public InlineSchemaSimple bar;
    }

    static class InlineSchemaSimple {

        @Schema(description = "property 1")
        public InlineSchemaPropertySimple property1;


        private InlineSchemaPropertySimple property2;

        @Schema(description = "property 2", example = "example")
        public InlineSchemaPropertySimple getProperty2() {
            return null;
        }
    }

    @Schema(description = "property")
    static class InlineSchemaPropertySimple {
        // public String bar;
    }
}
