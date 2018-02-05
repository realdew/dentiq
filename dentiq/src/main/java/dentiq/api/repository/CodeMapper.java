package dentiq.api.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import dentiq.api.model.JobAdAttr;
import dentiq.api.model.LocationCode;

@Mapper
@Repository
public interface CodeMapper {

	
	public List<LocationCode> listLocationCode() throws Exception;
	
	public List<LocationCode> listSidoCode() throws Exception;
	
	public List<LocationCode> listSiguCode() throws Exception;
	
	@Select("select	* from LOCATION_CODE where LOCATION_CODE=#{code}")
	public LocationCode getLocationCode(String code) throws Exception;
	
	
	public List<JobAdAttr> getJobAdAttrList() throws Exception;
}
