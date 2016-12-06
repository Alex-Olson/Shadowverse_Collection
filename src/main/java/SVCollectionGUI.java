import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class SVCollectionGUI extends JFrame {
    private JPanel rootPanel;
    private JTable svCollectionTable;
    private JButton countButton;
    //chances of each card opened being a certain rarity. 8 cards are in a pack, and the 8th card cannot be bronze (odds added to silver).
    final double CHANCE_OF_LEGENDARY = .015;
    final double CHANCE_OF_GOLD = .06;
    final double CHANCE_OF_SILVER_NOT_8TH = .25;
    final double CHANCE_OF_SILVER_8TH = .925;
    final double CHANCE_OF_BRONZE = .675;
    //number of cards in each rarity in the standard/darkness sets. https://steam.shadowverse.com/drawrates/
    final double LEGENDARIES_IN_STANDARD = 24;
    final double GOLDS_IN_STANDARD = 69;
    final double SILVERS_IN_STANDARD = 99;
    final double BRONZES_IN_STANDARD = 126;

    final double LEGENDARIES_IN_DARKNESS = 9;
    final double GOLDS_IN_DARKNESS = 23;
    final double SILVERS_IN_DARKNESS = 31;
    final double BRONZES_IN_DARKNESS = 46;
    //card create/liquefy vial values
    final double LEGENDARY_VIAL_LIQUEFY = 1000;
    final double GOLD_VIAL_LIQUEFY = 250;
    final double SILVER_VIAL_LIQUEFY = 50;
    final double BRONZE_VIAL_LIQUEFY = 10;

    final double LEGENDARY_VIAL_CREATE = 3500;
    final double GOLD_VIAL_CREATE = 800;
    final double SILVER_VIAL_CREATE = 200;
    final double BRONZE_VIAL_CREATE = 50;

    SVCollectionGUI(final SVCollectionDM svCollectionDM) {
        super("Shadowverse Collection");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        svCollectionTable.setModel(svCollectionDM);
        //sorting stuff found here: http://stackoverflow.com/questions/28823670/how-to-sort-jtable-in-shortest-way
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(svCollectionTable.getModel());
        svCollectionTable.setRowSorter(rowSorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        rowSorter.setSortable(5, false);
        rowSorter.setSortKeys(sortKeys);

        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double avgDustGainStandard = 0.0;
                double avgDustGainDarkness = 0.0;

                int duplicatesStandard = SVCollectionDB.getNumberOfDuplicates("Standard", "Legendary");
                avgDustGainStandard += (duplicatesStandard/LEGENDARIES_IN_STANDARD * LEGENDARY_VIAL_LIQUEFY + ((LEGENDARIES_IN_STANDARD - duplicatesStandard)/LEGENDARIES_IN_STANDARD) * LEGENDARY_VIAL_CREATE) * CHANCE_OF_LEGENDARY * 8;

                duplicatesStandard = SVCollectionDB.getNumberOfDuplicates("Standard", "Gold");
                avgDustGainStandard += (duplicatesStandard/GOLDS_IN_STANDARD * GOLD_VIAL_LIQUEFY + ((GOLDS_IN_STANDARD - duplicatesStandard)/GOLDS_IN_STANDARD) * GOLD_VIAL_CREATE) * CHANCE_OF_GOLD * 8;

                duplicatesStandard = SVCollectionDB.getNumberOfDuplicates("Standard", "Silver");
                avgDustGainStandard += (duplicatesStandard/SILVERS_IN_STANDARD * SILVER_VIAL_LIQUEFY + ((SILVERS_IN_STANDARD - duplicatesStandard)/SILVERS_IN_STANDARD) * SILVER_VIAL_CREATE) * (CHANCE_OF_SILVER_NOT_8TH * 7 + CHANCE_OF_SILVER_8TH);

                duplicatesStandard = SVCollectionDB.getNumberOfDuplicates("Standard", "Bronze");
                avgDustGainStandard += (duplicatesStandard/BRONZES_IN_STANDARD * BRONZE_VIAL_LIQUEFY + ((BRONZES_IN_STANDARD - duplicatesStandard)/BRONZES_IN_STANDARD) * BRONZE_VIAL_CREATE) * CHANCE_OF_BRONZE * 7;


                int duplicatesDarkness = SVCollectionDB.getNumberOfDuplicates("Darkness", "Legendary");
                avgDustGainDarkness += (duplicatesDarkness/LEGENDARIES_IN_DARKNESS * LEGENDARY_VIAL_LIQUEFY + ((LEGENDARIES_IN_DARKNESS - duplicatesDarkness)/LEGENDARIES_IN_DARKNESS) * LEGENDARY_VIAL_CREATE) * CHANCE_OF_LEGENDARY * 8;

                duplicatesDarkness = SVCollectionDB.getNumberOfDuplicates("Darkness", "Gold");
                avgDustGainDarkness += (duplicatesDarkness/GOLDS_IN_DARKNESS * GOLD_VIAL_LIQUEFY + ((GOLDS_IN_DARKNESS - duplicatesDarkness)/GOLDS_IN_DARKNESS) * GOLD_VIAL_CREATE) * CHANCE_OF_GOLD * 8;

                duplicatesDarkness = SVCollectionDB.getNumberOfDuplicates("Darkness", "Silver");
                avgDustGainDarkness += (duplicatesDarkness/SILVERS_IN_DARKNESS * SILVER_VIAL_LIQUEFY + ((SILVERS_IN_DARKNESS - duplicatesDarkness)/SILVERS_IN_DARKNESS) * SILVER_VIAL_CREATE) * (CHANCE_OF_SILVER_NOT_8TH * 7 + CHANCE_OF_SILVER_8TH);

                duplicatesDarkness = SVCollectionDB.getNumberOfDuplicates("Darkness", "Bronze");
                avgDustGainDarkness += (duplicatesDarkness/BRONZES_IN_DARKNESS * BRONZE_VIAL_LIQUEFY + ((BRONZES_IN_DARKNESS - duplicatesDarkness)/BRONZES_IN_DARKNESS) * BRONZE_VIAL_CREATE) * CHANCE_OF_BRONZE * 7;


                JOptionPane.showMessageDialog(rootPanel, "Standard pack value: " + avgDustGainStandard +
                        " vials\nDarkness pack value: " + avgDustGainDarkness + " vials");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SVCollectionDB.closeDB();
                dispose();
                System.exit(0);
            }
        });
    }
}
