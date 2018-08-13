package service.cm.core.task;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import both.common.util.LoggerUtil;

public class TaskManager {

	/**
	 * 任务队列
	 */
	private Queue<ITask> queue = new LinkedList<ITask>();

	/**
	 * lock
	 */
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * instance
	 */
	private static TaskManager instance = new TaskManager();

	private boolean isStarted = false;

	/**
	 * getInstance
	 * 
	 * @return
	 */
	public static TaskManager getInstance() {
		return instance;
	}

	public void put(ITask task) {
		lock.writeLock().lock();
		queue.offer(task);
		lock.writeLock().unlock();
	}

	public void execute() {
		if (isStarted)
			return;
		isStarted = true;
		Thread th = new Thread(new Runnable() {

			public void run() {
				while (isStarted) {
					// LoggerUtil.debug("---------->开始执行任务");
					lock.readLock().lock();
					final ITask t = queue.poll();
					lock.readLock().unlock();

					if (t != null) {
						Thread th = new Thread(new Runnable() {

							public void run() {
								t.run();
							}
						});
						th.start();
					}

					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
					}
				}

			}
		});

		th.start();
	}
}