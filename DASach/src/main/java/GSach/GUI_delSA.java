package GSach;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI_delSA extends JFrame {
    JComboBox<Integer> NamXB;
    JButton delSA;
    JTable tbl;

    public GUI_delSA() {
        setTitle("Quản lý sách");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());

        NamXB = new JComboBox<>(new Integer[] {2019, 2020, 2021});
        delSA = new JButton("Xóa sách");

        String[] ColumnName = {"Mã sách", "Tên sách", "Năm xuất bản", "Giá bán"};
        tbl = new JTable(new DefaultTableModel(ColumnName,0));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(tbl, BorderLayout.CENTER);
        panel.add(delSA, BorderLayout.SOUTH);
        panel.add(NamXB, BorderLayout.NORTH);
        getContentPane().add(panel, BorderLayout.CENTER);

        delSA.addActionListener(e -> delSA());
        getlist();

    }

    private void getlist() {
        DefaultTableModel model = (DefaultTableModel) tbl.getModel();
        model.setRowCount(0);

        List<Sach> listS = XLSach.getSA();
        for (Sach sach : listS) {
            model.addRow(new Object[]{sach.getMaS(), sach.getTenS(), sach.getNamXB(), sach.getGiaB()});
        }
    }

    private void delSA() {
        int selected = (int)NamXB.getSelectedItem();
        int cf = JOptionPane.showConfirmDialog(null,"Xoa sách đã chọn?","Confirm",JOptionPane.YES_NO_OPTION );
        if (cf == JOptionPane.YES_OPTION) {
            if (XLSach.deleteSA(selected)) {
                JOptionPane.showMessageDialog(null, "Đã xóa thành công");
                getlist();
            } else {
                JOptionPane.showMessageDialog(null,"Xóa thất bại");
            }
        }


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GUI_delSA().setVisible(true);
        });
    }


}
