package io.swagger.v3.core.resolving.v31;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.SwaggerTestBase;
import io.swagger.v3.core.resolving.v31.model.AnnotatedPet;
import io.swagger.v3.core.resolving.v31.model.AnnotatedPetSinglePatternProperty;
import io.swagger.v3.core.util.OpenAPISchema2JsonSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;


import static org.testng.Assert.assertEquals;

public class PatternAndSchemaPropertiesTest extends SwaggerTestBase {

    @Test
    public void testPatternAndSchemaProperties() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(AnnotatedPet.class));

        assertEquals(((Schema)model.getPatternProperties().get("what.*ever")).getFormat(), "int32");
        assertEquals(((Schema)model.getPatternProperties().get("it.*takes")).get$ref(), "#/components/schemas/Category");

        assertEquals(((Schema)model.getProperties().get("anotherCategory")).get$ref(), "#/components/schemas/Category");
        assertEquals(((Schema)model.getProperties().get("anotherInteger")).getFormat(), "int32");

        SerializationMatchers.assertEqualsToYaml(context.getDefinedModels(), """
                AnnotatedPet:
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
                      - available
                      - pending
                      - sold
                    anotherCategory:
                      $ref: "#/components/schemas/Category"
                    anotherInteger:
                      maximum: 10
                      type: integer
                      description: prop schema 1
                      format: int32
                  description: Annotated Pet
                  nullable: true
                Category:
                  type: object
                  properties:
                    id:
                      type: integer
                      format: int64
                    name:
                      type: string
                  description: prop schema 2
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
                    name: Tag""");
        context.getDefinedModels().values().forEach(s -> new OpenAPISchema2JsonSchema().process(s));

        SerializationMatchers.assertEqualsToYaml31(context.getDefinedModels(), """
                AnnotatedPet:
                  type:
                  - object
                  - "null"
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
                      - available
                      - pending
                      - sold
                    anotherCategory:
                      $ref: "#/components/schemas/Category"
                    anotherInteger:
                      maximum: 10
                      type: integer
                      description: prop schema 1
                      format: int32
                  patternProperties:
                    what.*ever:
                      maximum: 10
                      type: integer
                      description: prop schema 1
                      format: int32
                    it.*takes:
                      $ref: "#/components/schemas/Category"
                  description: Annotated Pet
                Category:
                  type: object
                  properties:
                    id:
                      type: integer
                      format: int64
                    name:
                      type: string
                  description: prop schema 2
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
                """);
    }

    @Test
    public void testSinglePatternAndSchemaProperties() throws Exception {

        final ModelResolver modelResolver = new ModelResolver(mapper());

        ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        Schema model = context
                .resolve(new AnnotatedType(AnnotatedPetSinglePatternProperty.class));

        assertEquals(((Schema)model.getPatternProperties().get("what.*ever")).getFormat(), "int32");
        assertEquals(((Schema)model.getProperties().get("anotherCategory")).get$ref(), "#/components/schemas/Category");
    }

}
