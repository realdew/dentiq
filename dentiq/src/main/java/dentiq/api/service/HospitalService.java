package dentiq.api.service;

import java.util.List;

import dentiq.api.controller.PageableList;
import dentiq.api.model.Hospital;

public interface HospitalService {
	
	/**
	 * 병원의 로고 이미지를 변경한다.
	 * @param hospitalId	병원 ID
	 * @param fileName		병원 로고 이미지 파일명 (경로는 제외하고 파일명만)
	 * @return 저장된 병원 로고 이미지 파일명 (경로는 제외하고 파일명만)
	 * @throws Exception
	 */
	public String updateHospitalLogoImageName(Integer hospitalId, String fileName) throws Exception;
	
	/**
	 * 병원 ID로 병원 정보를 리턴한다.
	 * @param hospitalId	병원 ID
	 * @return 병원 정보 객체
	 * @throws Exception
	 */
	public Hospital getById(Integer hospitalId) throws Exception;
	
	/**
	 * 사용자 ID로 해당 사용자의 병원 정보를 리턴한다.
	 * @param userId	사용자 ID
	 * @return			해당 사용자의 병원 정보 객체
	 * @throws Exception
	 */
	public Hospital getByUserId(Integer userId) throws Exception;
	
	
	
	
	
	
	
	
	
	
	
	
	
	/************************************ 병원 정보 ******************************************/
	
	/**
	 * 심평원 병원 목록을 검색한다.
	 * @param name	병원명 (like 검색 수행)
	 * @param pageNo 페이지 번호
	 * @return
	 * @throws Exception
	 */
	public PageableList<Hospital> searchHiraHosiptalList(String name, Integer pageNo) throws Exception;
	
	
	/**
	 * 등록된 병원의 목록을 출력한다.
	 * @param page 페이지 번호
	 * @return
	 * @throws Exception
	 */
	public List<Hospital> listHospitals(int page) throws Exception;
	
	
	public List<Hospital> searchHospitals() throws Exception;
	
	/*
	public void modify(Hospital hospital) throws Exception;
	
	public void regist(Hospital hospital) throws Exception;
	
	public void remove(Integer id) throws Exception;
	*/
	
	
	
	

}
