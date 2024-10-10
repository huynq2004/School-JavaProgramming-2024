import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DieuHoaHaiChieu extends DieuHoa implements Serializable {
    private double nhietDoLamNongToiDa;

    public DieuHoaHaiChieu() {
        super();
    }

    public DieuHoaHaiChieu(String hangSanXuat, int congSuat, double giaBan, double nhietDoLamNongToiDa) {
        super(hangSanXuat, congSuat, giaBan);
        this.nhietDoLamNongToiDa = nhietDoLamNongToiDa;
    }

    public DieuHoaHaiChieu(int id, String hangSanXuat, int congSuat, double giaBan, double nhietDoLamNongToiDa) {
        super(id, hangSanXuat, congSuat, giaBan);
        this.nhietDoLamNongToiDa = nhietDoLamNongToiDa;
    }

    // Getter và Setter
    public void setNhietDoLamNongToiDa (double nhietDoLamNongToiDa) {
        this.nhietDoLamNongToiDa = nhietDoLamNongToiDa;
    }

    public double getNhietDoLamNongToiDa() {
        return nhietDoLamNongToiDa;
    }

    public void kiemTraNhietDoLamNongToiDa (double nhietDoLamNongToiDa) throws Exception {
        if (nhietDoLamNongToiDa < 25) {
            throw new Exception("Nhiệt độ làm nóng tối đa cần lớn hơn hoặc bằng 25 độ C với điều hòa hai chiều!");
        }
    }

    @Override
    public void nhapThongTin() {
        super.nhapThongTin();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("Nhiệt độ làm nóng tối đa (độ C): ");
                nhietDoLamNongToiDa = scanner.nextInt();
                kiemTraNhietDoLamNongToiDa(nhietDoLamNongToiDa);
                break;
            } catch (InputMismatchException e) {
                System.out.println("Dữ liệu không hợp lệ! Vui lòng nhập số.");
                scanner.next();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Nhiệt độ làm nóng tối đa: " + nhietDoLamNongToiDa + " độ C.");
    }

    @Override
    public String toString(){
        return super.toString() + " " + nhietDoLamNongToiDa + " C";
    }
}
