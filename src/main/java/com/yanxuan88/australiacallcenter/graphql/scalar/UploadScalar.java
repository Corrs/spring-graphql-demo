package com.yanxuan88.australiacallcenter.graphql.scalar;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import javax.servlet.http.Part;


/**
 * 自定义scalar Upload
 *
 * @author co
 * @since 2023/8/29 下午1:57:28
 */
public class UploadScalar implements Coercing<Part, Void> {
    @Override
    public Void serialize(Object dataFetcherResult) {
        throw new CoercingSerializeException("Upload is an input-only type");
    }

    @Override
    public Part parseValue(Object input) {
        if (input instanceof Part) {
            return (Part) input;
        } else if (null == input) {
            return null;
        } else {
            throw new CoercingParseValueException(
                    "Expected type "
                            + Part.class.getName()
                            + " but was "
                            + input.getClass().getName());
        }
    }

    @Override
    public Part parseLiteral(Object input) {
        throw new CoercingParseLiteralException(
                "Must use variables to specify Upload values");
    }
}
