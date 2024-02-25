package org.game;

public class Battle {
    private Deck deck1;
    private Deck deck2;
    private String battlelog ="";

    public Battle(Deck deck1, Deck deck2){
        this.deck1 = deck1;
        this.deck2 = deck2;
    }

    public String GetBattlelog(){
        return battlelog;
    }

    public String determineWinner(){
        int count = 0;
        String result = "draw";
        while(count<1000){
            deck1.shuffle();
            deck2.shuffle();
            Card topdeck1 = deck1.getFirstCard();
            Card topdeck2 = deck2.getFirstCard();
            Fight Battleround = new Fight(topdeck1, topdeck2);
            Card winner = Battleround.Winner(); //can be null in case of a draw, dann springt einfach keiner der nächsten fälle an
            if(topdeck1.equals(winner)){ //case player1 wins -> the revealed card from p2 (topdeck2) is added to p1 deck, and is removed from p2 deck where it is still the top card
                deck1.addCard(topdeck2);
                deck2.removeFirstCard();
            } else if (topdeck2.equals(winner)) { //vice versa
                deck2.addCard(topdeck1);
                deck1.removeFirstCard();
            }
            //schreiben fightlog in battlelog und replacen dabei gleich die Placeholder für die Namen (weil Fight keine Ahnung hat, von wem die decks sind)
            battlelog += String.format(Battleround.GetFightLog(), deck1.GetUsername(), deck2.GetUsername());
            //schreiben score in battlelog
            battlelog += deck1.GetUsername() + ": " + deck1.GetDecksize() + " Cards in deck, " + deck2.GetUsername() + ": " + deck2.GetDecksize() + " Cards in deck\n";
            //determine if the match has concluded
            if(deck1.getFirstCard() == null){
                result = deck2.GetUsername();
                break;
            } else if(deck2.getFirstCard() == null){
                result = deck1.GetUsername();
                break;
            }
            ++count;
        }
        return result;
    }
}

