import java.util.Scanner;

public class DieuHoaInverter extends DieuHoa {
    // Biến thành viên mới
    private boolean tietKiemDien;

    // Hàm khởi tạo
    public DieuHoaInverter() {
        super();
    }
    
    public DieuHoaInverter(String hangSanXuat, int congSuat, double giaBan, boolean tietKiemDien) {
        super(hangSanXuat, congSuat, giaBan); // Gọi hàm khởi tạo của lớp cha
        this.tietKiemDien = tietKiemDien;
    }

    // Getter và Setter
    public boolean isTietKiemDien() {
        return tietKiemDien;
    }

    public void setTietKiemDien(boolean tietKiemDien) {
        this.tietKiemDien = tietKiemDien;
    }
    
    @Override
    public void nhapThongTin() {
        super.nhapThongTin(); // Gọi hàm nhập của lớp cha
        Scanner scanner = new Scanner(System.in);
        System.out.print("Điều hòa có tiết kiệm điện không? (true/false): ");
        tietKiemDien = scanner.nextBoolean();
    }

    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Tiết kiệm điện: " + (tietKiemDien ? "Có" : "Không"));
    }

}
