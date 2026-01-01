package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;

import static io.swagger.v3.core.util.TestUtils.normalizeLineEnds;
import static org.testng.Assert.assertEquals;

public class NumericFormatTest {
    @Test
    public void testFormatOfInteger() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithIntegerFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);
        assertEquals(normalizeLineEnds(json),
                """
                {
                  "ModelWithIntegerFields" : {
                    "type" : "object",
                    "properties" : {
                      "id" : {
                        "minimum" : 3,
                        "type" : "integer",
                        "format" : "int32"
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testFormatOfDecimal() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithDecimalFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);
        assertEquals(normalizeLineEnds(json),
                """
                {
                  "ModelWithDecimalFields" : {
                    "type" : "object",
                    "properties" : {
                      "id" : {
                        "minimum" : 3.3,
                        "exclusiveMinimum" : false,
                        "type" : "number",
                        "format" : "double"
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testFormatOfBigDecimal() {
        final Map<String, Schema> models = ModelConverters.getInstance().readAll(ModelWithoutScientificFields.class);
        assertEquals(models.size(), 1);

        String json = Json.pretty(models);

        assertEquals(normalizeLineEnds(json),
                """
                {
                  "ModelWithoutScientificFields" : {
                    "type" : "object",
                    "properties" : {
                      "id" : {
                        "maximum" : 9999999999999999.99,
                        "exclusiveMaximum" : false,
                        "minimum" : -9999999999999999.99,
                        "exclusiveMinimum" : false,
                        "type" : "number"
                      }
                    }
                  }
                }\
                """);

    }

    static class ModelWithIntegerFields {
        @io.swagger.v3.oas.annotations.media.Schema
        @Min(value = 3)
        public Integer id;
    }

    static class ModelWithDecimalFields {
        @io.swagger.v3.oas.annotations.media.Schema
        @DecimalMin(value = "3.3")
        public Double id;
    }

    static class ModelWithoutScientificFields {
        @io.swagger.v3.oas.annotations.media.Schema
        @DecimalMin(value = "-9999999999999999.99")
        @DecimalMax(value = "9999999999999999.99")
        public BigDecimal id;
    }
}
