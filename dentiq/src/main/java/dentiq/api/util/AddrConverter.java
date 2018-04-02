package dentiq.api.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import dentiq.api.model.juso.AddrJuso;
import dentiq.api.model.juso.AddrJusoResults;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class AddrConverter {
	
	public static void main(String[] args) throws Exception {
		AddrConverter thisObj = new AddrConverter();
		//thisObj.execJuso();
		thisObj.execCoord();
	}
	
	public void execJuso() throws Exception {
		
//		// DB Connection 설정
		Connection conn = null;
		try {
			conn = getConnection();
			
			List<Juso> jusoList = getJuso(conn);
			
			// 처리한다.
			
			List<Juso> updatedJusoList = convertJuso(jusoList);
			
			
			
			
			
			//updateJuso(conn, jusoList);
			
			
			
			//conn.commit();
			conn.rollback();
			conn.close();
		} catch(Exception ex) {
			if ( conn != null ) try { conn.rollback(); } catch(Exception ignore) {}
			if ( conn != null ) try { conn.close(); } catch(Exception ignore) {}
		} 
		
		
	}
	
	public void execCoord() throws Exception {
		
//		// DB Connection 설정
		Connection conn = null;
		try {
			conn = getConnection();
			
			List<Juso> jusoList = getJuso(conn);
			
			// 처리한다.
			
			List<Juso> updatedJusoList = convertCoord(jusoList);
			
			
			
			
			
			//updateJuso(conn, jusoList);
			
			
			
			//conn.commit();
			conn.rollback();
			conn.close();
		} catch(Exception ex) {
			if ( conn != null ) try { conn.rollback(); } catch(Exception ignore) {}
			if ( conn != null ) try { conn.close(); } catch(Exception ignore) {}
		} 
		
		
	}
	
	// UTM-K 좌표 변환
	public List<Juso> convertCoord(List<Juso> jusoList) throws Exception {
		if ( jusoList == null || jusoList.size() == 0 ) return null;
		
		CoordUtil cUtil = new CoordUtil();
		for ( Juso juso : jusoList ) {
			double[] newCoord = cUtil.transGRS80toWGS84(Double.parseDouble(juso.ENT_X), Double.parseDouble(juso.ENT_Y));
			System.out.println("좌표변환 ID:" + juso.ID + " ==> X:" + newCoord[0] + "  Y:" + newCoord[1]);
			juso.LAT_LON_X = newCoord[0] + "";
			juso.LAT_LON_Y = newCoord[1] + "";
		}
		
		return jusoList;
	}
	
	public List<Juso> convertJuso(List<Juso> jusoList) throws Exception {
		if ( jusoList == null || jusoList.size() == 0 ) return null;
		
		
		
		JusoUtil jusoUtil = new JusoUtil();
		
		for ( Juso juso : jusoList ) {
			System.out.println("지역개발원 주소 API 호출 : " + juso.ADDR);
		
			AddrJusoResults jusoResults = jusoUtil.searchAddr(1, 10, "json", juso.ADDR);
			AddrJuso[] addrJuso = jusoResults.getJuso();
			if ( addrJuso==null || addrJuso.length != 1 ) {
				System.out.println("주소 조회 결과가 1개가 아닙니다. ");
				for ( int i=0; addrJuso!=null && i<addrJuso.length; i++ ) {
					System.out.println("#" + i + " ==> \t\t " + addrJuso[i]);
				}
				throw new Exception("주소 조회 결과가 1개가 아닙니다.");
			}
			
			System.out.println("\t성공 [" + addrJuso[0] + "]");
			
			
			juso.update(addrJuso[0]);
			
			//TODO 좌표 조회
		}
		
		return jusoList;
	}
	
	
	public Connection getConnection() throws Exception {
		/*
		spring.datasource.driverClassName=net.sf.log4jdbc.sql.jdbcapi.DriverSpy
		spring.datasource.type=com.zaxxer.hikari.HikariDataSource
		spring.datasource.url=jdbc:log4jdbc:mariadb://localhost:3306/test?useSSL=false
		spring.datasource.username=root
		spring.datasource.password=1111
		 */
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:log4jdbc:mariadb://localhost:3306/test?useSSL=false");
        config.setUsername("root");
        config.setPassword("1111");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "10");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "20");
		
		HikariDataSource ds = new HikariDataSource(config);		
		Connection conn = ds.getConnection();
		conn.setAutoCommit(false);
		
		return conn;
	}
	
	public List<Juso> getJuso(Connection conn) throws Exception {
		List<Juso> jusoList = new LinkedList<Juso>();
		
		//String sql = "SELECT * from HOSPITAL where ADDR_MAIN is null";
		String sql = "select * from HOSPITAL where LAT_LON_X is null and ENT_X is not null";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rset = pstmt.executeQuery();
		while ( rset.next() ) {
			
			Juso juso = new Juso();
			
			Field[] fields = Juso.class.getFields();
			for ( Field field : fields ) {
				String name = field.getName();
				String value = rset.getString(name);
				System.out.println(name + " : [" + value + "]");
				field.set(juso, value);
			}
			
			jusoList.add(juso);
		}		
		
		rset.close();
		pstmt.close();
		
		return jusoList;
	}
	
	public void updateJuso(Connection conn, List<Juso> updatedJusoList) throws Exception {
		if ( updatedJusoList == null || updatedJusoList.size()< 1 ) return;
		
		
		String sql = "update HOSPITAL_TEMP set "
				+ "LOCATION_=?, SIDO_CODE=?, SIDO_NAME=?, SIGU_CODE=?, SIGU_NAME=?, "
				+ "ADDR_MAIN=?, ADDR_DETAIL=?, "
				+ "JIBUN_ADDR=?, ADM_CD=?, RN_MGT_SN=?, UDRT_YN=?, BULD_MNNM=?, BULD_SLNO=?, BD_NM=?, EMD_NM=?, ENT_X=?, ENT_Y=?, ZIP_NO=? "
				+ "where HOSPITAL_ID=?";
		
		for ( Juso juso : updatedJusoList ) {
			PreparedStatement pstmt = conn.prepareStatement("sql");
			
			pstmt.setString(1,  juso.LOCATION_CODE);
			pstmt.setString(2,  juso.SIDO_CODE);
			pstmt.setString(3,  juso.SIDO_NAME);
			pstmt.setString(4,  juso.SIGU_CODE);
			pstmt.setString(5,  juso.SIGU_NAME);
			
			pstmt.setString(6,  juso.ADDR_MAIN);
			pstmt.setString(7,  juso.ADDR_DETAIL);
			
			pstmt.setString(8,  juso.JIBUN_ADDR);
			pstmt.setString(9,  juso.ADM_CD);
			pstmt.setString(10,  juso.RN_MGT_SN);
			pstmt.setString(11,  juso.UDRT_YN);
			pstmt.setString(12,  juso.BULD_MNNM);
			pstmt.setString(13,  juso.BULD_SLNO);			
			pstmt.setString(14,  juso.BD_NM);
			pstmt.setString(15,  juso.EMD_NM);
			pstmt.setString(16,  juso.ENT_X);
			pstmt.setString(17,  juso.ENT_Y);
			pstmt.setString(18,  juso.ZIP_NO);
			
			
			pstmt.setInt(19, new Integer(juso.ID));
			
			int updatedRows = pstmt.executeUpdate();
			if ( updatedRows != 1 ) throw new Exception("변경 행이 1이 아님 " + updatedRows);
			
			pstmt.close();
		}
		
	}
	
}


@ToString
class Juso {
	@Getter @Setter public String ID;
	@Getter @Setter public String LOCATION_CODE;
	@Getter @Setter public String SIDO_CODE;
	@Getter @Setter public String SIDO_NAME;
	@Getter @Setter public String SIGU_CODE;
	@Getter @Setter public String SIGU_NAME;
	
	@Getter @Setter public String ADDR_MAIN;
	@Getter @Setter public String ADDR_DETAIL;
	
	@Getter @Setter public String JIBUN_ADDR;
	@Getter @Setter public String ADM_CD;
	@Getter @Setter public String RN_MGT_SN;
	@Getter @Setter public String UDRT_YN;
	@Getter @Setter public String BULD_MNNM;
	@Getter @Setter public String BULD_SLNO;
	@Getter @Setter public String BD_NM;
	@Getter @Setter public String EMD_NM;		
	@Getter @Setter public String ZIP_NO;
	
	@Getter @Setter public String ENT_X;
	@Getter @Setter public String ENT_Y;
	
	@Getter @Setter public String ADDR;
	
	@Getter @Setter public String LAT_LON_X;
	@Getter @Setter public String LAT_LON_Y;
	
	
	public Juso() {}
	
	public void update(AddrJuso addrJuso) {
		this.ADDR_MAIN = addrJuso.getAddrMain();
		//this.ADDR_DETAIL;
		
		this.JIBUN_ADDR = addrJuso.getJibunAddr();
		this.ADM_CD = addrJuso.getAdmCd();
		this.RN_MGT_SN = addrJuso.getRnMgtSn();
		this.UDRT_YN = addrJuso.getUdrtYn();
		this.BULD_MNNM = addrJuso.getBuldMnnm();
		this.BULD_SLNO = addrJuso.getBuldSlno();
		this.BD_NM = addrJuso.getBdNm();
		this.EMD_NM = addrJuso.getEmdNm();
		this.ZIP_NO = addrJuso.getZipNo();
		
		this.SIDO_CODE = this.ADM_CD.substring(0, 2);
		this.SIGU_CODE = this.ADM_CD.substring(0, 5);
		this.LOCATION_CODE = this.SIDO_CODE + "." + this.SIGU_CODE;
		
		//this.ENT_X;
		//this.ENT_Y;
	}
}
/*
LOCATION_CODE=#{locationCode}, SIDO_CODE=#{sidoCode}, SIDO_NAME=#{sidoName}, SIGU_CODE=#{siguCode}, SIGU_NAME=#{siguName}, 
			ADDR_MAIN=#{addrMain}, ADDR_DETAIL=#{addrDetail}, 
			JIBUN_ADDR=#{addrJuso.jibunAddr}, ADM_CD=#{addrJuso.admCd}, RN_MGT_SN=#{addrJuso.rnMgtSn}, UDRT_YN=#{addrJuso.udrtYn}, BULD_MNNM=#{addrJuso.buldMnnm}, BULD_SLNO=#{addrJuso.buldSlno}, BD_NM=#{addrJuso.bdNm}, EMD_NM=#{addrJuso.emdNm}, ENT_X=#{addrJuso.entX}, ENT_Y=#{addrJuso.entY}, ZIP_NO=#{addrJuso.zipNo},
 */

