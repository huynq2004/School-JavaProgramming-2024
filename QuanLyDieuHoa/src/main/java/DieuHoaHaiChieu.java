import java.util.Scanner;

public class DieuHoaHaiChieu extends DieuHoa {
    // Biến thành viên mới
    private boolean coChucNangLamNong;

    // Hàm khởi tạo
    public DieuHoaHaiChieu() {
        super();
    }
    
    public DieuHoaHaiChieu(String hangSanXuat, int congSuat, double giaBan, boolean coChucNangLamNong) {
        super(hangSanXuat, congSuat, giaBan);
        this.coChucNangLamNong = coChucNangLamNong;
    }

    // Getter và Setter
    public boolean isCoChucNangLamNong() {
        return coChucNangLamNong;
    }

    public void setCoChucNangLamNong(boolean coChucNangLamNong) {
        this.coChucNangLamNong = coChucNangLamNong;
    }

    @Override
    public void nhapThongTin() {
        super.nhapThongTin(); // Gọi hàm nhập của lớp cha
        Scanner scanner = new Scanner(System.in);
        System.out.print("Điều hòa có chức năng làm nóng không? (true/false): ");
        coChucNangLamNong = scanner.nextBoolean();
    }
    
    // Override hàm hienThiThongTin
    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Có chức năng làm nóng: " + (coChucNangLamNong ? "Có" : "Không"));
    }
}
