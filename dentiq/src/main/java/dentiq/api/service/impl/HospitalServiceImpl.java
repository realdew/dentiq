package dentiq.api.service.impl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dentiq.api.controller.PageableList;
import dentiq.api.model.Hospital;
import dentiq.api.repository.HospitalMapper;
import dentiq.api.repository.criteria.PageCriteria;
import dentiq.api.service.HospitalService;

@Service
public class HospitalServiceImpl implements HospitalService {
	
	public static final Logger logger = LoggerFactory.getLogger(HospitalServiceImpl.class);
	
	@Autowired
	private HospitalMapper hospitalMapper;
	
	
	
	
	
	/**
	 * 병원 정보를 조회한다.
	 * 
	 * @param	병원 ID
	 * @return	병원 정보
	 */
	@Override
	public Hospital get(Integer id) throws Exception {
		return hospitalMapper.readHospital(id);
	}
	
	
	
	
	@Override
	public List<Hospital> listHospitals(int page) throws Exception {
		PageCriteria pageCriteria = new PageCriteria(page, 10);
		return hospitalMapper.listHospitals(pageCriteria);
	}

	
	
	@Deprecated
	@Override
	public List<Hospital> searchHospitals() throws Exception {
		return null;
	}

	
	/**
	 * HIRA 병원 정보를 병원명으로 검색한다.
	 */
	@Deprecated
	@Override
	//public List<Hospital> searchHiraHosiptalList(String name, Integer pageNo) throws Exception {
	public PageableList<Hospital> searchHiraHosiptalList(String name, Integer pageNo) throws Exception {
		int DEFAULT_ITEM_CNT_PER_PAGE = 20;
		
		int totalCnt = hospitalMapper.countHiraHospitals(name);
		if ( totalCnt == 0 ) {		// 검색 결과가 없을 경우.
			PageableList<Hospital> page = new PageableList<Hospital>(DEFAULT_ITEM_CNT_PER_PAGE);
			return page;
		}

		// 검색 결과가 있을 경우.
		PageCriteria pageCriteria = null;
		if ( pageNo ==null ) {
			pageCriteria = new PageCriteria(1, DEFAULT_ITEM_CNT_PER_PAGE);	// 페이지 지정이 없으면, 처음 1페이지(20개)
		} else {
			pageCriteria = new PageCriteria(pageNo, DEFAULT_ITEM_CNT_PER_PAGE);
		}
		List<Hospital> hospitalList = hospitalMapper.listHiraHospitals(name, pageCriteria);
		
		PageableList<Hospital> page = 
					new PageableList<Hospital>(pageNo, totalCnt, DEFAULT_ITEM_CNT_PER_PAGE, hospitalList);
		
		return page;
		
	}
	
	
	
	
	
}
