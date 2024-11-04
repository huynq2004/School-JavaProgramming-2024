import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DBManager {

    //kết nối cơ sở dữ liệu
    private static Connection connect() {

        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found:"+ e.getMessage());
        }


        String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyDieuHoa;encrypt=true;trustServerCertificate=true;integratedSecurity=true";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Kết nối không thành công: " + e.getMessage());
        }
        return conn;
    }

    //load danh sách điều hòa từ database
    public static ArrayList<DieuHoa> loadFromDatabase(IQuanLyDieuHoa parent, int sign) {
        ArrayList<DieuHoa> danhSachDieuHoa = new ArrayList<>();
        Connection conn = connect();

        if (conn == null) {
            JOptionPane.showMessageDialog(parent, "Không thể kết nối tới cơ sở dữ liệu.", "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
            System.out.println("Không thể kết nối tới cơ sở dữ liệu.");
            return danhSachDieuHoa;
        }

        try {
            // Truy vấn điều hòa thường
            String queryDieuHoa = "SELECT * FROM DieuHoa";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryDieuHoa);

            while (rs.next()) {
                int id = rs.getInt("id");
                String hangSanXuat = rs.getString("hangSanXuat");
                int congSuat = rs.getInt("congSuat");
                double giaBan = rs.getDouble("giaBan");

                // Kiểm tra nếu điều hòa này có phải là Inverter hay Hai Chiều không
                String queryInverter = "SELECT * FROM DieuHoaInverter WHERE id = ?";
                PreparedStatement pstmtInverter = conn.prepareStatement(queryInverter);
                pstmtInverter.setInt(1, id);
                ResultSet rsInverter = pstmtInverter.executeQuery();

                if (rsInverter.next()) {
                    // Nếu có trong bảng Inverter
                    int mucTietKiemDien = rsInverter.getInt("mucTietKiemDien");
                    DieuHoaInverter dieuHoaInverter = new DieuHoaInverter(id, hangSanXuat, congSuat, giaBan, mucTietKiemDien);
                    danhSachDieuHoa.add(dieuHoaInverter);
                } else {
                    // Kiểm tra nếu là điều hòa Hai Chiều
                    String queryHaiChieu = "SELECT * FROM DieuHoaHaiChieu WHERE id = ?";
                    PreparedStatement pstmtHaiChieu = conn.prepareStatement(queryHaiChieu);
                    pstmtHaiChieu.setInt(1, id);
                    ResultSet rsHaiChieu = pstmtHaiChieu.executeQuery();

                    if (rsHaiChieu.next()) {
                        // Nếu có trong bảng Hai Chiều
                        double nhietDoLamNongToiDa = rsHaiChieu.getDouble("nhietDoLamNongToiDa");
                        DieuHoaHaiChieu dieuHoaHaiChieu = new DieuHoaHaiChieu(id, hangSanXuat, congSuat, giaBan, nhietDoLamNongToiDa);
                        danhSachDieuHoa.add(dieuHoaHaiChieu);
                        rsHaiChieu.close();
                        pstmtHaiChieu.close();
                    } else {
                        // Nếu không, tạo đối tượng DieuHoa thường
                        DieuHoa dieuHoa = new DieuHoa(id, hangSanXuat, congSuat, giaBan);
                        danhSachDieuHoa.add(dieuHoa);
                    }
                }

                rsInverter.close();
                pstmtInverter.close();

            }

            rs.close();
            stmt.close();
            conn.close();
            if (sign == 0) JOptionPane.showMessageDialog(parent, "Dữ liệu tải thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent, "Lỗi khi truy vấn dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.out.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        }

        return danhSachDieuHoa;
    }

    public static void themDieuHoa(DieuHoa dieuHoa) {
        // Thêm bản ghi vào bảng DieuHoa mà không cần chỉ định ID
        String sqlDieuHoa = "INSERT INTO DieuHoa (hangSanXuat, congSuat, giaBan) VALUES (?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement stmtDieuHoa = conn.prepareStatement(sqlDieuHoa, Statement.RETURN_GENERATED_KEYS)) {
            stmtDieuHoa.setString(1, dieuHoa.getHangSanXuat());
            stmtDieuHoa.setInt(2, dieuHoa.getCongSuat());
            stmtDieuHoa.setDouble(3, dieuHoa.getGiaBan());
            stmtDieuHoa.executeUpdate();

            // Lấy ID vừa được tạo
            ResultSet generatedKeys = stmtDieuHoa.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                dieuHoa.setId(generatedId); // Cập nhật ID cho đối tượng
            }

            // Kiểm tra loại của đối tượng và thêm vào bảng thích hợp
            if (dieuHoa instanceof DieuHoaInverter) {
                DieuHoaInverter dieuHoaInverter = (DieuHoaInverter) dieuHoa;
                String sqlInverter = "INSERT INTO DieuHoaInverter (id, mucTietKiemDien) VALUES (?, ?)";
                try (PreparedStatement stmtInverter = conn.prepareStatement(sqlInverter)) {
                    stmtInverter.setInt(1, dieuHoaInverter.getId());
                    stmtInverter.setInt(2, dieuHoaInverter.getMucTietKiemDien());
                    stmtInverter.executeUpdate();
                }
            } else if (dieuHoa instanceof DieuHoaHaiChieu) {
                DieuHoaHaiChieu dieuHoaHaiChieu = (DieuHoaHaiChieu) dieuHoa;
                String sqlHaiChieu = "INSERT INTO DieuHoaHaiChieu (id, nhietDoLamNongToiDa) VALUES (?, ?)";
                try (PreparedStatement stmtHaiChieu = conn.prepareStatement(sqlHaiChieu)) {
                    stmtHaiChieu.setInt(1, dieuHoaHaiChieu.getId());
                    stmtHaiChieu.setDouble(2, dieuHoaHaiChieu.getNhietDoLamNongToiDa());
                    stmtHaiChieu.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void suaDieuHoa(DieuHoa dieuHoa) {
        String sql = "UPDATE DieuHoa SET hangSanXuat = ?, congSuat = ?, giaBan = ? WHERE id = ?";
        PreparedStatement stmt = null;
        Connection conn = null;

        try {
            conn = connect();
            stmt = conn.prepareStatement(sql);
            // Cập nhật thông tin chung trong bảng DieuHoa
            stmt.setString(1, dieuHoa.getHangSanXuat());
            stmt.setInt(2, dieuHoa.getCongSuat());
            stmt.setDouble(3, dieuHoa.getGiaBan());
            stmt.setInt(4, dieuHoa.getId());

            stmt.executeUpdate();

            // Kiểm tra loại điều hòa và cập nhật bảng tương ứng
            if (dieuHoa instanceof DieuHoaInverter) {
                // Điều Hòa Inverter
                DieuHoaInverter inverter = (DieuHoaInverter) dieuHoa;

                // Cập nhật bảng DieuHoaInverter
                String sqlUpdateInverter = "UPDATE DieuHoaInverter SET mucTietKiemDien = ? WHERE id = ?";
                stmt = conn.prepareStatement(sqlUpdateInverter);
                stmt.setInt(1, inverter.getMucTietKiemDien());
                stmt.setInt(2, inverter.getId());

                stmt.executeUpdate();
            } else if (dieuHoa instanceof DieuHoaHaiChieu) {
                // Điều Hòa Hai Chiều
                DieuHoaHaiChieu haiChieu = (DieuHoaHaiChieu) dieuHoa;

                // Cập nhật bảng DieuHoaHaiChieu
                String sqlUpdateHaiChieu = "UPDATE DieuHoaHaiChieu SET nhietDoLamNongToiDa = ? WHERE id = ?";
                stmt = conn.prepareStatement(sqlUpdateHaiChieu);
                stmt.setDouble(1, haiChieu.getNhietDoLamNongToiDa());
                stmt.setInt(2, haiChieu.getId());

                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean xoaDieuHoa(int id) {
        String sql = "DELETE FROM DieuHoa WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            // affectedRows > 0 => có dòng bị xóa => xóa thành công
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Xóa thất bại
        }
    }

    public static ArrayList<DieuHoa> timKiemDieuHoa(String hangSanXuat) {
        String sql = "SELECT * FROM DieuHoa WHERE hangSanXuat LIKE ?";
        ArrayList<DieuHoa> danhSachDieuHoa = new ArrayList<>();

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + hangSanXuat + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String hang = rs.getString("hangSanXuat");
                int congSuat = rs.getInt("congSuat");
                double giaBan = rs.getDouble("giaBan");
                DieuHoa dieuHoa = new DieuHoa(id, hang, congSuat, giaBan);
                danhSachDieuHoa.add(dieuHoa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSachDieuHoa;
    }

}
