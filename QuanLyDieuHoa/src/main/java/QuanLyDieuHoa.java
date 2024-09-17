public class QuanLyDieuHoa {
    public static void main(String[] args) {
        // Tạo đối tượng từ các lớp
        DieuHoa dieuHoaThuong = new DieuHoa();
//        DieuHoaInverter dieuHoaInverter = new DieuHoaInverter();
//        DieuHoaHaiChieu dieuHoaHaiChieu = new DieuHoaHaiChieu();

        // Nhập thông tin
        System.out.println("Nhập thông tin cho điều hòa thường:");
        dieuHoaThuong.nhapThongTin();
        
//        System.out.println("\nNhập thông tin cho điều hòa Inverter:");
//        dieuHoaInverter.nhapThongTin();
//        
//        System.out.println("\nNhập thông tin cho điều hòa hai chiều:");
//        dieuHoaHaiChieu.nhapThongTin();
//
        // Hiển thị thông tin
        System.out.println("\nThông tin điều hòa thường:");
        dieuHoaThuong.hienThiThongTin();
//
//        System.out.println("\nThông tin điều hòa Inverter:");
//        dieuHoaInverter.hienThiThongTin();
//
//        System.out.println("\nThông tin điều hòa hai chiều:");
//        dieuHoaHaiChieu.hienThiThongTin();
    }
}
