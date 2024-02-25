package org.game;

public class Fight {
    private Card card1;
    private Card card2;
    private String fightlog = "";

    public Fight(Card card1, Card card2){
        this.card1 = card1;
        this.card2 = card2;
    };
    public double damagecalc(Card Attacker, Card Defender){
        double multiplier = 1;
        //element effectivities for if a spell is involved
        if("spell".equals(Attacker.getCardType()) || "spell".equals(Defender.getCardType())){
            if("Water".equals(Attacker.getElement()) && "Fire".equals(Defender.getElement())){
                multiplier = 2;
            }
            else if("Fire".equals(Attacker.getElement()) && "Regular".equals(Defender.getElement())){
                multiplier = 2;
            }
            else if("Regular".equals(Attacker.getElement()) && "Water".equals(Defender.getElement())){
                multiplier = 2;
            }
            else if("Water".equals(Attacker.getElement()) && "Regular".equals(Defender.getElement())){
                multiplier = 0.5;
            }
            else if("Regular".equals(Attacker.getElement()) && "Fire".equals(Defender.getElement())){
                multiplier = 0.5;
            }
            else if("Fire".equals(Attacker.getElement()) && "Water".equals(Defender.getElement())){
                multiplier = 0.5;
            }
        }
        //certain spells vs monsters that cancel out the elemental effects
        if("spell".equals(Attacker.getCardType()) && "creature".equals(Defender.getCardType())){
            if("Kraken".equals(Defender.getCreatureType())){
                multiplier = 0;
            }
            else if("Water".equals(Attacker.getElement()) && "Knight".equals(Defender.getCreatureType())){
                multiplier = -1; //this should be an insta kill, so I could either put just a huge number or a negative one and hardcode those to "autowin" later
            }
        }
        //creature multipliers
        if("creature".equals(Attacker.getCardType()) && "creature".equals(Defender.getCardType())){
            if("Goblin".equals(Attacker.getCreatureType()) && "Dragon".equals(Defender.getCreatureType())){
                multiplier = 0;
            }
            else if("Orc".equals(Attacker.getCreatureType()) && "Wizard".equals(Defender.getCreatureType())) {
                multiplier = 0;
            }
            else if("Dragon".equals(Attacker.getCreatureType()) && "Elf".equals(Defender.getCreatureType()) && "Fire".equals(Defender.getElement())){
                multiplier = 0;
            }
        }
        return multiplier * Attacker.getAttackValue();
    }

    public String GetFightLog(){
        return fightlog;
    }

    public Card Winner(){
        double c1dmg = damagecalc(card1,card2);
        double c2dmg = damagecalc(card2,card1);
        //For the fightlog
        String c1raw = String.valueOf(card1.getAttackValue());
        String c2raw = String.valueOf(card2.getAttackValue());
        //start writing fightlog
        fightlog = "%s: " + card1.getName() + " (" + c1raw + " Damage) vs %s: "
                + card2.getName() + " (" + c2raw + " Damage) => ";
        //instakill handling, both dmg being negative is impossible thus we need not check it
        if(c1dmg<0) {
            fightlog += card1.getName() + " wins instantly\n";
            return card1;
        }
        if(c2dmg<0) {
            fightlog += card2.getName() + " wins instantly\n";
            return card2;
        }
        //From here on out we know there's no instakill, thus we can start writing the log normally
        //if a spell was involved we need to log how the damage was adjusted based on the element
        if("spell".equals(card1.getCardType()) || "spell".equals(card2.getCardType())) {
            fightlog += c1raw + " VS " + c2raw +
                    " -> " + String.valueOf(c1dmg) + " VS " + String.valueOf(c2dmg) + " => ";
        }
        //declare and log winner by damage comparison
        if(c1dmg < c2dmg){
            fightlog += card2.getName() + " wins\n";
            return card2;
        }
        if(c1dmg > c2dmg){
            fightlog += card1.getName() + " wins\n";
            return card1;
        }
        else{
            fightlog += "Draw\n";
            return null;
        }
    }
}
