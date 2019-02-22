package org.big.test;

public class ThreadLocalCustomer {

	ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

	public ThreadLocalCustomer(int value) {
		threadLocal.set(value);
	}

}
