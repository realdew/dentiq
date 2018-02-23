package dentiq.api.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import dentiq.api.model.Hospital;
import dentiq.api.repository.criteria.PageCriteria;

@Mapper
@Repository
public interface HospitalMapper {
	
	// HIRA 병원 검색은 현재는 '병원명'만으로 검색 및 카운팅 
	@Select("select	* from V_HIRA_HOSP_INFO where NAME like CONCAT('%',#{name},'%') limit #{pageCriteria.startIndexOnPage}, #{pageCriteria.itemCountPerPage}")
	public List<Hospital> listHiraHospitals(@Param("name") String name, @Param("pageCriteria") PageCriteria pageCriteria) throws Exception;
		
	@Select("select	count(1) from V_HIRA_HOSP_INFO where NAME like CONCAT('%',#{name},'%')")
	public int countHiraHospitals(String name) throws Exception;
	
	
	/**
	 * 병원 정보를 생성한다.
	 * @param hospital
	 * @return 병원 ID
	 * @throws Exception
	 */
	public Integer createHospital(Hospital hospital) throws Exception;
	
	public int updateHospital(Hospital hospital) throws Exception;
	
	@Select("select	* from HOSPITAL where ID=#{id}")
	public Hospital readHospital(@Param("id") int id) throws Exception;
	
	@Select("select	* from HOSPITAL limit #{startIndexOnPage}, #{itemCountPerPage}")
	public List<Hospital> listHospitals(PageCriteria pageCriteria) throws Exception;
	
	@Select("select count(*) from HOSPITAL")
	public int countHospitals() throws Exception;
	
	//public List<Hospital> searchHospitals(HospitalSearchCriteria searchCriteria) throws Exception;
	
	
	/*
	@Select("select * from hospital where id=#{id}")
	public Hospital read(Integer id) throws Exception;
	
	
	public Hospital getOne(@Param("id") Integer id) throws Exception;
	
	@Select("select * from hospital")
	public List<Hospital> listAll() throws Exception;
	
	
	public List<Hospital> listPage(int page) throws Exception;
	
	public List<Hospital> listCriteria(PageCriteria cri) throws Exception;
	*/
}
