package com.dice.reactStyle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

    // SubmissionPublisher NOT PERSISTENT WITH submit
public class DiceReact{

    public static void main(String[] args){
        // USE maxStorage = 1, LIKE ROUND, AND publisher.submit(j), INSTEAD USE PHASER
        int maxStorage = 1;
        List<Subscriber> list =  Arrays.asList(new Subscriber("Slow",800), new Subscriber("Norm",500), 
                new Subscriber("Fast",200)
                , new Subscriber("Slow",2000), new Subscriber("-- Slow --",2500)
//                , new Subscriber("--Norm--",1400), new Subscriber("AND",1800), new Subscriber("tyyty",2000)
        );
        publisherInside(maxStorage,list);
    }
    
    public static void publisherInside(int storage, List<Subscriber> list) {
        SubmissionPublisher<Integer> publisher = 
                new SubmissionPublisher<>(ForkJoinPool.commonPool(), storage);
        long start = System.currentTimeMillis();
        list.forEach(i -> publisher.subscribe(i));
        IntStream.range(0,10).forEach(j -> publisher.submit(j));
        publisher.close();
        waitForThreads(list);
        long end=System.currentTimeMillis();
        System.out.println("time "+(end-start));
        System.out.println("And get = "+Subscriber.counter.get()); //looking for drops/errors
    }
    public static void waitForThreads(final List<Subscriber> list) {
        boolean wait = true;
        try{
        while(wait) {
            wait = !list.stream().allMatch(s -> s.imSleep);
            TimeUnit.MILLISECONDS.sleep(500);
        }
        }catch(InterruptedException ex){
            System.out.println("Error in main delay - "+ex);
        }
    }
}
class Subscriber implements Flow.Subscriber<Integer> {
    public static AtomicInteger counter = new AtomicInteger(0);
    Flow.Subscription subs;
    private String name;
    int delay;
    boolean imSleep = true;
    public Subscriber(String str, int time) {
        name = str;
        delay = time;
    }
    @Override
    public void onSubscribe(Flow.Subscription subscription){
        this.subs = subscription;
        imSleep = false;
        System.out.println(name+" is subscribe");
        System.out.println(name+" Thread name "+Thread.currentThread().getName());
        subs.request(1);
    }

    @Override
    public void onNext(Integer item){
        System.out.println(name+" take "+item);
        counter.getAndIncrement();
        delay();
        subs.request(1);
    }

    @Override
    public void onError(Throwable throwable){
        imSleep = true;
        System.out.println("Name : \t\t"+name+" onError");
    }

    @Override
    public void onComplete(){
        System.out.println(name+"\t\t onComplete! "+Thread.currentThread().getName());
        imSleep = true;
    }
    public void delay() {
        try{
            TimeUnit.MILLISECONDS.sleep(delay);
        }catch(InterruptedException ex){
            System.out.println("Error in TIME DELAY "+ex);
        }
    }
    public String getName(){
        return name;
    }   
}