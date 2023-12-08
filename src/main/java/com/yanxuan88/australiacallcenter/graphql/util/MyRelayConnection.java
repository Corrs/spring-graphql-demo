package com.yanxuan88.australiacallcenter.graphql.util;

import graphql.relay.DefaultConnection;
import graphql.relay.Edge;
import graphql.relay.PageInfo;

import java.util.List;

public class MyRelayConnection<T> extends DefaultConnection<T> {

    private Long total;

    public MyRelayConnection(List<Edge<T>> edges, PageInfo pageInfo, Long total) {
        super(edges, pageInfo);
        this.total = total == null ? 0 : total;
    }

    public Long getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "MyRelayConnection{" +
                "total=" + total +
                "} " + super.toString();
    }
}
