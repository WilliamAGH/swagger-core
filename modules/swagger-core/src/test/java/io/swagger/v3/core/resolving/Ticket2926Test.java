package io.swagger.v3.core.resolving;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

public class Ticket2926Test extends SwaggerTestBase {

    @Test
    public void testExtensionsInMapDeserializeAndSerialize() throws Exception {
        String yaml = """
                openapi: 3.0.1
                info:
                  title: My title
                  description: API under test
                  version: 1.0.7
                  x-info: test
                servers:
                - url: http://localhost:9999/api
                  x-server: test
                  description: desc
                  variables:\s
                    serVar:\s
                      description: desc
                      x-serverVariable: test
                paths:
                  /foo/bar:
                    get:
                      callbacks:
                        /foo/bar:
                          get:
                            description: getoperation
                          x-callback: test
                      responses:
                        default:
                          description: it works!
                          content:
                            application/json:
                              schema:
                                title: inline_response_200
                                type: object
                                properties:
                                  name:
                                    type: string
                              x-mediatype: test
                          x-response: test
                        x-responses: test
                        x-responses-object:\s
                          aaa: bbb
                        x-responses-array:\s
                          - aaa
                          - bbb
                      x-operation: test
                    x-pathitem: test
                  x-paths: test
                x-openapi-object:\s
                  aaa: bbb
                x-openapi-array:\s
                  - aaa
                  - bbb
                x-openapi: test""";

        OpenAPI aa = Yaml.mapper().readValue(yaml, OpenAPI.class);
        SerializationMatchers.assertEqualsToYaml(aa, yaml);

    }

}
