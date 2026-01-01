package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticket4474Test extends SwaggerTestBase {

    @AfterMethod
    public void afterTest() {
        ModelResolver.enumsAsRef = false;
    }

    @Test
    public void testAnyOf() throws Exception {
        ModelResolver.enumsAsRef = true;

        final ModelResolver modelResolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context
                .resolve(new AnnotatedType(Document.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), """
                Document:
                  type: object
                  properties:
                    data:
                      type: object
                      additionalProperties:
                        type: object
                        anyOf:
                        - type: array
                          items:
                            type: boolean
                        - type: array
                          items:
                            type: integer
                            format: int32
                        - type: array
                          items:
                            type: integer
                            format: int64
                        - type: array
                          items:
                            type: number
                            format: double
                        - type: array
                          items:
                            type: string
                        - type: boolean
                        - type: integer
                          format: int32
                        - type: integer
                          format: int64
                    listData:
                      type: array
                      items:
                        type: object
                        anyOf:
                        - type: array
                          items:
                            type: boolean
                        - type: array
                          items:
                            type: integer
                            format: int32
                        - type: array
                          items:
                            type: integer
                            format: int64
                        - type: array
                          items:
                            type: number
                            format: double
                        - type: array
                          items:
                            type: string
                        - type: boolean
                        - type: integer
                          format: int32
                        - type: integer
                          format: int64
                    itemData:
                      type: object
                      anyOf:
                      - type: array
                        items:
                          type: boolean
                      - type: array
                        items:
                          type: integer
                          format: int32
                      - type: array
                        items:
                          type: integer
                          format: int64
                      - type: array
                        items:
                          type: number
                          format: double
                      - type: array
                        items:
                          type: string
                      - type: boolean
                      - type: integer
                        format: int32
                      - type: integer
                        format: int64""");
    }

    static class Document {
        @io.swagger.v3.oas.annotations.media.Schema(
                anyOf = {
                        Boolean[].class,
                        Integer[].class,
                        Long[].class,
                        Double[].class,
                        String[].class,
                        Boolean.class,
                        Integer.class,
                        Long.class,
                        Map.class,
                })
        public Map<String, Object> data = new HashMap<>();

        @io.swagger.v3.oas.annotations.media.Schema(
                anyOf = {
                        Boolean[].class,
                        Integer[].class,
                        Long[].class,
                        Double[].class,
                        String[].class,
                        Boolean.class,
                        Integer.class,
                        Long.class,
                        Map.class,
                })
        public List<Object> listData = new ArrayList<>();

        @io.swagger.v3.oas.annotations.media.Schema(
                anyOf = {
                        Boolean[].class,
                        Integer[].class,
                        Long[].class,
                        Double[].class,
                        String[].class,
                        Boolean.class,
                        Integer.class,
                        Long.class,
                        Map.class,
                })
        public Object itemData = new Object();
    }
}
