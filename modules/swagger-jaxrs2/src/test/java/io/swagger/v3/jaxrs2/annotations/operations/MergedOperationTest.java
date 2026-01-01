package io.swagger.v3.jaxrs2.annotations.operations;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.testng.Assert.assertEquals;

public class MergedOperationTest extends AbstractAnnotationTest {
    @Test(description = "shows a response when no annotation is present")
    public void testUnannotatedMethod() {
        String yaml = readIntoYaml(UnannotatedMethodClass.class);
        String expectedYaml = "openapi: 3.0.1\n";
        assertEquals(yaml, expectedYaml);
    }

    static class UnannotatedMethodClass {
        @GET
        public SimpleResponse getSimpleResponse() {
            return null;
        }
    }

    static class SimpleResponse {
        private String id;
    }

    @Test(description = "shows how a method with parameters and no special annotations is processed")
    public void testAnnotatedParameters() {
        String yaml = readIntoYaml(MethodWithParameters.class);

        assertEquals(yaml,
                """
                openapi: 3.0.1
                paths:
                  /findAll:
                    get:
                      description: method with parameters
                      operationId: getSimpleResponseWithParameters
                      parameters:
                      - name: id
                        in: query
                        schema:
                          type: string
                      - name: x-authorized-by
                        in: header
                        schema:
                          type: array
                          items:
                            type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/SimpleResponse"
                components:
                  schemas:
                    SimpleResponse:
                      type: object
                """);
    }

    static class MethodWithParameters {
        @GET
        @Operation(description = "method with parameters")
        @Path("/findAll")
        public SimpleResponse getSimpleResponseWithParameters(
                @QueryParam("id") String id,
                @HeaderParam("x-authorized-by") String[] auth) {
            return null;
        }
    }

    @Test(description = "shows how annotations can decorate an operation")
    public void testPartiallyAnnotatedMethod() {
        String yaml = readIntoYaml(MethodWithPartialAnnotation.class);
        String expectedYaml = """
                openapi: 3.0.1
                paths:
                  /findAll:
                    get:
                      description: returns a value
                      operationId: getSimpleResponseWithParameters
                      parameters:
                      - name: id
                        in: query
                        schema:
                          pattern: "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"
                          type: string
                          description: a GUID for the user in uuid-v4 format
                          format: uuid
                      - name: x-authorized-by
                        in: header
                        schema:
                          type: array
                          items:
                            type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/SimpleResponse"
                components:
                  schemas:
                    SimpleResponse:
                      type: object
                """;

        assertEquals(yaml, expectedYaml);
    }

    static class MethodWithPartialAnnotation {
        @GET
        @Operation(description = "returns a value")
        @Path("/findAll")
        public SimpleResponse getSimpleResponseWithParameters(
                @Schema(
                        description = "a GUID for the user in uuid-v4 format",
                        required = true,
                        format = "uuid",
                        pattern = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")
                @QueryParam("id") String id,
                @HeaderParam("x-authorized-by") String[] auth) {
            return null;
        }
    }

    @Test(description = "shows how a request body is passed")
    public void testRequestBody() {
        String yaml = readIntoYaml(MethodWithRequestBody.class);
        String expectedYaml = """
                openapi: 3.0.1
                paths:
                  /add:
                    post:
                      description: receives a body
                      operationId: addValue
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/InputValue"
                      responses:
                        "201":
                          description: value successfully processed
                components:
                  schemas:
                    InputValue:
                      type: object
                """;

        assertEquals(yaml, expectedYaml);
    }

    static class MethodWithRequestBody {
        @POST
        @Operation(description = "receives a body",
                responses = @ApiResponse(
                        responseCode = "201",
                        description = "value successfully processed")
        )
        @Path("/add")
        public void addValue(InputValue input) {
        }
    }

    static class InputValue {
        private Long id;
        private String name;
    }
}
