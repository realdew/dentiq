package dentiq.api.model.kakao.coordinate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString 
public class KakaocoordinateAddress {
	@Getter @Setter String address_name;			// 전체 지번 주소
	@Getter @Setter String region_1depth_name;		// 지역 1Depth명 - 시도 단위
	@Getter @Setter String region_2depth_name;		// 지역 2Depth명 - 구 단위
	@Getter @Setter String region_3depth_name;		// 지역 3Depth명 - 동 단위
	@Getter @Setter String region_3depth_h_name;	// 지역 3Depth 행정동 명칭
	@Getter @Setter String h_code;					// 행정 코드
	@Getter @Setter String b_code;					// 법정 코드
	@Getter @Setter String mountain_yn;				// 산 여부
	@Getter @Setter String main_address_no;			// 지번 주 번지
	@Getter @Setter String sub_address_no;			// 지번 부 번지. 없을 경우 ""
	@Getter @Setter String zip_code;				// 우편번호 (6자리) -- **구** 우편번호
	@Getter @Setter String x;						// X 좌표값 혹은 longitude
	@Getter @Setter String y;						// Y 좌표값 혹은 latitude
}