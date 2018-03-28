package dentiq.api.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;



@JsonInclude(Include.NON_NULL)
public class JobAdGroupByLocationCode extends LocationCode {
	@Getter @Setter
	private int cnt = 0;
	
	@Getter @Setter
	private boolean requested = false;
	
	@Getter @Setter protected List<LocationCode> childrenList;
	
	public void addChild(JobAdGroupByLocationCode child) throws Exception {
		
		if ( !child.getSidoCode().equals(this.sidoCode) ) {
			throw new Exception();
		}
				
		if ( childrenList == null ) this.childrenList = new ArrayList<LocationCode>();
		
		childrenList.add(child);
		
		this.cnt += child.getCnt();
	}
	
	public String toString() {
		return super.toString() + " : ==>  CNT:" + this.cnt + ", REQ:" + this.requested + ", \n\tCHILD:" + this.childrenList;
	}
}
