package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.TestObject2915;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket2915Test extends SwaggerTestBase {
    @Test
    public void testPropertyName() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context
                .resolve(new AnnotatedType(TestObject2915.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), """
                QuantitativeValue:
                  required:
                  - value
                  type: object
                  properties:
                    value:
                      type: number
                      format: double
                    unitText:
                      type: string
                    unitCode:
                      type: string
                  description: A combination of a value and associated unit
                TestObject2616:
                  type: object
                  properties:
                    name:
                      type: string
                    perServing:
                      $ref: "#/components/schemas/QuantitativeValue"
                    per100Gram:
                      $ref: "#/components/schemas/QuantitativeValue"
                  description: Nutritional value specification""");
    }

}
