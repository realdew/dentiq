package dentiq.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dentiq.api.controller.PageableList;
import dentiq.api.model.Hospital;
import dentiq.api.model.LocationCode;
import dentiq.api.repository.CodeMapper;
import dentiq.api.repository.HospitalMapper;
import dentiq.api.repository.criteria.PageCriteria;
import dentiq.api.service.HospitalService;
import dentiq.api.service.exception.LogicalException;

@Service
public class HospitalServiceImpl implements HospitalService {
	
	public static final Logger logger = LoggerFactory.getLogger(HospitalServiceImpl.class);
	
	@Autowired
	private HospitalMapper mapper;
	
	@Autowired
	private CodeMapper codeMapper;
	
	/**
	 * HIRA 병원 정보를 병원명으로 검색한다.
	 */
	@Override
	//public List<Hospital> searchHiraHosiptalList(String name, Integer pageNo) throws Exception {
	public PageableList<Hospital> searchHiraHosiptalList(String name, Integer pageNo) throws Exception {
		int DEFAULT_ITEM_CNT_PER_PAGE = 20;
		
		int totalCnt = mapper.countHiraHospitals(name);
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
		List<Hospital> hospitalList = mapper.listHiraHospitals(name, pageCriteria);
		
		PageableList<Hospital> page = 
					new PageableList<Hospital>(pageNo, totalCnt, DEFAULT_ITEM_CNT_PER_PAGE, hospitalList);
		
		return page;
		
	}
	
	@Override
	public Hospital createHospital(Integer userId, Hospital hospital) throws Exception {
		
		
		//--------------------------- LOCATION 코드 검증 및 처리 ------------------------
		String admCd = hospital.getAdmCd();	// 행정구역 코드
		try {
			Long.parseLong(admCd);	// 숫자 형식 여부 확인
			
			String sidoCode = admCd.substring(0, 2);
			String siguCode = admCd.substring(0,  5);			
			String locationCode = sidoCode + LocationCode.CODE_DELIMETER + siguCode;
			
			// 여기서 DB 검증 한번 하여야 한다.			
			LocationCode code = codeMapper.getLocationCode(locationCode);
			if ( code != null && code.getSidoCode().equals(sidoCode) && code.getSiguCode().equals(siguCode) ) {
				hospital.setSidoCode(sidoCode);
				hospital.setSiguCode(siguCode);
				hospital.setLocationCode(locationCode);
				
				hospital.setSidoName(code.getSidoName());
				hospital.setSiguName(code.getSiguName());
				
			} else {
				throw new LogicalException("LOCATION_CODE 처리 중 오류 locationCode[" + locationCode + "] ==> " + code);
			}
			
		} catch(Exception ex) {
			System.out.println(ex);
			ex.printStackTrace(System.out);
			throw new LogicalException("행정구역코드(admCd) 포맷 오류 [" + admCd + "] <== [" + ex + "]");
		}
		//------------------------------------------------------------------------------
		
		hospital.setUserId(userId);
		
		
		int rowsUpdated = mapper.createHospital(hospital);
		if ( rowsUpdated == 1 ) {
			return mapper.readHospital(hospital.getId());
		} else {
			throw new LogicalException("");
		}
	}
	
	
	/**
	 * 병원 정보를 update한다.
	 * 
	 * @param	hospital 등록/수정할 병원 정보
	 * @return	등록/수정된 병원 정보
	 * @throws	Exception
	 */
	public Hospital updateHospital(Integer userId, Hospital hospital) throws Exception {
		return null;
	}
	
	/**
	 * 병원 정보를 조회한다.
	 * 
	 * @param	병원 ID
	 * @return	병원 정보
	 */
	@Override
	public Hospital get(Integer id) throws Exception {
		return mapper.readHospital(id);
	}
	
	
	@Override
	public List<Hospital> listHospitals(int page) throws Exception {
		PageCriteria pageCriteria = new PageCriteria(page, 10);
		return mapper.listHospitals(pageCriteria);
	}

	
	
	@Deprecated
	@Override
	public List<Hospital> searchHospitals() throws Exception {
		return null;
	}

	/*
	@Override
	public void modify(Hospital hospital) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("안즉 안돼요.");
	}

	@Override
	public void regist(Hospital hospital) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("안즉 안돼요.");
	}

	@Override
	public void remove(Integer id) throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("안즉 안돼요.");
	}
	*/

}
