package zcc.es.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EsPage {
    private int currentPage;
    private int pageSize;
    private int recordCount;
    private List<Map<String, Object>> resultData;
    private int pageCount;
    private int beginPageIndex;
    private int endPageIndex;

    public List<Map<String, Object>> getResultData() {
        if (null == this.resultData) {
            this.resultData = new ArrayList();
        }

        return this.resultData;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
        this.pageCount = (recordCount + this.pageSize - 1) / this.pageSize;
        if (this.pageCount <= 10) {
            this.beginPageIndex = 1;
            this.endPageIndex = this.pageCount;
        } else {
            this.beginPageIndex = this.currentPage - 4;
            this.endPageIndex = this.currentPage + 5;
            if (this.beginPageIndex < 1) {
                this.beginPageIndex = 1;
                this.endPageIndex = 10;
            }

            if (this.endPageIndex > this.pageCount) {
                this.endPageIndex = this.pageCount;
                this.beginPageIndex = this.pageCount - 10 + 1;
            }
        }

    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getRecordCount() {
        return this.recordCount;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public int getBeginPageIndex() {
        return this.beginPageIndex;
    }

    public int getEndPageIndex() {
        return this.endPageIndex;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setResultData(List<Map<String, Object>> resultData) {
        this.resultData = resultData;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setBeginPageIndex(int beginPageIndex) {
        this.beginPageIndex = beginPageIndex;
    }

    public void setEndPageIndex(int endPageIndex) {
        this.endPageIndex = endPageIndex;
    }

    public EsPage() {
    }

    public String toString() {
        return "EsPage(currentPage=" + this.getCurrentPage() + ", pageSize=" + this.getPageSize() + ", recordCount=" + this.getRecordCount() + ", resultData=" + this.getResultData() + ", pageCount=" + this.getPageCount() + ", beginPageIndex=" + this.getBeginPageIndex() + ", endPageIndex=" + this.getEndPageIndex() + ")";
    }
}
