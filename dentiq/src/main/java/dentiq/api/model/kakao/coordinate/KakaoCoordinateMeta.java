package dentiq.api.model.kakao.coordinate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString 
public class KakaoCoordinateMeta {
	@Getter @Setter int total_count;				// 검색어에 검색된 문서수
	@Getter @Setter int pageable_ount;				// total_count 중에 노출가능 문서수
	@Getter @Setter boolean is_end;					// 현재 페이지가 마지막 페이지인지 여부. 값이 false이면 page를 증가시켜 다음 페이지를 요청할 수 있음
}