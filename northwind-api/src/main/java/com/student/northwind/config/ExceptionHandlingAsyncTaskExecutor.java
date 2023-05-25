package com.student.northwind.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlingAsyncTaskExecutor  implements AsyncTaskExecutor, InitializingBean, DisposableBean {
    private final AsyncTaskExecutor executor;

    public void execute(Runnable task) {
        this.executor.execute(this.createWrappedRunnable(task));
    }

    /** @deprecated */
    @Deprecated
    public void execute(Runnable task, long startTimeout) {
        this.executor.execute(this.createWrappedRunnable(task), startTimeout);
    }

    private <T> Callable<T> createCallable(Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (Exception var3) {
                this.handle(var3);
                throw var3;
            }
        };
    }

    private Runnable createWrappedRunnable(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception var3) {
                this.handle(var3);
            }

        };
    }

    protected void handle(Exception e) {
        this.log.error("Caught async exception", e);
    }

    public Future<?> submit(Runnable task) {
        return this.executor.submit(this.createWrappedRunnable(task));
    }

    public <T> Future<T> submit(Callable<T> task) {
        return this.executor.submit(this.createCallable(task));
    }

    public void destroy() throws Exception {
        if (this.executor instanceof DisposableBean) {
            var bean = (DisposableBean)this.executor;
            bean.destroy();
        }
    }

    public void afterPropertiesSet() throws Exception {
        if (this.executor instanceof InitializingBean) {
            var bean = (InitializingBean)this.executor;
            bean.afterPropertiesSet();
        }

    }
}
