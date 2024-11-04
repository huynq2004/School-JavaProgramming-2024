package GSach;

public class Tailieu {
    String MaS, TenS;

    public Tailieu(String MaS, String TenS) {
        this.MaS = MaS;
        this.TenS = TenS;
    }

    public Tailieu(){}

    public String getMaS() {
        return MaS;
    }

    public void setMaS(String maS) {
        MaS = maS;
    }

    public String getTenS() {
        return TenS;
    }

    public void setTenS(String tenS) {
        TenS = tenS;
    }

    public float Thanhtien(int NamXB, float GiaB){
        float thanhtien = 0;
        if (NamXB <2015) thanhtien += GiaB * 0.85;
        else thanhtien += GiaB * 0.95;
        return thanhtien;
    }
}
