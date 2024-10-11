import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

//public class IQuanLyDieuHoa extends JFrame implements ActionListener
public class IQuanLyDieuHoa extends JFrame {
    private JTextField txtHangSanXuat, txtCongSuat, txtGiaBan;
    private JComboBox<String> cbLoaiDieuHoa;
    private JButton btnThem, btnXoa, btnTimKiem;
    private JTable tblDieuHoa;
    private DefaultTableModel model;
    private JPopupMenu popupMenu;

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
        model = new DefaultTableModel(columnNames, 0);
        tblDieuHoa = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tblDieuHoa);
        add(scrollPane, BorderLayout.CENTER);

        //Các nút điều khiển
        JPanel panelButtons = new JPanel(new FlowLayout()); // Sử dụng FlowLayout để căn giữa các nút
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnTimKiem = new JButton("Tìm kiếm");
        panelButtons.add(btnThem);
        panelButtons.add(btnXoa);
        panelButtons.add(btnTimKiem);
        add(panelButtons, BorderLayout.SOUTH);

        //Tạo menu chuột phải
        popupMenu = new JPopupMenu();
        JMenuItem menuSave = new JMenuItem("Lưu danh sách");
        JMenuItem menuLoadBinary = new JMenuItem("Tải từ file nhị phân");
        JMenuItem menuLoadText = new JMenuItem("Tải từ file văn bản");

        popupMenu.add(menuSave);
        popupMenu.add(menuLoadBinary);
        popupMenu.add(menuLoadText);

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
        btnTimKiem.addActionListener(e -> timKiemDieuHoa());

//        // Đăng ký sự kiện cho các nút nêú dùng implement
//        btnThem.addActionListener(this);
//        btnThem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                themDieuHoa();
//            }
//        });
//        btnXoa.addActionListener(this);
//        btnTimKiem.addActionListener(this);

        menuSave.addActionListener(e -> QuanLyDieuHoa.luudanhsach());
        menuLoadBinary.addActionListener(e -> {
            QuanLyDieuHoa.taidanhsach();
            updateTable();
        });
        menuLoadText.addActionListener(e -> {
            QuanLyDieuHoa.taidstxt();
            updateTable();
        });


    }

    //Cập nhật bảng
    private void updateTable() {
        DefaultTableModel model = (DefaultTableModel) tblDieuHoa.getModel();
        model.setRowCount(0); // Xóa toàn bộ dòng hiện tại

        for (DieuHoa dieuHoa : QuanLyDieuHoa.danhSachDieuHoa) {
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
            QuanLyDieuHoa.danhSachDieuHoa.add(dieuHoa);
            updateTable(); // Cập nhật bảng sau khi thêm điều hòa
        }
    }

    //Xóa điều hòa người dùng chọn trên dòng
    private void xoaDieuHoa() {
        int selectedRow = tblDieuHoa.getSelectedRow(); // Lấy dòng được chọn

        if (selectedRow == -1) {
            // Nếu không có dòng nào được chọn
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng để xóa.");
            return;
        }

        // Xóa khỏi danh sách điều hòa dựa trên vị trí dòng được chọn
        int idDieuHoa = (int) tblDieuHoa.getValueAt(selectedRow, 0); // Giả sử cột 0 chứa ID

        boolean daXoa = false;
        for (int i = 0; i < QuanLyDieuHoa.danhSachDieuHoa.size(); i++) {
            if (QuanLyDieuHoa.danhSachDieuHoa.get(i).getId() == idDieuHoa) {
                QuanLyDieuHoa.danhSachDieuHoa.remove(i);
                daXoa = true;
                break;
            }
        }

        if (daXoa) {
            // Xóa dòng khỏi model của JTable
            DefaultTableModel model = (DefaultTableModel) tblDieuHoa.getModel();
            model.removeRow(selectedRow); // Cập nhật bảng sau khi xóa

            JOptionPane.showMessageDialog(null, "Đã xóa điều hòa có ID: " + idDieuHoa);
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy điều hòa với ID: " + idDieuHoa);
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

        for (DieuHoa dieuHoa : QuanLyDieuHoa.danhSachDieuHoa) {
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
