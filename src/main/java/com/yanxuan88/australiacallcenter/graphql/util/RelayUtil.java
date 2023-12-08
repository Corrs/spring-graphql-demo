package com.yanxuan88.australiacallcenter.graphql.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import graphql.relay.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Base64.getEncoder;

/**
 * relay 工具类
 *
 * @author co
 * @since 2023-12-08 14:06:16
 */
public final class RelayUtil {

    /**
     * 通过mybatis-plus的分页构建relay connection
     *
     * @param page 分页数据
     * @param <T>  泛型
     * @return connection
     */
    public static <T> Connection<T> build(Page<T> page) {
        List<Edge<T>> edges = buildEdges(page.getRecords());
        if (edges.size() == 0) {
            return emptyConnection();
        }

        Edge<T> firstEdge = edges.get(0);
        Edge<T> lastEdge = edges.get(edges.size() - 1);

        PageInfo pageInfo = new DefaultPageInfo(
                firstEdge.getCursor(),
                lastEdge.getCursor(),
                page.hasPrevious(),
                page.hasNext()
        );

        return new MyRelayConnection<>(
                edges,
                pageInfo,
                page.getTotal()
        );
    }

    private static <T> Connection<T> emptyConnection() {
        return new MyRelayConnection<>(Collections.emptyList(), new DefaultPageInfo(null, null, false, false), 0L);
    }

    private static <T> List<Edge<T>> buildEdges(List<T> data) {
        List<Edge<T>> edges = new ArrayList<>();
        int ix = 0;
        for (T object : data) {
            edges.add(new DefaultEdge<>(object, new DefaultConnectionCursor(createCursor(ix++))));
        }
        return edges;
    }

    private static String createCursor(int offset) {
        byte[] bytes = ("simple-cursor" + offset).getBytes(StandardCharsets.UTF_8);
        return getEncoder().encodeToString(bytes);
    }
}
