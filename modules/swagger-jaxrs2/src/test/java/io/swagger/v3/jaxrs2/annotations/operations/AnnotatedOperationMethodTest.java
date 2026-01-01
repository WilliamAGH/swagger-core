package io.swagger.v3.jaxrs2.annotations.operations;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.resources.GenericResponsesResource;
import io.swagger.v3.jaxrs2.resources.HiddenAnnotatedUserResource;
import io.swagger.v3.jaxrs2.resources.HiddenUserResource;
import io.swagger.v3.jaxrs2.resources.PetResource;
import io.swagger.v3.jaxrs2.resources.PetResourceSlashesinPath;
import io.swagger.v3.jaxrs2.resources.SimpleUserResource;
import io.swagger.v3.jaxrs2.resources.UserResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.testng.annotations.Test;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
    @Test
    public void testSimpleGetOperation() {
        String openApiYAML = readIntoYaml(SimpleGetOperationTest.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex
                      operationId: getWithNoParameters
                      responses:
                        "200":
                          description: voila!
                      deprecated: true""";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetOperationTest {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithNoParameters",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                }
        )
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testSimpleGetOperationWithoutResponses() {
        String openApiYAML = readIntoYaml(SimpleGetWithoutResponses.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs or responses
                      operationId: getWithNoParametersAndNoResponses
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                      deprecated: true""";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetWithoutResponses {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs or responses",
                operationId = "getWithNoParametersAndNoResponses",
                deprecated = true
        )
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testGetOperationWithResponsePayloadAndAlternateCodes() {
        String openApiYAML = readIntoYaml(GetOperationWithResponsePayloadAndAlternateCodes.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/SampleResponseSchema"
                        default:
                          description: boo
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/GenericError"
                              examples:
                                boo:
                                  summary: example of boo
                                  description: boo
                                  value: example
                                  externalValue: example of external value
                      deprecated: true
                """;

        assertEquals(extractedYAML, expectedYAML);
    }

    static class GetOperationWithResponsePayloadAndAlternateCodes {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = SampleResponseSchema.class)
                                )
                        ),
                        @ApiResponse(
                                description = "boo",
                                content = @Content(
                                        mediaType = "*/*",
                                        schema = @Schema(implementation = GenericError.class),
                                        examples = {
                                                @ExampleObject(name = "boo", value = "example",
                                                        summary = "example of boo", externalValue = "example of external value")
                                        }

                                )
                        )
                }
        )
        @Path("/path")
        @GET
        public void simpleGet() {
        }
    }

    static class SampleResponseSchema {
        @Schema(description = "the user id")
        public String id;
    }

    static class SampleHeaderSchema {
        public String id;
    }

    static class GenericError {
        public int code;
        public String message;
    }

    @Test(description = "reads an operation with response examples defined")
    public void testOperationWithResponseExamples() {
        String openApiYAML = readIntoYaml(GetOperationWithResponseExamples.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/SampleResponseSchema"
                              examples:
                                basic:
                                  summary: shows a basic example
                                  description: basic
                                  value: "{id: 19877734}"
                      deprecated: true
                """;
        assertEquals(extractedYAML, expectedYAML);
    }

    @Test(description = "reads an operation with response examples defined")
    public void testOperationWithParameterExample() {
        String openApiYAML = readIntoYaml(GetOperationWithParameterExample.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with a parameter example
                      operationId: getWithPayloadResponse
                      parameters:
                      - in: query
                        schema:
                          type: string
                        example:
                          id: 19877734
                      responses:
                        "200":
                          description: voila!
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/SampleResponseSchema"
                              examples:
                                basic:
                                  summary: shows a basic example
                                  description: basic
                                  value: "{id: 19877734}"
                      deprecated: true
                """;
        assertEquals(extractedYAML, expectedYAML);
    }

    static class GetOperationWithResponseExamples {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = SampleResponseSchema.class),
                                        examples = {
                                                @ExampleObject(
                                                        name = "basic",
                                                        summary = "shows a basic example",
                                                        value = "{id: 19877734}")
                                        }
                                )
                        )
                }
        )
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    static class GetOperationWithParameterExample {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with a parameter example",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                content = @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = SampleResponseSchema.class),
                                        examples = {
                                                @ExampleObject(
                                                        name = "basic",
                                                        summary = "shows a basic example",
                                                        value = "{id: 19877734}")
                                        }
                                )
                        )
                }
        )
        @GET
        @Path("/path")
        public void simpleGet(@Parameter(in = ParameterIn.QUERY, example = "{\"id\": 19877734}") String exParam) {
        }
    }

    static class GetOperationWithResponseSingleHeader {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                headers = {@Header(
                                        name = "Rate-Limit-Limit",
                                        description = "The number of allowed requests in the current period",
                                        schema = @Schema(type = "integer"))})
                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testOperationWithResponseSingleHeader() {
        String openApiYAML = readIntoYaml(GetOperationWithResponseSingleHeader.class);
        int start = openApiYAML.indexOf("get:");
        String extractedYAML = openApiYAML.substring(start);
        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          headers:
                            Rate-Limit-Limit:
                              description: The number of allowed requests in the current period
                              style: simple
                              schema:
                                type: integer
                      deprecated: true
                """;
        assertEquals(expectedYAML, extractedYAML);
    }

    static class GetOperationWithResponseMultipleHeaders {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                headers = {@Header(
                                        explode = Explode.TRUE,
                                        name = "Rate-Limit-Limit",
                                        description = "The number of allowed requests in the current period",
                                        schema = @Schema(type = "integer")),
                                        @Header(
                                                name = "X-Rate-Limit-Desc",
                                                description = "The description of rate limit",
                                                schema = @Schema(type = "string"))})
                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testOperationWithResponseArraySchema() {
        String openApiYAML = readIntoYaml(GetOperationResponseHeaderWithArraySchema.class);
        int start = openApiYAML.indexOf("get:");
        String extractedYAML = openApiYAML.substring(start);
        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          headers:
                            Rate-Limit-Limit:
                              description: The number of allowed requests in the current period
                              style: simple
                              schema:
                                maxItems: 10
                                minItems: 1
                                type: array
                                items:
                                  type: integer
                      deprecated: true
                """;
        assertEquals(expectedYAML, extractedYAML);
    }

    static class GetOperationResponseHeaderWithArraySchema {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                headers = {@Header(
                                        name = "Rate-Limit-Limit",
                                        description = "The number of allowed requests in the current period",
                                        array = @ArraySchema(maxItems = 10, minItems = 1,schema = @Schema(type = "integer")))})})
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    static class GetOperationResponseWithoutHiddenHeader {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                headers = {@Header(
                                        hidden = true,
                                        name = "Rate-Limit-Limit",
                                        description = "The number of allowed requests in the current period",
                                        schema = @Schema(type = "integer")),
                                        @Header(
                                                name = "X-Rate-Limit-Desc",
                                                description = "The description of rate limit",
                                                schema = @Schema(type = "string"))})
                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    static class GetOperationWithResponseMultipleHeadersAndExamples {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                headers = {@Header(
                                        examples = {
                                                @ExampleObject(
                                                        name = "ex 1",
                                                        description = "example description",
                                                        value = "example value"
                                                ),
                                                @ExampleObject(
                                                        name = "ex 2",
                                                        description = "example description 2",
                                                        value = "example value 2"
                                                )
                                        },
                                        name = "Rate-Limit-Limit",
                                        description = "The number of allowed requests in the current period",
                                        schema = @Schema(type = "object")),
                                        @Header(
                                                name = "X-Rate-Limit-Desc",
                                                description = "The description of rate limit",
                                                array = @ArraySchema(schema = @Schema()),
                                                example = "example1")})

                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    static class GetOperationResponseWithHeaderExplodeAttribute {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                headers = {@Header(
                                        name = "Rate-Limit-Limit",
                                        description = "The number of allowed requests in the current period",
                                        explode = Explode.TRUE,
                                        schema = @Schema(type = "object")),
                                        @Header(
                                                name = "X-Rate-Limit-Desc",
                                                description = "The description of rate limit",
                                                explode = Explode.FALSE,
                                                schema = @Schema(type = "array"))})
                })
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testOperationWithResponseMultipleHeaders() {
        String openApiYAML = readIntoYaml(GetOperationWithResponseMultipleHeaders.class);
        int start = openApiYAML.indexOf("get:");
        String extractedYAML = openApiYAML.substring(start);
        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          headers:
                            X-Rate-Limit-Desc:
                              description: The description of rate limit
                              style: simple
                              schema:
                                type: string
                            Rate-Limit-Limit:
                              description: The number of allowed requests in the current period
                              style: simple
                              schema:
                                type: integer
                      deprecated: true
                """;
        assertEquals(expectedYAML, extractedYAML);
    }

    static class GetOperationWithResponseMultipleHeadersWithImplementationSchema {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex output",
                operationId = "getWithPayloadResponse",
                deprecated = true,
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!",
                                headers = {@Header(
                                        explode = Explode.TRUE,
                                        name = "Rate-Limit-Limit",
                                        description = "The number of allowed requests in the current period",
                                        array = @ArraySchema(maxItems = 10, minItems = 1,schema = @Schema(implementation = SampleHeaderSchema.class))),
                                        @Header(
                                                explode = Explode.TRUE,
                                                name = "X-Rate-Limit-Desc",
                                                description = "The description of rate limit",
                                                schema = @Schema(implementation = SampleHeaderSchema.class))})})



        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testOperationWithResponseMultipleHeadersImplementationSchema() {
        String openApiYAML = readIntoYaml(GetOperationWithResponseMultipleHeadersWithImplementationSchema.class);
        int start = openApiYAML.indexOf("get:");
        String extractedYAML = openApiYAML.substring(start);
        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          headers:
                            X-Rate-Limit-Desc:
                              description: The description of rate limit
                              style: simple
                              explode: true
                              schema:
                                $ref: "#/components/schemas/SampleHeaderSchema"
                            Rate-Limit-Limit:
                              description: The number of allowed requests in the current period
                              style: simple
                              explode: true
                              schema:
                                maxItems: 10
                                minItems: 1
                                type: array
                                items:
                                  $ref: "#/components/schemas/SampleHeaderSchema"
                      deprecated: true
                components:
                  schemas:
                    SampleHeaderSchema:
                      type: object
                      properties:
                        id:
                          type: string
                """;
        assertEquals(expectedYAML, extractedYAML);
    }

    @Test
    public void testOperationWithResponseMultipleHeadersAndExplodeAttribute() {
        String openApiYAML = readIntoYaml(GetOperationResponseWithHeaderExplodeAttribute.class);
        int start = openApiYAML.indexOf("get:");
        String extractedYAML = openApiYAML.substring(start);
        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          headers:
                            X-Rate-Limit-Desc:
                              description: The description of rate limit
                              style: simple
                              explode: false
                              schema:
                                type: array
                            Rate-Limit-Limit:
                              description: The number of allowed requests in the current period
                              style: simple
                              explode: true
                              schema:
                                type: object
                      deprecated: true
                """;
        assertEquals(expectedYAML, extractedYAML);
    }

    @Test
    public void testOperationResponseWithoutHiddenHeader() {
        String openApiYAML = readIntoYaml(GetOperationResponseWithoutHiddenHeader.class);
        int start = openApiYAML.indexOf("get:");
        String extractedYAML = openApiYAML.substring(start);
        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          headers:
                            X-Rate-Limit-Desc:
                              description: The description of rate limit
                              style: simple
                              schema:
                                type: string
                      deprecated: true
                """;
        assertEquals(expectedYAML, extractedYAML);
    }

    @Test
    public void testOperationWithResponseMultipleHeadersAndExamples() {
        String openApiYAML = readIntoYaml(GetOperationWithResponseMultipleHeadersAndExamples.class);
        int start = openApiYAML.indexOf("get:");
        String extractedYAML = openApiYAML.substring(start);
        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex output
                      operationId: getWithPayloadResponse
                      responses:
                        "200":
                          description: voila!
                          headers:
                            X-Rate-Limit-Desc:
                              description: The description of rate limit
                              style: simple
                              example: example1
                            Rate-Limit-Limit:
                              description: The number of allowed requests in the current period
                              style: simple
                              schema:
                                type: object
                              examples:
                                ex 1:
                                  description: example description
                                  value: example value
                                ex 2:
                                  description: example description 2
                                  value: example value 2
                      deprecated: true
                """;
        assertEquals(expectedYAML, extractedYAML);
    }

    @Test(description = "reads the pet resource from sample")
    public void testCompletePetResource() throws IOException {
        String expectedYAML = """
                openapi: 3.0.1
                paths:
                  /pet/findByTags:
                    get:
                      summary: Finds Pets by tags
                      description: Multiple tags can be provided with comma separated strings. Use tag1, tag2, tag3 for testing.
                      operationId: findPetsByTags
                      parameters:
                      - name: tags
                        in: query
                        description: Tags to filter by
                        required: true
                        schema:
                          type: string
                      responses:
                        default:
                          description: Pets matching criteria
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Pet"
                        "400":
                          description: Invalid tag value
                      deprecated: true
                  /pet/findByCategory/{category}:
                    get:
                      summary: Finds Pets by category
                      operationId: findPetsByCategory
                      parameters:
                      - name: category
                        in: path
                        description: Category value that need to be considered for filter
                        required: true
                        style: matrix
                        schema:
                          $ref: "#/components/schemas/Category"
                      - name: skip
                        in: query
                        schema:
                          type: integer
                          format: int32
                      - name: limit
                        in: query
                        schema:
                          type: integer
                          format: int32
                      responses:
                        default:
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Pet"
                        "400":
                          description: Invalid category value
                  /pet/{petId}:
                    get:
                      summary: Find pet by ID
                      description: Returns a pet when 0 < ID <= 10.  ID > 10 or nonintegers will simulate API error conditions
                      operationId: getPetById
                      parameters:
                      - name: petId
                        in: path
                        description: ID of pet that needs to be fetched
                        required: true
                        schema:
                          type: integer
                          format: int64
                      responses:
                        default:
                          description: The pet
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Pet"
                            application/xml:
                              schema:
                                $ref: "#/components/schemas/Pet"
                        "400":
                          description: Invalid ID supplied
                        "404":
                          description: Pet not found
                  /pet/bodynoannotation:
                    post:
                      summary: Add a new pet to the store no annotation
                      operationId: addPetNoAnnotation
                      requestBody:
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Pet"
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/Pet"
                      responses:
                        "405":
                          description: Invalid input
                  /pet/bodyid:
                    post:
                      summary: Add a new pet to the store passing an integer with generic parameter annotation
                      operationId: addPetByInteger
                      requestBody:
                        description: Pet object that needs to be added to the store
                        content:
                          application/json:
                            schema:
                              type: integer
                              format: int32
                          application/xml:
                            schema:
                              type: integer
                              format: int32
                        required: true
                      responses:
                        "405":
                          description: Invalid input
                  /pet/bodyidnoannotation:
                    post:
                      summary: Add a new pet to the store passing an integer without parameter annotation
                      operationId: addPetByIntegerNoAnnotation
                      requestBody:
                        content:
                          application/json:
                            schema:
                              type: integer
                              format: int32
                          application/xml:
                            schema:
                              type: integer
                              format: int32
                      responses:
                        "405":
                          description: Invalid input
                  /pet:
                    put:
                      summary: Update an existing pet
                      operationId: updatePet
                      requestBody:
                        description: Pet object that needs to be added to the store
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Pet"
                        required: true
                      responses:
                        "400":
                          description: Invalid ID supplied
                        "404":
                          description: Pet not found
                        "405":
                          description: Validation exception
                    post:
                      summary: Add a new pet to the store
                      operationId: addPet
                      requestBody:
                        description: Pet object that needs to be added to the store
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Pet"
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/Pet"
                        required: true
                      responses:
                        "405":
                          description: Invalid input
                  /pet/findByStatus:
                    get:
                      summary: Finds Pets by status
                      description: Multiple status values can be provided with comma separated strings
                      operationId: findPetsByStatus
                      parameters:
                      - name: status
                        in: query
                        description: Status values that need to be considered for filter
                        required: true
                        schema:
                          type: string
                      - name: skip
                        in: query
                        schema:
                          type: integer
                          format: int32
                      - name: limit
                        in: query
                        schema:
                          type: integer
                          format: int32
                      responses:
                        default:
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Pet"
                        "400":
                          description: Invalid status value
                components:
                  schemas:
                    Category:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        name:
                          type: string
                      xml:
                        name: Category
                    Tag:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        name:
                          type: string
                      xml:
                        name: Tag
                    Pet:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        category:
                          $ref: "#/components/schemas/Category"
                        name:
                          type: string
                        photoUrls:
                          type: array
                          xml:
                            wrapped: true
                          items:
                            type: string
                            xml:
                              name: photoUrl
                        tags:
                          type: array
                          xml:
                            wrapped: true
                          items:
                            $ref: "#/components/schemas/Tag"
                        status:
                          type: string
                          description: pet status in the store
                          enum:
                          - available,pending,sold
                      xml:
                        name: Pet""";

        compareAsYaml(PetResource.class, expectedYAML);
        compareAsYaml(PetResourceSlashesinPath.class, expectedYAML);

    }

    @Test(description = "reads the resource with generic response from sample")
    public void testGenericResponseResource() throws IOException {
        String yaml = """
                openapi: 3.0.1
                paths:
                  /:
                    get:
                      summary: Returns a list of somethings
                      operationId: getSomethings
                      responses:
                        default:
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/SomethingResponse"
                  /overridden:
                    get:
                      summary: Returns a list of somethings
                      operationId: getSomethingsOverridden
                      responses:
                        default:
                          content:
                            '*/*':
                              schema:
                                $ref: "#/components/schemas/Something"
                components:
                  schemas:
                    SomethingResponse:
                      type: object
                      properties:
                        data:
                          $ref: "#/components/schemas/DataSomething"
                    DataSomething:
                      type: object
                      properties:
                        items:
                          type: array
                          items:
                            $ref: "#/components/schemas/Something"
                    Something:
                      type: object
                      properties:
                        id:
                          type: string
                """;
        compareAsYaml(GenericResponsesResource.class, yaml);
    }

    @Test(description = "reads the user resource from sample")
    public void testCompleteUserResource() throws IOException {
        String expectedYAML = """
                openapi: 3.0.1
                paths:
                  /user:
                    post:
                      summary: Create user
                      description: This can only be done by the logged in user.
                      operationId: createUser
                      requestBody:
                        description: Created user object
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/User"
                        required: true
                      responses:
                        default:
                          description: default response
                          content:
                            'application/json': {}
                            'application/xml': {}
                  /user/createWithArray:
                    post:
                      summary: Creates list of users with given input array
                      operationId: createUsersWithArrayInput
                      requestBody:
                        description: List of user object
                        content:
                          '*/*':
                            schema:
                              type: array
                              items:
                                $ref: "#/components/schemas/User"
                        required: true
                      responses:
                        default:
                          description: default response
                          content:
                            'application/json': {}
                            'application/xml': {}
                  /user/createWithList:
                    post:
                      summary: Creates list of users with given input array
                      operationId: createUsersWithListInput
                      requestBody:
                        description: List of user object
                        content:
                          '*/*':
                            schema:
                              type: array
                              items:
                                $ref: "#/components/schemas/User"
                        required: true
                      responses:
                        default:
                          description: default response
                          content:
                            'application/json': {}
                            'application/xml': {}
                  /user/{username}:
                    get:
                      summary: Get user by user name
                      operationId: getUserByName
                      parameters:
                      - name: username
                        in: path
                        description: 'The name that needs to be fetched. Use user1 for testing. '
                        required: true
                        schema:
                          type: string
                      responses:
                        default:
                          description: The user
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/User"
                        "400":
                          description: User not found
                    put:
                      summary: Updated user
                      description: This can only be done by the logged in user.
                      operationId: updateUser
                      parameters:
                      - name: username
                        in: path
                        description: name that need to be deleted
                        required: true
                        schema:
                          type: string
                        examples:
                          example2:
                            summary: Summary example 2
                            description: example2
                            value: example2
                            externalValue: external value 2
                          example1:
                            summary: Summary example 1
                            description: example1
                            value: example1
                            externalValue: external value 1
                      requestBody:
                        description: Updated user object
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/User"
                        required: true
                      responses:
                        "200":
                          description: user updated
                        "400":
                          description: Invalid user supplied
                        "404":
                          description: User not found
                    delete:
                      summary: Delete user
                      description: This can only be done by the logged in user.
                      operationId: deleteUser
                      parameters:
                      - name: username
                        in: path
                        description: The name that needs to be deleted
                        required: true
                        schema:
                          type: string
                      responses:
                        "200":
                          description: user deteled
                        "400":
                          description: Invalid username supplied
                        "404":
                          description: User not found
                  /user/login:
                    get:
                      summary: Logs user into the system
                      operationId: loginUser
                      parameters:
                      - name: username
                        in: query
                        description: The user name for login
                        required: true
                        schema:
                          type: string
                      - name: password
                        in: query
                        description: The password for login in clear text
                        required: true
                        schema:
                          type: string
                      responses:
                        default:
                          description: Successfully logged in
                          content:
                            application/json:
                              schema:
                                type: string
                            application/xml:
                              schema:
                                type: string
                        "400":
                          description: Invalid username/password supplied
                  /user/logout:
                    get:
                      summary: Logs out current logged in user session
                      operationId: logoutUser
                      responses:
                        default:
                          description: default response
                          content:
                            'application/json': {}
                            'application/xml': {}
                components:
                  schemas:
                    User:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        username:
                          type: string
                        firstName:
                          type: string
                        lastName:
                          type: string
                        email:
                          type: string
                        password:
                          type: string
                        phone:
                          type: string
                        userStatus:
                          type: integer
                          description: User Status
                          format: int32
                      xml:
                        name: User""";
        compareAsYaml(UserResource.class, expectedYAML);
    }

    @Test(description = "reads the simple user resource from sample")
    public void testSimpleUserResource() throws IOException {

        String expectedYAML = """
                openapi: 3.0.1
                paths:
                  /user:
                    post:
                      summary: Create user
                      description: This can only be done by the logged in user.
                      operationId: createUser
                      requestBody:
                        description: Created user object
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/User"
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/User"
                        required: true
                      responses:
                        default:
                          description: default response
                          content:
                            'application/json': {}
                            'application/xml': {}
                  /user/createUserWithReturnType:
                    post:
                      summary: Create user with return type
                      description: This can only be done by the logged in user.
                      operationId: createUserWithReturnType
                      requestBody:
                        description: Created user object
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/User"
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/User"
                        required: true
                      responses:
                        default:
                          description: default response
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/User"
                            application/xml:
                              schema:
                                $ref: "#/components/schemas/User"
                  /user/createUserWithResponseAnnotation:
                    post:
                      summary: Create user with response annotation
                      description: This can only be done by the logged in user.
                      operationId: createUserWithResponseAnnotation
                      requestBody:
                        description: Created user object
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/User"
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/User"
                        required: true
                      responses:
                        "200":
                          description: aaa
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/User"
                            application/xml:
                              schema:
                                $ref: "#/components/schemas/User"
                  /user/createUserWithReturnTypeAndResponseAnnotation:
                    post:
                      summary: Create user with return type and response annotation
                      description: This can only be done by the logged in user.
                      operationId: createUserWithReturnTypeAndResponseAnnotation
                      requestBody:
                        description: Created user object
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/User"
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/User"
                        required: true
                      responses:
                        "200":
                          description: aaa
                components:
                  schemas:
                    User:
                      type: object
                      properties:
                        id:
                          type: integer
                          format: int64
                        username:
                          type: string
                        firstName:
                          type: string
                        lastName:
                          type: string
                        email:
                          type: string
                        password:
                          type: string
                        phone:
                          type: string
                        userStatus:
                          type: integer
                          description: User Status
                          format: int32
                      xml:
                        name: User""";
        compareAsYaml(SimpleUserResource.class, expectedYAML);
    }

    @Test(description = "reads and skips the hidden user resource")
    public void testHiddenUserResource() {
        String openApiYAML = readIntoYaml(HiddenUserResource.class);
        assertEquals(openApiYAML, "openapi: 3.0.1\n");
    }

    @Test(description = "reads and skips the hidden user resource")
    public void testHiddenAnnotatedUserResource() throws IOException {
        String openApiYAML = readIntoYaml(HiddenAnnotatedUserResource.class);
        assertEquals(openApiYAML, "openapi: 3.0.1\n");
        compareAsYaml(HiddenAnnotatedUserResource.HiddenAnnotatedUserResourceMethodAndData.class, """
                openapi: 3.0.1
                paths:
                  /user/2:
                    post:
                      summary: Create user
                      description: This can only be done by the logged in user.
                      operationId: createUserWithHiddenBeanProperty
                      requestBody:
                        description: Created user object
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/UserResourceBean"
                        required: true
                      responses:
                        default:
                          description: default response
                          content:
                            'application/json': {}
                            'application/xml': {}
                components:
                  schemas:
                    UserResourceBean:
                      type: object
                      properties:
                        foo:
                          type: string""");

    }

    @Test
    public void testSimpleGetOperationWithSecurity() {

        String openApiYAML = readIntoYaml(SimpleGetOperationWithSecurity.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex
                      operationId: getWithNoParameters
                      responses:
                        "200":
                          description: voila!
                      security:
                      - petstore-auth:
                        - write:pets""";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(expectedYAML, extractedYAML);
    }

    static class SimpleGetOperationWithSecurity {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                },
                security = @SecurityRequirement(
                        name = "petstore-auth",
                        scopes = "write:pets"))
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testSimpleGetOperationWithMultipleSecurity() {

        String openApiYAML = readIntoYaml(SimpleGetOperationWithMultipleSecurityScopes.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex
                      operationId: getWithNoParameters
                      responses:
                        "200":
                          description: voila!
                      security:
                      - petstore-auth:
                        - write:pets
                        - read:pets""";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetOperationWithMultipleSecurityScopes {
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                },
                security = @SecurityRequirement(
                        name = "petstore-auth",
                        scopes = {"write:pets", "read:pets"}))
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testSimpleGetOperationWithMultipleSecurityAnnotations() {

        String openApiYAML = readIntoYaml(SimpleGetOperationWithMultipleSecurityAnnotations.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = """
                get:
                      summary: Simple get operation
                      description: Defines a simple get operation with no inputs and a complex
                      operationId: getWithNoParameters
                      responses:
                        "200":
                          description: voila!
                      security:
                      - review-auth:
                        - write:review
                      - petstore-auth:
                        - write:pets
                        - read:pets
                      - api_key: []""";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleGetOperationWithMultipleSecurityAnnotations {
        @SecurityRequirement(name = "review-auth", scopes = {"write:review"})
        @Operation(
                summary = "Simple get operation",
                description = "Defines a simple get operation with no inputs and a complex",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                },
                security = {@SecurityRequirement(
                        name = "petstore-auth",
                        scopes = {"write:pets", "read:pets"}),
                        @SecurityRequirement(
                                name = "api_key",
                                scopes = {}),})
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }

    @Test
    public void testSimpleDeprecatedGetOperation() {
        String openApiYAML = readIntoYaml(SimpleDeprecatedGetOperationTest.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = """
                get:
                      summary: Deprecated get operation
                      description: Defines a deprecated get operation with no inputs and a complex
                      operationId: getWithNoParameters
                      responses:
                        "200":
                          description: voila!
                      deprecated: true""";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleDeprecatedGetOperationTest {
        @Operation(
                summary = "Deprecated get operation",
                description = "Defines a deprecated get operation with no inputs and a complex",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                }
        )
        @GET
        @Path("/path")
        @Deprecated
        public void deprecatedGet() {
        }
    }

    @Test
    public void testSimpleGetOperationInDeprecatedClass() {
        String openApiYAML = readIntoYaml(DeprecatedSimpleGetOperationTest.class);
        int start = openApiYAML.indexOf("get:");
        int end = openApiYAML.length() - 1;

        String expectedYAML = """
                get:
                      summary: Simple get operation in deprecated class
                      description: Defines a simple get operation in a deprecated class with no inputs
                      operationId: getWithNoParameters
                      responses:
                        "200":
                          description: voila!
                      deprecated: true""";
        String extractedYAML = openApiYAML.substring(start, end);

        assertEquals(extractedYAML, expectedYAML);
    }

    @Deprecated
    static class DeprecatedSimpleGetOperationTest {
        @Operation(
                summary = "Simple get operation in deprecated class",
                description = "Defines a simple get operation in a deprecated class with no inputs",
                operationId = "getWithNoParameters",
                responses = {
                        @ApiResponse(
                                responseCode = "200",
                                description = "voila!")
                }
        )
        @GET
        @Path("/path")
        public void simpleGet() {
        }
    }
}
