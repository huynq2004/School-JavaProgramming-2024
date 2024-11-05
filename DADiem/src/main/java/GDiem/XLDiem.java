package GDiem;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class XLDiem {
    private static final String URL = "jdbc:mysql://localhost:3306/DLDiem";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static Connection cn;

    public static Connection getCon(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch(ClassNotFoundException e){
            System.out.println("Driver không tìm thấy" + e.getMessage());
        }

        Connection con = null;

        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            return con;
        } catch (SQLException e) {
            System.out.println("Không thể kết nối" + e.getMessage());
            return null;
        }

    }

    public static List<HocVien> getHV() throws SQLException {
        List<HocVien> ds = new ArrayList<HocVien>();
        String sql = "select * from tbHocvien";

        try (Connection con = getCon(); Statement ps = con.createStatement()){
            try (ResultSet rs = ps.executeQuery(sql)) {
                while (rs.next()) {
                    ds.add(new HocVien(
                            rs.getString("MaHV"),
                            rs.getString("Hoten"),
                            rs.getString("Lop"),
                            rs.getString("GT"),
                            rs.getFloat("Diem")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ds;
    }

    public static List<HocVien> getHVbyLop(String Lop) {
        List<HocVien> ds = new ArrayList<HocVien>();
        String sql = "select * from HocVien where Lop like ?";

        try (Connection con = getCon(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, "%" + Lop + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new HocVien(
                            rs.getString("MaHV"),
                            rs.getString("Hoten"),
                            rs.getString("Lop"),
                            rs.getString("GT"),
                            rs.getFloat("Diem")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

            return ds;
        }

    public static List<HocVien> getHVbyLopGT(String Lop, String GT){
        List<HocVien> ds = new ArrayList<HocVien>();
        String sql = "select * from tbHocvien where Lop like ? and GT like ?";


        try (Connection con = getCon(); PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, "%" + Lop + "%");
            ps.setString(2, "%" + GT + "%" );

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new HocVien(
                            rs.getString("MaHV"),
                            rs.getString("Hoten"),
                            rs.getString("Lop"),
                            rs.getString("GT"),
                            rs.getFloat("Diem")
                    ));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ds;
    }
}
