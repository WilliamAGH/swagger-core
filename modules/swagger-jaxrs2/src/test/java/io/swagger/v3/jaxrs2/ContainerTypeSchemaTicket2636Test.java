package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.HashMap;

public class ContainerTypeSchemaTicket2636Test extends AbstractAnnotationTest {

    @Test
    public void testContainerTypeSchemaTicket2636() throws Exception {
        String expectedYaml = """
                openapi: 3.0.1
                paths:
                  /path:
                    get:
                      summary: Op
                      description: 'RequestBody contains a Schema class that extends a Map '
                      operationId: getWithNoParameters
                      requestBody:
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/MyModel"
                        required: true
                      responses:
                        "200":
                          description: voila!
                components:
                  schemas:
                    MyModel:
                      type: object
                      properties:
                        empty:
                          type: boolean
                      additionalProperties:
                        type: string""";
        compareAsYaml(RequestBodyInheritanceModelIssue.class, expectedYaml);
    }

    static class RequestBodyInheritanceModelIssue {
        @Operation(
                summary = "Op",
                description = "RequestBody contains a Schema class that extends a Map ",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                })
        @GET
        @Path("/path")
        public void simpleGet(@RequestBody(required=true,
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = MyModel.class)))
                                      String data) {
        }
    }

    class MyModel extends HashMap<String, String> {

    }
}
