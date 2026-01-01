package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.TestObject2992;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket2992Test extends SwaggerTestBase {

    @Test
    public void testLocalTime() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(TestObject2992.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), """
                LocalTime:
                  type: object
                  properties:
                    hour:
                      type: integer
                      format: int32
                    minute:
                      type: integer
                      format: int32
                    second:
                      type: integer
                      format: int32
                    nano:
                      type: integer
                      format: int32
                TestObject2992:
                  type: object
                  properties:
                    name:
                      type: string
                    a:
                      $ref: "#/components/schemas/LocalTime"
                    b:
                      $ref: "#/components/schemas/LocalTime"
                    c:
                      $ref: "#/components/schemas/LocalTime"
                    d:
                      type: string
                      format: date-time
                    e:
                      type: string
                      format: date-time
                    f:
                      type: string
                      format: date-time""");

        PrimitiveType.enablePartialTime();
        context = new ModelConverterContextImpl(modelResolver);

        context
                .resolve(new AnnotatedType(TestObject2992.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), """
                TestObject2992:
                  type: object
                  properties:
                    name:
                      type: string
                    a:
                      type: string
                      format: partial-time
                    b:
                      type: string
                      format: partial-time
                    c:
                      type: string
                      format: partial-time
                    d:
                      type: string
                      format: date-time
                    e:
                      type: string
                      format: date-time
                    f:
                      type: string
                      format: date-time""");
    }

}
