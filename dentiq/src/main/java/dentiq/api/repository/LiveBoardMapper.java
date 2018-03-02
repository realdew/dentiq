package dentiq.api.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import dentiq.api.model.JobAdCountByLocation;
import dentiq.api.repository.criteria.JobAdSearchCriteria;

public interface LiveBoardMapper {
	
	//public List<JobAdCountByLocation> aggregateJobAdByLocation(@Param("locationCodeList") List<String> locationCodeList, @Param(""));
	
	// aggregateJobAds(@Param("searchCriteria") JobAdSearchCriteria searchCriteria) throws Exception;

}
