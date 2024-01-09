package com.yanxuan88.australiacallcenter.graphql.scalar;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;


/**
 * 自定义scalar Date
 *
 * @author co
 * @since 2023/8/29 下午1:57:28
 */
public class LocalDateTimeScalar implements Coercing<LocalDateTime, String> {
    private final String YMD_HMS_REGEXP = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)) ([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
    private final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");
    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof Long) {
            return LocalDateTime.ofInstant(new Date((Long) dataFetcherResult).toInstant(), DEFAULT_ZONE_OFFSET).format(DTF);
        }
        if (dataFetcherResult instanceof Date) {
            return LocalDateTime.ofInstant(((Date) dataFetcherResult).toInstant(), DEFAULT_ZONE_OFFSET).format(DTF);
        }
        if (dataFetcherResult instanceof LocalDateTime) {
            return ((LocalDateTime) dataFetcherResult).atOffset(DEFAULT_ZONE_OFFSET).format(DTF);
        }
        String possibleDateValue = String.valueOf(dataFetcherResult);
        if (looksLikeAnDate(possibleDateValue)) {
            return possibleDateValue;
        } else {
            throw new CoercingSerializeException("无法将【" + possibleDateValue + "】序列化为日期");
        }
    }

    @Override
    public LocalDateTime parseValue(Object input) throws CoercingParseValueException {
        return parseDateFromVariable(input);
    }

    @Override
    public LocalDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
        return parseDateFromAstLiteral(input);
    }

    private boolean looksLikeAnDate(String possibleDateValue) {
        return Pattern.matches(YMD_HMS_REGEXP, possibleDateValue);
    }

    private LocalDateTime parseDateFromVariable(Object input) {
        if (input instanceof String) {
            String possibleDateValue = input.toString();
            if (isBlank(possibleDateValue)) return null;
            if (looksLikeAnDate(possibleDateValue)) return parse(possibleDateValue);
        }
        throw new CoercingParseValueException("无法将变量【" + input + "】解析为日期");
    }

    private LocalDateTime parseDateFromAstLiteral(Object input) {
        if (input instanceof StringValue) {
            String possibleDateValue = ((StringValue) input).getValue();
            if (isBlank(possibleDateValue)) return null;
            if (looksLikeAnDate(possibleDateValue)) return parse(possibleDateValue);
        }
        throw new CoercingParseLiteralException("数据不是日期 : '" + input + "'");
    }

    private LocalDateTime parse(String dateStr) {
        return LocalDateTime.from(LocalDateTime.parse(dateStr, DTF).toInstant(DEFAULT_ZONE_OFFSET).atOffset(DEFAULT_ZONE_OFFSET));
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().length() <= 0;
    }
}
