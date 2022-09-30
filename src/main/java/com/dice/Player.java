package com.dice;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Player {
    private String name;
    private int totalScore;
    private int roundTries;
    private int totalTries;
    private Phaser phaser;
    public Player(String name){
        this.name=name;
    }
    public void delay(int milSec) {
        try{ TimeUnit.MILLISECONDS.sleep(milSec);
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }
    public Player show(int sec) {
        long start=System.currentTimeMillis();
        delay(sec);
        long end=System.currentTimeMillis();
        System.out.println("I'm here about "+(end-start)/1000+"sec.");
        System.out.println("Thread name "+Thread.currentThread().getName());
        return this;
    }
    public void setPhaser(Phaser phaser){
        this.phaser=phaser;
        this.phaser.register();
//        delay(1000);
    }
    public int getTotalTries(){
        return totalTries;
    }
    public void setTotalTries(int totalTries){
        this.totalTries=totalTries;
    }
    public String getName(){
        return name;
    }
    public void totalTriesInc() {
        this.totalTries++;
    }
    public void roundTriesInc() {
        this.roundTries++;
    }
    public int totalScoreInc() {
        return ++totalScore;
    }
    public int getTotalScore(){
        return totalScore;
    }
    public void setTotalScore(int totalScore){
        this.totalScore=totalScore;
    }
    public int getRoundTries(){
        return roundTries;
    }
    public void setRoundTries(int roundTries){
        this.roundTries=roundTries;
    }
    @Override
    public String toString(){
        return "Player{"+"name="+name+", totalScore="+totalScore
            +", roundTries="+roundTries+", totalTries="+totalTries+'}';
    }
}