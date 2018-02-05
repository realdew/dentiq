package dentiq.api.repository.criteria;

import lombok.Getter;

public class PageCriteria {
	
	public static final int DEFAULT_SIZE = 10;
	
	@Getter
	private final int startIndexOnPage;
	@Getter
	private final int itemCountPerPage;
	@Getter
	private final int pageNo;
	
	
	public PageCriteria(int pageNo, int size) {
		this.itemCountPerPage = size;
		
		this.pageNo = pageNo;
		this.startIndexOnPage = (pageNo-1) * size;
	}
	
	public PageCriteria() {
		this(1);
	}
	
	public PageCriteria(int pageNo) {
		this(pageNo, DEFAULT_SIZE);
	}
	
	
	public String toString() {
		return "PageCriteria page:" + this.pageNo + ", count:" + this.itemCountPerPage 
				+ " ==> start:" + this.startIndexOnPage + ", size:" + this.itemCountPerPage;
	}
}