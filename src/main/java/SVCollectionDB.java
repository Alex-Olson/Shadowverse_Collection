import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

public class SVCollectionDB {
    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/shadowverse_collection";
    static final String USERNAME = "alex";
    static final String PASSWORD = "blah";

    static Statement statement = null;
    static PreparedStatement prepStatement = null;
    static Connection connection = null;
    static ResultSet rs = null;
    static String prepStatementInsert = null;

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
                prepStatementInsert = "INSERT INTO shadowverse_collection_info VALUES ( ? , ? , ? , ? , ? , ? )";
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

            }


        } catch (IOException ioe){
            System.out.println(ioe);
        } catch (SQLException sqle){
            System.out.println(sqle);
        } finally {
            closeDB();
        }

    }

    public static void closeDB(){
        try {
            statement.close();
            if (prepStatement != null){
                prepStatement.close();
            }
            connection.close();
        } catch (SQLException sqlex){
            sqlex.printStackTrace();
        }
    }

}
