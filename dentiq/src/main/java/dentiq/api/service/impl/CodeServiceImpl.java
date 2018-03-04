package dentiq.api.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dentiq.api.model.JobAdAttr;
import dentiq.api.model.JobAdAttrGroup;
import dentiq.api.model.LocationCode;
import dentiq.api.repository.CodeMapper;
import dentiq.api.service.CodeService;
import dentiq.api.util.CodeCache;

/**
 * TODO 향후에는 Cache하는 것으로 변경할 것
 * @author lee
 *
 */
@Service
public class CodeServiceImpl implements CodeService {

	@Autowired
	private CodeMapper mapper;
	
	@Override
	public Map<String, LocationCode> getLocationCodeTree() throws Exception {
		
		CodeCache codeCache = CodeCache.getInstance();
		Map<String, LocationCode> locationCodeTree = codeCache.getLocationCodeTree();
		if ( locationCodeTree == null ) {	setupCodeCacheLocationCode(); }
		
		return codeCache.getLocationCodeTree();
	}
	
	
	@Override
	public List<LocationCode> listLocationCode() throws Exception {
		CodeCache codeCache = CodeCache.getInstance();
		List<LocationCode> locationCodeList = codeCache.listLocationCode();
		if ( locationCodeList == null ) {	setupCodeCacheLocationCode();	}
		
		return codeCache.listLocationCode();
	}
	@Override
	public List<LocationCode> listSidoCode() throws Exception {
		CodeCache codeCache = CodeCache.getInstance();
		List<LocationCode> sidoCodeList = codeCache.listSidoCode();
		if ( sidoCodeList == null ) {		setupCodeCacheLocationCode();	}		
		
		return codeCache.listSidoCode();
	}
	@Override
	public List<LocationCode> listSiguCode() throws Exception {
		CodeCache codeCache = CodeCache.getInstance();
		List<LocationCode> siguCodeList = codeCache.listSiguCode();
		if ( siguCodeList == null ) {		setupCodeCacheLocationCode();	}
		
		return codeCache.listSiguCode();
	}
	
	@Override
	public LocationCode getLocationCode(String locationCodeStr) throws Exception {
		throw new Exception("DEPRECATED");
	}
	
	private void setupCodeCacheLocationCode() throws Exception {
		CodeCache codeCache = CodeCache.getInstance();
		codeCache.setLocationCodeList(mapper.listLocationCode());
	}
	

	

	@Override
	public List<JobAdAttr> getJobAdAttrList() throws Exception {
		CodeCache codeCache = CodeCache.getInstance();
		List<JobAdAttr> jobAdAttrCodeList = codeCache.getJobAdAttrList();
		if ( jobAdAttrCodeList == null ) {		setupCodeCacheJobAdAttr();	}
		
		return codeCache.getJobAdAttrList();
	}
	
	@Override
	public List<JobAdAttrGroup> getJobAdAttrContainer() throws Exception {
		CodeCache codeCache = CodeCache.getInstance();
		List<JobAdAttrGroup> jobAdAttrGroupList = codeCache.getJobAdAttrContainer();
		if ( jobAdAttrGroupList == null ) {		setupCodeCacheJobAdAttr();	}
		
		return codeCache.getJobAdAttrContainer();
	}
	
	private void setupCodeCacheJobAdAttr() throws Exception {
		CodeCache codeCache = CodeCache.getInstance();
		codeCache.setJobAdAttrList(mapper.getJobAdAttrList());
	}
	
	/*
	@Override
	public List<LocationCode> listLocationCode() throws Exception {
		return mapper.listLocationCode();
	}
	@Override
	public List<LocationCode> listSidoCode() throws Exception {
		return mapper.listSidoCode();
	}
	@Override
	public List<LocationCode> listSiguCode() throws Exception {
		return mapper.listSiguCode();
	}
	
	@Override
	public LocationCode getLocationCode(String locationCodeStr) throws Exception {
		return mapper.getLocationCode(locationCodeStr);
	}
	

//	@Override
//	public List<LocationCodeGroup> getLocationCodeGroup() throws Exception {
//		List<LocationCodeGroup> groupList = new ArrayList<LocationCodeGroup>();
//		
//		List<LocationCode> locationCodeList = this.listLocationCode();
//		int cnt = locationCodeList.size();
//		
//		String lastSidoCode = "";
//		LocationCodeGroup currentGroup = null;
//		for ( int i=0; i<cnt; i++ ) {
//			LocationCode code = locationCodeList.get(i);
//			if ( !lastSidoCode.equals(code.getSidoCode()) ) {
//				currentGroup = new LocationCodeGroup(code.getSidoCode(), code.getSidoName());
//				groupList.add(currentGroup);
//			}
//			currentGroup.addLocationCode(code);
//			lastSidoCode = code.getSidoCode();
//		}
//		
//		return groupList;
//	}
	
	
	

	@Override
	public List<JobAdAttr> getJobAdAttrList() throws Exception {
		return mapper.getJobAdAttrList();
	}
	
	@Override
	public List<JobAdAttrGroup> getJobAdAttrContainer() throws Exception {
		List<JobAdAttrGroup> groupList = new ArrayList<JobAdAttrGroup>();
		
		// Query에서 이미 정렬되어 있다는 점을 이용한다.
		List<JobAdAttr> jobAdAttrList = null;
		try {
			jobAdAttrList = this.getJobAdAttrList();	// Query에서 이미 정렬되어 있다는 점을 이용한다.
			if ( jobAdAttrList==null ) return null;
			
			int cnt = jobAdAttrList.size();
			if ( cnt<1 ) return null;
			
			String lastGroupId = "";
			JobAdAttrGroup currentGroup = null;
			for ( int i=0; i<cnt; i++ ) {
				JobAdAttr attr = jobAdAttrList.get(i);
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
			attrGroup.reorderAttrList();
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
	
	*/


}
