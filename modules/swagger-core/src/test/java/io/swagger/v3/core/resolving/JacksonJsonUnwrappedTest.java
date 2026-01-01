package io.swagger.v3.core.resolving;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.JacksonUnwrappedRequiredProperty;
import org.testng.annotations.Test;

public class JacksonJsonUnwrappedTest {

  @Test(description = "test the @JsonUnwrapped behaviour when required Properties")
  public void jacksonJsonUnwrappedTest() {

    SerializationMatchers
        .assertEqualsToYaml(ModelConverters.getInstance().read(
            JacksonUnwrappedRequiredProperty.class), """
            InnerTypeRequired:
              required:
              - name
              type: object
              properties:
                foo:
                  type: integer
                  format: int32
                name:
                  type: string
            JacksonUnwrappedRequiredProperty:
              required:
              - name
              type: object
              properties:
                foo:
                  type: integer
                  format: int32
                name:
                  type: string
            """);
    }
}
