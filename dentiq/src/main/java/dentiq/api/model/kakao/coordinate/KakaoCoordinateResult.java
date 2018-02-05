package dentiq.api.model.kakao.coordinate;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString 
public class KakaoCoordinateResult {
	@Getter @Setter KakaoCoordinateMeta meta;
	@Getter @Setter List<KakaocoordinateDocument> documents;
}
