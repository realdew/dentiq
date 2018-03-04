package dentiq.api.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dentiq.api.service.exception.LogicalException;
import dentiq.api.util.CodeCache;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)
public class JobAdAttrGroup {

	@Getter
	private String groupId;
	
	@Getter
	private String groupName;
	
	@Getter
	private int displayOrder;
	
	@Getter
	private List<JobAdAttr> attrList;
	
	// 생성자: DB의 데이터를 읽어 CodeService가 생성할 때 사용한다.
	public JobAdAttrGroup(String groupId, String groupName, int displayOrder) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.displayOrder = displayOrder;
	}
	
	public JobAdAttrGroup(String groupId) {
		this.groupId = groupId;
	}
	
	// 클라이언트로부터 입력될 때 생성하는 것
	public static List<JobAdAttrGroup> createJobAdAttrGroupListForInput(List<String> attrCodeList) throws LogicalException {
		// attrCodeList 입력 형태 : EMP=EMP_1&EMP=EMP_2&EMP_EMP_3
		if ( attrCodeList==null ) return null;
		int attrCount = attrCodeList.size();
		if ( attrCount<=0 ) return null;
		
		Map<String, JobAdAttrGroup> map = new HashMap<String, JobAdAttrGroup>();
		for ( int i=0; i<attrCount; i++ ) {
			System.out.println("ATTR : [" + attrCodeList.get(i) + "]");
			if ( attrCodeList.get(i)==null || attrCodeList.get(i).trim().equals("") ) continue;
			
			JobAdAttr attr = new JobAdAttr(attrCodeList.get(i));
			String groupId = attr.getGroupId();
			JobAdAttrGroup attrGroup = map.get(groupId);
			if ( attrGroup==null ) {
				attrGroup = new JobAdAttrGroup(groupId);
				map.put(groupId, attrGroup);
			}
			attrGroup.addAttr(attr);
		}
		
		//Collection<JobAdAttrGroup> collection = map.values();	
		return new ArrayList<JobAdAttrGroup>(map.values());
	}
	
	public static List<JobAdAttrGroup> createJobAdAttrGroupListForOutput(List<String> attrCodeList) throws Exception {
		
		// 만일 attrCodeList가 없는 경우는 전체인 경우인데... 전체라면... 값을 다 보여주어야 하지 않는가?
		
		
		if ( attrCodeList==null ) return null;
		int attrCount = attrCodeList.size();
		if ( attrCount<=0 ) return null;
		
		CodeCache codeCache = CodeCache.getInstance();
		
		Map<String, JobAdAttrGroup> map = new HashMap<String, JobAdAttrGroup>();
		for ( String attrCodeStr : attrCodeList ) {
			if ( attrCodeStr == null || attrCodeStr.trim().equals("") ) continue;
			
			JobAdAttr attr = codeCache.getJobAdAttr(attrCodeStr);
			
			JobAdAttrGroup attrGroup = map.get(attr.getGroupId());
			if ( attrGroup == null ) {
				attrGroup = new JobAdAttrGroup(attr.getGroupId(), attr.getGroupName(), attr.getGroupDisplayOrder());
				map.put(attr.getGroupId(), attrGroup);
			}
			attrGroup.addAttr(attr);
		}
		
		
		// 개별 그룹 내의 ATTR들을 정렬한다.
		for (Map.Entry<String, JobAdAttrGroup> entry : map.entrySet() ) {
			entry.getValue().reorderInnerAttrList();
		}
		
		
		// 각 그룹을 정렬한다.
		ArrayList<JobAdAttrGroup> attrGroupList = new ArrayList<JobAdAttrGroup>(map.values());
		attrGroupList.sort(new Comparator<JobAdAttrGroup>() {
			@Override
			public int compare(JobAdAttrGroup a, JobAdAttrGroup b) {
				return a.getDisplayOrder() - b.getDisplayOrder();
			}
		});
				
		//Collection<JobAdAttrGroup> collection = map.values();	
		return attrGroupList;
	}
	
	
	
	
	// 속성 리스트 내의 속성들을 재정렬한다 (CODE_DISPLAY_ORDER 순서대로)
	public synchronized void reorderInnerAttrList() {
		if ( this.attrList==null || this.attrList.size()<2 ) return;
		
		this.attrList.sort(new Comparator<JobAdAttr>() {
			@Override
			public int compare(JobAdAttr a, JobAdAttr b) {
				int comp = a.getGroupDisplayOrder() - b.getGroupDisplayOrder();
				if ( comp!=0 ) return comp;
				
				return a.getCodeDisplayOrder() - b.getCodeDisplayOrder();
			}
		});
	}
	
	public String toString() {
		String str = "{ GROUP_ID [" + this.groupId + "] ORDER:" + this.displayOrder + ": \n\t";
		for ( int i=0; this.attrList!=null && i<this.attrList.size(); i++ ) {
			str += this.attrList.get(i) + " | ";
		}
		return str + "}\n";
	}
	
	
	

	public boolean addAttr(JobAdAttr newAttr) throws LogicalException {
		if ( attrList == null ) {
			if ( this.groupId!=null && !this.groupId.equals(newAttr.getGroupId()) ) {
				System.out.println(this.groupId + " <-> " + newAttr.getGroupId() );
				throw new LogicalException();
			}
			
			this.attrList = new ArrayList<JobAdAttr>();
			this.attrList.add(newAttr);
			return true;
		}
		
		//int attrCount = this.attrList.size();
		
		if ( this.groupId != null && !this.groupId.equals(newAttr.getGroupId()) ) {
			System.out.println(this.groupId + " <-> " + newAttr.getGroupId() );
			throw new LogicalException();
		}
		
		
		for ( JobAdAttr oldAttr : this.attrList ) {
//		for ( int i=0; i<attrCount; i++ ) {
//			JobAdAttr oldAttr = this.attrList.get(i);
			//System.out.println("OLD : " + oldAttr + "\t NEW : " + newAttr);
			
			
			if ( oldAttr.equals(newAttr) ) {
				//System.out.println("FALSE : OLD : " + oldAttr + "\t NEW : " + newAttr);
				return false;
			}
			if ( !oldAttr.getGroupId().equals(newAttr.getGroupId()) ) throw new LogicalException();
		}
		
		this.attrList.add(newAttr);
		if ( this.groupId==null ) this.groupId = newAttr.getGroupId();
		return true;
	}
	
	
	
}
