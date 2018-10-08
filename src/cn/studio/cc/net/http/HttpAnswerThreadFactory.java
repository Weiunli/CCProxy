package cn.studio.cc.net.http;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import cn.studio.cc.utils.LogUtils;

/**
 * http��Ӧ�̹߳���
 * @author CC
 *
 */
public class HttpAnswerThreadFactory implements ThreadFactory {
	/** �̳߳ر�� */
	private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
	/** �߳������߳��� */
	private final ThreadGroup group;
	/** �̱߳�� */
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	/** �߳�����ǰ׺ */
	private final String namePrefix;

	public HttpAnswerThreadFactory() {
		group = new ThreadGroup(Thread.currentThread().getThreadGroup(), "Http��Ӧ�߳���") {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				super.uncaughtException(t, e);
				/* �߳������̵߳�δ�����쳣�����м�¼ */
				LogUtils.error(e, t.getName() + "�����쳣");
			}
		};
		namePrefix = "Http��Ӧ�߳�(" + POOL_NUMBER.getAndIncrement() + ")";
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		/* ��ֹ�����ػ��߳� */
		if (t.isDaemon()) {
			t.setDaemon(false);
		}
		/* ��ֹ�Զ������ȼ� */
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}
}
