package dentiq.api.model.kakao.coordinate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString 
public class KakaocoordinateRoadAddress {
	@Getter @Setter String address_name;			//	전체 도로명 주소
	@Getter @Setter String region_1depth_name;		//	지역명1
	@Getter @Setter String region_2depth_name;		//	지역명2
	@Getter @Setter String region_3depth_name;		//	지역명3
	@Getter @Setter String road_name;				//	도로명
	@Getter @Setter String underground_yn;			//	지하 여부	(Y or N)
	@Getter @Setter String main_building_no;		//	건물 본번
	@Getter @Setter String sub_building_no;			//	건물 부번. 없을 경우 ""
	@Getter @Setter String building_name;			//	건물 이름
	@Getter @Setter String zone_no;					//	우편번호(5자리)  -- **신** 우편번호
	@Getter @Setter String x;						//	X 좌표값 혹은 longitude
	@Getter @Setter String y;						//	Y 좌표값 혹은 latitude
}