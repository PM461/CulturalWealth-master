package it.uniba.dib.sms222316;

import android.util.Log;

import java.util.Date;

public class SavedGames {

    Long PlayerNumber;
    Date CreationDate;
    Long Numberofturn;
    String GameId;
    String Uid;


    public SavedGames(String uid , String gameId , Long numberofturn , Date creationDate , Long playerNumber) {
        Uid = uid;
        GameId = gameId;
        Numberofturn = numberofturn;
        CreationDate = creationDate;
        PlayerNumber =playerNumber;
    }

    public Long getPlayerNumber() {
        return PlayerNumber;
    }

    public void setPlayerNumber(Long playerNumber) {
        PlayerNumber = playerNumber;
    }

    public Date getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(Date creationDate) {
        CreationDate = creationDate;
    }

    public Long getNumberofturn() {
        return Numberofturn;
    }

    public void setNumberofturn(Long numberofturn) {
        Numberofturn = numberofturn;
    }

    public String getGameId() {
        return GameId;
    }

    public void setGameId(String gameId) {
        GameId = gameId;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
