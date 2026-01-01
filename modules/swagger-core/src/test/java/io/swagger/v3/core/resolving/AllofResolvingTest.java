package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

public class AllofResolvingTest extends SwaggerTestBase {

    @Test
    public void testAllofResolving() {

        final ModelResolver modelResolver = new ModelResolver(mapper()).openapi31(false).schemaResolution(io.swagger.v3.oas.models.media.Schema.SchemaResolution.ALL_OF);
        final ModelConverterContextImpl c = new ModelConverterContextImpl(modelResolver);
        // ModelConverters c = ModelConverters.getInstance(false, io.swagger.v3.oas.models.media.Schema.SchemaResolution.INLINE);
        c.resolve(new AnnotatedType(UserSchema.class));

        String expectedYaml = """
                UserProperty:
                  type: object
                  description: Represents a user-specific property
                  example: User-specific example value
                UserSchema:
                  type: object
                  properties:
                    propertyOne:
                      allOf:
                      - type: object
                        description: First user schema property
                        nullable: true
                      - $ref: "#/components/schemas/UserProperty"
                    propertyTwo:
                      allOf:
                      - type: object
                        description: Second user schema property
                        example: example value for propertyTwo
                      - $ref: "#/components/schemas/UserProperty"
                    propertyThree:
                      allOf:
                      - type: object
                        description: "Third user schema property, with example for testing"
                        example: example value for propertyThree
                      - $ref: "#/components/schemas/UserProperty"
                """;

        SerializationMatchers.assertEqualsToYaml(c.getDefinedModels(), expectedYaml);
        // stringSchemaMap = c.readAll(InlineSchemaSecond.class);
        c.resolve(new AnnotatedType(OrderSchema.class));
        expectedYaml = """
                BasicProperty:
                  type: object
                  description: Represents a basic schema property
                OrderProperty:
                  type: object
                  properties:
                    basicProperty:
                      $ref: "#/components/schemas/BasicProperty"
                  description: Represents an order-specific property
                  example: Order-specific example value
                OrderSchema:
                  type: object
                  properties:
                    propertyOne:
                      allOf:
                      - type: object
                        description: First order schema property
                        nullable: true
                      - $ref: "#/components/schemas/OrderProperty"
                    userProperty:
                      allOf:
                      - type: object
                        description: "Order schema property, references UserProperty"
                        example: example value for userProperty
                      - $ref: "#/components/schemas/UserProperty"
                UserProperty:
                  type: object
                  description: Represents a user-specific property
                  example: User-specific example value
                UserSchema:
                  type: object
                  properties:
                    propertyOne:
                      allOf:
                      - type: object
                        description: First user schema property
                        nullable: true
                      - $ref: "#/components/schemas/UserProperty"
                    propertyTwo:
                      allOf:
                      - type: object
                        description: Second user schema property
                        example: example value for propertyTwo
                      - $ref: "#/components/schemas/UserProperty"
                    propertyThree:
                      allOf:
                      - type: object
                        description: "Third user schema property, with example for testing"
                        example: example value for propertyThree
                      - $ref: "#/components/schemas/UserProperty"
                """;
        SerializationMatchers.assertEqualsToYaml(c.getDefinedModels(), expectedYaml);
    }

    // Renamed class to better describe what it represents
    static class UserSchema {

        @Schema(description = "First user schema property", nullable = true)
        public UserProperty propertyOne;

        private UserProperty propertyTwo;

        @Schema(description = "Second user schema property", example = "example value for propertyTwo")
        public UserProperty getPropertyTwo() {
            return propertyTwo;
        }

        // Third property with no specific annotation. It's good to add some description or example for clarity
        @Schema(description = "Third user schema property, with example for testing", example = "example value for propertyThree")
        public UserProperty getPropertyThree() {
            return null;  // returning null as per the test scenario
        }
    }

    // Renamed class to represent a different entity for the schema test
    static class OrderSchema {

        @Schema(description = "First order schema property", nullable = true)
        public OrderProperty propertyOne;

        private UserProperty userProperty;

        @Schema(description = "Order schema property, references UserProperty", example = "example value for userProperty")
        public UserProperty getUserProperty() {
            return userProperty;
        }
    }

    // Renamed properties to make them clearer about their role in the schema
    @Schema(description = "Represents a user-specific property", example = "User-specific example value")
    static class UserProperty {
        // public String value;
    }

    @Schema(description = "Represents an order-specific property", example = "Order-specific example value")
    static class OrderProperty {
        public BasicProperty basicProperty;
    }

    static class BasicSchema {

        @Schema(description = "First basic schema property")
        public BasicProperty propertyOne;

        private BasicProperty propertyTwo;

        @Schema(description = "Second basic schema property", example = "example value for propertyTwo")
        public BasicProperty getPropertyTwo() {
            return propertyTwo;
        }
    }

    // Renamed to represent a basic property common in various schemas
    @Schema(description = "Represents a basic schema property")
    static class BasicProperty {
        // public String value;
    }
}
