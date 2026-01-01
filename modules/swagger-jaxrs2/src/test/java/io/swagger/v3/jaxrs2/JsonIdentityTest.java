package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.resources.JsonIdentityCyclicResource;
import io.swagger.v3.jaxrs2.resources.JsonIdentityResource;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.io.IOException;

public class JsonIdentityTest {

    @Test(description = "Test JsonIdentity")
    public void testJsonIdentity() throws IOException {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(JsonIdentityResource.class);
        SerializationMatchers.assertEqualsToYaml(openAPI, EXPECTED_YAML);
    }

    @Test(description = "Test JsonIdentity Cyclic")
    public void testJsonIdentityCyclic() throws IOException {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(JsonIdentityCyclicResource.class);
        SerializationMatchers.assertEqualsToYaml(openAPI, EXPECTED_YAML_CYCLIC);
    }

    static final String EXPECTED_YAML_CYCLIC = """
            openapi: 3.0.1
            paths:
              /pet:
                post:
                  description: Add a single object
                  operationId: test
                  requestBody:
                    content:
                      '*/*':
                        schema:
                          $ref: "#/components/schemas/ModelWithJsonIdentityCyclic"
                    required: true
                  responses:
                    default:
                      description: default response
                      content:
                        application/json: {}
                        application/xml: {}
            components:
              schemas:
                SourceDefinition:
                  type: object
                  properties:
                    driver:
                      type: string
                    name:
                      type: string
                    model:
                      type: integer
                      format: int64
                ModelWithJsonIdentityCyclic:
                  type: object
                  properties:
                    id:
                      type: integer
                      format: int64
                    sourceDefinitions:
                      type: array
                      items:
                        $ref: "#/components/schemas/SourceDefinition"
            """;

    static final String EXPECTED_YAML = """
            openapi: 3.0.1
            paths:
              /pet:
                post:
                  description: Add a single object
                  operationId: test
                  requestBody:
                    content:
                      '*/*':
                        schema:
                          $ref: "#/components/schemas/ModelWithJsonIdentity"
                    required: true
                  responses:
                    default:
                      description: default response
                      content:
                        application/json: {}
                        application/xml: {}
            components:
              schemas:
                ModelWithJsonIdentity:
                  type: object
                  properties:
                    PropertyGeneratorAsId:
                      type: string
                    PropertyGeneratorAsProperty:
                      $ref: "#/components/schemas/SourceDefinition1"
                    ChangedPropertyName:
                      type: string
                    ChangedPropertyName2:
                      type: string
                    SourceWithoutPropertyAsId:
                      type: string
                    SourceWithoutPropertyAsProperty:
                      $ref: "#/components/schemas/SourceDefinition3"
                    IntSequenceGeneratorAsId:
                      type: integer
                      format: int32
                    IntSequenceGeneratorAsProperty:
                      $ref: "#/components/schemas/SourceDefinition4"
                    IntSequenceWithoutPropertyAsId:
                      type: integer
                      format: int32
                    IntSequenceWithoutPropertyAsProperty:
                      $ref: "#/components/schemas/SourceDefinition5"
                    UUIDGeneratorAsId:
                      type: string
                      format: uuid
                    UUIDGeneratorAsProperty:
                      $ref: "#/components/schemas/SourceDefinition6"
                    UUIDGeneratorWithoutPropertyAsId:
                      type: string
                      format: uuid
                    UUIDGeneratorWithoutPropertyAsProperty:
                      $ref: "#/components/schemas/SourceDefinition7"
                    GeneratorsNone:
                      $ref: "#/components/schemas/SourceDefinition8"
                    CustomGenerator:
                      type: string
                    WithoutJsonIdentityReference:
                      $ref: "#/components/schemas/SourceDefinition10"
                    IntSequenceGeneratorAtClassLevel:
                      $ref: "#/components/schemas/SourceDefinition11"
                SourceDefinition1:
                  type: object
                  properties:
                    driver:
                      type: string
                    name:
                      type: string
                SourceDefinition10:
                  type: object
                  properties:
                    driver:
                      type: string
                    name:
                      type: string
                SourceDefinition11:
                  type: object
                  properties:
                    'name':
                      type: string
                    '@id':
                      type: integer
                      format: int32
                SourceDefinition3:
                  type: object
                  properties:
                    name:
                      type: string
                    driverId:
                      type: string
                    '@id':
                      type: string
                SourceDefinition4:
                  type: object
                  properties:
                    name:
                      type: string
                    testName2:
                      type: integer
                      format: int32
                SourceDefinition5:
                  type: object
                  properties:
                    name:
                      type: string
                    '@id':
                      type: integer
                      format: int32
                SourceDefinition6:
                  type: object
                  properties:
                    name:
                      type: string
                    UUID2:
                      type: string
                      format: uuid
                SourceDefinition7:
                  type: object
                  properties:
                    name:
                      type: string
                    '@id':
                      type: string
                      format: uuid
                SourceDefinition8:
                  type: object
                  properties:
                    name:
                      type: string
                    driverId:
                      type: string
            """;

}