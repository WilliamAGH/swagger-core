package io.swagger.v3.jaxrs2.resources.model;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serial;

public class CustomGenerator extends ObjectIdGenerators.PropertyGenerator {
    @Serial
    private static final long serialVersionUID = 1L;

    protected CustomGenerator(Class<?> scope) {
        super(scope);
    }

    @Override
    public ObjectIdGenerator<Object> forScope(Class<?> scope) {
        return null;
    }

    @Override
    public ObjectIdGenerator<Object> newForSerialization(Object context) {
        return null;
    }

    @Override
    public IdKey key(Object key) {
        return null;
    }

    @Override
    public Object generateId(Object forPojo) {
        return null;
    }
}