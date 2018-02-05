package dentiq.api.repository.criteria;


import dentiq.api.util.StringUtil;
import lombok.Getter;

public class DistanceCriteria {
	// @Param("xPos") String xPos, @Param("yPos") String yPos, @Param("distance") int distance,
	
	@Getter
	private String xPos;
	
	@Getter
	private String yPos;
	
	@Getter
	private int distance;
	
	public DistanceCriteria(String xPos, String yPos, Integer distance) throws Exception {
		if ( !StringUtil.isNumberFormat(xPos) || !StringUtil.isNumberFormat(yPos) || distance==null || distance<=0 )
			throw new Exception("DistanceCriteria Format Error xPos[" + xPos + "], yPos[" + yPos + "], distance[" + distance + "]");
		
		this.xPos = xPos;
		this.yPos = yPos;
		this.distance = distance;
	}
}
