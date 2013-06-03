package com.keijack.database.hibernate;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Keijack
 * 
 * @param <M>
 */
public class QueryPageResult<M> implements Serializable {
    /**
     * 可序列ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 总数
     */
    private long total;
    /**
     * 总页数
     */
    private long totalPages;
    /**
     * 当前页
     */
    private long currentPage;

    /**
     * 第一条数据在数据库是第几行, 从第0行开始
     */
    private long fisrtDataRowNumInDatabase;
    /**
     * 查询的列表结果
     */
    private List<M> datas;

    public QueryPageResult() {
	super();
    }

    public long getTotal() {
	return total;
    }

    public void setTotal(long total) {
	this.total = total;
    }

    public long getTotalPages() {
	return totalPages;
    }

    public void setTotalPages(long totalPages) {
	this.totalPages = totalPages;
    }

    public long getCurrentPage() {
	return currentPage;
    }

    public void setCurrentPage(long currentPage) {
	this.currentPage = currentPage;
    }

    public List<M> getDatas() {
	return datas;
    }

    public void setDatas(List<M> datas) {
	this.datas = datas;
    }

    public long getFisrtDataRowNumInDatabase() {
	return fisrtDataRowNumInDatabase;
    }

    public void setFisrtDataRowNumInDatabase(long fisrtDataRowNumInDatabase) {
	this.fisrtDataRowNumInDatabase = fisrtDataRowNumInDatabase;
    }

}
