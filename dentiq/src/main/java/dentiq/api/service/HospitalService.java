package dentiq.api.service;

import java.util.List;

import dentiq.api.controller.PageableList;
import dentiq.api.model.Hospital;

public interface HospitalService {
	
	public PageableList<Hospital> searchHiraHosiptalList(String name, Integer pageNo) throws Exception;
	
	public Hospital createHospital(Hospital hospital) throws Exception;
	
	public List<Hospital> listHospitals(int page) throws Exception;
	

	public Hospital get(Integer id) throws Exception;

	
	public List<Hospital> searchHospitals() throws Exception;
	
	/*
	public void modify(Hospital hospital) throws Exception;
	
	public void regist(Hospital hospital) throws Exception;
	
	public void remove(Integer id) throws Exception;
	*/

}
