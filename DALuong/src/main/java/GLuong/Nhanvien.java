package GLuong;

public class Nhanvien extends Person {
    String Diachi;
    float Luong;

    public Nhanvien(String maNV, String hoten, String diachi, float luong) {
        super(maNV, hoten);
        Diachi = diachi;
        Luong = luong;
    }

    public Nhanvien() {
    }

    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String diachi) {
        Diachi = diachi;
    }

    public float getLuong() {
        return Luong;
    }

    public void setLuong(float luong) {
        Luong = luong;
    }


}
