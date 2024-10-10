import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DieuHoa implements Serializable {
    private String hangSanXuat;
    private int congSuat;
    private double giaBan;
    private static int count = 0;
    private int id;

    public DieuHoa() {
        this.id = ++count;
    }

    public DieuHoa(String hangSanXuat, int congSuat, double giaBan) {
        this.hangSanXuat = hangSanXuat;
        this.congSuat = congSuat;
        this.giaBan = giaBan;
        this.id = count++;
    }

    public DieuHoa(int id, String hangSanXuat, int congSuat, double giaBan) {
        this.hangSanXuat = hangSanXuat;
        this.congSuat = congSuat;
        this.giaBan = giaBan;
        this.id = id;
    }

    // Getters và Setters
    public String getHangSanXuat() {
        return hangSanXuat;
    }

    public int getId() {
        return id;
    }

    public int getCongSuat() {
        return congSuat;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public static int getCount() {
        return count;
    }

    //Setters
    public void setHangSanXuat(String hangSanXuat) {
        this.hangSanXuat = hangSanXuat;
    }

    public void setCongSuat(int congSuat) {
        this.congSuat = congSuat;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void setCount(int count) {
        DieuHoa.count = count;
    }

    public static void kiemTraCongSuat(int congSuat) throws Exception {
        if (congSuat <= 0) {
            throw new Exception("Công suất phải lớn hơn 0!");
        }
    }

    public static void kiemTraGiaBan(double giaBan) throws Exception {
        if (giaBan <= 0) {
            throw new Exception("Giá bán phải lớn hơn 0!");
        }
    }

    public void nhapThongTin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập hãng sản xuất: ");
        hangSanXuat = scanner.nextLine();

        while (true) {
            try {
                System.out.print("Nhập công suất (W): ");
                congSuat = scanner.nextInt();
                kiemTraCongSuat(congSuat); //Sử dụng throw-throws
                break;
            } catch (InputMismatchException e) {
                System.out.println("Dữ liệu không hợp lệ! Vui lòng nhập số nguyên cho công suất.");
                scanner.next(); //Bỏ dữ liệu không hợp lệ
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            try {
                System.out.print("Nhập giá bán (VND): ");
                giaBan = scanner.nextDouble();
                kiemTraGiaBan(giaBan); //throw-throws
                break;
            } catch (InputMismatchException e) {
                System.out.println("Dữ liệu không hợp lệ! Vui lòng nhập số thực cho giá bán.");
                scanner.next();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void hienThiThongTin() {
        System.out.println("ID: " + id);
        System.out.println("Hãng sản xuất: " + hangSanXuat);
        System.out.println("Công suất: " + congSuat + " W");
        System.out.println("Giá bán: " + giaBan + " VND");
    }

    public String toString(){
        return id + " " + hangSanXuat + " " + congSuat  + " " + giaBan;
    }
}

