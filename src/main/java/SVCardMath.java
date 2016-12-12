public class SVCardMath {

    //chances of each card opened being a certain rarity. 8 cards are in a pack, and the 8th card cannot be bronze (odds added to silver).
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


    public SVCardMath(){}

    public static double getAverageVialsGained(String set){

        double averageVialsGained = 0.0;
        int duplicates;

        if (set.equalsIgnoreCase("Standard")) {

            duplicates = SVCollectionDB.getNumberOfDuplicates(set, "Legendary");
            averageVialsGained += (duplicates / LEGENDARIES_IN_STANDARD * LEGENDARY_VIAL_LIQUEFY + ((LEGENDARIES_IN_STANDARD - duplicates) / LEGENDARIES_IN_STANDARD) * LEGENDARY_VIAL_CREATE) * CHANCE_OF_LEGENDARY * 8;

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
