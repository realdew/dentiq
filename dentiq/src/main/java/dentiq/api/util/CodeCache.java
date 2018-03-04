package dentiq.api.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


import dentiq.api.model.JobAdAttr;
import dentiq.api.model.JobAdAttrGroup;
import dentiq.api.model.LocationCode;
import dentiq.api.service.CodeService;

public class CodeCache implements CodeService {
	
	private CodeCache() {
		System.out.println("CodeCache 생성되었음");
	}
	
	public static CodeCache getInstance() {
		return CodeCacheHolder.instance;
	}
	
	private static class CodeCacheHolder {
		private static final CodeCache instance = new CodeCache();
	}
	
	//=================================================
	
	public void reload() throws Exception {
		
	}

	private List<LocationCode>			locationCodeList;
	private List<LocationCode>			sidoCodeList;
	private List<LocationCode>			siguCodeList;
	
	private Map<String, LocationCode>	locationCodeMap;
	
	private Map<String, LocationCode>	locationCodeTree;
	
	
	@Override
	public Map<String, LocationCode> getLocationCodeTree() {
		return this.locationCodeTree;
	}
//	public synchronized void setLocationCodeTree(Map<String, LocationCode> locationCodeTree) {
//		this.locationCodeTree = locationCodeTree;
//	}
	
	private void makeLocationCodeTree() throws Exception {
		System.out.println("1");
				
		System.out.println("2 " + sidoCodeList + "\n" + siguCodeList);
		// 시도를 먼저 만든다.
		Map<String, LocationCode> sidoMap = new HashMap<String, LocationCode>();
		for ( LocationCode sido: sidoCodeList) {
			sidoMap.put(sido.getLocationCode(), sido);
			System.out.println("시도 추가됨 [" + sido.getLocationCode() + "]  " + sido);
		}
		
		System.out.println("3");
		// 시구를 각각의 시도에 넣는다.
		for ( LocationCode sigu: siguCodeList ) {				
			LocationCode sido = sidoMap.get(sigu.getSidoCode());
			sido.addChild(sigu);
		}
		
		System.out.println("4 " + sidoMap);
		this.locationCodeTree = sidoMap;
	}
	
	
	@Override
	public List<LocationCode> listLocationCode() throws Exception {
		return this.locationCodeList;
	}
	public synchronized void setLocationCodeList(List<LocationCode> locationCodeList) throws Exception {
		this.locationCodeList = locationCodeList;
			
		if ( locationCodeList == null || locationCodeList.size() == 0 ) return;
		
		this.sidoCodeList = new ArrayList<LocationCode>();
		this.siguCodeList = new ArrayList<LocationCode>();
		this.locationCodeMap = new Hashtable<String, LocationCode>();
		
		for ( LocationCode locationCode : locationCodeList ) {
			if ( locationCode.isSido() ) this.sidoCodeList.add(locationCode);
			else if ( locationCode.isSigu() ) this.siguCodeList.add(locationCode);
			
			locationCodeMap.put(locationCode.getLocationCode(), locationCode);
		}
		
		makeLocationCodeTree();
	}
	
	@Override
	public List<LocationCode> listSidoCode() throws Exception {
		return this.sidoCodeList;
	}

	@Override
	public List<LocationCode> listSiguCode() throws Exception {
		return this.siguCodeList;
	}
	
	@Override
	public LocationCode getLocationCode(String code) throws Exception {
		return this.locationCodeMap.get(code);
	}

	
	private List<JobAdAttr>				jobAdAttrList;
	
	private List<JobAdAttrGroup>		jobAdAttrGroupList;
	
	private Map<String, JobAdAttr>		jobAdAttrMap;
	
	public JobAdAttr getJobAdAttr(String attrCode) {
		return jobAdAttrMap.get(attrCode);
	}

	@Override
	public List<JobAdAttr> getJobAdAttrList() {
		return this.jobAdAttrList;
	}
	public void setJobAdAttrList(List<JobAdAttr> jobAdAttrList) throws Exception {
		if ( jobAdAttrList == null ) return;
		
		this.jobAdAttrList = jobAdAttrList;
		
		this.jobAdAttrGroupList = createJobAdAttrGroup(jobAdAttrList);
		
		this.jobAdAttrMap = new Hashtable<String, JobAdAttr>();
		for ( JobAdAttr attr : jobAdAttrList ) {
			this.jobAdAttrMap.put(attr.getCodeId(), attr);
		}
	}

	
	@Override
	public List<JobAdAttrGroup> getJobAdAttrContainer() throws Exception {
		return this.jobAdAttrGroupList;
	}
	
	private List<JobAdAttrGroup> createJobAdAttrGroup(List<JobAdAttr> _jobAdAttrList) throws Exception {
		List<JobAdAttrGroup> groupList = new ArrayList<JobAdAttrGroup>();
		
		if ( _jobAdAttrList == null || _jobAdAttrList.size() == 0 ) return null;
		
		// Query에서 이미 정렬되어 있다는 점을 이용한다.
		try {
			//jobAdAttrList = this.getJobAdAttrList();	// Query에서 이미 정렬되어 있다는 점을 이용한다.
			
			int cnt = _jobAdAttrList.size();
			if ( cnt<1 ) return null;
			
			String lastGroupId = "";
			JobAdAttrGroup currentGroup = null;
			for ( int i=0; i<cnt; i++ ) {
				JobAdAttr attr = _jobAdAttrList.get(i);
				if ( !lastGroupId.equals(attr.getGroupId()) ) {
					currentGroup = new JobAdAttrGroup(attr.getGroupId(), attr.getGroupName(), attr.getGroupDisplayOrder());
					groupList.add(currentGroup);
				}
				currentGroup.addAttr(attr);
				lastGroupId = attr.getGroupId();
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		
		
		// 정렬
		for ( int i=0; i<groupList.size(); i++ ) {
			JobAdAttrGroup attrGroup = groupList.get(i);
			attrGroup.reorderInnerAttrList();
		}
		
		// 정렬
		groupList.sort(new Comparator<JobAdAttrGroup>() {
			@Override
			public int compare(JobAdAttrGroup a, JobAdAttrGroup b) {
				return a.getDisplayOrder() - b.getDisplayOrder();
			}
			
		});
		
		
		System.out.println("정렬완료 : " + groupList);
		
		return groupList;
	}
	
	
}
