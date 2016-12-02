import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class SVCollectionGUI extends JFrame {
    private JPanel rootPanel;
    private JTable svCollectionTable;

    SVCollectionGUI(final SVCollectionDM svCollectionDM) {
        super("Shadowverse Collection");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        svCollectionTable.setModel(svCollectionDM);
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(svCollectionTable.getModel());
        svCollectionTable.setRowSorter(rowSorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();


        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
        sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
        rowSorter.setSortKeys(sortKeys);

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
