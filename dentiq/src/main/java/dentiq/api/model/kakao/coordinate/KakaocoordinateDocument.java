package dentiq.api.model.kakao.coordinate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString 
public class KakaocoordinateDocument {
	@Getter @Setter String address_name;			// 전체 지번 주소 또는 전체 도로명 주소. (input에 따라 결정됨)
	@Getter @Setter String address_type;			// address_name의 값의 type. 지명, 도로명, 지번주소, 도로명주소 여부 (REGION or ROAD or REGION_ADDR or ROAD_ADDR)
	@Getter @Setter String x;						// X 좌표값 혹은 longitude
	@Getter @Setter String y;						// Y 좌표값 혹은 latitude
	@Getter @Setter KakaocoordinateAddress address;	// 지번주소 상세 정보
	@Getter @Setter KakaocoordinateRoadAddress road_address;	// 도로명주소 상세 정보
}