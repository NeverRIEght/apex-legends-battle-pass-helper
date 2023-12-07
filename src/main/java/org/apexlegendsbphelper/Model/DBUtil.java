package org.apexlegendsbphelper.Model;
import java.io.File;
import java.sql.*;

public class DBUtil {
    public static void main(String[] a) throws Exception {
        StringBuilder dbUrlBuilder = new StringBuilder();
        dbUrlBuilder.append("jdbc:h2:file:");
        dbUrlBuilder.append(System.getProperty("user.dir"));
        dbUrlBuilder.append(File.separator);
        dbUrlBuilder.append("data");
        dbUrlBuilder.append(File.separator);
        dbUrlBuilder.append("apexlegendsbphelper");

        Connection conn = DriverManager.getConnection(dbUrlBuilder.toString(), "sa", "");

        // add application code here

        conn.close();
    }
}
