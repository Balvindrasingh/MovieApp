package com.example.MovieApp;

public class ThreadStandAloneExample {

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    System.out.printf("Thread: %s count: %d%n", Thread.currentThread().getName(), i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        Thread standAloneThread = new Thread("StandAloneThread"){
            @Override
            public void run(){
                for (int i = 1; i <= 5; i++) {
                    System.out.printf("Thread: %s count: %d%n", Thread.currentThread().getName(), i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };



        //Create Thread objects
        Thread thread1 = new Thread(runnable,"Thread 1");
        Thread thread2 = new Thread(runnable,"Thread 2");
        Thread thread3 = new Thread(runnable,"Thread 3");
        Thread thread4 = new Thread(runnable,"Thread 4");
        Thread thread5 = new Thread(runnable,"Thread 5");
        standAloneThread.start();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

    }
}
