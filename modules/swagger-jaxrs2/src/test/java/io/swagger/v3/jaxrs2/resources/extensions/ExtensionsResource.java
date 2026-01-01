package io.swagger.v3.jaxrs2.resources.extensions;

import io.swagger.v3.jaxrs2.resources.model.ExtensionUser;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@OpenAPIDefinition(
        extensions = {
                @Extension(name = "x-openapi", properties = {
                        @ExtensionProperty(name = "name", value = "Josh")}),
                @Extension(name = "openapi-extensions", properties = {
                        @ExtensionProperty(name = "lastName", value = "Hart"),
                        @ExtensionProperty(name = "address", value = "House")})
        },
        info = @Info(
                extensions = {
                        @Extension(name = "x-info", properties = {
                                @ExtensionProperty(name = "name", value = "Josh")}),
                        @Extension(name = "info-extensions", properties = {
                                @ExtensionProperty(name = "lastName", value = "Hart"),
                                @ExtensionProperty(name = "address", value = "House")})
                },
                contact = @Contact(
                        extensions = {
                                @Extension(name = "x-contact", properties = {
                                        @ExtensionProperty(name = "name", value = "Josh")}),
                                @Extension(name = "contact-extensions", properties = {
                                        @ExtensionProperty(name = "lastName", value = "Hart"),
                                        @ExtensionProperty(name = "address", value = "House")})
                        }
                ),
                license = @License(
                        extensions = {
                                @Extension(name = "x-license", properties = {
                                        @ExtensionProperty(name = "name", value = "Josh")}),
                                @Extension(name = "license-extensions", properties = {
                                        @ExtensionProperty(name = "lastName", value = "Hart"),
                                        @ExtensionProperty(name = "address", value = "House")})
                        }
                )
        ),
        servers = @Server(
                extensions = {
                        @Extension(name = "x-server", properties = {
                                @ExtensionProperty(name = "name", value = "Josh")}),
                        @Extension(name = "server-extensions", properties = {
                                @ExtensionProperty(name = "lastName", value = "Hart"),
                                @ExtensionProperty(name = "address", value = "House")})
                },
                variables = @ServerVariable(
                        name = "aa",
                        defaultValue = "aa",
                        extensions = {
                                @Extension(name = "x-servervar", properties = {
                                        @ExtensionProperty(name = "name", value = "Josh")}),
                                @Extension(name = "servervar-extensions", properties = {
                                        @ExtensionProperty(name = "lastName", value = "Hart"),
                                        @ExtensionProperty(name = "address", value = "House")})
                        }
                )
        ),
        externalDocs = @ExternalDocumentation(
                extensions = {
                        @Extension(name = "x-externalDocs", properties = {
                                @ExtensionProperty(name = "name", value = "Josh")}),
                        @Extension(name = "externalDocs-extensions", properties = {
                                @ExtensionProperty(name = "lastName", value = "Hart"),
                                @ExtensionProperty(name = "address", value = "House")})
                }

        )
)
@SecurityScheme(name = "myOauth2Security",
        type = SecuritySchemeType.OAUTH2,
        in = SecuritySchemeIn.HEADER,
        description = "myOauthSecurity Description",
        flows = @OAuthFlows(
                implicit = @OAuthFlow(
                        authorizationUrl = "http://x.com",
                        scopes = @OAuthScope(
                                name = "write:pets",
                                description = "modify pets in your account"
                        ),
                        extensions = {
                                @Extension(name = "x-oauthflow", properties = {
                                        @ExtensionProperty(name = "name", value = "Josh")}),
                                @Extension(name = "oauthflow-extensions", properties = {
                                        @ExtensionProperty(name = "lastName", value = "Hart"),
                                        @ExtensionProperty(name = "address", value = "House")})
                        }
                ),
                extensions = {
                        @Extension(name = "x-oauthflows", properties = {
                                @ExtensionProperty(name = "name", value = "Josh")}),
                        @Extension(name = "oauthflows-extensions", properties = {
                                @ExtensionProperty(name = "lastName", value = "Hart"),
                                @ExtensionProperty(name = "address", value = "House")})
                }
        ),
        extensions = {
                @Extension(name = "x-security", properties = {
                        @ExtensionProperty(name = "name", value = "Josh")}),
                @Extension(name = "security-extensions", properties = {
                        @ExtensionProperty(name = "lastName", value = "Hart"),
                        @ExtensionProperty(name = "address", value = "House")})
        }
)
@SecurityRequirement(name = "security_key",
        scopes = {"write:pets", "read:pets"}
)
@SecurityRequirement(name = "myOauth2Security",
        scopes = {"write:pets"}
)
public class ExtensionsResource {

    @GET
    @Path("/")
    @Tag(
            name = "MyTag",
            extensions = {
                    @Extension(name = "x-tag", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "tag-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
            }
    )
    @Operation(operationId = "operationId",
            summary = "Operation Summary",
            description = "Operation Description",
            extensions = {
                    @Extension(name = "x-operation", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "x-operation-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")}),
                    @Extension(properties = {
                            @ExtensionProperty(name = "codes", value = "[\"11\", \"12\"]", parseValue = true),
                            @ExtensionProperty(name = "name", value = "Josh")})
            })
    public Response getSummaryAndDescription(
            @Parameter(
                    extensions = @Extension(
                            name = "x-parameter",
                            properties = {
                                    @ExtensionProperty(name = "parameter", value = "value")
                            }
                    ),
                    examples = {
                            @ExampleObject(
                                    name = "example1",
                                    value = "example1",
                                    summary = "Summary example 1",
                                    externalValue = "external value 1",
                                    extensions = {
                                            @Extension(name = "x-examples", properties = {
                                                    @ExtensionProperty(name = "name", value = "Josh")}),
                                            @Extension(name = "examples-extensions", properties = {
                                                    @ExtensionProperty(name = "lastName", value = "Hart"),
                                                    @ExtensionProperty(name = "address", value = "House")})
                                    }
                            )
                    }
            )
            @QueryParam("subscriptionId") String subscriptionId) {

        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/user")
    @Operation(
            operationId = "getUser"
    )
    @Callback(
            callbackUrlExpression = "http://$request.query.url",
            name = "subscription",
            extensions = {
                    @Extension(name = "x-callback", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "callback-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
            },
            operation = {
                    @Operation(
                            method = "post",
                            description = "payload data will be sent",
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true, schema = @Schema(
                                            type = "string",
                                            format = "uuid",
                                            description = "the generated UUID"))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Return this code if the callback was received and processed successfully"
                                    ),
                                    @ApiResponse(
                                            responseCode = "205",
                                            description = "Return this code to unsubscribe from future data updates"
                                    ),
                                    @ApiResponse(
                                            responseCode = "default",
                                            description = "All other response codes will disable this callback subscription"
                                    )
                            }),
                    @Operation(
                            method = "get",
                            description = "payload data will be received"
                    ),
                    @Operation(
                            method = "put",
                            description = "payload data will be sent"
                    )})
    public void getUser(
            //@Parameter(description = "Parameter with no IN", required = true)
            @RequestBody(
                    description = "Request Body in Param",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    extensions = {
                                            @Extension(name = "x-schema", properties = {
                                                    @ExtensionProperty(name = "name", value = "Josh")}),
                                            @Extension(name = "schema-extensions", properties = {
                                                    @ExtensionProperty(name = "lastName", value = "Hart"),
                                                    @ExtensionProperty(name = "address", value = "House")})
                                    }
                            ),
                            extensions = {
                                    @Extension(name = "x-content", properties = {
                                            @ExtensionProperty(name = "name", value = "Josh")}),
                                    @Extension(name = "content-extensions", properties = {
                                            @ExtensionProperty(name = "lastName", value = "Hart"),
                                            @ExtensionProperty(name = "address", value = "House")})
                            },
                            encoding = @Encoding(
                                    name = "application/xml",
                                    extensions = {
                                            @Extension(name = "x-encoding", properties = {
                                                    @ExtensionProperty(name = "name", value = "Josh")}),
                                            @Extension(name = "encoding-extensions", properties = {
                                                    @ExtensionProperty(name = "lastName", value = "Hart"),
                                                    @ExtensionProperty(name = "address", value = "House")})
                                    }
                            )
                    ),
                    extensions = {
                            @Extension(name = "x-extension", properties = {
                                    @ExtensionProperty(name = "name", value = "param")}),
                            @Extension(name = "x-extension2", properties = {
                                    @ExtensionProperty(name = "another", value = "val")})})
                    ExtensionUser user) {
    }

    @POST
    @Path("/user")
    @Operation(operationId = "setUser"
    )
    @ApiResponse(
            description = "200",
            content = @Content(
                    extensions = {
                            @Extension(name = "x-content", properties = {
                                    @ExtensionProperty(name = "name", value = "Josh")}),
                            @Extension(name = "content-extensions", properties = {
                                    @ExtensionProperty(name = "lastName", value = "Hart"),
                                    @ExtensionProperty(name = "address", value = "House")})
                    },
                    encoding = @Encoding(
                            extensions = {
                                    @Extension(name = "x-encoding", properties = {
                                            @ExtensionProperty(name = "name", value = "Josh")}),
                                    @Extension(name = "encoding-extensions", properties = {
                                            @ExtensionProperty(name = "lastName", value = "Hart"),
                                            @ExtensionProperty(name = "address", value = "House")})
                            }
                    )
            ),
            links = @Link(
                    name = "aa",
                    description = "aa",
                    operationId = "getUser",
                    extensions = {
                            @Extension(name = "x-links", properties = {
                                    @ExtensionProperty(name = "name", value = "Josh")}),
                            @Extension(name = "links-extensions", properties = {
                                    @ExtensionProperty(name = "lastName", value = "Hart"),
                                    @ExtensionProperty(name = "address", value = "House")})
                    }

            ),
            extensions = {
                    @Extension(name = "x-response", properties = {
                            @ExtensionProperty(name = "name", value = "Josh")}),
                    @Extension(name = "response-extensions", properties = {
                            @ExtensionProperty(name = "lastName", value = "Hart"),
                            @ExtensionProperty(name = "address", value = "House")})
            }
    )
    public ExtensionUser setUser(
            @Parameter(
                    description = "Parameter with no IN",
                    required = true,
                    schema = @Schema(
                            description = "the user",
                            extensions = {
                                    @Extension(name = "x-schema", properties = {
                                            @ExtensionProperty(name = "name", value = "Josh")}),
                                    @Extension(name = "schema-extensions", properties = {
                                            @ExtensionProperty(name = "lastName", value = "Hart"),
                                            @ExtensionProperty(name = "address", value = "House")})
                            }
                    )
            )
            @RequestBody(description = "Request Body in Param",
                    extensions = {
                            @Extension(name = "x-extension", properties = {
                                    @ExtensionProperty(name = "name", value = "param")}),
                            @Extension(name = "x-extension2", properties = {
                                    @ExtensionProperty(name = "another", value = "val")})})
                    ExtensionUser user) {
        return null;
    }


    public static final String YAML =
            """
            openapi: 3.0.1
            info:
              contact:
                x-contact:
                  name: Josh
                x-contact-extensions:
                  lastName: Hart
                  address: House
              license:
                x-license:
                  name: Josh
                x-license-extensions:
                  lastName: Hart
                  address: House
              x-info:
                name: Josh
              x-info-extensions:
                lastName: Hart
                address: House
            externalDocs:
              x-externalDocs:
                name: Josh
              x-externalDocs-extensions:
                lastName: Hart
                address: House
            servers:
            - variables:
                aa:
                  default: aa
                  x-servervar:
                    name: Josh
                  x-servervar-extensions:
                    lastName: Hart
                    address: House
              x-server-extensions:
                lastName: Hart
                address: House
              x-server:
                name: Josh
            paths:
              /:
                get:
                  tags:
                  - MyTag
                  summary: Operation Summary
                  description: Operation Description
                  operationId: operationId
                  parameters:
                  - name: subscriptionId
                    in: query
                    schema:
                      type: string
                    examples:
                      example1:
                        summary: Summary example 1
                        description: example1
                        value: example1
                        externalValue: external value 1
                        x-examples-extensions:
                          lastName: Hart
                          address: House
                        x-examples:
                          name: Josh
                    x-parameter:
                      parameter: value
                  responses:
                    default:
                      description: default response
                      content:
                        '*/*': {}
                  security:
                  - security_key:
                    - write:pets
                    - read:pets
                  - myOauth2Security:
                    - write:pets
                  x-name: Josh
                  x-operation:
                    name: Josh
                  x-operation-extensions:
                    lastName: Hart
                    address: House
                  x-codes:
                  - "11"
                  - "12"
              /user:
                get:
                  operationId: getUser
                  requestBody:
                    description: Request Body in Param
                    content:
                      application/json:
                        schema:
                          type: string
                          x-schema:
                            name: Josh
                          x-schema-extensions:
                            lastName: Hart
                            address: House
                        encoding:
                          application/xml:
                            x-encoding-extensions:
                              lastName: Hart
                              address: House
                            x-encoding:
                              name: Josh
                        x-content:
                          name: Josh
                        x-content-extensions:
                          lastName: Hart
                          address: House
                    x-extension:
                      name: param
                    x-extension2:
                      another: val
                  responses:
                    default:
                      description: default response
                      content:
                        '*/*': {}
                  callbacks:
                    subscription:
                      http://$request.query.url:
                        get:
                          description: payload data will be received
                        put:
                          description: payload data will be sent
                        post:
                          description: payload data will be sent
                          parameters:
                          - name: subscriptionId
                            in: path
                            required: true
                            schema:
                              type: string
                              description: the generated UUID
                              format: uuid
                          responses:
                            "200":
                              description: Return this code if the callback was received and processed
                                successfully
                            "205":
                              description: Return this code to unsubscribe from future data updates
                            default:
                              description: All other response codes will disable this callback
                                subscription
                  security:
                  - security_key:
                    - write:pets
                    - read:pets
                  - myOauth2Security:
                    - write:pets
                post:
                  operationId: setUser
                  requestBody:
                    description: Request Body in Param
                    content:
                      '*/*':
                        schema:
                          $ref: "#/components/schemas/ExtensionUser"
                    x-extension:
                      name: param
                    x-extension2:
                      another: val
                  responses:
                    default:
                      description: "200"
                      content:
                        '*/*':
                          schema:
                            $ref: "#/components/schemas/ExtensionUser"
                          x-content:
                            name: Josh
                          x-content-extensions:
                            lastName: Hart
                            address: House
                      links:
                        aa:
                          operationId: getUser
                          description: aa
                          x-links-extensions:
                            lastName: Hart
                            address: House
                          x-links:
                            name: Josh
                      x-response-extensions:
                        lastName: Hart
                        address: House
                      x-response:
                        name: Josh
                  security:
                  - security_key:
                    - write:pets
                    - read:pets
                  - myOauth2Security:
                    - write:pets
            components:
              schemas:
                ExtensionUser:
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
                      x-userStatus:
                        name: Josh
                      x-userStatus-extensions:
                        lastName: Hart
                        address: House
                  description: User
                  xml:
                    name: User
                  x-user-extensions:
                    lastName: Hart
                    address: House
                  x-user:
                    name: Josh
              securitySchemes:
                myOauth2Security:
                  type: oauth2
                  description: myOauthSecurity Description
                  in: header
                  flows:
                    implicit:
                      authorizationUrl: http://x.com
                      scopes:
                        write:pets: modify pets in your account
                      x-oauthflow-extensions:
                        lastName: Hart
                        address: House
                      x-oauthflow:
                        name: Josh
                    x-oauthflows:
                      name: Josh
                    x-oauthflows-extensions:
                      lastName: Hart
                      address: House
                  x-security:
                    name: Josh
                  x-security-extensions:
                    lastName: Hart
                    address: House
            x-openapi:
              name: Josh
            x-openapi-extensions:
              lastName: Hart
              address: House""";

}
