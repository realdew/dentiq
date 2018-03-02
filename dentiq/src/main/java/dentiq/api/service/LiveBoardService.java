package dentiq.api.service;

import java.util.List;


import dentiq.api.model.LiveBoardDash;

public interface LiveBoardService {
	
	
	// 지역 or 관심지역
	public LiveBoardDash getLiveBoardDash(List<String> locationCodeList, List<String> attrStrList) throws Exception;
	

}
