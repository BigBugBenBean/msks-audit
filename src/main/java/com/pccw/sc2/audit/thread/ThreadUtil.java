package com.pccw.sc2.audit.thread;

import cn.hutool.core.thread.GlobalThreadPool;
import cn.hutool.core.thread.NamedThreadFactory;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.*;

/**
 * @author Bill
 */
public class ThreadUtil {

    public static void main(String[] args) {
//        String logPath = "/home/bigbean/logs/msks-audit/backup";
//        WatchUtil.createAll(logPath,2, new LogWatcher());

        String pathSeparator = File.separator;
        System.out.println(pathSeparator);


//        WatchMonitor watchMonitor = WatchUtil.create("/home/bigbean/logs/msks-audit", 3, WatchMonitor.EVENTS_ALL);
//        watchMonitor.setWatcher(new LogWatcher());
//        watchMonitor.start();

//        ExecutorService es = Executors.newFixedThreadPool(5,new cn.hutool.core.thread.NamedThreadFactory("auditlg", false));
//        ((ThreadPoolExecutor)GlobalThreadPool.getExecutor()).setThreadFactory(new cn.hutool.core.thread.NamedThreadFactory("auditlg", false));
//
//
//
//        for (int i = 0; i < 50; i++) {
//            final String aaa = "aaa"+i;
//            GlobalThreadPool.execute(new Runnable() {
//                String name = aaa;
//
//                @Override
//                public void run() {
////                    try {
////                        Thread.sleep(500);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
//                    System.out.println(Thread.currentThread().getName()+"-------"+name);
//                }
//            });
//        }
//        ExecutorService es = GlobalThreadPool.getExecutor();
//
//        int activeCount = ((ThreadPoolExecutor)es).getActiveCount();
//        int corePoolSize = ((ThreadPoolExecutor)es).getCorePoolSize();
//        int poolSize = ((ThreadPoolExecutor)es).getPoolSize();
//        int largestPoolSize = ((ThreadPoolExecutor) es).getLargestPoolSize();
//        System.out.println(activeCount);
//        System.out.println(corePoolSize);
//        System.out.println(poolSize);
//        System.out.println(largestPoolSize);
    }

    public static ExecutorService newExecutor(int threadSize) {
        return new ThreadPoolExecutor(threadSize, threadSize, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    public static ExecutorService newExecutor() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    public static ExecutorService newSingleExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ThreadPoolExecutor newExecutor(int corePoolSize, int maximumPoolSize) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    public static ThreadPoolExecutor newExecutorByBlockingCoefficient(float blockingCoefficient) {
        if (blockingCoefficient >= 1 || blockingCoefficient < 0) {
            throw new IllegalArgumentException("[blockingCoefficient] must between 0 and 1, or equals 0.");
        }

        int poolSize = (int) (Runtime.getRuntime().availableProcessors() / (1 - blockingCoefficient));

        return new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    public static void execute(Runnable runnable) {
        GlobalThreadPool.execute(runnable);
    }

    public static Runnable excAsync(final Runnable runnable, boolean isDeamon) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        thread.setDaemon(isDeamon);
        thread.start();

        return runnable;
    }

    public static <T> Future<T> execAsync(Callable<T> task) {
        return GlobalThreadPool.submit(task);
    }

    public static Future<?> execAsync(Runnable runnable) {
        return GlobalThreadPool.submit(runnable);
    }

    public static <T> CompletionService<T> newCompletionService() {
        return new ExecutorCompletionService<T>(GlobalThreadPool.getExecutor());
    }

    public static <T> CompletionService<T> newCompletionService(ExecutorService executor) {
        return new ExecutorCompletionService<T>(executor);
    }

    public static CountDownLatch newCountDownLatch(int threadCount) {
        return new CountDownLatch(threadCount);
    }

    public static Thread newThread(Runnable runnable, String name) {
        final Thread t = new Thread(currentThreadGroup(), runnable, name);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

    public static boolean sleep(Number timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout.longValue());
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    public static boolean sleep(Number millis) {
        if (millis == null) {
            return true;
        }

        try {
            Thread.sleep(millis.longValue());
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    public static boolean safeSleep(Number millis) {
        long millisLong = millis.longValue();
        long done = 0;
        while (done < millisLong) {
            long before = System.currentTimeMillis();
            if (false == sleep(millisLong - done)) {
                return false;
            }
            long after = System.currentTimeMillis();
            done += (after - before);
        }
        return true;
    }

    public static StackTraceElement[] getStackTrace() {
        return Thread.currentThread().getStackTrace();
    }

    public static StackTraceElement getStackTraceElement(int i) {
        StackTraceElement[] stackTrace = getStackTrace();
        if (i < 0) {
            i += stackTrace.length;
        }
        return stackTrace[i];
    }

    public static <T> ThreadLocal<T> createThreadLocal(boolean isInheritable) {
        if (isInheritable) {
            return new InheritableThreadLocal<>();
        } else {
            return new ThreadLocal<>();
        }
    }

    public static void interupt(Thread thread, boolean isJoin) {
        if (null != thread && false == thread.isInterrupted()) {
            thread.interrupt();
            if (isJoin) {
                waitForDie(thread);
            }
        }
    }

    public static void waitForDie(Thread thread) {
        boolean dead = false;
        do {
            try {
                thread.join();
                dead = true;
            } catch (InterruptedException e) {
                // ignore
            }
        } while (!dead);
    }

    public static Thread[] getThreads() {
        return getThreads(Thread.currentThread().getThreadGroup().getParent());
    }

    public static Thread[] getThreads(ThreadGroup group) {
        final Thread[] slackList = new Thread[group.activeCount() * 2];
        final int actualSize = group.enumerate(slackList);
        final Thread[] result = new Thread[actualSize];
        System.arraycopy(slackList, 0, result, 0, actualSize);
        return result;
    }

    public static Thread getMainThread() {
        for (Thread thread : getThreads()) {
            if (thread.getId() == 1) {
                return thread;
            }
        }
        return null;
    }

    public static ThreadGroup currentThreadGroup() {
        final SecurityManager s = System.getSecurityManager();
        return (null != s) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    public static ThreadFactory newNamedThreadFactory(String prefix, boolean isDeamon) {
        return new NamedThreadFactory(prefix, isDeamon);
    }

    public static ThreadFactory newNamedThreadFactory(String prefix, ThreadGroup threadGroup, boolean isDeamon) {
        return new NamedThreadFactory(prefix, threadGroup, isDeamon);
    }

    public static ThreadFactory newNamedThreadFactory(String prefix, ThreadGroup threadGroup, boolean isDeamon, UncaughtExceptionHandler handler) {
        return new NamedThreadFactory(prefix, threadGroup, isDeamon, handler);
    }
}
