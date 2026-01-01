package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import tools.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class Ticket3197Test extends SwaggerTestBase {

    private ModelResolver modelResolver;
    private ModelConverterContextImpl context;


    @BeforeMethod
    public void beforeMethod() {
        ModelResolver.composedModelPropertiesAsSibling = false;
        modelResolver = new ModelResolver(new ObjectMapper());
        context = new ModelConverterContextImpl(modelResolver);
    }

    @AfterTest
    public void afterTest() {
        ModelResolver.composedModelPropertiesAsSibling = false;
    }

    @Test
    public void testTicket3197() throws Exception {
        final Schema model = context.resolve(new AnnotatedType(Car.class));
        assertNotNull(model);
        String yaml = """
                Car:
                  required:
                  - type
                  type: object
                  properties:
                    carMetaData:
                      type: string
                    type:
                      type: string
                  discriminator:
                    propertyName: type
                    mapping:
                      RaceCar: "#/components/schemas/RaceCar"
                      SportCar: "#/components/schemas/SportCar"
                  oneOf:
                  - $ref: "#/components/schemas/RaceCar"
                  - $ref: "#/components/schemas/SportCar"
                RaceCar:
                  required:
                  - carMetaData
                  - id
                  type: object
                  allOf:
                  - $ref: "#/components/schemas/Car"
                  - type: object
                    properties:
                      id:
                        type: integer
                        format: int64
                      model:
                        type: string
                SportCar:
                  required:
                  - id
                  type: object
                  allOf:
                  - $ref: "#/components/schemas/Car"
                  - type: object
                    properties:
                      id:
                        type: integer
                        format: int64
                      model:
                        type: string
                """;

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), yaml);
    }

    @Test
    public void testTicket3197AsSibling() throws Exception {

        ModelResolver.composedModelPropertiesAsSibling = true;
        ModelResolver myModelResolver = new ModelResolver(new ObjectMapper());
        ModelResolver.composedModelPropertiesAsSibling = true;
        ModelConverterContextImpl myContext = new ModelConverterContextImpl(myModelResolver);

        final Schema model = myContext.resolve(new AnnotatedType(Car.class));
        assertNotNull(model);
        String yaml = """
                Car:
                  required:
                  - type
                  type: object
                  properties:
                    carMetaData:
                      type: string
                    type:
                      type: string
                  discriminator:
                    propertyName: type
                    mapping:
                      RaceCar: "#/components/schemas/RaceCar"
                      SportCar: "#/components/schemas/SportCar"
                  oneOf:
                  - $ref: "#/components/schemas/RaceCar"
                  - $ref: "#/components/schemas/SportCar"
                RaceCar:
                  required:
                  - carMetaData
                  - id
                  type: object
                  properties:
                    id:
                      type: integer
                      format: int64
                    model:
                      type: string
                  allOf:
                  - $ref: "#/components/schemas/Car"
                SportCar:
                  required:
                  - id
                  type: object
                  properties:
                    id:
                      type: integer
                      format: int64
                    model:
                      type: string
                  allOf:
                  - $ref: "#/components/schemas/Car"
                """;

        SerializationMatchers.assertEqualsToYaml(myContext.getDefinedModels(), yaml);
        ModelResolver.composedModelPropertiesAsSibling = false;
    }


    @io.swagger.v3.oas.annotations.media.Schema(discriminatorProperty = "type", discriminatorMapping = {
            @DiscriminatorMapping(value = "RaceCar", schema = RaceCar.class),
            @DiscriminatorMapping(value = "SportCar", schema = SportCar.class)
    },
            oneOf = {
                    RaceCar.class,
                    SportCar.class
            })
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
            property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = RaceCar.class),
            @JsonSubTypes.Type(value = SportCar.class)
    })
    static abstract class Car {

        private String carMetaData;

        public String getCarMetaData() { return carMetaData; }
    }

    static class RaceCar extends Car {

        @JsonProperty(required = true)
        public Long id;

        public String model;

        @io.swagger.v3.oas.annotations.media.Schema(required = true)
        public String carMetaData;
    }

    static class SportCar extends Car {

        @JsonProperty(required = true)
        public Long id;

        public String model;
    }
}
