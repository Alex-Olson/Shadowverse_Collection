import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SVCollectionDM extends AbstractTableModel {

    private int rows = 0;

    ResultSet rs;

    public SVCollectionDM(ResultSet rs){
        this.rs = rs;
    }


    @Override
    public int getRowCount() {

        countRows();

        return rows;
    }

    public void countRows(){
        rows = 0;
        try {
            rs.beforeFirst();
            while (rs.next()){
                rows++;
            }
        } catch (SQLException sqle){
            System.out.println(sqle);
        }
    }

    @Override
    public int getColumnCount() {
        try {
            return rs.getMetaData().getColumnCount();
        } catch (SQLException sqle) {
            System.out.println(sqle);
            return 0;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try{
            rs.absolute(rowIndex+1);
            Object o = rs.getObject(columnIndex+1);
            return o.toString();
        } catch (SQLException sqle){
            System.out.println(sqle);
            return sqle.toString();
        }
    }

    @Override
    public boolean isCellEditable(int row, int col){
        return col == 5;
    }

    @Override
    public void setValueAt(Object newInt, int row, int column){
        int newNumOwned;
        try {

            newNumOwned = Integer.parseInt(newInt.toString());
            if (newNumOwned < 0 || newNumOwned > 3){
                throw new NumberFormatException("Can't have less than 0 or more than 3 of a card");
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return;
        }

        try {
            rs.absolute(row +1);
            rs.updateInt("Number_Owned", newNumOwned);
            rs.updateRow();
            fireTableDataChanged();
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public void updateModel(ResultSet rs){
        this.rs = rs;
        countRows();
    }

    @Override
    public String getColumnName(int column){
        try {
            return rs.getMetaData().getColumnName(column + 1).replace("_", " ");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return "?";
        }
    }
}
