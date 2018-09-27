package sc2.msks.impl.msgexchange;

import java.io.File;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.pccw.sc2.audit.log.ExceptionLogVO;
import com.pccw.sc2.audit.log.TransationLogVO;
import com.pccw.sc2.audit.thread.AbstractHandler;
import com.pccw.sc2.audit.thread.ExceptionHandler;
import com.pccw.sc2.audit.thread.FileListener;
import com.pccw.sc2.audit.thread.TransactionHandler;

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
    public FileAlterationMonitor createLogWatchMonitor() throws Exception {
//        WatchMonitor all = WatchUtil.createAll(logPath, 3 , this.logWatcher());
//        all.start();
        
        // 轮询间隔 5 秒
        long interval = TimeUnit.SECONDS.toMillis(1);
        // 创建过滤器
		// IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
        // IOFileFilter files       = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(".log"));
        // IOFileFilter filter      = FileFilterUtils.or(directories, files);
        // 使用过滤器
        FileAlterationObserver observer = new FileAlterationObserver(new File(logPath), null);
        //不使用过滤器
        //FileAlterationObserver observer = new FileAlterationObserver(new File(rootDir));
        observer.addListener(fileListener());
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        // try {
			monitor.start();
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
        return monitor;
    }
    
    @Bean
    public FileListener fileListener() {
    	FileListener f = new FileListener();
        return f;
    }

    @Bean
//    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.NO)
    public AbstractHandler<ExceptionLogVO> exceptionLineHandler() {
        return new ExceptionHandler();
    }

    @Bean
//    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.NO)
    public AbstractHandler<TransationLogVO> transcationLineHandler() {
        return new TransactionHandler();
    }
}