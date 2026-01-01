package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.Issue4290;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

public class Ticket4290Test extends SwaggerTestBase {
    @Test
    public void testAnyOf() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(mapper());

        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final Schema model = context
                .resolve(new AnnotatedType(Issue4290.class));

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), """
                Issue4290:
                  type: object
                  properties:
                    value:
                      type: object
                      description: "A string, a number or a boolean"
                      anyOf:
                      - type: string
                      - type: number
                      - type: boolean""");
    }

}
