package org.apexlegendsbphelper.Model;
import java.sql.*;

public class DBUtil {
    public static void main(String[] a)
            throws Exception {
        Connection conn = DriverManager.
                getConnection("jdbc:h2:file:../../../data/apexhelperdb", "sa", "");
        // add application code here
        conn.close();
    }
}
