package GDiem;

public class HocVien {
    String MaHV, Hoten, Lop, GT;
    float Diem;

    public HocVien(String maHV, String hoten, String lop, String GT, float diem) {
        MaHV = maHV;
        Hoten = hoten;
        Lop = lop;
        this.GT = GT;
        Diem = diem;
    }

    public HocVien() {
    }

    public String getMaHV() {
        return MaHV;
    }

    public void setMaHV(String maHV) {
        MaHV = maHV;
    }

    public String getHoten() {
        return Hoten;
    }

    public void setHoten(String hoten) {
        Hoten = hoten;
    }

    public String getLop() {
        return Lop;
    }

    public void setLop(String lop) {
        Lop = lop;
    }

    public String getGT() {
        return GT;
    }

    public void setGT(String GT) {
        this.GT = GT;
    }

    public float getDiem() {
        return Diem;
    }

    public void setDiem(float diem) {
        Diem = diem;
    }

    public String Ketqua(){
        if (Diem >= 20) return "Đỗ";
        else return "Trượt";
    }
}
