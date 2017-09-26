package com.elong.mt.llz.data.crawler.pojo.entity;

public class Page {
	private Integer page = 1; //显示第几页数据，默认第一页
	private Integer pageSize = 10;//一页默认显示5条
	private Integer totalRow = 0; //总记录数
	private Integer totalPage = 0; //最大页数

	public Page() {
		super();
	}

	public Page(Integer page, Integer pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}

    public Integer getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

    //getter属性名begin
    public Integer getBegin(){
        return (page-1)*pageSize;
    }
    //setter属性名begin
    public Integer getPage() {
        return page;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public static int getTotalPage(int totalRows, int pageSize) {
        if ((totalRows % pageSize) == 0) {
            return totalRows / pageSize;
        }
        return (totalRows / pageSize) +1;
    }
}