package io.swagger.v3.core.resolving;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.resolving.resources.User2169;
import org.testng.annotations.Test;

public class JsonPropertyTest {

    @Test(description = "test ticket 2169")
    public void testTicket2169() {

        SerializationMatchers.assertEqualsToYaml(ModelConverters.getInstance().read(User2169.class), """
                User2169:
                  required:
                  - Age
                  - GetterJsonPropertyOnField
                  - GetterJsonPropertyOnFieldReadOnly
                  - GetterJsonPropertyOnFieldReadWrite
                  - GetterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse
                  - GetterJsonPropertyOnFieldReadWriteSchemaReadOnlyFalse
                  - GetterJsonPropertyOnFieldSchemaReadOnlyTrue
                  - Name
                  type: object
                  properties:
                    Name:
                      type: string
                    Age:
                      type: integer
                      format: int32
                    GetterJsonPropertyOnFieldReadWriteCreatorSchemaReadOnlyFalse:
                      type: string
                    publi:
                      type: string
                    getter:
                      type: string
                    setter:
                      type: string
                      writeOnly: true
                    getterSetter:
                      type: string
                    jsonProp:
                      type: string
                    jsonPropReadOnly:
                      type: string
                      readOnly: true
                    jsonPropWriteOnly:
                      type: string
                      writeOnly: true
                    jsonPropReadWrite:
                      type: string
                    getter_jsonProp:
                      type: string
                    getter_jsonPropReadOnly:
                      type: string
                      readOnly: true
                    getter_jsonPropWriteOnly:
                      type: string
                    getter_jsonPropReadWrite:
                      type: string
                    setter_jsonProp:
                      type: string
                      writeOnly: true
                    setter_jsonPropReadOnly:
                      type: string
                    setter_jsonPropWriteOnly:
                      type: string
                      writeOnly: true
                    setter_jsonPropReadWrite:
                      type: string
                    gettersetter_jsonPropGet:
                      type: string
                    gettersetter_jsonPropReadOnlyGet:
                      type: string
                      readOnly: true
                    gettersetter_jsonPropWriteOnlyGet:
                      type: string
                    gettersetter_jsonPropReadWriteGet:
                      type: string
                    gettersetter_jsonPropSet:
                      type: string
                    gettersetter_jsonPropReadOnlySet:
                      type: string
                    gettersetter_jsonPropWriteOnlySet:
                      type: string
                      writeOnly: true
                    gettersetter_jsonPropReadWriteSet:
                      type: string
                    getterIgnore_jsonPropSet:
                      type: string
                      writeOnly: true
                    getterIgnore_jsonPropReadOnlySet:
                      type: string
                    getterIgnore_jsonPropWriteOnlySet:
                      type: string
                      writeOnly: true
                    getterIgnore_jsonPropReadWriteSet:
                      type: string
                    setterIgnore_jsonPropGet:
                      type: string
                    setterIgnore_jsonPropReadOnlyGet:
                      type: string
                      readOnly: true
                    setterIgnore_jsonPropWriteOnlyGet:
                      type: string
                    setterIgnore_jsonPropReadWriteGet:
                      type: string
                    getterSchemaReadOnlyTrue:
                      type: string
                      readOnly: true
                    data:
                      $ref: "#/components/schemas/Data"
                    GetterJsonPropertyOnField:
                      type: string
                    GetterJsonPropertyOnFieldReadWrite:
                      type: string
                    GetterJsonPropertyOnFieldReadWriteSchemaReadOnlyFalse:
                      type: string
                    GetterJsonPropertyOnFieldReadOnly:
                      type: string
                      readOnly: true
                    GetterJsonPropertyOnFieldSchemaReadOnlyTrue:
                      type: string
                      readOnly: true
                    approvePairing:
                      type: boolean
                      writeOnly: true
                """);
    }

    @Test(description = "test ticket 2845")
    public void testTicket2845() {

        SerializationMatchers.assertEqualsToYaml(ModelConverters.getInstance().readAll(Ticket2845Holder.class), """
                Ticket2845Child:
                  type: object
                  properties:
                    bar:
                      type: string
                    meow:
                      type: string
                Ticket2845Holder:
                  type: object
                  properties:
                    child:
                      $ref: "#/components/schemas/Ticket2845Child"\
                """);

        /*
            TODO: Test demonstrating annotation not being resolved when class is used/refernces elsewhere with different annotations
            in this case the annotation isn't resolved or not consistently resolved as the same object is also present
            and referenced  (in the same or different class) with no or different @JsonIgnoreProperties annotations.
            The possible solutions are either resolve into different unrelated schemas or resolve inline
            (see https://github.com/swagger-api/swagger-core/issues/3366 and other related tickets)
         */
        SerializationMatchers.assertEqualsToYaml(
                ModelConverters.getInstance().readAll(Ticket2845HolderNoAnnotationNotWorking.class),
                """
                Ticket2845Child:
                  type: object
                  properties:
                    foo:
                      type: string
                    bar:
                      type: string
                    meow:
                      type: string
                Ticket2845HolderNoAnnotationNotWorking:
                  type: object
                  properties:
                    child:
                      $ref: "#/components/schemas/Ticket2845Child"
                    childNoAnnotation:
                      $ref: "#/components/schemas/Ticket2845Child"\
                """);
    }

    static class Ticket2845Parent {
        public String foo;
        public String bar;
        public String bob;
    }

    @JsonIgnoreProperties({"bob"})
    static class Ticket2845Child extends Ticket2845Parent {
        public String meow;
    }

    static class Ticket2845Holder {
        @JsonIgnoreProperties({"foo"})
        public Ticket2845Child child;

    }

    static class Ticket2845HolderNoAnnotationNotWorking {
        @JsonIgnoreProperties({"foo"})
        public Ticket2845Child child;

        public Ticket2845Child childNoAnnotation;
    }
}
