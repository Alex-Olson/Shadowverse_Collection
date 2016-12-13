import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
public class SVCollectionDB {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/shadowverse_collection";

    //todo: change these if using different mysql username/password
    private static final String USERNAME = "alex";
    private static final String PASSWORD = "blah";

    private static Statement statement = null;
    private static PreparedStatement prepStatement = null;
    private static Connection connection = null;
    private static ResultSet rs = null;
    private static SVCollectionDM svCollectionDM;

    /**
     * main will create or reload the table used for the gui, and display the gui
     * the table is created using the data parsed from the excel file
     * @param args any strings passed to the program before main is called.
     */
    public static void main(String[] args) {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException classNotFound) {
            classNotFound.printStackTrace();
            System.exit(-1);
        }

        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String showTable = "SHOW TABLES LIKE 'shadowverse_collection_info'";
            rs = statement.executeQuery(showTable);
            if (!rs.next()){
                String makeTable = "CREATE TABLE IF NOT EXISTS shadowverse_collection_info (Card_Set varchar(15), Class varchar(15), Card_Name varchar(50), Rarity varchar(9), Mana_Cost integer, Number_Owned integer,  PRIMARY KEY (Card_Name))";
                statement.executeUpdate(makeTable);
                String prepStatementInsert = "INSERT INTO shadowverse_collection_info VALUES ( ? , ? , ? , ? , ? , ? )";
                prepStatement = connection.prepareStatement(prepStatementInsert);
                //links used to learn how to parse excel files: https://github.com/minneapolis-edu/apache_poi + https://poi.apache.org/index.html + http://stackoverflow.com/questions/5878341/cannot-import-xssf-in-apache-poi
                FileInputStream readStream = new FileInputStream("shadowverse_card_data.xlsx");
                XSSFWorkbook workbook = new XSSFWorkbook(readStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                for (int r = 0; r < sheet.getPhysicalNumberOfRows(); r++) {
                    XSSFRow row = sheet.getRow(r);
                    for (int c = 0; c < row.getPhysicalNumberOfCells(); c++) {
                        if (c != 4) {
                            prepStatement.setString(c + 1, row.getCell(c).getStringCellValue());
                        } else {
                            prepStatement.setInt(c + 1, (int)row.getCell(c).getNumericCellValue());
                        }
                    }
                    prepStatement.setInt(6, 0);
                    prepStatement.executeUpdate();
                }
                readStream.close();
            }
            getCollectionData();
            SVCollectionGUI gui = new SVCollectionGUI(svCollectionDM);
        } catch (IOException | SQLException ex){
            ex.printStackTrace();
        }
    }

    /**
     * getCollectionData obtains all of the data in the shadowverse_collection table, and
     * creates or updates svCollectionDM with the resultset obtained.
     */
    private static void getCollectionData(){
        try {
            String selectAll = "SELECT * FROM shadowverse_collection_info";
            rs = statement.executeQuery(selectAll);

            if (svCollectionDM == null) {
                svCollectionDM = new SVCollectionDM(rs);
            } else {
                svCollectionDM.updateModel(rs);
            }
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    /**
     * gets the number of cards that would create "duplicates" (having more than 3 of the card)
     *
     * @param set The string of the set (Currently only two, Standard/Darkness)
     *            you want to query.
     * @param rarity The rarity (4 options, Bronze/Silver/Gold/Legendary) you want to query for.
     * @return an integer of how many cards found in the set+rarity had 3 copies already.
     */
    static int getNumberOfDuplicates(String set, String rarity){
        try {
            String prepStatementCountDupes = "SELECT COUNT(*) AS num_of_dupes FROM shadowverse_collection_info WHERE " +
                    "Card_Set = ? AND Number_Owned = 3 AND Rarity = ?";
            prepStatement = connection.prepareStatement(prepStatementCountDupes);
            prepStatement.setString(1, set);
            prepStatement.setString(2, rarity);
            rs = prepStatement.executeQuery();
            rs.next();
            return rs.getInt("num_of_dupes");
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
        return 0;
    }

    /**
     * Closes the database connection/statements.
     */
    static void closeDB(){
        try {
            statement.close();
            if (prepStatement != null){
                prepStatement.close();
            }
            connection.close();
        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

}
