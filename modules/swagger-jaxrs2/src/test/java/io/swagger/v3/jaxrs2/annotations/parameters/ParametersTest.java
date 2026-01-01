package io.swagger.v3.jaxrs2.annotations.parameters;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.resources.ResourceWithJacksonBean;
import io.swagger.v3.jaxrs2.resources.ResourceWithKnownInjections;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ParametersTest extends AbstractAnnotationTest {

    @Test(description = "scan class level and field level annotations")
    public void scanClassAndFieldLevelAnnotations() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ResourceWithKnownInjections.class);
        List<io.swagger.v3.oas.models.parameters.Parameter> resourceParameters = openAPI.getPaths().get("/resource/{id}").getGet().getParameters();
        assertNotNull(resourceParameters);
        assertEquals(resourceParameters.size(), 3);
        assertEquals(resourceParameters.get(0).getName(), "id");
        assertEquals(resourceParameters.get(1).getName(), "fieldParam");
        assertEquals(resourceParameters.get(2).getName(), "methodParam");
    }

    @Test
    public void testParameters() {
        String openApiYAML = readIntoYaml(ParametersTest.SimpleOperations.class);
        int start = openApiYAML.indexOf("/test:");
        int end = openApiYAML.length() - 1;
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = """
                /test:
                    post:
                      description: "subscribes a client to updates relevant to the requestor's account,\\
                        \\ as identified by the input token.  The supplied url will be used as the\\
                        \\ delivery address for response payloads"
                      operationId: subscribe
                      parameters:
                      - name: subscriptionId
                        in: path
                        required: true
                        style: simple
                        schema:
                          $ref: "#/components/schemas/SubscriptionResponse"
                      - name: formId
                        in: query
                        required: true
                        schema:
                          type: string
                        example: Example
                      - name: explodeFalse
                        in: query
                        required: true
                        explode: false
                        schema:
                          $ref: "#/components/schemas/SubscriptionResponse"
                      - name: explodeTrue
                        in: query
                        required: true
                        explode: true
                        schema:
                          $ref: "#/components/schemas/SubscriptionResponse"
                      - name: explodeAvoiding
                        in: query
                        required: true
                        schema:
                          type: string
                          description: the generated id
                          format: id
                          readOnly: true
                      - name: arrayParameter
                        in: query
                        required: true
                        explode: true
                        content:
                          application/json:
                            schema:
                              type: number
                              description: the generated id
                              readOnly: true
                          application/xml:
                            schema:
                              type: number
                              description: the generated id
                              readOnly: true
                      - name: arrayParameterImplementation
                        in: query
                        required: true
                        explode: true
                        schema:
                          maxItems: 10
                          minItems: 1
                          uniqueItems: true
                          type: array
                          items:
                            $ref: "#/components/schemas/SubscriptionResponse"
                      - name: arrayParameterImplementation2
                        in: query
                        required: true
                        explode: true
                        schema:
                          $ref: "#/components/schemas/SubscriptionResponse"
                      responses:
                        default:
                          description: test description
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/SubscriptionResponse"
                components:
                  schemas:
                    SubscriptionResponse:
                      type: object
                      properties:
                        subscriptionId:
                          type: string""";
        assertEquals(extractedYAML, expectedYAML);
    }

    @Test
    public void testArraySchemaParameters() {
        String openApiYAML = readIntoYaml(ParametersTest.ArraySchemaOperations.class);
        int start = openApiYAML.indexOf("/test:");
        int end = openApiYAML.length() - 1;
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = """
                /test:
                    post:
                      description: "subscribes a client to updates relevant to the requestor's account,\\
                        \\ as identified by the input token.  The supplied url will be used as the\\
                        \\ delivery address for response payloads"
                      operationId: subscribe
                      parameters:
                      - name: arrayParameter
                        in: query
                        required: true
                        explode: true
                        schema:
                          maxItems: 10
                          minItems: 1
                          uniqueItems: true
                          type: array
                          items:
                            $ref: "#/components/schemas/SubscriptionResponse"
                      responses:
                        default:
                          description: test description
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/SubscriptionResponse"
                components:
                  schemas:
                    SubscriptionResponse:
                      type: object
                      properties:
                        subscriptionId:
                          type: string""";
        assertEquals(extractedYAML, expectedYAML);
    }

    @Test
    public void testRepeatableParameters() {
        String openApiYAML = readIntoYaml(ParametersTest.RepeatableParametersOperations.class);
        int start = openApiYAML.indexOf("/test:");
        int end = openApiYAML.length() - 1;
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = """
                /test:
                    post:
                      description: "subscribes a client to updates relevant to the requestor's account,\\
                        \\ as identified by the input token.  The supplied url will be used as the\\
                        \\ delivery address for response payloads"
                      operationId: subscribe
                      parameters:
                      - name: subscriptionId
                        in: path
                        required: true
                        style: simple
                        schema:
                          $ref: "#/components/schemas/SubscriptionResponse"
                      - name: formId
                        in: query
                        required: true
                        schema:
                          type: string
                        example: Example
                      - name: explodeFalse
                        in: query
                        required: true
                        explode: false
                        schema:
                          $ref: "#/components/schemas/SubscriptionResponse"
                      - name: explodeTrue
                        in: query
                        required: true
                        explode: true
                        schema:
                          $ref: "#/components/schemas/SubscriptionResponse"
                      - name: explodeAvoiding
                        in: query
                        required: true
                        schema:
                          type: string
                          description: the generated id
                          format: id
                          readOnly: true
                      - name: arrayParameter
                        in: query
                        required: true
                        explode: true
                        content:
                          application/json:
                            schema:
                              type: number
                              description: the generated id
                              readOnly: true
                          application/xml:
                            schema:
                              type: number
                              description: the generated id
                              readOnly: true
                      - name: arrayParameterImplementation
                        in: query
                        required: true
                        explode: true
                        schema:
                          maxItems: 10
                          minItems: 1
                          uniqueItems: true
                          type: array
                          items:
                            $ref: "#/components/schemas/SubscriptionResponse"
                      - name: arrayParameterImplementation2
                        in: query
                        required: true
                        explode: true
                        schema:
                          $ref: "#/components/schemas/SubscriptionResponse"
                      responses:
                        default:
                          description: test description
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/SubscriptionResponse"
                components:
                  schemas:
                    SubscriptionResponse:
                      type: object
                      properties:
                        subscriptionId:
                          type: string""";
        assertEquals(extractedYAML, expectedYAML);
    }

    @Test(description = "JsonUnwrapped, JsonIgnore, JsonValue should be honoured")
    public void testJacksonFeatures() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ResourceWithJacksonBean.class);
        io.swagger.v3.oas.models.media.Schema o = openAPI.getComponents().getSchemas().get("JacksonBean");

        assertEquals(o.getProperties().keySet(), Stream.of("identity", "bean", "code", "message",
                "precodesuf", "premessagesuf").collect(Collectors.toSet()));
    }

    static class SimpleOperations {
        @Path("/test")
        @POST
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                parameters = {
                        @Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class), style = ParameterStyle.SIMPLE),
                        @Parameter(in = ParameterIn.QUERY, name = "formId", required = true,
                                example = "Example"),
                        @Parameter(in = ParameterIn.QUERY, name = "explodeFalse", required = true, explode = Explode.FALSE,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class)),
                        @Parameter(in = ParameterIn.QUERY, name = "explodeTrue", required = true, explode = Explode.TRUE,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class)),
                        @Parameter(in = ParameterIn.QUERY, name = "explodeAvoiding", required = true, explode = Explode.TRUE,
                                schema = @Schema(
                                        type = "int",
                                        format = "id",
                                        description = "the generated id",
                                        accessMode = Schema.AccessMode.READ_ONLY
                                )),
                        @Parameter(in = ParameterIn.QUERY, name = "arrayParameter", required = true, explode = Explode.TRUE,
                                array = @ArraySchema(maxItems = 10, minItems = 1,
                                        schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class),
                                        uniqueItems = true
                                )
                                ,
                                schema = @Schema(
                                        type = "int",
                                        format = "id",
                                        description = "the generated id",
                                        accessMode = Schema.AccessMode.READ_ONLY),
                                content = @Content(schema = @Schema(type = "number",
                                        description = "the generated id",
                                        accessMode = Schema.AccessMode.READ_ONLY))
                        ),
                        @Parameter(in = ParameterIn.QUERY, name = "arrayParameterImplementation", required = true, explode = Explode.TRUE,
                                array = @ArraySchema(maxItems = 10, minItems = 1,
                                        schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class),
                                        uniqueItems = true
                                )
                        ),
                        @Parameter(in = ParameterIn.QUERY, name = "arrayParameterImplementation2", required = true, explode = Explode.TRUE,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class))

                },
                responses = {
                        @ApiResponse(
                                description = "test description", content = @Content(
                                mediaType = "*/*",
                                schema =
                                @Schema(
                                        implementation = ParametersTest.SubscriptionResponse.class)
                        ))
                })
        @Consumes({"application/json", "application/xml"})
        public ParametersTest.SubscriptionResponse subscribe() {
            return null;
        }
    }

    static class ArraySchemaOperations {
        @Path("/test")
        @POST
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                parameters = {
                        @Parameter(in = ParameterIn.QUERY, name = "arrayParameter", required = true, explode = Explode.TRUE,
                                array = @ArraySchema(maxItems = 10, minItems = 1,
                                        schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class),
                                        uniqueItems = true
                                )
                        ),
                },
                responses = {
                        @ApiResponse(
                                description = "test description", content = @Content(
                                mediaType = "*/*",
                                schema =
                                @Schema(
                                        implementation = ParametersTest.SubscriptionResponse.class)
                        ))
                })
        @Consumes({"application/json", "application/xml"})
        public ParametersTest.SubscriptionResponse subscribe() {
            return null;
        }
    }

    static class RepeatableParametersOperations {
        @Path("/test")
        @POST
        @Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true,
                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class), style = ParameterStyle.SIMPLE)
        @Parameter(in = ParameterIn.QUERY, name = "formId", required = true,
                example = "Example")
        @Parameter(in = ParameterIn.QUERY, name = "explodeFalse", required = true, explode = Explode.FALSE,
                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class))
        @Parameter(in = ParameterIn.QUERY, name = "explodeTrue", required = true, explode = Explode.TRUE,
                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class))
        @Parameter(in = ParameterIn.QUERY, name = "explodeAvoiding", required = true, explode = Explode.TRUE,
                schema = @Schema(
                        type = "int",
                        format = "id",
                        description = "the generated id",
                        accessMode = Schema.AccessMode.READ_ONLY
                ))
        @Parameter(in = ParameterIn.QUERY, name = "arrayParameter", required = true, explode = Explode.TRUE,
                array = @ArraySchema(maxItems = 10, minItems = 1,
                        schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class),
                        uniqueItems = true
                )
                ,
                schema = @Schema(
                        type = "int",
                        format = "id",
                        description = "the generated id",
                        accessMode = Schema.AccessMode.READ_ONLY),
                content = @Content(schema = @Schema(type = "number",
                        description = "the generated id",
                        accessMode = Schema.AccessMode.READ_ONLY))
        )
        @Parameter(in = ParameterIn.QUERY, name = "arrayParameterImplementation", required = true, explode = Explode.TRUE,
                array = @ArraySchema(maxItems = 10, minItems = 1,
                        schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class),
                        uniqueItems = true
                )
        )
        @Parameter(in = ParameterIn.QUERY, name = "arrayParameterImplementation2", required = true, explode = Explode.TRUE,
                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class))
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                responses = {
                        @ApiResponse(
                                description = "test description", content = @Content(
                                mediaType = "*/*",
                                schema =
                                @Schema(
                                        implementation = ParametersTest.SubscriptionResponse.class)
                        ))
                })
        @Consumes({"application/json", "application/xml"})
        public ParametersTest.SubscriptionResponse subscribe() {
            return null;
        }
    }

    static class SubscriptionResponse {
        public String subscriptionId;
    }
}
