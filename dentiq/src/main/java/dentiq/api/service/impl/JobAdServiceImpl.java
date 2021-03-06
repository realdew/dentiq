package dentiq.api.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dentiq.api.model.Hospital;
import dentiq.api.model.JobAd;
import dentiq.api.model.JobAdAttr;
import dentiq.api.model.JobAdAttrCounter;
import dentiq.api.model.JobAdAttrGroup;
import dentiq.api.model.JobAdDashboard;
//import dentiq.api.model.JobAdCounter;
//import dentiq.api.model.JobAdDashboard;
import dentiq.api.model.JobAdGroupByLocationCode;
import dentiq.api.model.LocationCode;
import dentiq.api.model.User;
import dentiq.api.repository.HospitalMapper;
import dentiq.api.repository.JobAdMapper;
import dentiq.api.repository.UserMapper;
import dentiq.api.repository.criteria.JobAdSearchCriteria;
import dentiq.api.service.JobAdService;

@Service
@Transactional(readOnly=true)
public class JobAdServiceImpl implements JobAdService {
	
	@Autowired private JobAdMapper mapper;
	
	@Autowired private HospitalMapper hospitalMapper;
	
	@Autowired private UserMapper userMapper;
	
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
		int updatedRows = mapper.createJobAd(jobAd);
		if ( updatedRows != 1 ) throw new Exception("JobAd 생성 실패 : 변경행 [" + updatedRows + "]");
				
		
		updateJobAdAttr(jobAd.getId(), jobAd.getAttr());
		
		return mapper.getJobAd(jobAd.getId());
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
		
		
		
		
		
		int updatedRows = mapper.updateJobAdBasic(jobAd);
		if ( updatedRows != 1 ) throw new Exception("JobAd 수정 실패 : 변경행 [" + updatedRows + "]");
		
		updateJobAdAttr(jobAd.getId(), jobAd.getAttr());
		
		return mapper.getJobAd(jobAd.getId());
	}
	
	
	protected List<JobAdAttr> updateJobAdAttr(Long jobAdId, List<String> attrStrList) throws Exception {
		if ( jobAdId==null ) throw new Exception();
		
		
		// 기존 것을 삭제 (주의 : 값이 입력되지 않은 것(null or size=0)이면, 전체가 삭제된다.
		mapper.deleteJobAdAttr(jobAdId);
		
		// 새로운 값들을 삽입
		if ( attrStrList != null && attrStrList.size() > 0 ) {
			List<JobAdAttr> attrCodeList = new ArrayList<JobAdAttr>();
			for ( String attrStr : attrStrList ) {
				if ( attrStr!=null && !attrStr.trim().equals("") ) {		// null이나 빈 값('')이 아닐 때만 insert한다.
					attrCodeList.add( new JobAdAttr(attrStr.trim()) );
				}
			}
			
			System.out.println("JOB_AD_ATTR 생성 대상 : " + attrCodeList);
			mapper.createJobAdAttr(jobAdId, attrCodeList);
		}
		
		return mapper.getJobAdAttrList(jobAdId);
	}
	
	
	
	@Override
	public JobAd getWithHospital(Long id) throws Exception {
		return mapper.getJobAdWithHospital(id);
	}
	

	@Override
	public JobAd get(Long id) throws Exception {
		return mapper.getJobAd(id);
	}
	
	@Override
	public void deleteJobAd(Long jobAdId) throws Exception {
		int updatedRows = mapper.deleteJobAdAttr(jobAdId);
		if ( updatedRows != 1 ) throw new Exception("jobAd 삭제 실패 : 변경행 [" + updatedRows + "]");
		
	}


	
	
	
	
	
	
	
	
	
	
	
	@Override
	public JobAdDashboard aggregateJobAdsForDashboard(
												List<String> locationCodeList, Integer adType,
												String xPos, String yPos, Integer distance,
												String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
										) throws Exception {
		
		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(		// 주의!!! Dashboard용 생성자를 사용하여야 한다.
												locationCodeList, 
												// adType,
												xPos, yPos, distance, 
												hospitalName, hospitalAddr, atrrGroupList);
		
		List<JobAdGroupByLocationCode> list = mapper.aggregateJobAdsForDashboard(searchCriteria);
		
		return new JobAdDashboard(locationCodeList, list);
	}
	
	@Override
	public JobAdDashboard aggregateJobAds(
												List<String> locationCodeList, Integer adType,
												String xPos, String yPos, Integer distance,
												String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
										) throws Exception {
		
		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(		// 주의!!! Dashboard용인 아닌 일반 검색자를 사용하여야 한다.
												locationCodeList, 
												adType,
												xPos, yPos, distance, 
												hospitalName, hospitalAddr, atrrGroupList);
		
		List<JobAdGroupByLocationCode> list = mapper.aggregateJobAds(searchCriteria);
		
		return new JobAdDashboard(locationCodeList, list);
	}
	
//	
//	@Deprecated
//	@Override
//	public JobAdDashboard aggregateJobAds(
//												// boolean includeChildren,
//												List<String> locationCodeList, Integer adType,
//												String xPos, String yPos, Integer distance,
//												String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
//										) throws Exception {
//		
//		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(		// 주의!!! Dashboard용 생성자를 사용하여야 한다.
//				locationCodeList, 
//				// adType,
//				xPos, yPos, distance, 
//				hospitalName, hospitalAddr, atrrGroupList);
//		
//		List<JobAdGroupByLocationCode> list = mapper.aggregateJobAds(searchCriteria);
//		
//		int totalCnt = 0;
//		int requestedCnt = 0;
//		return new JobAdDashboard(locationCodeList, list, totalCnt, requestedCnt);
//		
//	}
//	
//	@Override
//	public JobAdDashboard aggregateJobAdsBySiguInSpecificSido(
//												// boolean includeChildren,
//												List<String> locationCodeList, Integer adType,
//												String xPos, String yPos, Integer distance,
//												String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
//										) throws Exception {
//		
//		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(		// 주의!!! Dashboard용 생성자를 사용하여야 한다.
//				locationCodeList, 
//				// adType,
//				xPos, yPos, distance, 
//				hospitalName, hospitalAddr, atrrGroupList);
//		
//		List<JobAdGroupByLocationCode> list = mapper.aggregateJobAdsBySiguInSpecificSido(searchCriteria);
//		
//		int totalCnt = 0;
//		int requestedCnt = 0;
//		return new JobAdDashboard(locationCodeList, list, totalCnt, requestedCnt);
//		
//	}
//	
//	@Override
//	public JobAdDashboard aggregateJobAdsBySigu(
//												// boolean includeChildren,
//												List<String> locationCodeList, Integer adType,
//												String xPos, String yPos, Integer distance,
//												String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
//										) throws Exception {
//		
//		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(		// 주의!!! Dashboard용 생성자를 사용하여야 한다.
//				locationCodeList, 
//				// adType,
//				xPos, yPos, distance, 
//				hospitalName, hospitalAddr, atrrGroupList);
//		
//		List<JobAdGroupByLocationCode> list = mapper.aggregateJobAdsBySigu(searchCriteria);
//		
//		int totalCnt = 0;
//		int requestedCnt = 0;
//		return new JobAdDashboard(locationCodeList, list, totalCnt, requestedCnt);
//		
//	}
//	
//	
//	@Override
//	public JobAdDashboard aggregateJobAdsBySido(
//												// boolean includeChildren,
//												List<String> locationCodeList, Integer adType,
//												String xPos, String yPos, Integer distance,
//												String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList
//										) throws Exception {
//		
//		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(		// 주의!!! Dashboard용 생성자를 사용하여야 한다.
//				locationCodeList, 
//				// adType,
//				xPos, yPos, distance, 
//				hospitalName, hospitalAddr, atrrGroupList);
//		
//		List<JobAdGroupByLocationCode> list = mapper.aggregateJobAdsBySido(searchCriteria);
//		
//		int totalCnt = 0;
//		int requestedCnt = 0;
//		return new JobAdDashboard(locationCodeList, list, totalCnt, requestedCnt);
//		
//		
////		if ( locationCodeList==null || locationCodeList.size()==0 ) {
////			if ( includeChildren ) {
////				// 전체 시도에 대해서 시도 group
////			} else {
////				return null;
////			}
////		}
//		
//		// 시도가 포함되어 있는가?
//		
//		
//		// 시구가 포함되어 있는가?
//		
//		
//		
//		// 특정 시도의 하위 시구 group
//		
//		// 시도 + 시구 들 ==> 시도 내의 공고 개수 ... 해당 시구의 공고 개수
//		
//		// 시두 들 ==> 시도 내의 공도 개수
//		
//		
//		
//		
//		
//		
//		
//		// Dashboard인 경우에는 전체를 의미 vs 관심지역인 경우에는 없는 것	// Controller 단에서 해결할 것
//		
//		
//		// locationCodeList가 있을 때. locationCode가 시도 1개일 경우.
//		
//	}
//
//	
//	
//	
//	// @Override
//	public JobAdDashboard aggregationJobAdsBySido(
//											List<String> locationCodeList, Integer adType,
//											String xPos, String yPos, Integer distance, 
//											String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList, String aggregationBase
//										) throws Exception {
//		
//		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(		// 주의!!! Dashboard용 생성자를 사용하여야 한다.
//				locationCodeList, 
//				// adType,
//				xPos, yPos, distance, 
//				hospitalName, hospitalAddr, atrrGroupList);
//		
//		/*
//		대쉬보드 형태
//			A. location 입력되지 않으면,			시도별로 Group하여 aggregation한 결과를 리턴
//			B. location이 시도(1개)가 입력되면,	해당 시도 내의 시구들을 Group하여 aggregation한 결과를 리턴
//			C. location이 특정 시구가 입력되면,
//				==> client에서 특정 시구 들의 소속 시도를 전송한다. B의 형태로 처리한다.
//				==> 또는, 송신된 특정 시구들의 소속 시도를 확인해서, 소속 시도가 1개가 아니면 에러처리. 하나의 시도가 추출되면, B로 처리
//			
//		관심지역 형태
//			A. location이 입력되지 않으면, 빈 값 출력
//			B. location이 입력되면, 시도와 시구가 섞여서 입력될 것임.
//				 /-- 시도에 대해서는 시도만
//				|
//				 \-- 시구에 대해서는 해당 시구만
//			LOCATION_CODE에는 시도 정보도 있으므로, LOCATION_CODE와 JOB_AD_VIEW를 시도는 0건 나옴. 여기서 java 코드로 시도에 aggregation 수행
//			
//		
//		
//		*/
//		
//		
//		// 지역코드가 입력되지 않으면, 빈 값 출력
//		if ( locationCodeList==null || locationCodeList.size()==0 ) {
//			return null;
//		}
//		
//		List<JobAdGroupByLocationCode> list = mapper.aggregateJobAdsBySido(searchCriteria);
//		
//		// 시도 또는 시구 단위로만 Aggregation 수행한다.
//		
//		
//		
////		
////		
////		
////		
////		
////		// 집계 코드
////		Map<String, JobAdGroupByLocationCode> aggregationMap = new HashMap<String, JobAdGroupByLocationCode>();
////		for ( JobAdGroupByLocationCode item : list ) {
////			if ( item.isSido() ) {
////				aggregationMap.put(item.getSidoCode(), item);
////			}
////		}
////		for ( JobAdGroupByLocationCode item : list ) {
////			if ( item.isSigu() ) {
////				JobAdGroupByLocationCode aggregation = aggregationMap.get(item.getSidoCode());
////				if ( aggregation != null ) aggregation.addChild(item);
////			}
////		}
////		
////		
////		/** 이하의 코드는 화면 처리를 간단하게 하기 위하여 비효율적으로 작성되었음. 서버 코드 전체 수정 필요 **/
////		
//		int totalCnt = 0;		// 전체 건수
//		int requestedCnt = 0;	// 요청된 location code에 대한 누적 건수
//		for ( JobAdGroupByLocationCode item : list ) {
//			totalCnt += item.getCnt();
//			
//			if ( locationCodeList==null || locationCodeList.size()==0 ) {		// location code 없이 호출한 경우 : ex) 전체 보기 (서울, 부산, ...)
//				requestedCnt += item.getCnt();
//			} else {
//				for ( String requestedCode : locationCodeList ) {				// location code들이 입력된 경우
//					if ( !requestedCode.contains(LocationCode.CODE_DELIMETER) && item.getLocationCode().contains(requestedCode+".") ) {	// location code가 시도코드인 경우 ex) 서울을 선택한 경우, 서울의 하위 구들이 표시되는 경우 (강남구, 서초구, ...)
//						requestedCnt += item.getCnt();		// TODO 이거 어디다 쓰는 거지?
//					} else if ( requestedCode.equals(item.getLocationCode()) ) {	// location code가 full 코드인 경우 ex) 특정 구를 선택한 경우. 강남구 선택시
//						item.setRequested(true);
//						requestedCnt += item.getCnt();
//					}
//				}
//			}
//		}
////		
////		if ( aggregationMap.size() > 0 ) {
////			List<JobAdGroupByLocationCode> result2 = new ArrayList(aggregationMap.values());
////			System.out.println("집계 처리 결과 리턴 " + result2);
////			return new JobAdDashboard(locationCodeList, result2, totalCnt, requestedCnt);
////		}
//		
//		return new JobAdDashboard(locationCodeList, list, totalCnt, requestedCnt);
//	}

	
	
	@Override
	public JobAdDashboard getJobAdDashboard2(List<String> locationCodeList, Integer adType, String xPos, String yPos,
			Integer distance, String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList)
			throws Exception {
		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(		// 주의!!! Dashboard용 생성자를 사용하여야 한다.
				locationCodeList, 
				// adType,
				xPos, yPos, distance, 
				hospitalName, hospitalAddr, atrrGroupList);
		
		List<JobAdGroupByLocationCode> list;
		if ( locationCodeList==null || locationCodeList.size()==0 ) {	// 시도코드와 시구코드가 모두 입력되지 않았으면, 시도별 Dash보드 생성
			//searchCriteria.getLocationCodeCriteria().changeForDashboardSelfCalling();		//TODO 아, 그지 같다.
			list = mapper.getJobAdDashboard2BySido(searchCriteria);		
		} else {														// 시도코드나 시구코드 중의 하나가 입력되면 시구별 Dash보드(기본) 생성
			list = mapper.getJobAdDashboard2(searchCriteria);
		}
		//list = mapper.getJobAdDashboard2BySido(searchCriteria);
		
		
		
		/** 이하의 코드는 화면 처리를 간단하게 하기 위하여 비효율적으로 작성되었음. 서버 코드 전체 수정 필요 **/
		
		int totalCnt = 0;		// 전체 건수
		int requestedCnt = 0;	// 요청된 location code에 대한 누적 건수
		for ( JobAdGroupByLocationCode item : list ) {
			totalCnt += item.getCnt();
			
			if ( locationCodeList==null || locationCodeList.size()==0 ) {		// location code 없이 호출한 경우 : ex) 전체 보기 (서울, 부산, ...)
				requestedCnt += item.getCnt();
			} else {
				for ( String requestedCode : locationCodeList ) {				// location code들이 입력된 경우
					if ( !requestedCode.contains(LocationCode.CODE_DELIMETER) && item.getLocationCode().contains(requestedCode+".") ) {	// location code가 시도코드인 경우 ex) 서울을 선택한 경우, 서울의 하위 구들이 표시되는 경우 (강남구, 서초구, ...)
						requestedCnt += item.getCnt();		// TODO 이거 어디다 쓰는 거지?
					} else if ( requestedCode.equals(item.getLocationCode()) ) {	// location code가 full 코드인 경우 ex) 특정 구를 선택한 경우. 강남구 선택시
						item.setRequested(true);
						requestedCnt += item.getCnt();
					}
				}
			}
		}
		
		
		return new JobAdDashboard(locationCodeList, list, totalCnt, requestedCnt);
	}
//	@Override
//	@Deprecated
//	public JobAdDashboard getJobAdDashboard2(
//				List<String> sidoCodeList, List<String> siguCodeList, Integer adType,
//				String xPos, String yPos, Integer distance, 
//				String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList) throws Exception {
//		
//		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(
//				sidoCodeList, siguCodeList, adType,
//				xPos, yPos, distance, 
//				hospitalName, hospitalAddr, atrrGroupList);
//		
//		List<JobAdGroupByLocationCode> list;
//		if ( sidoCodeList==null && siguCodeList==null ) {	// 시도코드와 시구코드가 입력되지 않았으면, 시도별 Dash보드 생성
//			list = mapper.getJobAdDashboard2BySido(searchCriteria);
//		} else {											// 시도코드나 시구코드 중의 하나가 입력되면 시구별 Dash보드(기본) 생성
//			list = mapper.getJobAdDashboard2(searchCriteria);
//		}
//		//list = mapper.getJobAdDashboard2BySido(searchCriteria);
//		
//		int cnt = 0;
//		int listSize = list.size();
//		for ( int i=0; i<listSize; i++ ) {
//			cnt += list.get(i).getCnt();
//		}
//		
//		return new JobAdDashboard(list, cnt);
//	}

	
	
	
	
	

	
	@Override
	public List<JobAd> listJobAds(	List<String> locationCodeList, Integer adType,
									String xPos, String yPos, Integer distance,
									String hospitalName, String hospitalAddr, List<JobAdAttrGroup> attrList,
									List<String> sorting, int pageNo, int pageSize
					) throws Exception {		
		
		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(
												locationCodeList, adType,
												xPos, yPos, distance, 
												hospitalName, hospitalAddr, attrList,
												sorting, pageNo, pageSize);
		
		return mapper.listJobAdsGeneric2(searchCriteria);
	}
//	@Override
//	@Deprecated
//	public List<JobAd> listJobAds(	List<String> sidoCodeList, List<String> siguCodeList, Integer adType,
//									String xPos, String yPos, Integer distance,
//									String hospitalName, String hospitalAddr, List<JobAdAttrGroup> attrList,
//									List<String> sorting, int pageNo, int pageSize
//					) throws Exception {		
//		
//		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(
//												sidoCodeList, siguCodeList, adType,
//												xPos, yPos, distance, 
//												hospitalName, hospitalAddr, attrList,
//												sorting, pageNo, pageSize);
//		
//		return mapper.listJobAdsGeneric2(searchCriteria);
//	}
	
	
	
	
	/**
	 * 조건에 맞는 공고 개수를 리턴한다. 공고 유형별로 한번에 처리
	 * @return	전체 공고 개수
	 */
	@Override
	public List<Map> countJobAdsGroupByAdType(List<String> locationCodeList, Integer adType,
				String xPos, String yPos, Integer distance, 
				String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList)
			throws Exception {
		
		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(
									locationCodeList, adType,
									xPos, yPos, distance, 
									hospitalName, hospitalAddr, atrrGroupList);
		return mapper.countJobAdsGroupByAdType(searchCriteria);
	}
	
	/**
	 * 조건에 맞는 공고 개수를 리턴한다.
	 * @return	전체 공고 개수
	 */
	@Override
	public long countJobAds(List<String> locationCodeList, Integer adType,
				String xPos, String yPos, Integer distance, 
				String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList)
			throws Exception {
		
		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(
									locationCodeList, adType,
									xPos, yPos, distance, 
									hospitalName, hospitalAddr, atrrGroupList);
		return mapper.countJobAds(searchCriteria);
	}
//	@Override
//	@Deprecated
//	public long countJobAds(List<String> sidoCodeList, List<String> siguCodeList, Integer adType,
//							String xPos, String yPos, Integer distance, 
//							String hospitalName, String hospitalAddr, List<JobAdAttrGroup> atrrGroupList)
//			throws Exception {
//		
//		JobAdSearchCriteria searchCriteria = new JobAdSearchCriteria(
//									sidoCodeList, siguCodeList, adType,
//									xPos, yPos, distance, 
//									hospitalName, hospitalAddr, atrrGroupList);
//		return mapper.countJobAds(searchCriteria);
//	}

	/*
	@Override
	public List<NameCountPair> countJobAdsByEmpType() throws Exception {
		// TODO Auto-generated method stub
		// 두 개를 조합한다. 전체 건수 및 EMP Attr의 각 count
		Long totalCount = mapper.countJobAds(null);
		NameCountPair totalCountPair = new NameCountPair("TOTAL", totalCount);
		
		List<NameCountPair> attrCountOnEmp = mapper.countJobAdOnAttrGroup("EMP");
		attrCountOnEmp.add(totalCountPair);
		
		return attrCountOnEmp;
	}
	*/
	@Override
	public List<JobAdAttrCounter> countJobAdsByEmpType() throws Exception {
		// TODO Auto-generated method stub
		// 두 개를 조합한다. 전체 건수 및 EMP Attr의 각 count
		Long totalCount = mapper.countJobAds(null);
		JobAdAttrCounter total = new JobAdAttrCounter();
		total.setGroupId("EMP");
		total.setCodeId("0");
		total.setCodeName("전체");
		total.setCodeDisplayOrder(0);
		total.setCnt(totalCount);
		
		//TODO 관심지역, 우리 동네 합해야 하는데... 여기서 해야 하나?
		/*
		if ( userId != null ) {
			
		} else {
			
			
			
		}
		*/
		
		
		List<JobAdAttrCounter> attrCountOnEmp = mapper.countJobAdOnAttrGroup("EMP");
		
		attrCountOnEmp.add(0, total);
		
		return attrCountOnEmp;
	}


	
}
