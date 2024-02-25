package org.game;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private final String username;
    private ArrayList<Card> deck;

    public Deck(String username, Card card1, Card card2, Card card3, Card card4) {
        this.username = username;
        this.deck = new ArrayList<>();
        deck.add(card1);
        deck.add(card2);
        deck.add(card3);
        deck.add(card4);
    }

    public String GetUsername(){
        return username;
    }

    public int GetDecksize(){
        return deck.size();
    }

    public String viewDeck(){
        String output = "";
        for (Card card : deck) {
            output = output + card.getName() + ": " + String.valueOf(card.getAttackValue()) + " damage\n";
        }
        return output;
    }

    public Card getFirstCard() {
        return deck.isEmpty() ? null : deck.get(0);
    }

    public void addCard(Card card) {
        deck.add(card);
    }

    public void removeFirstCard() {
        if (!deck.isEmpty()) {
            deck.remove(0);
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }
}

