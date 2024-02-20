package com.yanxuan88.australiacallcenter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;

import java.util.concurrent.locks.Lock;
import java.util.function.Function;

public class JdbcLockClient {

    @Autowired
    private JdbcLockRegistry jdbcLockRegistry;


    public <R> R doWithLock(Object lockKey, Function<Lock, R> fun) {
        Lock lock = jdbcLockRegistry.obtain(lockKey);
        try {
            lock.lock();
            return fun.apply(lock);
        } finally {
            lock.unlock();
        }
    }
}
