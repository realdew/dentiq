package dentiq.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dentiq.api.controller.PageableList;
import dentiq.api.model.Hospital;
import dentiq.api.model.LocationCode;
import dentiq.api.model.User;
import dentiq.api.repository.CodeMapper;
import dentiq.api.repository.HospitalMapper;
import dentiq.api.repository.UserMapper;
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
	
	@Autowired
	private UserMapper userMapper;
	
	
	
	private Hospital addLocationInfo(Hospital hospital) throws Exception {
		String admCd = null;
		try {
			admCd = hospital.getAddrJuso().getAdmCd();	// 행정구역 코드
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
				throw new Exception("LOCATION_CODE 처리 중 오류 locationCode[" + locationCode + "] ==> " + code);
			}
			
		} catch(Exception ex) {
			System.out.println(ex);
			ex.printStackTrace(System.out);
			throw new Exception("행정구역코드(admCd) 포맷 오류 [" + admCd + "] <== [" + ex + "]");
		}
		
		return hospital;
	}
	
	
	
	@Override
	public Hospital createHospital(Integer userId, Hospital hospital) throws Exception {
		
		Hospital oldHospital = mapper.readHospitalByUserId(userId);
		if ( oldHospital != null ) {
			throw new Exception("기존 병원이 이미 존재합니다. 병원등록은 1개만 가능합니다. 기존 ID["+ oldHospital.getId() + "]");
		}
		
		User owner = userMapper.getUserById(userId);
		if ( owner == null ) {
			throw new Exception("존재하지 않는 사용자입니다. [" + userId + "]");
		}
		if ( !owner.getUserType().equals(User.USER_TYPE_HOSPITAL) ) {
			throw new Exception("병원회원만이 병원정보를 등록할 수 있습니다. [" + owner.getUserType() + "]");
		}
		if ( owner.getHospitalId() != null || (owner.getHospitalId()!=null && owner.getHospitalId()!=0) ) {
			throw new Exception ("이미 기존 병원이 등록되어 있습니다. [" + owner.getHospitalId() + "]");
		}
		
		hospital.setBizRegNo(owner.getBizRegNo());		// 사업자등록번호 추가
		
		
		//--------------------------- LOCATION 코드 검증 및 처리 ------------------------	
		hospital = addLocationInfo(hospital);
		//------------------------------------------------------------------------------		
		
		hospital.setUserId(userId);
		if ( mapper.createHospital(hospital) != 1 ) {
			throw new LogicalException("");
		}
		
		System.out.println("userId : " + userId + ",  new hospitalId : " + hospital.getId());		
		// 회원 정보 수정한다.
		// 회원 정보에 병원 ID를 삽입한다.
		if ( mapper.updateHospitalIdOfUser(userId, hospital.getId()) != 1 ) {
			throw new Exception();
		}
		
		return mapper.readHospital(hospital.getId());
	}
	
	
	/**
	 * 병원 정보를 update한다.
	 * 
	 * @param	hospital 등록/수정할 병원 정보
	 * @return	등록/수정된 병원 정보
	 * @throws	Exception
	 */
	public Hospital updateHospital(Integer userId, Hospital hospital) throws Exception {
		
		System.out.println("===> " + hospital);
		
		if ( !hospital.getUserId().equals(userId) ) {
			throw new Exception("수정 권한 없음 [userId:" + userId + "] [입력병원 userId:" + hospital.getUserId() + "]");
		}
		
		Hospital oldHospital = mapper.readHospitalByUserId(userId);
		if ( oldHospital==null ) {
			throw new Exception("기존에 등록된 병원정보가 없습니다.");
		}
		if ( !oldHospital.getUserId().equals(userId) ) {
			throw new Exception("해당 병원[userId:" + oldHospital.getUserId() + "]에 대한 수정 권한이 없습니다. [userId:" + userId + "]");
		}
		
		//--------------------------- LOCATION 코드 검증 및 처리 ------------------------	
		hospital = addLocationInfo(hospital);
		//------------------------------------------------------------------------------
		
		if ( mapper.updateHospital(hospital) != 1 ) {
			throw new LogicalException("");
		}
		
		return mapper.readHospital(hospital.getId());
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
	
	public Hospital getByUserId(Integer userId) throws Exception {
		return mapper.readHospitalByUserId(userId);
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

	
	/**
	 * HIRA 병원 정보를 병원명으로 검색한다.
	 */
	@Deprecated
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
}
