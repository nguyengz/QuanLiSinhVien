package vn.edu.stu.duongngocnguyen_dh51900713.Model;

public class Sinhvien {
    private String ten;
    private Double dtb;

    public Sinhvien() {
    }

    public Sinhvien(String ten, Double dtb) {
        this.ten = ten;
        this.dtb = dtb;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Double getDtb() {
        return dtb;
    }

    public void setDtb(Double dtb) {
        this.dtb = dtb;
    }

    @Override
    public String toString() {
        return "Tên:'" + ten +
                "\nĐiểm trung bình: " + dtb;
    }
}
