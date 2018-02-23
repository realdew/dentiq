package dentiq.api.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;


/**
 * 문자열 요소들을 가진 List를 DB의 한 행에 저장하기 위한 MyBatis Custom Type Hander
 * 
 * 예:	"A", "B", "C" 3개의 문자열 요소들을 가진 경우에, 이를 한개의 DB 컬럼에 A,B,C 형태의 문자열로 저장된다.
 * 		만일, "A", "", "B", null, "C" 인 경우에는 DB에 저장될 값은 A,B,C 형태의 문자열이 된다. 
 * 
 * Java에서 사용되는 입출력 객체는 SingleColumnList이며,
 * 문자열로 변환된 값이 저장되기 위한 DB의 컬럼 형식은 VARCHAR이다. 
 * 
 * 이 클래스는 MyBatis의 TypeHandler가 관련되어 있으며, mybatis 설정 파일의 다음 정보가 필요하다.
 * 
 *  <typeHandlers>
 *		<typeHandler handler="dentiq.api.util.SingleColumnListTypeHandler"/>
 *	</typeHandlers>
 * 
 * 
 * 
 * @see		dentiq.api.util.SingleColumnList
 * @date	2018.02.11
 * @author	lee
 *
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(SingleColumnList.class)
public class SingleColumnListTypeHandler extends BaseTypeHandler<SingleColumnList> {
	/*
	// DB에서 읽어 올때
	@JsonIgnore
	private void setAttrVal(String val) throws Exception {
		if ( val==null || val.trim().length()<0 ) return;
		
		String[] tokens = val.trim().split(",");
		if ( tokens==null || tokens.length<1 ) return;
		
		this.attr = new ArrayList<String>();
		for ( int i=0; i<tokens.length; i++ ) {
			this.attr.add(tokens[i].trim());
		}
		
		//this.attr = Arrays.asList(val.split(","));
	}
	
	// DB에 쓸 때
	@JsonIgnore
	private String getAttrVal() {
		if ( this.attr==null || this.attr.size()==0 ) return null;
		
		//return String.join(",", this.attr);		
		return this.attr.stream().map(String::trim).collect(Collectors.joining(","));
	}
	 */

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, SingleColumnList parameter, JdbcType jdbcType)
			throws SQLException {
		
		String value = null;
		
		if ( parameter != null && parameter.size() > 0 ) {
			value = parameter.stream().map(String::trim).collect(Collectors.joining(","));
		}
		
		ps.setString(i, value);
	}
	
	private SingleColumnList convertStringToSingleColumnList(String str) {
		if ( str==null || str.trim().length()<0 ) return null;
		
		String[] tokens = str.trim().split(",");
		if ( tokens==null || tokens.length<1 ) return null;
		
		SingleColumnList retVal = new SingleColumnList();
		for ( int i=0; i<tokens.length; i++ ) {
			retVal.add(tokens[i].trim());
		}
		
		return retVal;
	}

	@Override
	public SingleColumnList getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String value = rs.getString(columnName);
		
		return convertStringToSingleColumnList(value);
	}

	@Override
	public SingleColumnList getNullableResult(ResultSet rs, int columnIndex) throws SQLException {		
		return convertStringToSingleColumnList(rs.getString(columnIndex));
	}

	@Override
	public SingleColumnList getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return convertStringToSingleColumnList(cs.getString(columnIndex));
	}


}
