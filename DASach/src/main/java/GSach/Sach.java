package GSach;

public class Sach extends Tailieu {
    int NamXB;
    float GiaB;

    public Sach(){
    }

    public Sach(String MaS, String TenS, int NamXB, float GiaB){
        super(MaS, TenS);
        this.NamXB = NamXB;
        this.GiaB = GiaB;
    }

    public int getNamXB() {
        return NamXB;
    }

    public void setNamXB(int namXB) {
        NamXB = namXB;
    }

    public float getGiaB() {
        return GiaB;
    }

    public void setGiaB(float giaB) {
        GiaB = giaB;
    }

    @Override
    public float Thanhtien(int NamXB, float GiaB) {
        return super.Thanhtien(NamXB, GiaB);
    }
}
