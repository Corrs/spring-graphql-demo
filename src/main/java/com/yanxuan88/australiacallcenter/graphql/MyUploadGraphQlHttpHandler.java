/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yanxuan88.australiacallcenter.graphql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Ints;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.webmvc.GraphQlHttpHandler;
import org.springframework.http.MediaType;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Mono;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GraphQL file upload handler to expose as a WebMvc.fn endpoint via
 * {@link org.springframework.web.servlet.function.RouterFunctions}.
 *
 * @author co
 * @since 2023-12-12 11:04:51
 */
public class MyUploadGraphQlHttpHandler extends GraphQlHttpHandler {

    private static final Log logger = LogFactory.getLog(MyUploadGraphQlHttpHandler.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES = Arrays.asList(MediaType.MULTIPART_FORM_DATA);

    private final IdGenerator idGenerator = new AlternativeJdkIdGenerator();

    private final WebGraphQlHandler graphQlHandler;

    /**
     * Create a new instance.
     *
     * @param graphQlHandler common handler for GraphQL over HTTP requests
     */
    public MyUploadGraphQlHttpHandler(WebGraphQlHandler graphQlHandler) {
        super(graphQlHandler);
        this.graphQlHandler = graphQlHandler;
    }

    /**
     * Handle GraphQL requests over HTTP.
     *
     * @param serverRequest the incoming HTTP request
     * @return the HTTP response
     * @throws ServletException may be raised when reading the request body, e.g.
     *                          {@link HttpMediaTypeNotSupportedException}.
     */
    public ServerResponse handleRequest(ServerRequest serverRequest) throws ServletException {
        WebGraphQlRequest graphQlRequest = new WebGraphQlRequest(
                serverRequest.uri(), serverRequest.headers().asHttpHeaders(), readBody(serverRequest),
                this.idGenerator.generateId().toString(), LocaleContextHolder.getLocale());

        if (logger.isDebugEnabled()) {
            logger.debug("Executing: " + graphQlRequest);
        }

        Mono<ServerResponse> responseMono = this.graphQlHandler.handleRequest(graphQlRequest)
                .map(response -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Execution complete");
                    }
                    ServerResponse.BodyBuilder builder = ServerResponse.ok();
                    builder.headers(headers -> headers.putAll(response.getResponseHeaders()));
                    builder.contentType(selectResponseMediaType(serverRequest));
                    return builder.body(response.toMap());
                });

        return ServerResponse.async(responseMono);
    }

    private static Map<String, Object> readBody(ServerRequest request) {
        MultiValueMap<String, Part> multipartData = readMultipartData(request);
        MultiValueMap<String, String> params = readParams(request);
        Map<String, Object> map = read(params.getFirst("map"));
        Map<String, Object> operations = read(params.getFirst("operations"));
        Map<String, List<Part>> uploadVariableValues = map.entrySet().stream().map(entry -> {
            Part part = multipartData.getFirst(entry.getKey());
            String rKey = ((List<String>) entry.getValue()).get(0).replace("variables.", "");
            return new MyPart(part, rKey);
        }).collect(Collectors.groupingBy(MyPart::getVariableName, Collectors.mapping(MyPart::getPart, Collectors.toList())));
        Map<String, Object> variables = (Map<String, Object>) operations.get("variables");
        uploadVariableValues.forEach((k, v) -> {
            String[] ks = k.split("\\.");
            int len = ks.length;
            String nk = k;
            try {
                Ints.stringConverter().convert(ks[len - 1]);
                len--;
                nk = k.substring(0, k.lastIndexOf("."));
            } catch (NumberFormatException e) {}
            if (len == 1) {
                if (variables.containsKey(nk)) {
                    variables.put(nk, v);
                }
            } else {
                Map<String, Object> m = new HashMap<>();
                for (int i = 0; i < len - 1; i++) {
                    String key = ks[i];
                    if (i == 0) {
                        Object o = variables.get(key);
                        m = (Map<String, Object>) o;
                    } else {
                        m = (Map<String, Object>) m.get(key);
                    }
                    if (i == len - 2) {
                        m.put(ks[len - 1], v);
                    }
                }
            }
        });
        return operations;
    }

    private static MultiValueMap<String, String> readParams(ServerRequest request) {
        return request.params();
    }

    private static MultiValueMap<String, Part> readMultipartData(ServerRequest request) {
        try {
            return request.multipartData();
        } catch (IOException e) {
            throw new ServerWebInputException("I/O error while reading request multipartData", null, e);
        } catch (ServletException e) {
            throw new ServerWebInputException("Servlet error while reading request multipartData", null, e);
        }
    }

    private static MediaType selectResponseMediaType(ServerRequest serverRequest) {
        for (MediaType accepted : serverRequest.headers().accept()) {
            if (SUPPORTED_MEDIA_TYPES.contains(accepted)) {
                return accepted;
            }
        }
        return MediaType.APPLICATION_JSON;
    }

    private static Map<String, Object> read(String v) {
        try {
            return (Map<String, Object>) mapper.readValue(v, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static class MyPart {
        Part part;
        String variableName;

        public MyPart(Part part, String variableName) {
            this.part = part;
            this.variableName = variableName;
        }

        public Part getPart() {
            return part;
        }

        public String getVariableName() {
            return variableName;
        }
    }
}
