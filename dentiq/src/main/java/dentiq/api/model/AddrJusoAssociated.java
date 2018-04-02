package dentiq.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dentiq.api.model.juso.AddrJuso;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public abstract class AddrJusoAssociated {

	//******************* juso.go.kr 데이터 START
	
		@Getter @Setter private String addrMain;				// 주소-앞부분	
		@Getter @Setter private String addrDetail;				// 주소-상세
		
		@Getter @Setter private AddrJuso addrJuso;				// 주소 정보
			/* MyBatis에서 주소 정보 자동 매핑을 위한 setter. JSON에는 포함되면 안됨 */ 
			@JsonIgnore public void setJibunAddr(String jibunAddr)	{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setJibunAddr(jibunAddr);	}
			
			@JsonIgnore public void setAdmCd(String admCd)			{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setAdmCd(admCd);			}
			@JsonIgnore public void setRnMgtSn(String rnMgtSn)		{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setRnMgtSn(rnMgtSn);		}
			@JsonIgnore public void setUdrtYn(String udrtYn)		{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setUdrtYn(udrtYn);		}	
			@JsonIgnore public void setBuldMnnm(String buldMnnm)	{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setBuldMnnm(buldMnnm);	}
			@JsonIgnore public void setBuldSlno(String buldSlno)	{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setBuldSlno(buldSlno);	}	
			
			@JsonIgnore public void setBdNm(String bdNm)			{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setBdNm(bdNm);			}	
			@JsonIgnore public void setEmdNm(String emdNm)			{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setEmdNm(emdNm);			}
			@JsonIgnore public void setEntX(String entX)			{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setEntX(entX);			}
			@JsonIgnore public void setEntY(String entY)			{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setEntY(entY);			}
			
			@JsonIgnore public void setLatLonX(String latLonX)		{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setLatLonX(latLonX);		}
			@JsonIgnore public void setLatLonY(String latLonY)		{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setLatLonY(latLonY);		}		
			
			@JsonIgnore public void setZipNo(String zipNo)			{	if ( this.addrJuso==null ) this.addrJuso = new AddrJuso();   this.addrJuso.setZipNo(zipNo);			}
			
			
			
		
		
		/*
		@Getter @Setter private String jibunAddr;					// 지번주소

		@Getter @Setter private String admCd;					// 행정구역코드 (--> 시도/시군 코드, 좌표)
		@Getter @Setter private String rnMgtSn;					// 도로명 코드 (--> 좌표)
		@Getter @Setter private String udrtYn;					// 지하여부 (--> 좌표)
		@Getter @Setter private String buldMnnm;				// 건물본번 (--> 좌표)
		@Getter @Setter private String buldSlno;				// 건물부번 (--> 좌표)	

		@Getter @Setter private String bdNm;					// 빌딩명
		@Getter @Setter private String emdNm;					// 읍면동 이름
		@Getter @Setter private String entX;					// 출입구 X좌표
		@Getter @Setter private String entY;					// 출입구 Y좌표	
		
		@Getter @Setter private String zipNo;						// (신)우편번호 5자리
		*/
		//******************* juso.go.kr 데이터 END
}
