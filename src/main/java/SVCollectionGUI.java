import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Angel on 11/29/2016.
 */
public class SVCollectionGUI extends JFrame {
    private JPanel rootPanel;
    private JTable svCollectionTable;

    SVCollectionGUI(final SVCollectionDM svCollectionDM) {
        super("Cube Times");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        svCollectionTable.setModel(svCollectionDM);

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
