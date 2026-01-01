package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class Ticket3063Test extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;

    @BeforeTest
    public void setup() {
        modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }



    @Test
    public void testTicket3063() throws Exception {
        final Schema model = context.resolve(new AnnotatedType(BaseClass.class));
        assertNotNull(model);
        String yaml = """
                BaseClass:
                  required:
                  - type
                  type: object
                  properties:
                    type:
                      type: string
                      description: Type
                      example: AndroidDeviceRequirements
                  description: test
                  discriminator:
                    propertyName: type
                SubClass:
                  required:
                  - type
                  type: object
                  description: SubClass
                  allOf:
                  - $ref: "#/components/schemas/BaseClass"
                  - type: object
                    properties:
                      additionalPropertyWhichShouldBeThere:
                        type: integer
                        description: Test
                        format: int32""";

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), yaml);
    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "SubClass")
    public class SubClass extends BaseClass {
        @io.swagger.v3.oas.annotations.media.Schema(required = false, description = "Test")
        public int additionalPropertyWhichShouldBeThere = -1;

    }

    @io.swagger.v3.oas.annotations.media.Schema(description = "test", discriminatorProperty="type")
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SubClass.class, name = "SubClass")
    })
    public class BaseClass {

        @io.swagger.v3.oas.annotations.media.Schema(required = true, description = "Type", example="AndroidDeviceRequirements")
        public String type;

        public BaseClass(String type) {
            this.type = type;
        }

        public BaseClass() {
        }

    }
}
