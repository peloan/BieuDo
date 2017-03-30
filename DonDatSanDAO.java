package model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import common.StringProcess;
import model.bean.DonDatSan;

public class DonDatSanDAO {
	BasicDAO db = new BasicDAO();
	public ArrayList<DonDatSan> getDonDatSanList(String tim) {
		ArrayList<DonDatSan> donDatSanList = new ArrayList<DonDatSan>();
		DonDatSan datSan;
		if(db.Connect())
		{
			String sql = "select d.*, k.TenKH, s.TenSan  from ((DonDatSan d  join KhachHang k on d.MaKH=k.MaKH) join San s  on d.MaSan=s.MaSan) where d.MaThanhToan ='"+tim+"'";
			ResultSet re = db.SQLExeQUERY(sql);
			try {
				while(re.next())
				{
					datSan = new DonDatSan();
					datSan.setMaDonDS(re.getInt("MaDonDS"));
					datSan.setTenKH(re.getString("TenKH"));
					datSan.setMaKH(re.getInt("MaKH"));
					datSan.setTenSan(re.getString("TenSan"));
					datSan.setGay("1");
					datSan.setGiay("2");
					datSan.setXe("1");
					datSan.setNgayChoi(re.getString("NgayChoi"));
					datSan.setNgayDat(re.getString("NgayDat"));
					datSan.setSoNguoiChoi(re.getInt("SoNguoiChoi"));
					datSan.setGioChoi(re.getDouble("GioChoi"));
					datSan.setThanhTien(re.getInt("ThanhTien"));
					datSan.setMaTT(re.getString("MaThanhToan"));
					donDatSanList.add(datSan);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return donDatSanList;
	}
	public boolean xoaDon(String maTT, int maDon) {
		if(db.Connect())
		{
			String sql="delete from DonDatSan where MaThanhToan ='"+maTT+"'";
			boolean ok =false;
			ArrayList<Integer> donList = new ArrayList<Integer>();
			ResultSet re = db.SQLExeQUERY("select MaDonDS from ChiTietDichVu where MaDonDS="+maDon+"");
			try {
				while(re.next())
				{
					ok = true;
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			if(ok)
			{
				sql = "  delete from ChiTietDichVu where MaDonDS ='"+maDon+"' delete from DonDatSan where MaThanhToan ='"+maTT+"' ";
			}
			 
			if(db.SQLExeUPDATE(sql))
			{
				return true;
			}
		}
		return false;
	}
	public ArrayList<DonDatSan> getChiTietDV(String tim) {
		ArrayList<DonDatSan> dvList = new ArrayList<DonDatSan>();
		if(db.Connect())
		{
			
			DonDatSan d;
			String sql ="select c.MaDonDS, v.MaDichVu, v.SoLuong, d.TenDichVu from ((ChiTietDichVu v join DichVu d on v.MaDichVu=d.MaDichVu) "
					+ "join DonDatSan c on c.MaDonDS = v.MaDonDS) where  c.MaThanhToan='"+tim+"' order by d.TenDichVu asc";
			ResultSet re = db.SQLExeQUERY(sql);
			try {
				while(re.next())
				{
					d= new DonDatSan();
					d.setDichVu(re.getString("TenDichVu"));
					d.setSoDichVu(re.getInt("SoLuong"));
					dvList.add(d);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return dvList;
	}
	public static void main(String[] args) {
		DonDatSanDAO d = new DonDatSanDAO();
		for(int i=0;i<d.getChiTietDV("10").size();i++)
		System.out.println(d.getChiTietDV("10").get(i).getDichVu());
	}
	public boolean isThemDonDatSan(int maKH,String maSan, Date ngayDat, Date ngayChoi,int soNguoiChoi, int thanhTien, int tinhTrangThanhToan, String maCode,double gioChoi){
		if(db.Connect()){
			
			String sql=	String.format("INSERT INTO DonDatSan(MaKH,MaSan,NgayDat,NgayChoi,SoNguoiChoi,ThanhTien,TinhTrangThanhToan,MaThanhToan,GioChoi) "+
					" VALUES ( N'%s',N'%s',N'%s',N'%s',N'%s',N'%s',N'%s',N'%s',N'%s' )", maKH, maSan, StringProcess.convertDay(ngayDat), StringProcess.convertDay(ngayChoi), soNguoiChoi, thanhTien, tinhTrangThanhToan, maCode, gioChoi);
			if(db.SQLExeUPDATE(sql)){
				return true;
			}
		}
		return false;
	}
	
	
	
	public boolean isThemKhachHang(String tenKH,String sdt,String email,String diaChi){
		if(db.Connect()){
			String sql ="insert into KhachHang(TenKH,SDT,Email,DiaChi) values(N'"+tenKH+"','"+sdt+"',N'"+email+"',N'"+diaChi+"')";
			if(db.SQLExeUPDATE(sql)){
				return true;
			}
		}
		return false;
	}
	
	public int getMaKH(){
		int khachHang = 0;
		if(db.Connect()){
			String sql="SELECT MAX(MaKH) as 'MaxID' FROM KhachHang";
			ResultSet rs = db.SQLExeQUERY(sql );
			try {
				while(rs.next()){
					
					khachHang=rs.getInt("MaxID");
				}
			} catch (SQLException e) {
				System.out.println("ko lay dc");
				e.printStackTrace();
			}
		}
		return khachHang;
	}
	
	



	public int getMaDon() {
		int maDon = 0;
		if(db.Connect()){
			String sql="SELECT MAX(MaDonDS) as 'MaxID' FROM DonDatSan";
			ResultSet rs = db.SQLExeQUERY(sql );
			try {
				while(rs.next()){
					
					maDon=rs.getInt("MaxID");
				}
			} catch (SQLException e) {
				System.out.println("ko lay dc");
				e.printStackTrace();
			}
		}
		return maDon;
	}
	
	public String getMaThanhToan() {
		String maDon = null;
		if(db.Connect()){
			String sql="select MaThanhToan from DonDatSan where MaDonDS in (select Max(MaDonDS) from DonDatSan)";
			ResultSet rs = db.SQLExeQUERY(sql );
			try {
				while(rs.next()){
					
					
					maDon=rs.getString("MaThanhToan");
				}
			} catch (SQLException e) {
				System.out.println("ko lay dc");
				e.printStackTrace();
			}
		}
		return maDon;
	}

	public void themDV(int maDon, int maDV, int soXe, int tt) {
		if(maDon!=-1)
		{
			if(db.Connect()){
				String sql = "insert into ChiTietDichVu values('"+maDon+"','"+maDV+"','"+soXe+"','"+tt+"')";
				db.SQLExeUPDATE(sql);
					
			}
		}
		
	}

	public void updateDV(int maDV, int sl) {
		
		if(maDV!=-1)
		{
			if(db.Connect()){
				String sql = "update DichVu set SoLuong = SoLuong-"+sl+" where MaDichVu = "+maDV+"";
				db.SQLExeUPDATE(sql);
				
			}
		}
	}
	public ArrayList<DonDatSan> getDonDatSanList() {
		
		ArrayList<DonDatSan> donDatSanList = new ArrayList<DonDatSan>();
		if(db.Connect()){
			String sql = "SELECT D.MaDonDS, K.TenKH, K.MaKH, S.TenSan, D.NgayDat, D.SoNguoiChoi, D.NgayChoi, D.GioChoi, D.ThanhTien, D.TinhTrangThanhToan FROM DonDatSan D, KhachHang K, San S WHERE D.MaKH = K.MaKH and D.MaSan = S.MaSan";
			
			ResultSet re  = db.SQLExeQUERY(sql);
			try {
				while(re.next()){
					DonDatSan donDatSan = new DonDatSan();
					donDatSan.setMaDonDS(re.getInt("MaDonDS"));
					donDatSan.setMaKH(re.getInt("MaKH"));
					donDatSan.setTenKH(re.getNString("TenKH"));
					donDatSan.setTenSan(re.getNString("TenSan"));
					donDatSan.setNgayDat(re.getDate("NgayDat")+"");
					donDatSan.setNgayChoi(re.getDate("NgayChoi")+"");
					donDatSan.setGioChoiHT(StringProcess.doiGio(re.getDouble("GioChoi")));
					donDatSan.setSoNguoiChoi(re.getInt("SoNguoiChoi"));
					donDatSan.setThanhTien(re.getInt("ThanhTien"));
					boolean tinhTrangThanhToanHT = re.getBoolean("TinhTrangThanhToan");
					if(tinhTrangThanhToanHT ==true ){
						donDatSan.setTinhTrangTTShow("Đã thanh toán");
					}else{
						donDatSan.setTinhTrangTTShow("Chưa thanh toán");
					}
					
					donDatSanList.add(donDatSan);
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		return donDatSanList;
	}
	public boolean xoaKH(int maKH) {
		if(db.Connect())
		{
			String sql  = "delete from KhachHang where MaKH='"+maKH+"'";
			if(db.SQLExeUPDATE(sql))
				return true;
		}
		return false;
	}
	public boolean duyetdon(int maduyetdon) {
		// TODO Auto-generated method stub
		if(db.Connect()){
			String sql = String.format("update DonDatSan set TinhTrangThanhToan = 1 where MaDonDS = '%s' and TinhTrangThanhToan = 0", maduyetdon);
			//db.SQLExeUPDATE(sql);
			if(db.SQLExeUPDATE(sql)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	public boolean xoaTatCaDon() {
		if(db.Connect())
		{
			String sql  = "delete from DonDatSan where TinhTrangThanhToan = 0 delete from KhachHang where MaKH not in (select MaKH from  DonDatSan )";
			boolean ok =false;
			ResultSet re = db.SQLExeQUERY("select * from ChiTietDichVu where MaDonDS in (select MaDonDS from  DonDatSan where TinhTrangThanhToan = 0) ");
			try {
				while(re.next())
				{
					ok = true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(ok==true)
				sql = "delete from ChiTietDichVu where MaDonDS in (select MaDonDS from  DonDatSan where TinhTrangThanhToan = 0) delete from DonDatSan where TinhTrangThanhToan = 0 delete from KhachHang where MaKH not in (select MaKH from  DonDatSan )";
			if(db.SQLExeUPDATE(sql))
				return true;
		}
		return false;
	}

}
