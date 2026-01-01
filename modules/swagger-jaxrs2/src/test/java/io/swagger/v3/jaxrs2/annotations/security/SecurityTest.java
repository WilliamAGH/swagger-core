package io.swagger.v3.jaxrs2.annotations.security;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.resources.SecurityResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class SecurityTest extends AbstractAnnotationTest {
    @Test
    public void testSecuritySheme() {
        String openApiYAML = readIntoYaml(SecurityTest.OAuth2SchemeOnClass.class);
        int start = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, openApiYAML.length() - 1);
        String expectedYAML = """
                components:
                  securitySchemes:
                    myOauth2Security:
                      type: oauth2
                      in: header
                      flows:
                        implicit:
                          authorizationUrl: http://url.com/auth
                          scopes:
                            write:pets: modify pets in your account""";
        assertEquals(extractedYAML, expectedYAML);

    }

    @Test
    public void testSecurityRequirement() throws IOException {
        String expectedYAML = """
                openapi: 3.0.1
                paths:
                  /2:
                    get:
                      description: description 2
                      operationId: Operation Id 2
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
                      - security_key2:
                        - write:pets
                        - read:pets
                  /:
                    get:
                      description: description
                      operationId: Operation Id
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
                components:
                  securitySchemes:
                    myOauth2Security:
                      type: oauth2
                      description: myOauthSecurity Description
                      in: header
                      flows:
                        implicit:
                          authorizationUrl: http://x.com
                          scopes:
                            write:pets: modify pets in your account""";
        compareAsYaml(SecurityResource.class, expectedYAML);

    }

    @Test
    public void testMultipleSecurityShemes() {
        String openApiYAML = readIntoYaml(SecurityTest.MultipleSchemesOnClass.class);
        int start = openApiYAML.indexOf("components:");
        String extractedYAML = openApiYAML.substring(start, openApiYAML.length() - 1);
        String expectedYAML = """
                components:
                  securitySchemes:
                    apiKey:
                      type: apiKey
                      name: API_KEY
                      in: header
                    myOauth2Security:
                      type: oauth2
                      in: header
                      flows:
                        implicit:
                          authorizationUrl: http://url.com/auth
                          scopes:
                            write:pets: modify pets in your account""";
        assertEquals(extractedYAML, expectedYAML);

    }

    @Test
    public void testTicket2767() {
        String openApiYAML = readIntoYaml(SecurityTest.Ticket2767.class);
        String expectedYAML = """
                openapi: 3.0.1
                info:
                  title: Test
                  version: 1.0-SNAPSHOT
                security:
                - basicAuth: []
                components:
                  securitySchemes:
                    basicAuth:
                      type: http
                      scheme: basic
                """;
        assertEquals(openApiYAML, expectedYAML);

    }

    @Test
    public void testTicket2767_2() {
        String openApiYAML = readIntoYaml(SecurityTest.Ticket2767_2.class);
        String expectedYAML = """
                openapi: 3.0.1
                info:
                  title: Test
                  version: 1.0-SNAPSHOT
                security:
                - api_key: []
                components:
                  securitySchemes:
                    api_key:
                      type: apiKey
                      name: API_KEY
                """;
        assertEquals(openApiYAML, expectedYAML);

    }

    @SecurityScheme(name = "myOauth2Security",
            type = SecuritySchemeType.OAUTH2,
            in = SecuritySchemeIn.HEADER,
            flows = @OAuthFlows(
                    implicit = @OAuthFlow(authorizationUrl = "http://url.com/auth",
                            scopes = @OAuthScope(name = "write:pets", description = "modify pets in your account"))))
    static class OAuth2SchemeOnClass {

    }

    @SecurityScheme(name = "myOauth2Security",
            type = SecuritySchemeType.OAUTH2,
            in = SecuritySchemeIn.HEADER,
            flows = @OAuthFlows(
                    implicit = @OAuthFlow(authorizationUrl = "http://url.com/auth",
                            scopes = @OAuthScope(name = "write:pets", description = "modify pets in your account"))))
    @SecurityScheme(name = "apiKey", type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER, paramName = "API_KEY")
    static class MultipleSchemesOnClass {

    }


    @OpenAPIDefinition(
            security = {@SecurityRequirement(name = "basicAuth")},
            info = @Info( title = "Test", description = "", version = "1.0-SNAPSHOT"))
    @SecurityScheme(name="basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
    static class Ticket2767 {

    }

    @OpenAPIDefinition(
            security = {@SecurityRequirement(name = "api_key")},
            info = @Info( title = "Test", description = "", version = "1.0-SNAPSHOT"))
    @SecurityScheme(name="api_key", type = SecuritySchemeType.APIKEY, paramName = "API_KEY")
    static class Ticket2767_2 {

    }
}
