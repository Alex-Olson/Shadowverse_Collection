/**
 * SVCardMath handles Shadowverse related math functions.
 * The currency referenced here are "vials", which can be used to make cards, or can be gained by "liquefying"
 * them. The amount of vials it takes to make a card (or the amount you get for
 * liquefying) depends on rarity, with higher amounts for rarer cards.
 * The rarities in order of most common to rarest are Bronze, Silver, Gold, and Legendary.
 *
 *
 */
class SVCardMath {

    //chances of each card opened being a certain rarity. 8 cards are in a pack, and the 8th card cannot be bronze (bronze odds added to silver).
    private static final double CHANCE_OF_LEGENDARY = .015;
    private static final double CHANCE_OF_GOLD = .06;
    private static final double CHANCE_OF_SILVER_NOT_8TH = .25;
    private static final double CHANCE_OF_SILVER_8TH = .925;
    private static final double CHANCE_OF_BRONZE = .675;
    //number of cards in each rarity in the standard/darkness sets. https://steam.shadowverse.com/drawrates/
    private static final double LEGENDARIES_IN_STANDARD = 24;
    private static final double GOLDS_IN_STANDARD = 69;
    private static final double SILVERS_IN_STANDARD = 99;
    private static final double BRONZES_IN_STANDARD = 126;

    private static final double LEGENDARIES_IN_DARKNESS = 9;
    private static final double GOLDS_IN_DARKNESS = 23;
    private static final double SILVERS_IN_DARKNESS = 31;
    private static final double BRONZES_IN_DARKNESS = 46;
    //card create/liquefy vial values
    private static final double LEGENDARY_VIAL_LIQUEFY = 1000;
    private static final double GOLD_VIAL_LIQUEFY = 250;
    private static final double SILVER_VIAL_LIQUEFY = 50;
    private static final double BRONZE_VIAL_LIQUEFY = 10;

    private static final double LEGENDARY_VIAL_CREATE = 3500;
    private static final double GOLD_VIAL_CREATE = 800;
    private static final double SILVER_VIAL_CREATE = 200;
    private static final double BRONZE_VIAL_CREATE = 50;

    /**
     * Returns a double value that represents the average amount of vial value you will gain opening a pack from a set.
     *
     * The calculation is made with the goal of completing your collection (have 3 of all cards).
     * With that goal in mind, it is assumed in every pack that you will liquefy every copy you gain of a card
     * after the third, to use for cards you don't have 3 of already.
     *
     * Formula for how much dust value gained you get from one pack is:
     * (the odds you get a card of a certain rarity you have 3 or more of * the liquefy dust value of that rarity
     * + (the odds of getting a card you don't have 3 of * the create dust value of that rarity))
     * * the average amount of cards of that rarity you will pull in a pack
     * added to the dust values gained from the same formula for all other rarities.
     *
     * @param set The string of the set (Currently only two, Standard/Darkness)
     *            you want to evaluate the vial value of.
     * @return a double representing the average vials gained from opening a pack from
     * the set the user provided in the argument.
     */
    static double getAverageVialsGained(String set){

        double averageVialsGained = 0.0;
        int duplicates;

        if (set.equalsIgnoreCase("Standard")) {

            duplicates = SVCollectionDB.getNumberOfDuplicates(set, "Legendary");
            //(the odds you get a card of a certain rarity you have 3 or more of * the liquefy dust value of that rarity
            averageVialsGained += (duplicates / LEGENDARIES_IN_STANDARD * LEGENDARY_VIAL_LIQUEFY
                    //+ (the odds of getting a card you don't have 3 of * the create dust value of that rarity))
                    + ((LEGENDARIES_IN_STANDARD - duplicates) / LEGENDARIES_IN_STANDARD) * LEGENDARY_VIAL_CREATE)
                    //* the average amount of cards of that rarity you will pull in a pack
                    * CHANCE_OF_LEGENDARY * 8;

            //added to the dust values gained from the same formula for all other rarities.
            duplicates = SVCollectionDB.getNumberOfDuplicates(set, "Gold");
            averageVialsGained += (duplicates / GOLDS_IN_STANDARD * GOLD_VIAL_LIQUEFY + ((GOLDS_IN_STANDARD - duplicates) / GOLDS_IN_STANDARD) * GOLD_VIAL_CREATE) * CHANCE_OF_GOLD * 8;

            duplicates = SVCollectionDB.getNumberOfDuplicates(set, "Silver");
            averageVialsGained += (duplicates / SILVERS_IN_STANDARD * SILVER_VIAL_LIQUEFY + ((SILVERS_IN_STANDARD - duplicates) / SILVERS_IN_STANDARD) * SILVER_VIAL_CREATE) * (CHANCE_OF_SILVER_NOT_8TH * 7 + CHANCE_OF_SILVER_8TH);

            duplicates = SVCollectionDB.getNumberOfDuplicates(set, "Bronze");
            averageVialsGained += (duplicates / BRONZES_IN_STANDARD * BRONZE_VIAL_LIQUEFY + ((BRONZES_IN_STANDARD - duplicates) / BRONZES_IN_STANDARD) * BRONZE_VIAL_CREATE) * CHANCE_OF_BRONZE * 7;
        } else if (set.equalsIgnoreCase("Darkness")){

            duplicates = SVCollectionDB.getNumberOfDuplicates("Darkness", "Legendary");
            averageVialsGained += (duplicates/LEGENDARIES_IN_DARKNESS * LEGENDARY_VIAL_LIQUEFY + ((LEGENDARIES_IN_DARKNESS - duplicates)/LEGENDARIES_IN_DARKNESS) * LEGENDARY_VIAL_CREATE) * CHANCE_OF_LEGENDARY * 8;

            duplicates = SVCollectionDB.getNumberOfDuplicates("Darkness", "Gold");
            averageVialsGained += (duplicates/GOLDS_IN_DARKNESS * GOLD_VIAL_LIQUEFY + ((GOLDS_IN_DARKNESS - duplicates)/GOLDS_IN_DARKNESS) * GOLD_VIAL_CREATE) * CHANCE_OF_GOLD * 8;

            duplicates = SVCollectionDB.getNumberOfDuplicates("Darkness", "Silver");
            averageVialsGained += (duplicates/SILVERS_IN_DARKNESS * SILVER_VIAL_LIQUEFY + ((SILVERS_IN_DARKNESS - duplicates)/SILVERS_IN_DARKNESS) * SILVER_VIAL_CREATE) * (CHANCE_OF_SILVER_NOT_8TH * 7 + CHANCE_OF_SILVER_8TH);

            duplicates = SVCollectionDB.getNumberOfDuplicates("Darkness", "Bronze");
            averageVialsGained += (duplicates/BRONZES_IN_DARKNESS * BRONZE_VIAL_LIQUEFY + ((BRONZES_IN_DARKNESS - duplicates)/BRONZES_IN_DARKNESS) * BRONZE_VIAL_CREATE) * CHANCE_OF_BRONZE * 7;
        }
        return averageVialsGained;
    }


}
