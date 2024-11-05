package GDiem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GUI_findHV extends JFrame {
    private JComboBox<String> Lop;
    private JRadioButton Nam, Nu;
    private JButton Find;
    private JTable tbl;

    public GUI_findHV() {
        setTitle("Find HV");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());

        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Nam = new JRadioButton("Nam");
        Nu = new JRadioButton("Nu");
        Nam.setSelected(true);
        ButtonGroup bg = new ButtonGroup();
        bg.add(Nam);
        bg.add(Nu);

        panel1.add(Nam);
        panel1.add(Nu);

        Lop = new JComboBox<>(new String[] {"63PM1", "63PM2", "63TH1", "63TH2"});
        Find = new JButton("Tìm kiếm");

        panel1.add(Lop);
        panel1.add(Find);

        getContentPane().add(panel1, BorderLayout.NORTH);

        String[] Columns = {"Mã HV", "Họ tên", "Lớp", "Giới tính", "Điểm"};
        tbl = new JTable(new DefaultTableModel(Columns, 0));
        JPanel panel2 = new JPanel();
        panel2.add(new JScrollPane(tbl));
        getContentPane().add(panel2, BorderLayout.CENTER);

        Find.addActionListener(e -> timKiem());
        loadDS();


    }

    private void loadDS() {
        List<HocVien> ds = new ArrayList<HocVien>();
        DefaultTableModel dm = (DefaultTableModel) tbl.getModel();
        dm.setRowCount(0);
        try{
            ds = XLDiem.getHV();
            for(HocVien hv : ds) {
                dm.addRow(new Object[]{hv.getMaHV(), hv.getHoten(), hv.getLop(), hv.getGT(), hv.getDiem()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Kết nối thất bại " + ex.getMessage());
        }

    }

    private void timKiem() {
        String lop = Lop.getSelectedItem().toString();
        String GT;
        List<HocVien> ds = new ArrayList<HocVien>();

        if(Nam.isSelected() || Nu.isSelected()) {
            if (Nu.isSelected()) {
                GT = Nu.getText();
            } else {
                GT = Nam.getText();
            }
            ds = XLDiem.getHVbyLopGT(lop, GT);
        } else {
            ds = XLDiem.getHVbyLop(lop);
        }

        DefaultTableModel dm = (DefaultTableModel) tbl.getModel();
        dm.setRowCount(0);
        for(HocVien hv : ds) {
            dm.addRow(new Object[]{hv.getMaHV(), hv.getHoten(), hv.getLop(), hv.getGT(), hv.getDiem()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI_findHV().setVisible(true));
    }


}
