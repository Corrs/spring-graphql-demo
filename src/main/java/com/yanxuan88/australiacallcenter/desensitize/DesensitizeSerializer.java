package com.yanxuan88.australiacallcenter.desensitize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DesensitizeSerializer extends StdSerializer<Object> implements ContextualSerializer {
    private transient static final Map<Class<?>, Desensitization<?>> map = new ConcurrentHashMap<>();
    private transient Desensitization<Object> desensitization;

    public DesensitizeSerializer() {
        super(Object.class);
    }

    private void setDesensitization(Desensitization<Object> desensitization) {
        this.desensitization = desensitization;
    }
    private Desensitization<Object> getDesensitization() {
        return desensitization;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        Desensitization<Object> objectDesensitization = getDesensitization();
        if (objectDesensitization != null) {
            try {
                gen.writeObject(objectDesensitization.desensitize((String) value));
            } catch (Exception e) {
                gen.writeObject(value);
            }
        } else if (value instanceof String) {
            gen.writeString(DesensitizeUtil.masking((String)value));
        } else {
            gen.writeObject(value);
        }
    }

    @SuppressWarnings("all")
    public static Desensitization<?> getDesensitization(Class<?> clazz) {
        if (clazz.isInterface()) {
            throw new UnsupportedOperationException("desensitization is interface, what is expected is an implementation class !");
        }
        return map.computeIfAbsent(clazz, k -> {
            try {
                return (Desensitization<?>) clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new UnsupportedOperationException(e.getMessage(), e);
            }
        });
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Desensitize annotation = property.getAnnotation(Desensitize.class);
        return createContextual(annotation.desensitization());
    }

    private JsonSerializer<?> createContextual(Class<? extends Desensitization> clazz) {
        DesensitizeSerializer serializer = new DesensitizeSerializer();
        serializer.setDesensitization((Desensitization<Object>) getDesensitization(clazz));
        return serializer;
    }
}
