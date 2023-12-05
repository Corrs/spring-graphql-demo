package com.yanxuan88.australiacallcenter.config;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static com.yanxuan88.australiacallcenter.common.Constant.DATE_FORMATTER;
import static com.yanxuan88.australiacallcenter.common.Constant.DATE_TIME_FORMATTER;

/**
 * spring cache redis 序列化器
 * 用redis缓存spring cache时使用，解决了LocalDateTime序列化和反序列化问题
 *
 * @author co
 * @since 2023-12-05 16:37:00
 */
public class RedisCacheValueSerializer extends GenericJackson2JsonRedisSerializer {
    public RedisCacheValueSerializer() {
        super();
    }

    @Override
    public Object deserialize(byte[] source) throws SerializationException {
        Object value = super.deserialize(source);
        if (value instanceof String) {
            String str = value == null ? "" : (String) value;
            // 以下为LocalDate和LocalDateTime的判断和转换，可以替换为其他更具效率的方式
            try {
                if (str.length() == 10) {
                    return LocalDate.parse(str, DATE_FORMATTER);
                }
                if (str.length() == 19) {
                    return LocalDateTime.parse(str, DATE_TIME_FORMATTER);
                }
            } catch (DateTimeParseException ex) {

            }
        }
        return value;
    }

    @Override
    public byte[] serialize(Object source) throws SerializationException {
        if (source instanceof LocalDateTime) {
            return super.serialize(((LocalDateTime) source).format(DATE_TIME_FORMATTER));
        }

        if (source instanceof LocalDate) {
            return super.serialize(((LocalDate) source).format(DATE_FORMATTER));
        }
        return super.serialize(source);
    }
}
