import java.util.Scanner;

public class DieuHoa {
    private String hangSanXuat;
    private int congSuat;
    private double giaBan;

    public DieuHoa() {

    }

    public DieuHoa(String hangSanXuat, int congSuat, double giaBan) {
        this.hangSanXuat = hangSanXuat;
        this.congSuat = congSuat;
        this.giaBan = giaBan;
    }

    // Getters và Setters
    // Getters
    public String getHangSanXuat() {
        return hangSanXuat;
    }

    public int getCongSuat() {
        return congSuat;
    }

    public double getGiaBan() {
        return giaBan;
    }

    // Setters
    public void setHangSanXuat(String hangSanXuat) {
        this.hangSanXuat = hangSanXuat;
    }

    public void setCongSuat(int congSuat) {
        this.congSuat = congSuat;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    
    // Hàm nhập thông tin
    public void nhapThongTin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập hãng sản xuất: ");
        System.out.flush();
        hangSanXuat = scanner.nextLine();
        System.out.print("Nhập công suất (W): ");
        congSuat = Integer.valueOf(scanner.nextLine());
        System.out.print("Nhập giá bán (VND): ");
        giaBan = Double.valueOf(scanner.nextLine());
    }

    public void hienThiThongTin() {
        System.out.println("Hãng sản xuất: " + hangSanXuat);
        System.out.println("Công suất: " + congSuat + " W");
        System.out.println("Giá bán: " + giaBan + " VND");
    }
}
