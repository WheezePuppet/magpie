package edu.umw.cpsc.magpie.core;

public class CardAndReason {
    private Card theCard;
    private String theReason;

    public CardAndReason(Card c, String r) {
        theCard = c;
        theReason = r;
    }

    public Card getCard() { return theCard; }
    public String getReason() { return theReason; }

}
