package GLuong;

public class Person {
    String MaNV, Hoten;

    public Person(String maNV, String hoten) {
        this.MaNV = maNV;
        this.Hoten = hoten;
    }

    public Person() {
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String maNV) {
        MaNV = maNV;
    }

    public String getHoten() {
        return Hoten;
    }

    public void setHoten(String hoten) {
        Hoten = hoten;
    }


}
