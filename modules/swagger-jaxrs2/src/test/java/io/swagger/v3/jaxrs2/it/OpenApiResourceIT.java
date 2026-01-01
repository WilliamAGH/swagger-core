package io.swagger.v3.jaxrs2.it;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;

import io.restassured.http.ContentType;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;

/**
 * <p>
 * An functional integration test that runs during maven's integration-test phase,
 * uses RestAssured to define REST API tests, and Jetty's Maven plugin to serve a simple
 * sample app just prior to the integration-test phase starting.
 */
public class OpenApiResourceIT extends AbstractAnnotationTest {
    private static final String EXPECTED_JSON = """
            {
                "openapi": "3.0.1",
                "paths": {
                    "/cars/all": {
                        "get": {
                            "tags": [
                                "cars"
                            ],
                            "description": "Return whole car",
                            "operationId": "getAll",
                            "responses": {
                                "200": {
                                    "content": {
                                        "application/json": {
                                            "schema": {
                                                "type": "array",
                                                "items": {
                                                    "$ref": "#/components/schemas/Car"
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "/cars/summary": {
                        "get": {
                            "tags": [
                                "cars"
                            ],
                            "description": "Return car summaries",
                            "operationId": "getSummaries",
                            "responses": {
                                "200": {
                                    "content": {
                                        "application/json": {
                                            "schema": {
                                                "type": "array",
                                                "items": {
                                                    "$ref": "#/components/schemas/Car_Summary"
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "/cars/detail": {
                        "get": {
                            "tags": [
                                "cars"
                            ],
                            "description": "Return car detail",
                            "operationId": "getDetails",
                            "responses": {
                                "200": {
                                    "content": {
                                        "application/json": {
                                            "schema": {
                                                "type": "array",
                                                "items": {
                                                    "$ref": "#/components/schemas/Car_Detail"
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "/cars/sale": {
                        "get": {
                            "tags": [
                                "cars"
                            ],
                            "operationId": "getSaleSummaries",
                            "responses": {
                                "default": {
                                    "description": "default response",
                                    "content": {
                                        "application/json": {
                                            "schema": {
                                                "type": "array",
                                                "items": {
                                                    "$ref": "#/components/schemas/Car_Summary-or-Sale"
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "/files/upload": {
                        "post": {
                            "operationId": "uploadFile",
                            "requestBody": {
                                "content": {
                                    "multipart/form-data": {
                                        "schema": {
                                            "type": "object",
                                            "properties": {
                                                "fileIdRenamed": {
                                                    "type": "string"
                                                },
                                                "fileRenamed": {
                                                    "type": "string",
                                                    "format": "binary"
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            "responses": {
                                "default": {
                                    "description": "default response",
                                    "content": {
                                        "application/json": {
                                           \s
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "/files/attach": {
                        "put": {
                            "operationId": "putFile",
                            "requestBody": {
                                "content": {
                                    "application/octet-stream": {
                                        "schema": {
                                            "type": "string",
                                            "format": "binary"
                                        }
                                    }
                                }
                            },
                            "responses": {
                                "default": {
                                    "description": "default response",
                                    "content": {
                                        "application/json": {
                                           \s
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "/users/add": {
                        "post": {
                            "operationId": "addUser",
                            "requestBody": {
                                "content": {
                                    "application/x-www-form-urlencoded": {
                                        "schema": {
                                            "type": "object",
                                            "properties": {
                                                "id": {
                                                    "type": "string"
                                                },
                                                "name": {
                                                    "type": "string"
                                                },
                                                "gender": {
                                                    "type": "string"
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            "responses": {
                                "default": {
                                    "description": "default response",
                                    "content": {
                                        "application/json": {
                                           \s
                                        }
                                    }
                                }
                            }
                        }
                    },
                    "/widgets/{widgetId}": {
                        "get": {
                            "tags": [
                                "widgets"
                            ],
                            "summary": "Find pet by ID",
                            "description": "Returns a pet when ID <= 10.  ID > 10 or nonintegers will simulate API error conditions",
                            "operationId": "getWidget",
                            "parameters": [
                                {
                                    "name": "widgetId",
                                    "in": "path",
                                    "required": true,
                                    "schema": {
                                        "type": "string"
                                    }
                                }
                            ],
                            "responses": {
                                "200": {
                                    "description": "Returns widget with matching id",
                                    "content": {
                                        "application/json": {
                                            "schema": {
                                                "$ref": "#/components/schemas/Widget"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                "components": {
                    "schemas": {
                        "Tire_Detail": {
                            "type": "object",
                            "properties": {
                                "condition": {
                                    "type": "string"
                                },
                                "brand": {
                                    "type": "string"
                                }
                            }
                        },
                        "Car": {
                            "type": "object",
                            "properties": {
                                "model": {
                                    "type": "string"
                                },
                                "tires": {
                                    "type": "array",
                                    "items": {
                                        "$ref": "#/components/schemas/Tire"
                                    }
                                },
                                "price": {
                                    "type": "integer",
                                    "format": "int32"
                                },
                                "color": {
                                    "type": "string"
                                },
                                "manufacture": {
                                    "type": "string"
                                }
                            }
                        },
                        "Car_Summary-or-Sale": {
                            "type": "object",
                            "properties": {
                                "model": {
                                    "type": "string"
                                },
                                "price": {
                                    "type": "integer",
                                    "format": "int32"
                                },
                                "color": {
                                    "type": "string"
                                },
                                "manufacture": {
                                    "type": "string"
                                }
                            }
                        },
                        "Car_Detail": {
                            "type": "object",
                            "properties": {
                                "model": {
                                    "type": "string"
                                },
                                "tires": {
                                    "type": "array",
                                    "items": {
                                        "$ref": "#/components/schemas/Tire_Detail"
                                    }
                                },
                                "color": {
                                    "type": "string"
                                },
                                "manufacture": {
                                    "type": "string"
                                }
                            }
                        },
                        "Widget": {
                            "type": "object",
                            "properties": {
                                "a": {
                                    "type": "string"
                                },
                                "b": {
                                    "type": "string"
                                },
                                "id": {
                                    "type": "string"
                                }
                            }
                        },
                        "Car_Summary": {
                            "type": "object",
                            "properties": {
                                "model": {
                                    "type": "string"
                                },
                                "color": {
                                    "type": "string"
                                },
                                "manufacture": {
                                    "type": "string"
                                }
                            }
                        },
                        "Tire": {
                            "type": "object",
                            "properties": {
                                "condition": {
                                    "type": "string"
                                },
                                "brand": {
                                    "type": "string"
                                }
                            }
                        }
                    }
                }
            }\
            """;
    private static final String EXPECTED_YAML = """
            openapi: 3.0.1
            paths:
              /cars/all:
                get:
                  tags:
                  - cars
                  description: Return whole car
                  operationId: getAll
                  responses:
                    "200":
                      content:
                        application/json:
                          schema:
                            type: array
                            items:
                              $ref: "#/components/schemas/Car"
              /cars/detail:
                get:
                  tags:
                  - cars
                  description: Return car detail
                  operationId: getDetails
                  responses:
                    "200":
                      content:
                        application/json:
                          schema:
                            type: array
                            items:
                              $ref: "#/components/schemas/Car_Detail"
              /cars/sale:
                get:
                  tags:
                  - cars
                  operationId: getSaleSummaries
                  responses:
                    default:
                      description: default response
                      content:
                        application/json:
                          schema:
                            type: array
                            items:
                              $ref: "#/components/schemas/Car_Summary-or-Sale"
              /cars/summary:
                get:
                  tags:
                  - cars
                  description: Return car summaries
                  operationId: getSummaries
                  responses:
                    "200":
                      content:
                        application/json:
                          schema:
                            type: array
                            items:
                              $ref: "#/components/schemas/Car_Summary"
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
                        application/json: {}
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
              /users/add:
                post:
                  operationId: addUser
                  requestBody:
                    content:
                      application/x-www-form-urlencoded:
                        schema:
                          type: object
                          properties:
                            gender:
                              type: string
                            id:
                              type: string
                            name:
                              type: string
                  responses:
                    default:
                      description: default response
                      content:
                        application/json: {}
              /widgets/{widgetId}:
                get:
                  tags:
                  - widgets
                  summary: Find pet by ID
                  description: Returns a pet when ID <= 10.  ID > 10 or nonintegers will simulate
                    API error conditions
                  operationId: getWidget
                  parameters:
                  - name: widgetId
                    in: path
                    required: true
                    schema:
                      type: string
                  responses:
                    "200":
                      description: Returns widget with matching id
                      content:
                        application/json:
                          schema:
                            $ref: "#/components/schemas/Widget"
            components:
              schemas:
                Car:
                  type: object
                  properties:
                    color:
                      type: string
                    manufacture:
                      type: string
                    model:
                      type: string
                    price:
                      type: integer
                      format: int32
                    tires:
                      type: array
                      items:
                        $ref: "#/components/schemas/Tire"
                Car_Detail:
                  type: object
                  properties:
                    color:
                      type: string
                    manufacture:
                      type: string
                    model:
                      type: string
                    tires:
                      type: array
                      items:
                        $ref: "#/components/schemas/Tire_Detail"
                Car_Summary:
                  type: object
                  properties:
                    color:
                      type: string
                    manufacture:
                      type: string
                    model:
                      type: string
                Car_Summary-or-Sale:
                  type: object
                  properties:
                    color:
                      type: string
                    manufacture:
                      type: string
                    model:
                      type: string
                    price:
                      type: integer
                      format: int32
                Tire:
                  type: object
                  properties:
                    brand:
                      type: string
                    condition:
                      type: string
                Tire_Detail:
                  type: object
                  properties:
                    brand:
                      type: string
                    condition:
                      type: string
                Widget:
                  type: object
                  properties:
                    a:
                      type: string
                    b:
                      type: string
                    id:
                      type: string
            """;

    private static final int jettyPort = System.getProperties().containsKey("jetty.port") ? Integer.parseInt(System.getProperty("jetty.port")): -1;

    @BeforeMethod
    public void checkJetty() {
        if (jettyPort == -1) {
            throw new SkipException("Jetty not configured");
        }
    }

    @Test
    public void testSwaggerJson() throws Exception {

        final String actualBody = given()
                .port(jettyPort)
                .log().all()
                .when()
                .get("/openapi.json")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response().body().asString();

        compareAsJson(formatJson(actualBody), EXPECTED_JSON);
    }

    @Test
    public void testSwaggerJsonUsingAcceptHeader() throws Exception {
        final String actualBody = given()
                .port(jettyPort)
                .log().all()
                .accept(ContentType.JSON)
                .when()
                .get("/openapi")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response().body().asString();

        compareAsJson(formatJson(actualBody), EXPECTED_JSON);
    }

    @Test
    public void testSwaggerYaml() throws Exception {
        final String actualBody = given()
                .port(jettyPort)
                .log().all()
                .when()
                .get("/openapi.yaml")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType("application/yaml")
                .extract().response().body().asString();

        compareAsYaml(formatYaml(actualBody), EXPECTED_YAML);
    }

    @Test
    public void testSwaggerYamlUsingAcceptHeader() throws Exception {
        final String actualBody = given()
                .port(jettyPort)
                .log().all()
                .accept("application/yaml")
                .when()
                .get("/openapi")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType("application/yaml")
                .extract().response().body().asString();

        compareAsYaml(formatYaml(actualBody), EXPECTED_YAML);
    }

    @Test
    public void testYamlOpenAPI31() throws Exception {
        final String actualBody = given()
                .port(jettyPort)
                .log().all()
                .accept("application/yaml")
                .when()
                .get("/oas/openapi31")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType("application/yaml")
                .extract().response().body().asString();

        assertTrue(actualBody.contains("openapi: 3.1.0"));
    }

    @Test
    public void testServletOpenAPI31() throws Exception {
        final String actualBody = given()
                .port(jettyPort)
                .log().all()
                .when()
                .get("/oas/openapi.yaml")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType("application/yaml")
                .extract().response().body().asString();

        assertTrue(actualBody.contains("openapi: 3.1.0"));
    }

    @Test
    public void testYamlOpenAPI31WithBootstrapServlet() throws Exception {
        final String actualBody = given()
                .port(jettyPort)
                .log().all()
                .when()
                .get("/bootstrap")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .extract().response().body().asString();
        assertTrue(actualBody.contains("openapi: 3.1.0"));
    }

    private String formatYaml(String source) throws IOException {
        ObjectMapper mapper = Yaml.mapper(mapperBuilder ->
                mapperBuilder.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true));
        return mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(mapper.readValue(source, Object.class));
    }

    private String formatJson(String source) throws IOException {
        ObjectMapper mapper = Json.mapper(mapperBuilder ->
                mapperBuilder.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true));
        return mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(mapper.readValue(source, Object.class));
    }
}
