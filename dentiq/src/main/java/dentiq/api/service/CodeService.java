package dentiq.api.service;

import java.util.List;

import dentiq.api.model.JobAdAttr;
import dentiq.api.model.JobAdAttrGroup;
import dentiq.api.model.LocationCode;
import dentiq.api.model.LocationCodeGroup;

/**
 * 기초 코드성 데이터들에 대한 조회 서비스
 * @author lee
 *
 */
public interface CodeService {
	
	public List<LocationCode> listLocationCode() throws Exception;
	public List<LocationCode> listSidoCode() throws Exception;
	public List<LocationCode> listSiguCode() throws Exception;
	public LocationCode getLocationCode(String code) throws Exception;
	
	
	public List<JobAdAttr> getJobAdAttrList() throws Exception;
	
	public List<JobAdAttrGroup> getJobAdAttrContainer() throws Exception;

	// public List<LocationCodeGroup> getLocationCodeGroup() throws Exception;
	
	
}
