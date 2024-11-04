package GSach;

import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class XLSach {
    private static final String URL = "jdbc:mysql://localhost:3306/DLSach";
    private static final String user = "root";
    private static final String pass = "123456";

    public static Connection getCon(){

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, user, pass);
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            System.out.println("Kết nối thất bại " + e.getMessage());
        }
        return conn;
    }

    public static List<Sach> getSA(){
        List<Sach> listS = new ArrayList<>();
        Connection conn = getCon();
        Statement stmt = null;
        ResultSet rs = null;

        if (conn == null) {
            System.out.println("Khong the ket noi vi conn null");
            return listS;
        }

        try (conn){
            String sql = "select * from tbSach";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                listS.add(new Sach(
                rs.getString("MaS"),
                rs.getString("TenS"),
                rs.getInt("NamXB"),
                rs.getFloat("GiaB")
            ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listS;
    }

    public static boolean deleteSA(int NamXB){
        String sql = "delete from tbSach where NamXB = ?";

        try (Connection conn = getCon();PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, NamXB);
            if (stmt.executeUpdate()>0){
                return true;
            }
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
