package sc2.msks.impl.msgexchange;

import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.WatchUtil;
import cn.hutool.core.io.watch.Watcher;

import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.log.TransationLogVO;
import com.pccw.sc2.audit.thread.AbstractLineHandler;
import com.pccw.sc2.audit.thread.ExceptionLineHandler;
import com.pccw.sc2.audit.thread.LogWatcher;
import com.pccw.sc2.audit.thread.TransactionLineHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class BeanConfig {

    @Value("${log.path}")
    private String logPath;

    @Bean
    public TaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(8);
        // 设置最大线程数
        executor.setMaxPoolSize(30);
        // 设置队列容量
        executor.setQueueCapacity(20);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 设置默认线程名称
        executor.setThreadNamePrefix("auditlg-");
        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        return executor;
    }

    @Bean
    public WatchMonitor createLogWatchMonitor() {
        WatchMonitor all = WatchUtil.createAll(logPath, 2, this.logWatcher());
        all.start();
        return all;
    }
    
    @Bean
    public Watcher logWatcher() {
        LogWatcher logWatcher = new LogWatcher();
        return logWatcher;
    }

    @Bean
//    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.NO)
    public AbstractLineHandler<ExceptionLogVO> exceptionLineHandler() {
        return new ExceptionLineHandler();
    }

    @Bean
//    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.NO)
    public AbstractLineHandler<TransationLogVO> transcationLineHandler() {
        return new TransactionLineHandler();
    }
}