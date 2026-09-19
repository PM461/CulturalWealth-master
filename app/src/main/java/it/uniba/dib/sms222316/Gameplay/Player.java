package it.uniba.dib.sms222316.Gameplay;


import java.util.List;

import it.uniba.dib.sms222316.Rank.Points;


public class Player implements Cloneable {
    private String name;
    private int score, money;
    private int position;
    private int icon;
    private boolean prison;
    private boolean bankrupt;
    private int turnPrison;
    public Points point;


    public Player(String name, int icon,int  money) {
        this.point = new Points();
        bankrupt = false;
        this.name = name;
        this.icon = icon;
        score = 10;
        this.money = money;
        prison = false;
        turnPrison = 0;
        prison = false;
        this.position=0;
    }
    public Player() {
    }
    @Override
    public Player clone() {
        Player clonedPlayer = new Player(this.name, this.icon, this.money);
        clonedPlayer.point = new Points(this.score);
        clonedPlayer.score = this.score;
        clonedPlayer.position = this.position;
        clonedPlayer.prison = this.prison;
        clonedPlayer.bankrupt = this.bankrupt;
        clonedPlayer.turnPrison = this.turnPrison;

        return clonedPlayer;
    }

    public String getName() {return name;}

    public int getScore() {return score;}

    //TODO usa questa funzione prima di caricare i dati in database
    public void addScore() {score += point.getpoints();}

    public void setPosition(int position) {this.position = position;}

    public int getPosition() {return position;}

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


    public boolean isPrison() {return prison;}

    public void setPrison(boolean prison) {this.prison = prison;
    turnPrison=2;}

    public int getIcon() {
        return icon;
    }

    public int getTurnPrison() {return turnPrison;}

    public void setTurnPrison(int turnPrison) {this.turnPrison = turnPrison;}

    public void addMoney(int money) {this.money += money;}

    public void removeMoney(int money) {this.money -= money;}

    public void removeTurn() {turnPrison--;}
    public void setBankrupt() {this.bankrupt = true;}
    public boolean isBankrupt() {return bankrupt;}
}
