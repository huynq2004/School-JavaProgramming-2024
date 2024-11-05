package GLuong;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GUI_updateNV extends JFrame {
    JTable tbl;
    JTextField MaNV, Hoten, Luong;
    JComboBox Diachi;
    JButton timKiemNV, updateNV;

    public GUI_updateNV() {
                setTitle("Cập nhật nhân viên");
                setLayout(new BorderLayout());
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Diachi = new JComboBox<>(new String[]{"Hải Phòng", "Hà Nội", "Nam Định"});

                timKiemNV = new JButton("Tìm kiếm nhân viên");
                updateNV = new JButton("Cập nhật nhân viên");
                MaNV = new JTextField(20);
                Hoten = new JTextField(20);
                Luong = new JTextField(20);

                String[] Columns = {"Mã NV", "Họ tên", "Địa chỉ", "Lương"};
                tbl = new JTable(new DefaultTableModel(Columns, 0));

                JPanel northPanel = new JPanel(new FlowLayout());
                northPanel.add(timKiemNV);
                northPanel.add(updateNV);


                JPanel centerPanel = new JPanel(new FlowLayout());
                centerPanel.add(new JScrollPane(tbl));

                JPanel southPanel = new JPanel(new FlowLayout());
                southPanel.add(MaNV);
                southPanel.add(Hoten);
                southPanel.add(Luong);
                southPanel.add(Diachi);

                add(northPanel, BorderLayout.NORTH);
                add(centerPanel, BorderLayout.CENTER);
                add(southPanel, BorderLayout.SOUTH);

                timKiemNV.addActionListener(e ->
                    timkiem()
                );

                updateNV.addActionListener(e -> updateNhanvien());

            }

    private void updateNhanvien() {
    }

    private void timkiem() {
    }
}
