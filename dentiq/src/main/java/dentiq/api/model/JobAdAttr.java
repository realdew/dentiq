package dentiq.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dentiq.api.service.exception.LogicalException;
import lombok.Getter;
import lombok.Setter;


@JsonInclude(Include.NON_NULL)
public class JobAdAttr implements Comparable<JobAdAttr> {
	
	// select GROUP_ID, GROUP_DISPLAY_ORDER, GROUP_NAME, CODE_ID, CODE_DISPLAY_ORDER, CODE_NAME, DEPRECATED_YN from JOB_AD_ATTR_CODE
	
	public static final char GROUP_CODE_SEPARATOR = '.';
	
	@Getter @Setter
	private String groupId;
	@Getter @Setter
	private String codeId;
	
	@Getter @Setter
	private String groupName;
	@Getter @Setter
	private String codeName;
	
	@Getter @Setter
	private int groupDisplayOrder;
	@Getter @Setter
	private int codeDisplayOrder;
	
	@Getter @Setter
	private String deprecatedYn;
	
	
	public JobAdAttr() {}
	
	/*
	// 클라이언트에서 요청할 때 사용되는 생성자
	public JobAdAttr(String groupId, String codeId) {
		this.groupId = groupId;
		this.codeId  = codeId;
	}
	*/
	
	/**
	 * API 클라이언트에서 요청할 때 사용하는 생성자
	 * @param attrCode	예: EMP_1  ==> GROUP_ID:EMP, CODE_ID:EMP_1
	 * @throws LogicalException
	 */
	public JobAdAttr(String attrCode) throws LogicalException {
		deserialize(attrCode);
	}
	
	/**
	 * API 클라이언트로부터 전송된 KEY값으로 GROUP_ID와 CODE_ID를 설정한다.
	 * @param str 예: KEY:EMP_1  ==> GROUP_ID:EMP, CODE_ID:1
	 * @throws LogicalException
	 */
	private void deserialize(String attrCode) throws LogicalException {
		if ( attrCode==null ) throw new LogicalException();
		
		try {
			int separatorPos = attrCode.indexOf(GROUP_CODE_SEPARATOR);		
			this.groupId = attrCode.substring(0, separatorPos);
			this.codeId = attrCode;
		} catch(Exception ex) {
			throw new LogicalException();
		}
	}
	
	/**
	 * API 클라이언트에 전송하기 위하여 GROUP_ID와 CODE_ID를 조합한 KEY값을 생성한다.
	 * @return 예: GROUP_ID:EMP, CODE_ID:1   ==>   KEY:EMP_1
	 * @throws LogicalException
	 *
	public String serialize() throws LogicalException {
		if ( this.groupId==null || this.codeId==null ) throw new LogicalException();
		return this.groupId + GROUP_CODE_SEPARATOR + this.codeId;
	}
	*/
	
	
	public String toString() {
		return this.groupId + " (" + this.groupName + " , G_ORDER:" + this.groupDisplayOrder + "), " + this.codeId + " (" + this.codeName + ") C_ORDER:" + this.codeDisplayOrder;
	}
	
	public boolean equals(Object other) {
		if ( other == null ) return false;
		if ( !(other instanceof JobAdAttr) ) return false;
		
		if ( this.groupId==null || this.codeId==null ) return false;
		
		JobAdAttr attr = (JobAdAttr) other;
		//System.out.println("this : " + this + "\t other : " + other);		
		
		if ( this.groupId.equals(attr.groupId) && this.codeId.equals(attr.codeId) ) {
			//System.out.println("EQUAL   this : " + this + "\t other : " + other);	
			return true;
		}
		
		return false;
	}

	// 정렬 순서 : 그룹 우선 (1, 2, 3) --> 개별 속성
	@Override
	public int compareTo(JobAdAttr o) {
		int comp = this.groupDisplayOrder - o.groupDisplayOrder;
		if ( comp!=0 ) return comp;
		
		return this.codeDisplayOrder - o.codeDisplayOrder;
	}
}
