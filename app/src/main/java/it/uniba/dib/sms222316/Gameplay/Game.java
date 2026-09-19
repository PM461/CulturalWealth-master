package it.uniba.dib.sms222316.Gameplay;



import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game implements Cloneable {
        private final List<Player> players;
        private final List<Property> properties;
        private Date Date;
        private int TurnNumber;

    private int currentPlayerIndex;
        private boolean gameStarted;

        public Game(List<Player> players, List<Property> properties) {
            this.Date = Date.from(Instant.now());
            TurnNumber=1;
            this.players = players;
            this.properties = properties;
            currentPlayerIndex = 0;
            gameStarted = false;
        }
        public Game() {
            this.players = new ArrayList<>();
            this.properties = new ArrayList<>();
            this.Date = Date.from(Instant.now());
        }
        @Override
        public Game clone() {
            List<Player> clonedPlayers = new ArrayList<>();
            for (Player player : players) {
                clonedPlayers.add(player.clone()); // Assuming Player class implements Cloneable
            }

            List<Property> clonedProperties = new ArrayList<>();
            for (Property property : properties) {
                clonedProperties.add(property.clone()); // Assuming Property class implements Cloneable
            }

            Game clonedGame = new Game(clonedPlayers, clonedProperties);
            clonedGame.setCurrentPlayerIndex(this.currentPlayerIndex);
            clonedGame.setGameStarted(this.gameStarted);
            clonedGame.Date=this.Date;
            clonedGame.TurnNumber= TurnNumber;

            return clonedGame;
        }


    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    public void setCurrentPlayerIndex(int currentPlayerIndex) { this.currentPlayerIndex = currentPlayerIndex; }
    public void endTurn(List<Player> players) {

        if (players.stream().filter(l-> !l.isBankrupt()).count()<=1) {
            terminaPartita();
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        TurnNumber+=1;

        if(players.get(currentPlayerIndex).isBankrupt()) endTurn(players);

//
//        if(players.get(currentPlayerIndex).isBankrupt() && players.stream().filter(l-> !l.isBankrupt()).count()>1){
//            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
//            endTurn(players);
//        }
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void iniziaPartita() {
        gameStarted = true;
        // Inizializza il gioco, distribuisci le proprietà, assegna i soldi iniziali ai giocatori, ecc.
    }

    public void terminaPartita() {
        gameStarted = false;
        // Esegui le operazioni di pulizia o salvataggio dei dati alla fine della partita
    }


    public void gestisciAcquisto(Player player, Property property) {
            /**To do alert when there is no money to buy*/
        int prezzoProprieta = property.getCosto();
        if (player.getMoney() >= prezzoProprieta) {
            //player.addProperty(property);     ridondante salvarsi la lista delle proprietà se il giocatore è salvato nelle proprietà
            player.removeMoney(prezzoProprieta);
            property.setGiocatore(player);

        }
    }

    public void gestisciPagamentoAffitto( Property property) {


        int affitto = property.getAffitto(property.getPaints());

        Player player = players.get(currentPlayerIndex);
        if (HaveGroup(property.getGruppo() , player))affitto*=2;
        property.getGiocatore().addMoney(affitto);

        if (player.getMoney() >= affitto) {
            player.removeMoney(affitto);
        } else {
            player.setBankrupt();
            /**To do manage mortage to sell*/
            // Il giocatore non ha abbastanza denaro per pagare l'affitto
            // ... gestisci la situazione di insolvenza del giocatore
        }
    }

    public boolean HaveGroup(String Group , Player player)
    {

        boolean flag = false;
        for ( Property prop: properties)
        {
            if (prop.getTipo().equals("monument") && prop.getGiocatore()!=null) {
                if (prop.getGruppo().equals(Group) && prop.getGiocatore().equals(player)) flag = true;
                else return false;
            }
        }
        return  flag;
    }
    public void prossimoTurno() {
        if (!gameStarted) {
            //
        }


        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public int[] dadi() {

        if (!gameStarted) {

        }
        int[] randomNumber= new int[2];
        randomNumber[0] = (int) (Math.random() * 6) + 1;
        randomNumber[1] = (int) (Math.random() * 6) + 1;
        return randomNumber;
    }

    public boolean isPartitaFinita() {
        return !gameStarted;
    }
    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public int getNumberOfPlayers() {
        return this.players.size();
    }

    public List<Property> getProperties() {
        return properties;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int NumberOfPlayers()
    {
        return players.size();
    }


    public java.util.Date getDate() {
        return Date;
    }

    public int getTurnNumber() {
        return TurnNumber;
    }
}


