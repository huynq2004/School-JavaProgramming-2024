import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class IQuanLyDieuHoa extends JFrame {
    private JTextField txtHangSanXuat, txtCongSuat, txtGiaBan;
    private JComboBox<String> cbLoaiDieuHoa;
    private JButton btnThem, btnXoa, btnSua, btnTimKiem;
    private JTable tblDieuHoa;
    private NonEditableTableModel model;
    private JPopupMenu popupMenu;
    private JProgressBar progressBar; // Thanh tiến độ
    private JLabel lblStatus;

    DecimalFormat currencyFormat = new DecimalFormat("#,###");
    public IQuanLyDieuHoa() {
        // Tạo JFrame
        setTitle("Quản lý Điều Hòa");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tạo panel nhập liệu
        JPanel panelInput = new JPanel();
        panelInput.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Thêm khoảng cách giữa các thành phần

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelInput.add(new JLabel("Hãng sản xuất:"), gbc);

        gbc.gridx = 1;
        txtHangSanXuat = new JTextField(10);
        panelInput.add(txtHangSanXuat, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelInput.add(new JLabel("Công suất:"), gbc);

        gbc.gridx = 1;
        txtCongSuat = new JTextField(10);
        panelInput.add(txtCongSuat, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelInput.add(new JLabel("Giá bán:"), gbc);

        gbc.gridx = 1;
        txtGiaBan = new JTextField(10);
        panelInput.add(txtGiaBan, gbc);

        // Tạo JComboBox cho loại điều hòa
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelInput.add(new JLabel("Loại điều hòa:"), gbc);

        gbc.gridx = 1;
        cbLoaiDieuHoa = new JComboBox<>(new String[]{"Điều Hòa", "Điều Hòa Inverter", "Điều Hòa Hai Chiều"});
        panelInput.add(cbLoaiDieuHoa, gbc);
        add(panelInput, BorderLayout.NORTH);

        //Bảng hiển thị điều hòa
        String[] columnNames = {"ID", "Hãng sản xuất", "Công suất", "Giá bán", "Mức tiết kiệm điện", "Nhiệt độ làm nóng tối đa"};
        model = new NonEditableTableModel(columnNames, 0);
        tblDieuHoa = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tblDieuHoa);
        add(scrollPane, BorderLayout.CENTER);


        //Các nút điều khiển
        JPanel panelButtons = new JPanel(new FlowLayout());
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnTimKiem = new JButton("Tìm kiếm theo hãng sản xuất");
        panelButtons.add(btnThem);
        panelButtons.add(btnXoa);
        panelButtons.add(btnSua);
        panelButtons.add(btnTimKiem);
        add(panelButtons, BorderLayout.SOUTH);

        //Tạo menu chuột phải
        popupMenu = new JPopupMenu();
        JMenuItem menuLoad = new JMenuItem("Load danh sách từ CSDL");

        popupMenu.add(menuLoad);

        //click chuột phải ra menu
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(IQuanLyDieuHoa.this, e.getX(), e.getY());
                }
            }
        });

        // Xử lý sự kiện cho các nút bấm và menu chuột phải
        btnThem.addActionListener(e -> themDieuHoa());
        btnXoa.addActionListener(e -> xoaDieuHoa());
        btnSua.addActionListener(e -> suaDieuHoa());
        btnTimKiem.addActionListener(e -> timKiemDieuHoa());
        menuLoad.addActionListener(e -> {
            ConsoleManager.danhSachDieuHoa.clear();
            ConsoleManager.danhSachDieuHoa = DBManager.loadFromDatabase(this, 0);
            updateTable();
        });

    }

    //Cập nhật bảng
    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) tblDieuHoa.getModel();
        model.setRowCount(0); // Xóa toàn bộ dòng hiện tại

        for (DieuHoa dieuHoa : ConsoleManager.danhSachDieuHoa) {
            Object[] rowData = {
                    dieuHoa.getId(),
                    dieuHoa.getHangSanXuat(),
                    dieuHoa.getCongSuat(),
                    currencyFormat.format(dieuHoa.getGiaBan()),
                    "",
                    ""
            };

            // Kiểm tra loại điều hòa và thêm thông tin tương ứng
            if (dieuHoa instanceof DieuHoaInverter inverter) {
                rowData[4] = inverter.getMucTietKiemDien() + "%"; // Thêm mức tiết kiệm điện
            } else if (dieuHoa instanceof DieuHoaHaiChieu haiChieu) {
                rowData[5] = haiChieu.getNhietDoLamNongToiDa() + "độ C"; // Thêm nhiệt độ làm nóng tối đa
            }

            model.addRow(rowData); // Thêm dòng vào JTable
        }
    }

    //Thêm điều hòa
    private void themDieuHoa() {
        String hangSanXuat = txtHangSanXuat.getText();
        int congSuat = 0;
        double giaBan = 0;

        // Kiểm tra công suất
        try {
            congSuat = Integer.parseInt(txtCongSuat.getText());
            // Không khởi tạo dieuHoa ở đây, chỉ kiểm tra
            DieuHoa.kiemTraCongSuat(congSuat);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ! Vui lòng nhập số nguyên cho công suất.");
            return; // Thoát nếu có lỗi
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return; // Thoát nếu có lỗi
        }

        // Kiểm tra giá bán
        try {
            giaBan = Double.parseDouble(txtGiaBan.getText());
            // Không khởi tạo dieuHoa ở đây, chỉ kiểm tra
            DieuHoa.kiemTraGiaBan(giaBan);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ! Vui lòng nhập số thực cho giá bán.");
            return; // Thoát nếu có lỗi
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return; // Thoát nếu có lỗi
        }

        // Tạo đối tượng DieuHoa sau khi đã xác nhận cả công suất và giá bán hợp lệ
        DieuHoa dieuHoa = null;
        String loaiDieuHoa = (String) cbLoaiDieuHoa.getSelectedItem();

        switch (loaiDieuHoa) {
            case "Điều Hòa":
                dieuHoa = new DieuHoa(hangSanXuat, congSuat, giaBan);
                break;
            case "Điều Hòa Inverter":
                int mucTietKiemDien = 0;
                DieuHoaInverter dieuHoaInverter = null; // Tạo một biến cho DieuHoaInverter
                try {
                    mucTietKiemDien = Integer.parseInt(JOptionPane.showInputDialog("Nhập mức tiết kiệm điện:"));
                    dieuHoaInverter = new DieuHoaInverter(hangSanXuat, congSuat, giaBan, mucTietKiemDien);
                    dieuHoaInverter.kiemTraMucTietKiemDien(mucTietKiemDien); // Gọi kiểm tra trên đối tượng DieuHoaInverter
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ! Vui lòng nhập số nguyên cho mức tiết kiệm điện.");
                    return; // Thoát nếu có lỗi
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                    return; // Thoát nếu có lỗi
                }
                dieuHoa = dieuHoaInverter; // Gán dieuHoa với dieuHoaInverter
                break;
            case "Điều Hòa Hai Chiều":
                double nhietDoLamNongToiDa = 0;
                DieuHoaHaiChieu dieuHoaHaiChieu = null; // Tạo một biến cho DieuHoaHaiChieu
                try {
                    nhietDoLamNongToiDa = Double.parseDouble(JOptionPane.showInputDialog("Nhập nhiệt độ làm nóng tối đa:"));
                    dieuHoaHaiChieu = new DieuHoaHaiChieu(hangSanXuat, congSuat, giaBan, nhietDoLamNongToiDa);
                    dieuHoaHaiChieu.kiemTraNhietDoLamNongToiDa(nhietDoLamNongToiDa); // Gọi kiểm tra trên đối tượng DieuHoaHaiChieu
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ! Vui lòng nhập số cho nhiệt độ làm nóng tối đa.");
                    return; // Thoát nếu có lỗi
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                    return; // Thoát nếu có lỗi
                }
                dieuHoa = dieuHoaHaiChieu; // Gán dieuHoa với dieuHoaHaiChieu
                break;
        }

        if (dieuHoa != null) {
            DBManager.themDieuHoa(dieuHoa);
            ConsoleManager.danhSachDieuHoa = DBManager.loadFromDatabase(this, 1);
            updateTable(); // Cập nhật bảng sau khi thêm điều hòa
        }
    }

    //Xóa điều hòa
    private void xoaDieuHoa() {
        int selectedRow = tblDieuHoa.getSelectedRow(); // Lấy dòng được chọn

        if (selectedRow == -1) {
            // Nếu không có dòng nào được chọn
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để xóa.");
            return;
        }

        // Lấy ID điều hòa từ dòng được chọn
        int idDieuHoa = (int) tblDieuHoa.getValueAt(selectedRow, 0); // Giả sử cột 0 chứa ID

        // Hiển thị hộp thoại xác nhận xóa
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa điều hòa có ID: " + idDieuHoa + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Xóa điều hòa khỏi cơ sở dữ liệu
            boolean daXoaThanhCong = DBManager.xoaDieuHoa(idDieuHoa);

            // Nếu xóa thành công, cập nhật lại bảng
            if (daXoaThanhCong) {
                DefaultTableModel model = (DefaultTableModel) tblDieuHoa.getModel();
                model.removeRow(selectedRow); // Cập nhật bảng sau khi xóa dòng
                JOptionPane.showMessageDialog(null, "Đã xóa điều hòa có ID: " + idDieuHoa);
            } else {
                JOptionPane.showMessageDialog(null, "Xóa điều hòa thất bại. Vui lòng thử lại.");
            }
        }
    }

    //Sửa điều hòa
    private void suaDieuHoa(){
        int selectedRow = tblDieuHoa.getSelectedRow();

        if (selectedRow == -1) {
            // Nếu không có dòng nào được chọn
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để sửa.");
            return;
        }

        // Lấy thông tin từ các cột liên quan
        int id = Integer.parseInt(tblDieuHoa.getValueAt(selectedRow, 0).toString());
        String hangSanXuat = (String) tblDieuHoa.getValueAt(selectedRow, 1);
        int congSuat = Integer.parseInt(tblDieuHoa.getValueAt(selectedRow, 2).toString());
        String giaBanStr = tblDieuHoa.getValueAt(selectedRow, 3).toString();
        double giaBan = Double.parseDouble(giaBanStr.replace(",", "")); // Xử lý chuỗi giá bán về dạng số

        // Kiểm tra xem dòng này là Điều Hòa Inverter hay Điều Hòa Hai Chiều
        String mucTietKiemDienStr = (String) tblDieuHoa.getValueAt(selectedRow, 4);
        String nhietDoLamNongToiDaStr = (String) tblDieuHoa.getValueAt(selectedRow, 5);

        // Các trường nhập liệu chung cho tất cả loại điều hòa
        JTextField txtHangSanXuat = new JTextField(hangSanXuat);
        JTextField txtCongSuat = new JTextField(String.valueOf(congSuat));
        JTextField txtGiaBan = new JTextField(String.valueOf(giaBan));

        // Kiểm tra loại điều hòa và tạo các trường nhập liệu bổ sung nếu cần
        JTextField txtMucTietKiemDien = null;
        JTextField txtnhietDoLamNongToiDa = null;

        if (mucTietKiemDienStr != null && !mucTietKiemDienStr.isEmpty()) {
            // Điều Hòa Inverter: Thêm trường Mức tiết kiệm điện
            txtMucTietKiemDien = new JTextField(mucTietKiemDienStr.replace("%", ""));
        }

        if (nhietDoLamNongToiDaStr != null && !nhietDoLamNongToiDaStr.isEmpty()) {
            // Điều Hòa Hai Chiều: Thêm trường Nhiệt độ làm nóng tối đa
            txtnhietDoLamNongToiDa = new JTextField(nhietDoLamNongToiDaStr.replace("độ C", ""));
        }

        // Tạo panel chứa các trường nhập liệu
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Hãng sản xuất:"));
        panel.add(txtHangSanXuat);
        panel.add(new JLabel("Công suất:"));
        panel.add(txtCongSuat);
        panel.add(new JLabel("Giá bán:"));
        panel.add(txtGiaBan);

        if (txtMucTietKiemDien != null) {
            // Thêm trường Mức tiết kiệm điện nếu là Điều Hòa Inverter
            panel.add(new JLabel("Mức tiết kiệm điện (%):"));
            panel.add(txtMucTietKiemDien);
        }

        if (txtnhietDoLamNongToiDa != null) {
            // Thêm trường Nhiệt độ làm nóng tối đa nếu là Điều Hòa Hai Chiều
            panel.add(new JLabel("Nhiệt độ làm nóng tối đa (°C):"));
            panel.add(txtnhietDoLamNongToiDa);
        }

        // Hiển thị hộp thoại cho người dùng nhập liệu
        int result = JOptionPane.showConfirmDialog(null, panel, "Sửa thông tin Điều Hòa", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Lấy thông tin mới từ các trường nhập liệu
            try {
                String hangMoi = txtHangSanXuat.getText();
                int congSuatMoi = Integer.parseInt(txtCongSuat.getText());
                double giaBanMoi = Double.parseDouble(txtGiaBan.getText());

                if (txtMucTietKiemDien != null) {
                    // Nếu là Điều Hòa Inverter
                    int mucTietKiemDienMoi = Integer.parseInt(txtMucTietKiemDien.getText());
                    DieuHoaInverter dieuHoaInverter = new DieuHoaInverter(id, hangMoi, congSuatMoi, giaBanMoi, mucTietKiemDienMoi);

                    // Cập nhật cơ sở dữ liệu và giao diện
                    DBManager.suaDieuHoa(dieuHoaInverter);
                } else if (txtnhietDoLamNongToiDa != null) {
                    // Nếu là Điều Hòa Hai Chiều
                    double nhietDoLamNongMoi = Double.parseDouble(txtnhietDoLamNongToiDa.getText());
                    DieuHoaHaiChieu dieuHoaHaiChieu = new DieuHoaHaiChieu(id, hangMoi, congSuatMoi, giaBanMoi, nhietDoLamNongMoi);

                    // Cập nhật cơ sở dữ liệu và giao diện
                    DBManager.suaDieuHoa(dieuHoaHaiChieu);
                } else {
                    // Nếu là Điều Hòa thông thường
                    DieuHoa dieuHoa = new DieuHoa(id, hangMoi, congSuatMoi, giaBanMoi);

                    // Cập nhật cơ sở dữ liệu và giao diện
                    DBManager.suaDieuHoa(dieuHoa);
                }

                // Cập nhật bảng
                ConsoleManager.danhSachDieuHoa = DBManager.loadFromDatabase(this, 1);
                updateTable();

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ! Vui lòng nhập đúng định dạng cho các trường số.");
            }
        }

    }

    //Tìm điều hòa
    private void timKiemDieuHoa() {
        String hangSanXuat = txtHangSanXuat.getText(); // Lấy giá trị từ JTextField

        // Kiểm tra nếu người dùng chưa nhập hãng sản xuất
        if (hangSanXuat.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập hãng sản xuất.");
            return;
        }

        // Lọc danh sách điều hòa theo hãng sản xuất
        boolean timThay = false;
        DefaultTableModel model = (DefaultTableModel) tblDieuHoa.getModel();
        model.setRowCount(0); // Xóa toàn bộ dòng hiện tại để hiển thị kết quả mới

        ConsoleManager.danhSachDieuHoa = DBManager.timKiemDieuHoa(hangSanXuat);
        for (DieuHoa dieuHoa : ConsoleManager.danhSachDieuHoa) {
            if (dieuHoa.getHangSanXuat().equalsIgnoreCase(hangSanXuat)) {
                // Hiển thị điều hòa tìm được trong bảng (JTable)
                Object[] rowData = {
                        dieuHoa.getId(),
                        dieuHoa.getHangSanXuat(),
                        dieuHoa.getCongSuat(),
                        dieuHoa.getGiaBan(),
                        "", // Mức tiết kiệm điện
                        ""  // Nhiệt độ làm nóng tối đa
                };

                // Kiểm tra loại điều hòa và thêm thông tin tương ứng
                if (dieuHoa instanceof DieuHoaInverter) {
                    DieuHoaInverter inverter = (DieuHoaInverter) dieuHoa;
                    rowData[4] = inverter.getMucTietKiemDien(); // Thêm mức tiết kiệm điện
                } else if (dieuHoa instanceof DieuHoaHaiChieu) {
                    DieuHoaHaiChieu haiChieu = (DieuHoaHaiChieu) dieuHoa;
                    rowData[5] = haiChieu.getNhietDoLamNongToiDa(); // Thêm nhiệt độ làm nóng tối đa
                }

                model.addRow(rowData); // Thêm dòng vào JTable
                timThay = true;
            }
        }

        if (!timThay) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy điều hòa của hãng: " + hangSanXuat);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new IQuanLyDieuHoa().setVisible(true);
        });
    }
}
