package com.dice;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Phaser;
import java.util.function.Function;
import java.util.function.Supplier;

public class Dice{

    public static void main(String[] args){
        List<Player> players = 
//            Arrays.asList(new Player("Jerry"),new Player("Jimmi"),new Player("John"),new Player("Jack"),new Player("Jonny"),new Player("Jaxon"),new Player("Joshua"),new Player("Jo"));
//            Arrays.asList(new Player("Jerry"),new Player("Jimmi"),new Player("John"),new Player("Jack"),new Player("Jonny"),new Player("Jaxon"));
            Arrays.asList(new Player("Jerry"),new Player("Jimmi"),new Player("John"),new Player("Jack"));
        Phaser ph = new Phaser(0){
            int round =1;
            boolean end = false;
            {System.out.println("\n\n\t\t--- ROUND "+round+" --- ");}
            @Override
            protected boolean onAdvance(int phase,int registeredParties){
                taskManager(this,players);
                end = players.stream().parallel().anyMatch(p -> p.getTotalScore()==5);
                if(!end) System.out.println("\n\t\t--- ROUND "+(++round)+" --- ");
                return end;
            }  
        };
        players.forEach(p -> p.setPhaser(ph));
        players.stream().parallel().map(player -> task.apply(ph).apply(player))
            .map(t -> CompletableFuture.supplyAsync(t)
                .thenAcceptAsync(System.out::println))
            .forEach(CompletableFuture::join);
        System.out.println("\n\tGAME WINNER IS - "+gameWinner(players));
    }
    public static Function<Phaser,Function<Player, Supplier<Player>>> task = 
    phaser -> player -> () -> {
        System.out.println("\t\tI'm "+player.getName()+", and I have own Thread "+Thread.currentThread().getName());
        while(!phaser.isTerminated()) {
            roundTarget(player);   
            phaser.arriveAndAwaitAdvance();
        }
        return player;
    };
    public static void taskManager(Phaser phaser, List<Player> list){
        Player roundWinner = roundWinner(list);
        roundWinner.totalScoreInc();
        System.out.println("\n\tWinner is "+roundWinner.getName());
    }    
    public static void roundTarget(Player p) {
        int target;
        p.setRoundTries(0);
        SecureRandom rand = new SecureRandom();
        do {
            target = rand.nextInt(1,5);
//            target = rand.nextInt(1,7);
            p.totalTriesInc();
            p.roundTriesInc();
            System.out.println("player "+p.getName()+" drop "+target);
        } while(target != 4);
        System.out.println("\t"+p.getName()+" has a "+p.getRoundTries()+" round tries\n\t"+p);
    }
    public static Player roundWinner(List<Player> players) {
        Comparator<Player> comp = (a,b) -> a.getRoundTries()-b.getRoundTries();
        Supplier<Player> sup = () -> new Player("None");
        Player min1 = players.stream().min(comp).orElseGet(sup);
        Player min2 = players.stream().filter(me -> min1 != me).min(comp).orElseGet(sup);
        return min1.getRoundTries() != min2.getRoundTries()? min1 : sup.get();
    }
    public static Player gameWinner(List<Player> players) {
        Comparator<Player> compare = ((Comparator<Player>) (a,b) -> a.getTotalScore() - b.getTotalScore())
                .thenComparing((a,b) -> b.getTotalTries() - a.getTotalTries());
        return players.stream().max(compare).orElseGet(() -> new Player("There are no winner! Is Drow"));    
    }
}