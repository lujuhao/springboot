package com.mySpringBoot;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSecurity implements Runnable{

	private int ticket = 100;
	
	Lock lock = new ReentrantLock();
	
	@Override
	public void run(){
		while(true){
			lock.lock();
			if (ticket > 0) {
				try {
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ticket -- ;
				System.out.println(Thread.currentThread().getName()+"卖出一张票，当前剩余："+ticket);
				lock.unlock();
			}else{
				System.out.println("今天票卖完了");
				lock.unlock();
				break;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	
	public static void main(String[] args) {
		ThreadSecurity threadSecurity = new ThreadSecurity();
		
		Thread window1 = new Thread(threadSecurity, "窗口1");
		Thread window2 = new Thread(threadSecurity, "窗口2");
		Thread window3 = new Thread(threadSecurity, "窗口3");
		
		window1.start();
		window2.start();
		window3.start();
	}
}
