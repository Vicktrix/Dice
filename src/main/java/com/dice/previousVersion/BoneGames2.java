package com.dice.previousVersion;

import java.util.Random;

public class BoneGames2{
    public static int numPlayers =0;
    public static int round =0;
    public static void main(String[] args){
        BoneGames2 game = new BoneGames2();
        Player2 p1 = game.getPlayer("John");
        Player2 p2 = game.getPlayer("Jerry");
        Player2 p3 = game.getPlayer("Jimmi");
        System.out.println("We have a "+numPlayers+" players : "+p1.getName()
            +", "+p2.getName()+" and "+p3.getName());
        game.gameStart(p1,p2,p3);
    }
    public Player2 getPlayer(String name) {
        Player2 player=new Player2(name, ++numPlayers);
        return player;
    }
    public void gameStart(Player2 p1, Player2 p2, Player2 p3) {
        System.out.println("Game 'BoneGame2' Starts!");
        do {
            round(++round,p1,p2,p3);
        } while(p1.getTotalScore() < 10 
                && p2.getTotalScore() < 10
                && p3.getTotalScore() < 10); 
        System.out.println(p1.getName()+" has total score "+p1.getTotalScore()+", was dropped "+p1.getTotalTries());
        System.out.println(p2.getName()+" has total score "+p2.getTotalScore()+", was dropped "+p2.getTotalTries());
        System.out.println(p3.getName()+" has total score "+p3.getTotalScore()+", was dropped "+p3.getTotalTries());
    }
    public void round(int round, Player2 p1, Player2 p2, Player2 p3) {
        System.out.println("\n Round "+round+"\n");
        getTarget(p1);
        getTarget(p2);
        getTarget(p3);
        Player2 winer=takeRoundWinner(p1,p2,p3);
        System.out.println("Winner is "+winer.getName());
        System.out.print("and he has "+winer.getRoundTries()+" round Score");
        System.out.println(", and "+winer.getTotalScore()+" total score!\n\n");
    }
    public void getTarget(Player2 p) {
        int rand;
        p.setRoundTries(0);
        do {    
            rand = p.getRand();
            p.totalInc();
            p.roundInc();
            System.out.print("\nplayer "+p.getName()+" drop "+rand);
        } while(rand != 4);
        System.out.println("\n________________________________");
    }
    public Player2 takeRoundWinner(Player2 p1, Player2 p2, Player2 p3) {
        if((p1.getRoundTries() == p2.getRoundTries() && p2.getRoundTries() <= p3.getRoundTries())
            || (p1.getRoundTries() == p3.getRoundTries() && p3.getRoundTries() <= p2.getRoundTries())
            || (p3.getRoundTries() == p2.getRoundTries() && p2.getRoundTries() <= p1.getRoundTries())) {
            return new Player2("None",0);
        }
        Player2 min = (p1.getRoundTries() < p2.getRoundTries())? p1 : p2;
        min = (min.getRoundTries() < p3.getRoundTries())? min : p3;
        min.setRoundScore(min.getRoundTries());
        min.setTotalScore(min.getTotalScore()+1);
        return min;
    }
}
class Player2{
    private String name;
    private int playerNum;
    private int totalScore =0;
    private int roundScore =0;
    private int totalTries =0;
    private int roundTries =0;    
    public Player2(String name, int playerNum) {
        this.name = name;
        this.playerNum = playerNum;
    }
    public int getRand() {
        return (new Random()).nextInt(4)+1;
    }
    public void totalInc() {
        this.totalTries++;
    }
    public void roundInc() {
        this.roundTries++;
    }

    public String getName(){
        return name;
    }

    public int getTotalScore(){
        return totalScore;
    }

    public int getTotalTries(){
        return totalTries;
    }

    public void setTotalScore(int totalScore){
        this.totalScore=totalScore;
    }

    public void setRoundTries(int roundTries){
        this.roundTries=roundTries;
    }

    public int getRoundTries(){
        return roundTries;
    }

    public void setRoundScore(int roundScore){
        this.roundScore=roundScore;
    }
}