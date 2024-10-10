import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class QuanLyDieuHoa {
    public static ArrayList<DieuHoa> danhSachDieuHoa = new ArrayList<>();
    private static String FILE_PATH = "danhSachDieuHoa.dat";
    private static String pathtxt = "danhSachDieuHoa.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int luaChon;

//        do {
//            System.out.println("\n===== MENU =====");
//            System.out.println("1. Thêm điều hòa thường");
//            System.out.println("2. Thêm điều hòa Inverter");
//            System.out.println("3. Thêm điều hòa hai chiều");
//            System.out.println("4. Hiển thị danh sách điều hòa");
//            System.out.println("5. Tìm kiếm điều hòa theo hãng sản xuất");
//            System.out.println("6. Xóa điều hòa theo hãng sản xuất");
//            System.out.println("7. Xóa điều hòa theo ID");
//            System.out.println("8. Lưu danh sách vào file");
//            System.out.println("9. Tải danh sách từ file nhị phân");
//            System.out.println("10. Tải danh sách từ file txt");
//            System.out.println("0. Thoát");
//            System.out.print("Chọn chức năng: ");
//            luaChon = scanner.nextInt();
//
//            switch (luaChon) {
//                case 1:
//                    DieuHoa dieuHoaThuong = new DieuHoa();
//                    dieuHoaThuong.nhapThongTin();
//                    danhSachDieuHoa.add(dieuHoaThuong);
//                    break;
//                case 2:
//                    DieuHoaInverter dieuHoaInverter = new DieuHoaInverter();
//                    dieuHoaInverter.nhapThongTin();
//                    danhSachDieuHoa.add(dieuHoaInverter);
//                    break;
//                case 3:
//                    DieuHoaHaiChieu dieuHoaHaiChieu = new DieuHoaHaiChieu();
//                    dieuHoaHaiChieu.nhapThongTin();
//                    danhSachDieuHoa.add(dieuHoaHaiChieu);
//                    break;
//                case 4:
//                    hienThiDanhSach();
//                    break;
//                case 5:
//                    timKiemDieuHoa();
//                    break;
//                case 6:
//                    xoaDieuHoa(6);
//                    break;
//                case 7:
//                    xoaDieuHoa(7);
//                    break;
//                case 8:
//                    luudanhsach();
//                    break;
//                case 9:
//                    taidanhsach();
//                    break;
//                case 10:
//                    taidstxt();
//                    break;
//                case 0:
//                    System.out.println("Thoát chương trình.");
//                    break;
//                default:
//                    System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn lại.");
//            }
//        } while (luaChon != 0);
    }

    //Lưu dữ liệu (vào cả 2 file: nhị phân và binary
    public static void luudanhsach() {
        //vào file binary
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(danhSachDieuHoa);
            oos.close();
            System.out.println("Lưu dữ liệu thành công!");
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu dữ liệu: " + e.getMessage());
        }

        //Lưu vào file văn bản .txt
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathtxt))) {
            for (DieuHoa dieuHoa : danhSachDieuHoa) {
                writer.write(dieuHoa.toString() + "\n");
            }
            System.out.println("Lưu vào file văn bản thành công.");
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu file văn bản: " + e.getMessage());
        }
    }

    //Tải dữ liệu từ file binary
    public static void taidanhsach() {
        danhSachDieuHoa.clear();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            danhSachDieuHoa = (ArrayList<DieuHoa>) ois.readObject();
            ois.close();
            for (DieuHoa dieuHoa : danhSachDieuHoa) {
                if (dieuHoa.getId() > dieuHoa.getCount()) {
                    dieuHoa.setCount(dieuHoa.getId()+1);
                }
            }
            System.out.println("Tải dữ liệu thành công!");



        } catch (FileNotFoundException e) {
            System.out.println("Không tìm thấy file.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    //Tải dữ liệu từ file txt
    public static void taidstxt() {
        danhSachDieuHoa.clear();
        try (FileReader fileReader = new FileReader(pathtxt)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            // Đọc dữ liệu từ file
            while ((line = bufferedReader.readLine()) != null) {

                String[] words = line.split(" ");
                int id = Integer.parseInt(words[0]);
                String hangSanXuat = words[1];
                int congSuat = Integer.parseInt(words[2]);
                double giaBan = Double.parseDouble(words[3]);

                if (line.contains("%")) {
                    int mucTietKiemDien = Integer.parseInt(words[4]);
                    DieuHoaInverter dieuHoaInverter = new DieuHoaInverter(id, hangSanXuat, congSuat, giaBan, mucTietKiemDien);
                    danhSachDieuHoa.add(dieuHoaInverter);
                } else if (line.contains("C")) {
                    double nhietDoLamNongToiDa = Double.parseDouble(words[4]);
                    DieuHoaHaiChieu dieuHoaHaiChieu = new DieuHoaHaiChieu(id, hangSanXuat, congSuat, giaBan, nhietDoLamNongToiDa);
                    danhSachDieuHoa.add(dieuHoaHaiChieu);
                } else {
                    DieuHoa dieuHoa = new DieuHoa(id, hangSanXuat, congSuat, giaBan);
                    danhSachDieuHoa.add(dieuHoa);
                }
            }

            for (DieuHoa dieuHoa : danhSachDieuHoa) {
                if (dieuHoa.getId() > DieuHoa.getCount()) {
                    DieuHoa.setCount(dieuHoa.getId());
                }
            }
            System.out.println("Tải dữ liệu thành công!");

        } catch (IOException e) {
            System.out.println("Không tìm thấy file.");
        } catch (Exception e) {
            System.out.println("Danh sách điều hòa trống");
        }
    }

    public static void hienThiDanhSach() {
        if (danhSachDieuHoa.isEmpty()) {
            System.out.println("Danh sách điều hòa trống.");
        } else {
            for (DieuHoa dieuHoa : danhSachDieuHoa) {
                dieuHoa.hienThiThongTin();
                System.out.println("---------------------------");
            }
        }
    }

    public static void timKiemDieuHoa() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập hãng sản xuất cần tìm: ");
        String hangSanXuat = scanner.nextLine();

        boolean timThay = false;
        for (DieuHoa dieuHoa : danhSachDieuHoa) {
            if (dieuHoa.getHangSanXuat().equalsIgnoreCase(hangSanXuat)) {
                dieuHoa.hienThiThongTin();
                timThay = true;
                System.out.println("-----------------------------");
            }
        }
        if (!timThay) {
            System.out.println("Không tìm thấy điều hòa của hãng: " + hangSanXuat);
        }
    }

    public static void xoaDieuHoa(int num) {
        Scanner scanner = new Scanner(System.in);
        String del = "";
        int id = 0;
        if (num == 6) {
            System.out.print("Nhập hãng sản xuất của những điều hòa cần xóa: ");
            del = scanner.nextLine();
        } else if (num == 7) {
            System.out.println("Nhập ID của điều hòa cần xóa: ");
            id = scanner.nextInt();
        }

        boolean daXoa = false;
        if (num == 6) {
            for (int i = 0; i < danhSachDieuHoa.size(); i++) {
                if (danhSachDieuHoa.get(i).getHangSanXuat().equalsIgnoreCase(del)) {
                    danhSachDieuHoa.remove(i);
                    daXoa = true;
                    System.out.println("Đã xóa các điều hòa của hãng: " + del);
                    break;
                }
            }
        } else if (num == 7) {
            for (int i = 0; i < danhSachDieuHoa.size(); i++) {
                if (danhSachDieuHoa.get(i).getId() == id) {
                    danhSachDieuHoa.remove(i);
                    daXoa = true;
                    System.out.println("Đã xóa điều hòa có ID " + id + ".");
                    break;
                }
            }
        }

        if (!daXoa) {
            if (num == 6) {
                System.out.println("Không tìm thấy điều hòa của hãng: " + del);
            } else if (num == 7) {
                System.out.println("Không tìm thấy điều hòa " + id);
            }
        }

    }
}
