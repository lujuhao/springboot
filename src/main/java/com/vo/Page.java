package com.vo;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 自定义分页实体类
 * @author ljh
 */
public class Page<T> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
    
    private int pageNum = 1;//当前页
    private int pageSize = 10;//每页显示的条数
    private int size;//总共几页
    private long total;//总条数
    private List<T> list;//分页数据集合
    
    private String orderField;//排序字段
    private String orderType;//排序方式
    private String orderBy;//SQL排序 ：排序字段+排序方式
    
    private int startRow;
    private int endRow;
    private int pages;
    

    private int firstPage;
    private int prePage;
    private int nextPage;
    private int lastPage;

    private boolean isFirstPage = false;
    private boolean isLastPage = false;
    private boolean hasPreviousPage = false;
    private boolean hasNextPage = false;
    private int navigatePages;
    private int[] navigatepageNums;

    public Page() {
    	
    }

    /**
     * 初始化构造函数，从request中获取需要的分页参数
     * @param request
     */
    public Page(HttpServletRequest request) {
    	String pageNo = request.getParameter("pageNumber");
    	String pageSize = request.getParameter("pageSize");
    	
    	if (StringUtils.isNumeric(pageNo)) {
    		this.pageNum = Integer.parseInt(pageNo);
		}
    	if (StringUtils.isNumeric(pageSize)) {
    		this.pageSize = Integer.parseInt(pageSize);
		}
        
    	//this.pageNum = (this.pageSize+this.pageNum)/this.pageSize;
    	this.orderField = request.getParameter("orderField");
    	this.orderType = request.getParameter("orderType");
    	if (!StringUtils.isEmpty(orderField) && !StringUtils.isEmpty(orderType)) {
			this.orderBy = orderField + " " + orderType;
		}
    }


    private void calcNavigatepageNums() {
        if (pages <= navigatePages) {
            navigatepageNums = new int[pages];
            for (int i = 0; i < pages; i++) {
                navigatepageNums[i] = i + 1;
            }
        } else { 
            navigatepageNums = new int[navigatePages];
            int startNum = pageNum - navigatePages / 2;
            int endNum = pageNum + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            } else if (endNum > pages) {
                endNum = pages;
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatepageNums[i] = endNum--;
                }
            } else {
                for (int i = 0; i < navigatePages; i++) {
                    navigatepageNums[i] = startNum++;
                }
            }
        }
    }

    private void calcPage() {
        if (navigatepageNums != null && navigatepageNums.length > 0) {
            firstPage = navigatepageNums[0];
            lastPage = navigatepageNums[navigatepageNums.length - 1];
            if (pageNum > 1) {
                prePage = pageNum - 1;
            }
            if (pageNum < pages) {
                nextPage = pageNum + 1;
            }
        }
    }

    private void judgePageBoudary() {
        isFirstPage = pageNum == 1;
        isLastPage = pageNum == pages;
        hasPreviousPage = pageNum > 1;
        hasNextPage = pageNum < pages;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void setList(List<T> list) {
		if (list instanceof com.github.pagehelper.Page) {
			com.github.pagehelper.Page page = (com.github.pagehelper.Page) list;
			this.pageNum = page.getPageNum();
			this.pageSize = page.getPageSize();

			this.total = page.getTotal();
			this.pages = page.getPages();
			this.list = page;
			this.size = page.size();
			if (this.size == 0) {
				this.startRow = 0;
				this.endRow = 0;
			} else {
				this.startRow = page.getStartRow() + 1;
				this.endRow = this.startRow - 1 + this.size;
			}
			calcNavigatepageNums();
			calcPage();
			judgePageBoudary();
		}
		this.list = list;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int[] getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(int[] navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public String getOrderField() {
		return orderField;
	}

	public void setOrderField(String orderField) {
		this.orderField = orderField;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isFirstPage() {
		return isFirstPage;
	}

	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public boolean isLastPage() {
		return isLastPage;
	}

	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}

	@Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageInfo{");
        sb.append("pageNum=").append(pageNum);
        sb.append(", pageSize=").append(pageSize);
        sb.append(", size=").append(size);
        sb.append(", startRow=").append(startRow);
        sb.append(", endRow=").append(endRow);
        sb.append(", total=").append(total);
        sb.append(", pages=").append(pages);
        sb.append(", list=").append(list);
        sb.append(", firstPage=").append(firstPage);
        sb.append(", prePage=").append(prePage);
        sb.append(", nextPage=").append(nextPage);
        sb.append(", lastPage=").append(lastPage);
        sb.append(", isFirstPage=").append(isFirstPage);
        sb.append(", isLastPage=").append(isLastPage);
        sb.append(", hasPreviousPage=").append(hasPreviousPage);
        sb.append(", hasNextPage=").append(hasNextPage);
        sb.append(", navigatePages=").append(navigatePages);
        sb.append(", navigatepageNums=");
        if (navigatepageNums == null) sb.append("null");
        else {
            sb.append('[');
            for (int i = 0; i < navigatepageNums.length; ++i)
                sb.append(i == 0 ? "" : ", ").append(navigatepageNums[i]);
            sb.append(']');
        }
        sb.append('}');
        return sb.toString();
    }
	
}
