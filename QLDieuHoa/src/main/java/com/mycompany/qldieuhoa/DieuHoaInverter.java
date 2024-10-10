package com.mycompany.qldieuhoa;

/**
 *
 * @author huyng
 */
import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DieuHoaInverter extends DieuHoa implements Serializable {
    private int mucTietKiemDien;

    public DieuHoaInverter() {
        super();
    }

    public DieuHoaInverter(String hangSanXuat, int congSuat, double giaBan, int mucTietKiemDien) {
        super(hangSanXuat, congSuat, giaBan);
        this.mucTietKiemDien = mucTietKiemDien;
    }

    public DieuHoaInverter(int id, String hangSanXuat, int congSuat, double giaBan, int mucTietKiemDien) {
        super(id, hangSanXuat, congSuat, giaBan);
        this.mucTietKiemDien = mucTietKiemDien;
    }

    // Getter và Setter
    public int getMucTietKiemDien() {
        return mucTietKiemDien;
    }

    public void setMucTietKiemDien(int mucTietKiemDien) {
        this.mucTietKiemDien = mucTietKiemDien;
    }

    public void kiemTraMucTietKiemDien(int mucTietKiemDien) throws Exception {
        if (mucTietKiemDien < 0 || mucTietKiemDien > 100) {
            throw new Exception("Mức tiết kiệm điện phải từ 0 đến 100%!");
        }
    }

    @Override
    public void nhapThongTin() {
        super.nhapThongTin();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("Mức tiết kiệm điện (%): ");
                mucTietKiemDien = scanner.nextInt();
                kiemTraMucTietKiemDien(mucTietKiemDien);
                break;
            } catch (InputMismatchException e) {
                System.out.println("Dữ liệu không hợp lệ! Vui lòng nhập số nguyên cho mức tiết kiệm điện.");
                scanner.next();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void hienThiThongTin() {
        super.hienThiThongTin();
        System.out.println("Mức tiết kiệm điện: " + mucTietKiemDien + "%");
    }

    @Override
    public String toString(){
        return super.toString() + " " + mucTietKiemDien + " %";
    }
}

