package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

public class Ticket4679Test extends SwaggerTestBase{

    @Test(description = "Custom schema implementation in property overrides type value")
    public void testCustomSchemaImplementation() {

        String expectedYaml = """
                ModelWithCustomSchemaImplementationInProperty:
                  type: object
                  properties:
                    exampleField:
                      type: integer
                      format: int32
                    secondExampleField:
                      type: string
                """;

        Map<String, io.swagger.v3.oas.models.media.Schema> stringSchemaMap = ModelConverters.getInstance(true).readAll(ModelWithCustomSchemaImplementationInProperty.class);
        SerializationMatchers.assertEqualsToYaml31(stringSchemaMap, expectedYaml);
    }

    static class ModelWithCustomSchemaImplementationInProperty {

        @Schema(implementation = Integer.class)
        private String exampleField;

        @Schema(type = "string")
        private Integer secondExampleField;

        public String getExampleField() {
            return exampleField;
        }

        public void setExampleField(String exampleField) {
            this.exampleField = exampleField;
        }

        public Integer getSecondExampleField() {
            return secondExampleField;
        }

        public void setSecondExampleField(Integer secondExampleField) {
            this.secondExampleField = secondExampleField;
        }


    }
}
