package dentiq.api.util;

import java.util.ArrayList;

/**
 * 문자열 요소들을 가진 List를 DB의 한 행에 저장하기 위한 클래스
 * 
 * 예:	"A", "B", "C" 3개의 문자열 요소들을 가진 경우에, 이를 한개의 DB 컬럼에 A,B,C 형태의 문자열로 저장된다.
 * 		만일, "A", "", "B", null, "C" 인 경우에는 DB에 저장될 값은 A,B,C 형태의 문자열이 된다.
 * 
 * 이 클래스는 MyBatis의 TypeHandler가 관련되어 있으며, mybatis 설정 파일의 다음 정보가 필요하다.
 * 
 *  <typeHandlers>
 *		<typeHandler handler="dentiq.api.util.SingleColumnListTypeHandler"/>
 *	</typeHandlers>
 * 
 * 
 * 
 * @see		dentiq.api.util.SingleColumnListTypeHander
 * @date	2018.02.11
 * @author	lee
 *
 */
public class SingleColumnList extends ArrayList<String> {

	private static final long serialVersionUID = -1004083277154245469L;
	
	/**
	 * 추가되는 값이 null이거나, ''(빈 값)이면 추가하지 않는다.
	 */
	@Override
	public boolean add(String e) {
		if ( e!=null && !e.trim().equals("") ) return super.add(e);
		return true;
	}
	
	/**
	 * 추가되는 값이 null이거나, ''(빈 값)이면 추가하지 않는다.
	 */
	@Override
	public void add(int index, String element) {
		if ( element!=null && !element.trim().equals("") ) super.add(index,  element);
	}
	
	
}
