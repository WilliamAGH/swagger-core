package io.swagger.v3.jaxrs2.annotations.definition;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.testng.annotations.Test;

import java.io.IOException;

public class OpenApiDefinitionTest extends AbstractAnnotationTest {

    @Test
    public void testSimpleInfoGet() throws IOException {

        String expectedYAML = """
                openapi: 3.0.1
                info:
                  title: the title
                  description: My API
                  contact:
                    name: Fred
                    url: http://gigantic-server.com
                    email: Fred@gigagantic-server.com
                  license:
                    name: Apache 2.0
                    url: http://foo.bar
                  version: "0.0"
                externalDocs:
                  description: definition docs desc
                servers:
                - url: http://foo
                  description: server 1
                  variables:
                    var1:
                      description: var 1
                      enum:
                      - "1"
                      - "2"
                      default: "1"
                    var2:
                      description: var 2
                      enum:
                      - "1"
                      - "2"
                      default: "1"
                security:
                - req 1:
                  - a
                  - b
                - req 2:
                  - b
                  - c
                tags:
                - name: Tag 1
                  description: desc 1
                  externalDocs:
                    description: docs desc
                - name: Tag 2
                  description: desc 2
                  externalDocs:
                    description: docs desc 2
                - name: Tag 3
                """;

        compareAsYaml(OpenApiDefinitionTest.ClassWithAnnotation.class, expectedYAML);
    }

    @OpenAPIDefinition(
            info = @Info(
                    title = "the title",
                    version = "0.0",
                    description = "My API",
                    license = @License(name = "Apache 2.0", url = "http://foo.bar"),
                    contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
            ),
            tags = {
                    @Tag(name = "Tag 1", description = "desc 1", externalDocs = @ExternalDocumentation(description = "docs desc")),
                    @Tag(name = "Tag 2", description = "desc 2", externalDocs = @ExternalDocumentation(description = "docs desc 2")),
                    @Tag(name = "Tag 3")
            },
            externalDocs = @ExternalDocumentation(description = "definition docs desc"),
            security = {
                    @SecurityRequirement(name = "req 1", scopes = {"a", "b"}),
                    @SecurityRequirement(name = "req 2", scopes = {"b", "c"})
            },
            servers = {
                    @Server(
                            description = "server 1",
                            url = "http://foo",
                            variables = {
                                    @ServerVariable(name = "var1", description = "var 1", defaultValue = "1", allowableValues = {"1", "2"}),
                                    @ServerVariable(name = "var2", description = "var 2", defaultValue = "1", allowableValues = {"1", "2"})
                            })
            }
    )
    static class ClassWithAnnotation {

        public void foo() {
        }

    }

    @Test
    public void testServerVariableWithoutEnum() throws IOException {

        String expectedYAML = """
                openapi: 3.0.1
                info:
                  title: My Rest API
                  description: My RESTful API implementation
                  version: 1.0.0
                servers:
                - url: /{context-path}/{rest-api}
                  description: My REST API
                  variables:
                    context-path:
                      default: my-war
                    rest-api:
                      enum:
                      - api
                      - rest
                      - batchapi
                      default: api
                """;

        compareAsYaml(OpenApiDefinitionTest.ServerVariableWithoutEnum.class, expectedYAML);
    }

    @OpenAPIDefinition(
            info = @Info( title = "My Rest API", version = "1.0.0", description = "My RESTful API implementation" ),
            servers = {
                    @Server( description = "My REST API",
                            url = "/{context-path}/{rest-api}",
                            variables = {
                            @ServerVariable(name = "context-path", defaultValue = "my-war"),
                                    @ServerVariable(name = "rest-api", defaultValue = "api",
                                            allowableValues = {"api", "rest", "batchapi"}) } ) } )
    static class ServerVariableWithoutEnum {

        public void foo() {
        }

    }

}