package com.yanxuan88.australiacallcenter.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static com.yanxuan88.australiacallcenter.common.Constant.TRACE_ID;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfiguration implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new TraceIdThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(10);
        threadPool.setMaxPoolSize(20);
        threadPool.setQueueCapacity(200);
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        threadPool.setAwaitTerminationSeconds(60 * 15);
        threadPool.setThreadNamePrefix("Async-");
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPool.initialize();
        return threadPool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    /**
     * 自定义异常处理类
     */
    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            log.error("Async exception message - {}", throwable.getMessage());
            log.error("Async method name - {}", method.getName());
            log.error("Async parameter values - {}", obj);
        }

    }

    class TraceIdThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
        public TraceIdThreadPoolTaskExecutor() {
            super();
        }

        @Override
        public void execute(Runnable task) {
            super.execute(wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public Future<?> submit(Runnable task) {
            return super.submit(wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return super.submit(wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public ListenableFuture<?> submitListenable(Runnable task) {
            return super.submitListenable(wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            return super.submitListenable(wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        protected void cancelRemainingTask(Runnable task) {
            super.cancelRemainingTask(wrap(task, MDC.getCopyOfContextMap()));
        }

        public static void setTraceIdIfAbsent() {
            if (MDC.get(TRACE_ID) == null) {
                MDC.put(TRACE_ID, UUID.randomUUID().toString());
            }
        }

        /**
         * 用于父线程向线程池中提交任务时，将自身MDC中的数据复制给子线程
         */
        public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
            return () -> {
                if (context == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(context);
                }
                setTraceIdIfAbsent();
                try {
                    return callable.call();
                } finally {
                    MDC.clear();
                }
            };
        }

        /**
         * 用于父线程向线程池中提交任务时，将自身MDC中的数据复制给子线程
         */
        public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
            return () -> {
                if (context == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(context);
                }
                setTraceIdIfAbsent();
                try {
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        }
    }


}
