package dentiq.api.repository.criteria;


import dentiq.api.util.StringUtil;
import lombok.Getter;

public class DistanceCriteria {
	// @Param("xPos") String xPos, @Param("yPos") String yPos, @Param("distance") int distance,
	
	@Getter
	private String x;
	
	@Getter
	private String y;
	
	@Getter
	private int distance;
	
	public DistanceCriteria(String x, String y, Integer distance) throws Exception {
		if ( !StringUtil.isNumberFormat(x) || !StringUtil.isNumberFormat(y) || distance==null || distance<=0 )
			throw new Exception("DistanceCriteria Format Error xPos[" + x + "], yPos[" + y + "], distance[" + distance + "]");
		
		this.x = x;
		this.y = y;
		this.distance = distance;
	}
}
