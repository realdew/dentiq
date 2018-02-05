package dentiq.api.controller;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dentiq.api.service.exception.LogicalException;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
public class PageableList<T> {
	
	@Getter
	private PageInfo _PAGE_INFO_;

	@Getter
	private List<T> _CONTENT_LIST_;
	
//	public void setContentList(List<T> contentList) {
//		this.contentList = contentList;
//		if ( contentList!=null ) {
//			int currentItemCnt = contentList.size();
//		}
//		// 사이즈 계산
//	}
	
	public PageableList(int itemCntPerPage) {
		this._PAGE_INFO_ = new PageInfo(itemCntPerPage);
	}
	
//	public PageableList(PageInfo pageInfo, List<T> contentList) {
//		this.pageInfo = pageInfo;
//		if ( contentList != null ) {
//			this.contentList = contentList;
//			this.pageInfo.setCurrentItemCnt(contentList.size());
//		}
//	}	
	public PageableList(int pageNo, int totalItemCnt, List<T> contentList) throws LogicalException {
		this(pageNo, totalItemCnt, PageInfo.DEFAULT_ITEM_CNT_PER_PAGE, contentList);
	}
	public PageableList(int pageNo, int totalItemCnt, int itemCntPerPage, List<T> contentList) throws LogicalException {
		this._PAGE_INFO_ = new PageInfo(pageNo, totalItemCnt, itemCntPerPage);
		if ( contentList != null ) {
			this._CONTENT_LIST_ = contentList;
			this._PAGE_INFO_.setCurrentItemCnt(contentList.size());
		}
	}
	
	
	
}

class PageInfo {
	
	public static final int DEFAULT_ITEM_CNT_PER_PAGE = 10;
	
	@Getter
	private final int itemCntPerPage;	// 페이지 당 아이템 개수
	
	@Getter
	private final int pageNo;			// 현재 페이지 번호
	
	@Getter
	private final int totalPageCnt;	// 전체 페이지 개수
	
	@Getter
	private final int totalItemCnt;	// 전체 아이템 개수
	
	@Getter @Setter
	private int currentItemCnt;	// 현재 아이템 개수 <== List<T>의 개수
	
	
	PageInfo() {
		this(DEFAULT_ITEM_CNT_PER_PAGE);
	}
	
	PageInfo(int itemCntPerPage) {
		this.itemCntPerPage = itemCntPerPage;
		this.pageNo = 1;
		this.totalPageCnt = 1;
		this.totalItemCnt = 0;
		this.currentItemCnt = 0;
	}
	
	PageInfo(int pageNo, int totalItemCnt) throws LogicalException {
		this(pageNo, totalItemCnt, DEFAULT_ITEM_CNT_PER_PAGE);
	}
	
	PageInfo(int pageNo, int totalItemCnt, int itemCntPerPage ) throws LogicalException {
		this.pageNo = pageNo;
		this.totalItemCnt = totalItemCnt;
		this.itemCntPerPage = itemCntPerPage;
		
		this.totalPageCnt = this.totalItemCnt / this.itemCntPerPage + 1;
		
		if ( this.pageNo > this.totalPageCnt )
			throw new LogicalException("", "페이지 번호 초과, pageNo[" + this.pageNo + "] totalPage[" + this.totalPageCnt + "]");
	}
	
	
	
	
//	
//	public PageInfo(Integer pageNo, Integer itemCntPerPage, Integer totalPageCnt, Integer totalItemCnt, Integer currentItemCnt) {
//		this.pageNo			= pageNo;
//		this.itemCntPerPage	= itemCntPerPage;
//		this.totalPageCnt	= totalPageCnt;
//		this.totalItemCnt	= totalItemCnt;
//		this.currentItemCnt	= currentItemCnt;
//	}
}
