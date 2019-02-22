package org.big.test;

import java.util.concurrent.CountDownLatch;

public class ThreadLocalDemo {
	private static ThreadLocal<Integer> local = new ThreadLocal<>();
	static int value = 100;

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			new Thread() {
				public void run() {
					local.set(value);
					value = value + 1;
					System.out.println("1，" + Thread.currentThread().getId() + "，" + value);
					value = value + 1;
					System.out.println("2，" + Thread.currentThread().getId() + "，" + value);
				};
			}.start();
		}

	}

}
