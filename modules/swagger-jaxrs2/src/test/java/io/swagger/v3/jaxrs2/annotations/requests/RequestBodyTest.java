package io.swagger.v3.jaxrs2.annotations.requests;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.it.resources.MultiPartFileResource;
import io.swagger.v3.jaxrs2.it.resources.OctetStreamResource;
import io.swagger.v3.jaxrs2.it.resources.UrlEncodedResource;
import io.swagger.v3.jaxrs2.resources.model.Pet;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class RequestBodyTest extends AbstractAnnotationTest {

    private static final String REQUEST_BODY_IN_METHOD = "RequestBody in Method";
    private static final String REQUEST_BODY_IN_PARAMETER = "Request Body in Param";
    private static final String NO_IN_PARAMETER = "Parameter with no IN";
    private static final String REQUEST_BODY_IN_ANNOTATION = "RequestBody in Annotation";
    private static final String USER_PATH = "/user";

    @Test(description = "Returns a request with one RequestBody and multiple parameters")
    public void oneRequestBodyMultipleParameters() throws IOException {

        String expectedYAML = """
                openapi: 3.0.1
                paths:
                  /user:
                    put:
                      summary: Modify user
                      description: Modifying user.
                      operationId: methodWithRequestBodyWithoutAnnotation
                      requestBody:
                        content:
                          '*/*':
                            schema:
                              $ref: "#/components/schemas/User"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                    post:
                      summary: Create user
                      description: This can only be done by the logged in user.
                      operationId: methodWithRequestBodyAndTwoParameters
                      parameters:
                      - name: name
                        in: query
                        schema:
                          type: string
                      - name: code
                        in: query
                        schema:
                          type: string
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
                            '*/*': {}
                    delete:
                      summary: Delete user
                      description: This can only be done by the logged in user.
                      operationId: methodWithoutRequestBodyAndTwoParameters
                      parameters:
                      - name: name
                        in: query
                        schema:
                          type: string
                      - name: code
                        in: query
                        schema:
                          type: string
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                  /pet:
                    put:
                      summary: Modify pet
                      description: Modifying pet.
                      operationId: methodWithRequestBodyWithoutAnnotationAndTwoConsumes
                      requestBody:
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/User"
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/User"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                    post:
                      summary: Create pet
                      description: Creating pet.
                      operationId: methodWithTwoRequestBodyWithoutAnnotationAndTwoConsumes
                      requestBody:
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Pet"
                          application/xml:
                            schema:
                              $ref: "#/components/schemas/Pet"
                      responses:
                        default:
                          description: default response
                          content:
                            '*/*': {}
                    delete:
                        summary: Delete pet
                        description: Deleting pet.
                        operationId: methodWithOneSimpleRequestBody
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
                          default:
                            description: default response
                            content:
                              '*/*': {}
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
                        name: User
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

        compareAsYaml(RequestBodyTest.UserResource.class, expectedYAML);
    }

    static class UserResource {
        @POST
        @Path("/user")
        @Operation(summary = "Create user",
                description = "This can only be done by the logged in user.")
        public Response methodWithRequestBodyAndTwoParameters(
                @RequestBody(description = "Created user object", required = true,
                        content = @Content(
                                schema = @Schema(implementation = User.class))) User user,
                @QueryParam("name") String name, @QueryParam("code") String code) {
            return Response.ok().entity("").build();
        }

        @PUT
        @Path("/user")
        @Operation(summary = "Modify user",
                description = "Modifying user.")
        public Response methodWithRequestBodyWithoutAnnotation(
                User user) {
            return Response.ok().entity("").build();
        }

        @DELETE
        @Path("/user")
        @Operation(summary = "Delete user",
                description = "This can only be done by the logged in user.")
        public Response methodWithoutRequestBodyAndTwoParameters(
                @QueryParam("name") String name, @QueryParam("code") String code) {
            return Response.ok().entity("").build();
        }

        @PUT
        @Path("/pet")
        @Operation(summary = "Modify pet",
                description = "Modifying pet.")
        @Consumes({"application/json", "application/xml"})
        public Response methodWithRequestBodyWithoutAnnotationAndTwoConsumes(
                User user) {
            return Response.ok().entity("").build();
        }

        @POST
        @Path("/pet")
        @Operation(summary = "Create pet",
                description = "Creating pet.")
        @Consumes({"application/json", "application/xml"})
        public Response methodWithTwoRequestBodyWithoutAnnotationAndTwoConsumes(
                Pet pet, User user) {
            return Response.ok().entity("").build();
        }

        @DELETE
        @Path("/pet")
        @Operation(summary = "Delete pet",
                description = "Deleting pet.")
        @Consumes({"application/json", "application/xml"})
        public Response methodWithOneSimpleRequestBody(int id) {
            return Response.ok().entity("").build();
        }
    }

    @Test(description = "scan class with requesbody annotation")
    public void testRequestBodyAnnotationPriority() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(RequestBodyResource.class);
        PathItem userPathItem = openAPI.getPaths().get(USER_PATH);
        io.swagger.v3.oas.models.parameters.RequestBody getRequestBody = userPathItem.getGet().getRequestBody();
        assertNotNull(getRequestBody);
        assertEquals(getRequestBody.getDescription(), REQUEST_BODY_IN_ANNOTATION);
        io.swagger.v3.oas.models.parameters.RequestBody postRequestBody = userPathItem.getPost().getRequestBody();
        assertNotNull(postRequestBody);
        assertEquals(postRequestBody.getDescription(), REQUEST_BODY_IN_ANNOTATION);
        io.swagger.v3.oas.models.parameters.RequestBody putRequestBody = userPathItem.getPut().getRequestBody();
        assertNotNull(putRequestBody);
        assertEquals(putRequestBody.getDescription(), REQUEST_BODY_IN_METHOD);
        io.swagger.v3.oas.models.parameters.RequestBody deleteRequestBody = userPathItem.getDelete().getRequestBody();
        assertNotNull(deleteRequestBody);
        assertEquals(deleteRequestBody.getDescription(), REQUEST_BODY_IN_METHOD);
        io.swagger.v3.oas.models.parameters.RequestBody patchRequestBody = userPathItem.getPatch().getRequestBody();
        assertNotNull(patchRequestBody);
        assertEquals(patchRequestBody.getDescription(), REQUEST_BODY_IN_METHOD);

        userPathItem = openAPI.getPaths().get(USER_PATH + "/deleteUserMethod_Param_RequestBody");
        deleteRequestBody = userPathItem.getDelete().getRequestBody();
        assertNotNull(deleteRequestBody);
        assertEquals(deleteRequestBody.getDescription(), REQUEST_BODY_IN_PARAMETER);

        userPathItem = openAPI.getPaths().get(USER_PATH + "/deleteUserOperation_Method_Param_RequestBody");
        deleteRequestBody = userPathItem.getDelete().getRequestBody();
        assertNotNull(deleteRequestBody);
        assertEquals(deleteRequestBody.getDescription(), REQUEST_BODY_IN_PARAMETER);

        userPathItem = openAPI.getPaths().get(USER_PATH + "/deleteUserOperation_RequestBody");
        deleteRequestBody = userPathItem.getDelete().getRequestBody();
        assertNotNull(deleteRequestBody);
        assertEquals(deleteRequestBody.getDescription(), REQUEST_BODY_IN_PARAMETER);

        userPathItem = openAPI.getPaths().get(USER_PATH + "/deleteUserOperation_Method_Param");
        deleteRequestBody = userPathItem.getDelete().getRequestBody();
        assertNotNull(deleteRequestBody);
        assertEquals(deleteRequestBody.getDescription(), REQUEST_BODY_IN_METHOD);

    }

    @Path("/user")
    static class RequestBodyResource {
        @GET
        @Operation(requestBody = @RequestBody(description = "RequestBody in Annotation", required = true,
                content = @Content(schema = @Schema(implementation = User.class))))
        public User getUser() {
            return new User();
        }

        @POST
        @Operation(summary = "Create user",
                description = "This can only be done by the logged in user.",
                requestBody = @RequestBody(description = "RequestBody in Annotation", required = true,
                        content = @Content(schema = @Schema(implementation = User.class))))
        public Response createUser(
                @Parameter(description = "Parameter with no IN", required = true) User user) {
            return Response.ok().entity("").build();
        }

        @PUT
        @Operation(summary = "Update user",
                description = "This can only be done by the logged in user.")
        @RequestBody(description = "RequestBody in Method", required = true,
                content = @Content(schema = @Schema(implementation = User.class)))
        public Response updateUser() {
            return Response.ok().entity("").build();
        }

        @DELETE
        @Operation(summary = "Delete user",
                description = "This can only be done by the logged in user.")
        @RequestBody(description = "RequestBody in Method", required = true,
                content = @Content(schema = @Schema(implementation = User.class)))
        public Response deleteUser(@Parameter(description = "Parameter with no IN", required = true) User user) {
            return Response.ok().entity("").build();
        }

        @DELETE
        @Path("/deleteUserMethod_Param_RequestBody")
        @Operation(summary = "Delete user",
                description = "This can only be done by the logged in user.")
        @RequestBody(description = "RequestBody in Method", required = true,
                content = @Content(schema = @Schema(implementation = User.class)))
        public Response deleteUserMethod_Param_RequestBody(
                @Parameter(description = "Parameter with no IN", required = true)
                @RequestBody(description = "Request Body in Param") User user) {
            return Response.ok().entity("").build();
        }

        @DELETE
        @Path("/deleteUserOperation_Method_Param_RequestBody")
        @Operation(summary = "Delete user",
                description = "This can only be done by the logged in user.",
                requestBody = @RequestBody(description = "RequestBody in Annotation", required = true,
                        content = @Content(schema = @Schema(implementation = User.class))))
        @RequestBody(description = "RequestBody in Method", required = true,
                content = @Content(schema = @Schema(implementation = User.class)))
        public Response deleteUserOperation_Method_Param_RequestBody(
                @Parameter(description = "Parameter with no IN", required = true)
                @RequestBody(description = "Request Body in Param") User user) {
            return Response.ok().entity("").build();
        }

        @DELETE
        @Path("/deleteUserOperation_Method_Param")
        @Operation(summary = "Delete user",
                description = "This can only be done by the logged in user.",
                requestBody = @RequestBody(description = "RequestBody in Annotation", required = true,
                        content = @Content(schema = @Schema(implementation = User.class))))
        @RequestBody(description = "RequestBody in Method", required = true,
                content = @Content(schema = @Schema(implementation = User.class)))
        public Response deleteUserOperation_Method_Param(
                @Parameter(description = "Parameter with no IN", required = true) User user) {
            return Response.ok().entity("").build();
        }

        @DELETE
        @Path("/deleteUserOperation_RequestBody")
        @Operation(summary = "Delete user",
                description = "This can only be done by the logged in user.",
                requestBody = @RequestBody(description = "RequestBody in Annotation", required = true,
                        content = @Content(schema = @Schema(implementation = User.class))))
        public Response deleteUserOperation_RequestBody(
                @RequestBody(description = "Request Body in Param") User user) {
            return Response.ok().entity("").build();
        }

        @PATCH
        @Operation(summary = "Modify user",
                description = "This can only be done by the logged in user.",
                requestBody = @RequestBody(description = "RequestBody in Annotation", required = true,
                        content = @Content(schema = @Schema(implementation = User.class))))
        @RequestBody(description = "RequestBody in Method", required = true,
                content = @Content(schema = @Schema(implementation = User.class)))
        public Response modifyUser() {
            return Response.ok().entity("").build();
        }

    }

    @Test(description = "Test file upload")
    public void testFileUploadOctetStream() throws IOException {

        String expectedYAML = """
                openapi: 3.0.1
                paths:
                  /files/attach:
                    put:
                      operationId: putFile
                      requestBody:
                        content:
                          application/octet-stream:
                            schema:
                              type: string
                              format: binary
                      responses:
                        default:
                          description: default response
                          content:
                            application/json: {}\
                """;

        compareAsYaml(OctetStreamResource.class, expectedYAML);
    }

    @Test(description = "Test urlencoded")
    public void testUrlEncoded() throws IOException {

        String expectedYAML = """
                openapi: 3.0.1
                paths:
                  /users/add:
                    post:
                      operationId: addUser
                      requestBody:
                        content:
                          application/x-www-form-urlencoded:
                            schema:
                              type: object
                              properties:
                                id:
                                  type: string
                                name:
                                  type: string
                                gender:
                                  type: string
                      responses:
                        default:
                          description: default response
                          content:
                            application/json: {}\
                """;

        compareAsYaml(UrlEncodedResource.class, expectedYAML);
    }

    @Test(description = "Test multipart")
    public void testMultiPart() throws IOException {

        String expectedYAML = """
                openapi: 3.0.1
                paths:
                  /files/upload:
                    post:
                      operationId: uploadFile
                      requestBody:
                        content:
                          multipart/form-data:
                            schema:
                              type: object
                              properties:
                                fileIdRenamed:
                                  type: string
                                fileRenamed:
                                  type: string
                                  format: binary
                      responses:
                        default:
                          description: default response
                          content:
                            application/json: {}
                """;

        compareAsYaml(MultiPartFileResource.class, expectedYAML);
    }

}
