package com.yanxuan88.australiacallcenter;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;

import java.util.Arrays;

public class ExceptionTest {
    @Test
    void test() {
        System.out.println(Arrays.asList("0,1".split("\\s*,\\s*")));
        DuplicateKeyException e = new DuplicateKeyException("aaa");
        System.out.println(DataAccessException.class.isAssignableFrom(e.getClass()));
    }
}
