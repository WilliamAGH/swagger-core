package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket4771Test {

    @Test
    public void testArraySchemaItemsValidation(){
        ModelConverters.reset();
        System.clearProperty(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY);
        ResolvedSchema schema = ModelConverters.getInstance(false, Schema.SchemaResolution.INLINE).resolveAsResolvedSchema(new AnnotatedType().type(CustomClass[].class));
        String expectedJson = """
                {
                  "schema" : {
                    "type" : "array",
                    "items" : {
                      "type" : "object",
                      "properties" : {
                        "foo" : {
                          "type" : "string"
                        }
                      }
                    }
                  },
                  "referencedSchemas" : {
                    "CustomClass" : {
                      "type" : "object",
                      "properties" : {
                        "foo" : {
                          "type" : "string"
                        }
                      }
                    }
                  }
                }\
                """;
        SerializationMatchers.assertEqualsToJson(schema, expectedJson);
        ModelConverters.reset();
        schema = ModelConverters.getInstance(true, Schema.SchemaResolution.INLINE).resolveAsResolvedSchema(new AnnotatedType().type(CustomClass[].class));
        expectedJson = """
                {
                  "schema" : {
                    "type" : "array",
                    "items" : {
                      "$ref" : "#/components/schemas/CustomClass"
                    }
                  },
                  "referencedSchemas" : {
                    "CustomClass" : {
                      "type" : "object",
                      "properties" : {
                        "foo" : {
                          "type" : "string"
                        }
                      }
                    }
                  }
                }\
                """;
        SerializationMatchers.assertEqualsToJson31(schema, expectedJson);
        ModelConverters.reset();
        System.setProperty(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY, "true");
        schema = ModelConverters.getInstance(true, Schema.SchemaResolution.INLINE).resolveAsResolvedSchema(new AnnotatedType().type(CustomClass[].class));
        expectedJson = """
                {
                  "schema" : {
                    "type" : "array",
                    "items" : {
                      "type" : "object",
                      "properties" : {
                        "foo" : {
                          "type" : "string"
                        }
                      }
                    }
                  },
                  "referencedSchemas" : {
                    "CustomClass" : {
                      "type" : "object",
                      "properties" : {
                        "foo" : {
                          "type" : "string"
                        }
                      }
                    }
                  }
                }\
                """;
        SerializationMatchers.assertEqualsToJson31(schema, expectedJson);
        System.clearProperty(Schema.APPLY_SCHEMA_RESOLUTION_PROPERTY);
        ModelConverters.reset();


    }

    private static class CustomClass {
        public String foo;
    }
}
