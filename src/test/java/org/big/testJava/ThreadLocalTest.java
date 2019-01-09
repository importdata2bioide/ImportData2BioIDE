package org.big.testJava;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalTest {
	static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 7, 1, TimeUnit.MINUTES,
			new ArrayBlockingQueue<Runnable>(5));

	@SuppressWarnings("unused")
	public static void main(String[] args) throws InterruptedException {
		ThreadLocal<List<Integer>> connectionHolder = new ThreadLocal<List<Integer>>();
		@SuppressWarnings("unused")
		List<Integer> allList = new ArrayList<>();
		List<Integer> list = new ArrayList<>();
		for (int i = 1; i <= 20; i++) {
			list.add(i);
			if (list.size() == 1) {
				List<Integer> cpoylist = new ArrayList<>();
				cpoylist.addAll(list);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							
//							connectionHolder.set(cpoylist);
//							System.out.println(Thread.currentThread().getName()+":"+cpoylist.size());
//							for (Integer i : cpoylist) {
//								System.out.println(Thread.currentThread().getName()+"___"+i);
//							}
							System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目："
									+ executor.getQueue().size() + "，已执行完的任务数目：" + executor.getCompletedTaskCount());
						} catch (Exception e) {
							System.out.println("error IA001 ：" + e.getMessage());
						}
					}
				});
				list.clear();
				System.out.println("clear 后"+list.size());
			}
		}
		executor.shutdown();
	
		while (true) {
			if (executor.isTerminated()) {
				System.out.println("executor，执行完了");
				break;
			}
		}


	}

}
