import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SVCollectionGUI extends JFrame {
    private JPanel rootPanel;
    private JTable svCollectionTable;
    private JButton countButton;
    private JTextField nameTextField;
    private TableRowSorter<SVCollectionDM> rowSorter;


    /**
     *
     * @param svCollectionDM the table model used for the Jtable.
     */
    SVCollectionGUI(final SVCollectionDM svCollectionDM) {
        super("Shadowverse Collection");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        svCollectionTable.setModel(svCollectionDM);
        //sorting stuff found here: http://stackoverflow.com/questions/28823670/how-to-sort-jtable-in-shortest-way
        rowSorter = new TableRowSorter<>(svCollectionDM);
        svCollectionTable.setRowSorter(rowSorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        /**
         * fixes the integers not sorting correctly (sorted like 1, 10, 2, etc)
         */
        rowSorter.setComparator(4, new Comparator<String>() {
            //method taken from http://stackoverflow.com/questions/9090974/problems-with-jtable-sorting-of-integer-values

            @Override
            public int compare(String s1, String s2){
                return Integer.parseInt(s1) - Integer.parseInt(s2);
            }
        });

        rowSorter.setSortable(5, false);
        rowSorter.setSortKeys(sortKeys);

        /**
         * this adds a listener that filters the table by card name when the textbox is altered. method taken from http://docs.oracle.com/javase/tutorial/uiswing/components/table.html#sorting
         */
        nameTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterByName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterByName();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterByName();
            }
        });


        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double avgVialGainStandard = SVCardMath.getAverageVialsGained("Standard");
                double avgVialGainDarkness = SVCardMath.getAverageVialsGained("Darkness");

                JOptionPane.showMessageDialog(rootPanel, "Standard pack value: " + avgVialGainStandard +
                        " vials\nDarkness pack value: " + avgVialGainDarkness + " vials");
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

    /**
     * filters out the Jtable to only show rows with the character sequence that
     * matches the characters in nameTextField.
     */
    private void filterByName(){
        RowFilter<SVCollectionDM, Object> rf;
        try {
            rf = RowFilter.regexFilter("(?i)" + nameTextField.getText(), 2);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        rowSorter.setRowFilter(rf);
    }

}
