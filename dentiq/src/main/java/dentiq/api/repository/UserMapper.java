package dentiq.api.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import dentiq.api.model.LocationCode;
import dentiq.api.model.User;
import dentiq.api.model.juso.AddrJuso;

@Mapper
@Repository
public interface UserMapper {

	public User getBasicInfoByUserId(@Param("userId") Integer userId) throws Exception;
	public int updateBasicInfo(User user) throws Exception;
	
	public User loginByEmailAndPassword(@Param("email") String email, @Param("password") String password) throws Exception;
	
	public User getUserById(@Param("id") Integer id) throws Exception;

	public User getUserByEmail(@Param("email") String email) throws Exception;
	
//	public int createUser(User user) throws Exception;
//	
//	public int createBizUser(BizUser user) throws Exception;
	
	public int createCommonUser(User user) throws Exception;
	
	public List<User> getUsers() throws Exception;
	
	public int countUsersByEmail(@Param("email") String email) throws Exception;
	
	public int countUsersByBizRegNo(@Param("bizRegNo") String bizRegNo) throws Exception;
	
	/* 회원 주소 수정 */
//	public int updateUserAddr(@Param("addrRoad") String addrRaod, @Param("addrJibun") String addrJibun,
//									@Param("addrDetail") String addrDetail, @Param("zipNo") String zipNo) throws Exception;
	
	/* 회원 주소 수정 */
	public int updateUserAddr2(@Param("userId") Integer userId, @Param("juso") AddrJuso juso, @Param("entX") String entX, @Param("entY") String entY) throws Exception;
	
	public int updateUserAddr(@Param("userId") Integer userId, @Param("juso") AddrJuso juso, @Param("locationCode") LocationCode locationCode) throws Exception;
	
	public AddrJuso getUserAddr(@Param("userId") Integer userId) throws Exception;
	
	
	
}
