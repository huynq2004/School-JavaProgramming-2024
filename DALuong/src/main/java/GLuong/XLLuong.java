package GLuong;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class XLLuong {
    private static final String URL = "jdbc:mysql://localhost:3306/DLLuong";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public static Connection getCon() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                System.out.println("Driver không tìm thấy: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Không thể kết nối: " + e.getMessage());
            }
            return null;
    }

     public static List<Nhanvien> getHV() {
             List<Nhanvien> ds = new ArrayList<>();
             String sql = "SELECT * FROM tbNhanvien";

             try (Connection conn = getCon(); Statement ps = conn.createStatement(); ResultSet rs = ps.executeQuery(sql)) {
                 while (rs.next()) {
                     ds.add(new Nhanvien(
                             rs.getString("MaNV"),
                             rs.getString("Hoten"),
                             rs.getString("Diachi"),
                             rs.getFloat("Luong")
                     ));
                 }
             } catch (SQLException e) {
                 e.printStackTrace();
             }
             return ds;
    }

    public static List<Nhanvien> getNVbyMa(String MaNV) {
        List<Nhanvien> listS = new ArrayList<>();
        String sql = "SELECT * FROM tbNhanvien WHERE MaNV = ?";

        try (Connection conn = getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + MaNV + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    listS.add(new Nhanvien(
                            rs.getString("MaNV"),
                            rs.getString("Hoten"),
                            rs.getString("Diachi"),
                            rs.getFloat("Luong")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listS;
    }

    public static boolean updateNV(Nhanvien nhanvien) {
        String sql = "UPDATE tbSach SET Hoten = ?, Diachi = ?, Luong = ? WHERE MaNV = ?";

        try (Connection conn = getCon(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nhanvien.getHoten());
            stmt.setString(2, nhanvien.getDiachi());
            stmt.setFloat(3, nhanvien.getLuong());
            stmt.setString(4, nhanvien.getMaNV());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
