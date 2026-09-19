package it.uniba.dib.sms222316.Rank;


public class Points {
    int punteggio;
    float increment;

    public Points ()
    {
        this.punteggio = 0;
        this.increment = 1;
    }
    public Points (int punteggio)
    {
        this.punteggio = punteggio;
        this.increment =1;
    }

    public int getpoints()
    {
        return this.punteggio;
    }

    public void Dicepoint(int dice1 , int dice2)
    {
        int tot = dice1 + dice2;
        if (dice1 == dice2)this.punteggio+=(int)tot*2*this.increment;
        else  this.punteggio+=(int)tot*this.increment;

    }

    public void Dicepoint(boolean isprison)
    {
        this.punteggio+=(int)10*this.increment;
    }

    public void BuyPropPoints(int group)
    {
        this.punteggio +=(int)10 * group*this.increment;
    }

    public void BuyMuseumPoints()
    {
        this.punteggio +=(int)50*this.increment;
    }
    public void changePoints()
    {
        //implementare prima il metodo change
    }

    public void paypoints(int pay, boolean allgroup)
    {
        if (allgroup) this.punteggio+= (int)pay*0.5*this.increment;
        else this.punteggio+= (int)pay*0.25*this.increment;
    }

    public void doublejailexit()
    {
        this.punteggio +=(int)50*this.increment;
    }
    public void injailpoint()
    {
        this.punteggio -=(int)10;
    }

    public void resetpoint()
    {
        this.punteggio =(int)0;
    }

    public void setIncrement(float cre)
    {
        this.increment = 1*cre;
    }




}
