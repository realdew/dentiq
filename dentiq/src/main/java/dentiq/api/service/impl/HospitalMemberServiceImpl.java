package dentiq.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.JobAdAttr;
import dentiq.api.model.LocationCode;
import dentiq.api.model.User;
import dentiq.api.repository.CodeMapper;
import dentiq.api.repository.HospitalMapper;
import dentiq.api.repository.JobAdMapper;
import dentiq.api.repository.UserMapper;
import dentiq.api.service.HospitalMemberService;
import dentiq.api.service.exception.LogicalException;


@Service
public class HospitalMemberServiceImpl implements HospitalMemberService {

	
	@Autowired private HospitalMapper hospitalMapper;
	
	@Autowired private JobAdMapper jobAdMapper;
	
	@Autowired private CodeMapper codeMapper;
	
	@Autowired private UserMapper userMapper;
	
	

	
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
		
		Hospital oldHospital = hospitalMapper.readHospitalByUserId(userId);
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
		if ( hospitalMapper.createHospital(hospital) != 1 ) {
			throw new LogicalException("");
		}
		
		System.out.println("userId : " + userId + ",  new hospitalId : " + hospital.getId());		
		// 회원 정보 수정한다.
		// 회원 정보에 병원 ID를 삽입한다.
		if ( hospitalMapper.updateHospitalIdOfUser(userId, hospital.getId()) != 1 ) {
			throw new Exception();
		}
		
		return hospitalMapper.readHospital(hospital.getId());
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
		
		Hospital oldHospital = hospitalMapper.readHospitalByUserId(userId);
		if ( oldHospital==null ) {
			throw new Exception("기존에 등록된 병원정보가 없습니다.");
		}
		if ( !oldHospital.getUserId().equals(userId) ) {
			throw new Exception("해당 병원[userId:" + oldHospital.getUserId() + "]에 대한 수정 권한이 없습니다. [userId:" + userId + "]");
		}
		
		//--------------------------- LOCATION 코드 검증 및 처리 ------------------------	
		hospital = addLocationInfo(hospital);
		//------------------------------------------------------------------------------
		
		if ( hospitalMapper.updateHospital(hospital) != 1 ) {
			throw new LogicalException("");
		}
		
		return hospitalMapper.readHospital(hospital.getId());
	}
	
	
	
	
	
	
	
	
	

	
	
	@Override
	public List<JobAd> listJobAdOfHospital(Integer hospitalId) throws Exception {
		return jobAdMapper.listJobAdOfHospital(hospitalId);
	}
	@Override
	public List<Long> listJobAdIdOfHospital(Integer hospitalId) throws Exception {
		return jobAdMapper.listJobAdIdOfHospital(hospitalId);
	}
	@Override
	public List<JobAd> listJobAdOfUser(Integer userId) throws Exception {
		return jobAdMapper.listJobAdOfUser(userId);
	}
	@Override
	public List<Long> listJobAdIdOfUser(Integer userId) throws Exception {
		return jobAdMapper.listJobAdIdOfUser(userId);
	}
	
	
	@Override
	public Hospital getHospitalByUserId(Integer userId) throws Exception {
		return hospitalMapper.readHospitalByUserId(userId);
	}
	
	
	
	@Override
	@Transactional(readOnly=false)
	public JobAd createJobAd(Integer userId, JobAd jobAd) throws Exception {
		
		String hiringTermType = jobAd.getHiringTermType();
		if ( hiringTermType==null ) throw new Exception("채용기간형태(상시/기간)가 입력되지 않았습니다.");
		
		if ( hiringTermType.equals("2") ) {	// 1: 상시채용 2: 기간채용
			String hiringStartDate = jobAd.getHiringStartDate();
			String hiringEndDate   = jobAd.getHiringEndDate();
			if ( hiringStartDate==null || hiringStartDate.trim().equals("") || hiringEndDate==null || hiringEndDate.trim().equals("") ) {
				throw new Exception("기간채용("+hiringTermType+")일 때는 채용 시작/종료기간이 반드시 입력되어야 합니다. (" + hiringStartDate + "/" + hiringEndDate + ")");
			}
		}
		
		
		
				
		User user = userMapper.getUserById(userId);
		if ( user==null ) {
			throw new Exception("해당 사용자가 존재하지 않습니다. [" + userId + "]");
		}
		if ( user.getUserType() == null || !user.getUserType().equals(User.USER_TYPE_HOSPITAL) ) {
			throw new Exception("해당 사용자는 병원회원이 아닙니다. [" + user.getUserType() + "]");
		}
		

		Hospital hospital = hospitalMapper.readHospitalByUserId(userId);
		if ( hospital == null ) {
			throw new Exception("병원정보가 없습니다. [userId:" + userId + "]");
		}		
		if ( user.getHospitalId() == null || user.getHospitalId()==0 ) {
			throw new Exception("회원의 병원정보 없음 [" + user.getHospitalId() + "]");
		}		
		if ( !user.getHospitalId().equals(hospital.getId()) ) {
			throw new Exception("회원의 병원 정보 불일치 [userHospital:" + user.getHospitalId() + "] [hospital:" + hospital.getId() + "]");
		}
		
		
		
		
		jobAd.setHospitalId(hospital.getId());
		
		// 1. 먼저 생성
		int updatedRows = jobAdMapper.createJobAd(jobAd);
		if ( updatedRows != 1 ) throw new Exception("JobAd 생성 실패 : 변경행 [" + updatedRows + "]");
				
		
		updateJobAdAttr(jobAd.getId(), jobAd.getAttr());
		
		return jobAdMapper.getJobAd(jobAd.getId());
	}
	
	@Override
	public JobAd updateJobAdBasic(Integer userId, JobAd jobAd) throws Exception {
		
		String hiringTermType = jobAd.getHiringTermType();
		if ( hiringTermType==null ) throw new Exception("채용기간형태(상시/기간)가 입력되지 않았습니다.");
		
		if ( hiringTermType.equals("2") ) {	// 1: 상시채용 2: 기간채용
			String hiringStartDate = jobAd.getHiringStartDate();
			String hiringEndDate   = jobAd.getHiringEndDate();
			if ( hiringStartDate==null || hiringStartDate.trim().equals("") || hiringEndDate==null || hiringEndDate.trim().equals("") ) {
				throw new Exception("기간채용("+hiringTermType+")일 때는 채용 시작/종료기간이 반드시 입력되어야 합니다. (" + hiringStartDate + "/" + hiringEndDate + ")");
			}
		}
		
		
		User user = userMapper.getUserById(userId);
		if ( user==null ) {
			throw new Exception("해당 사용자가 존재하지 않습니다. [" + userId + "]");
		}
		if ( user.getUserType() == null || !user.getUserType().equals(User.USER_TYPE_HOSPITAL) ) {
			throw new Exception("해당 사용자는 병원회원이 아닙니다. [" + user.getUserType() + "]");
		}
		
		Hospital hospital = hospitalMapper.readHospitalByUserId(userId);
		if ( hospital == null ) {
			throw new Exception("병원정보가 없습니다. [userId:" + userId + "]");
		}		
		if ( user.getHospitalId() == null || user.getHospitalId()==0 ) {
			throw new Exception("회원의 병원정보 없음 [" + user.getHospitalId() + "]");
		}		
		if ( !user.getHospitalId().equals(hospital.getId()) ) {
			throw new Exception("회원의 병원 정보 불일치 [userHospital:" + user.getHospitalId() + "] [hospital:" + hospital.getId() + "]");
		}
		
		if ( !hospital.getId().equals(jobAd.getHospitalId()) ) {
			throw new Exception("변경 요청된 공고는 해당 병원의 것이 아닙니다. [" + jobAd.getHospitalId() + "] [" + hospital.getId() + "]");
		}
		
		
		
		
		
		int updatedRows = jobAdMapper.updateJobAdBasic(jobAd);
		if ( updatedRows != 1 ) throw new Exception("JobAd 수정 실패 : 변경행 [" + updatedRows + "]");
		
		updateJobAdAttr(jobAd.getId(), jobAd.getAttr());
		
		return jobAdMapper.getJobAd(jobAd.getId());
	}
	
	
	protected List<JobAdAttr> updateJobAdAttr(Long jobAdId, List<String> attrStrList) throws Exception {
		if ( jobAdId==null ) throw new Exception();
		
		
		// 기존 것을 삭제 (주의 : 값이 입력되지 않은 것(null or size=0)이면, 전체가 삭제된다.
		jobAdMapper.deleteJobAdAttr(jobAdId);
		
		// 새로운 값들을 삽입
		if ( attrStrList != null && attrStrList.size() > 0 ) {
			List<JobAdAttr> attrCodeList = new ArrayList<JobAdAttr>();
			for ( String attrStr : attrStrList ) {
				if ( attrStr!=null && !attrStr.trim().equals("") ) {		// null이나 빈 값('')이 아닐 때만 insert한다.
					attrCodeList.add( new JobAdAttr(attrStr.trim()) );
				}
			}
			
			System.out.println("JOB_AD_ATTR 생성 대상 : " + attrCodeList);
			jobAdMapper.createJobAdAttr(jobAdId, attrCodeList);
		}
		
		return jobAdMapper.getJobAdAttrList(jobAdId);
	}
	
	
	@Override
	public void deleteJobAd(Long jobAdId) throws Exception {
		int updatedRows = jobAdMapper.deleteJobAdAttr(jobAdId);
		if ( updatedRows != 1 ) throw new Exception("jobAd 삭제 실패 : 변경행 [" + updatedRows + "]");
		
	}
}
