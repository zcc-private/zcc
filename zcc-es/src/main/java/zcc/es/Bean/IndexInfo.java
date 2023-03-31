package zcc.es.Bean;

import java.io.Serializable;

public class IndexInfo implements Serializable {
    private String alias;
    private int order;
    private int version;
    private int numberOfshards;
    private int numberOfReplicas;
    private String indexName;
    private Class clazz;

    public String getAlias() {
        return this.alias;
    }

    public int getOrder() {
        return this.order;
    }

    public int getVersion() {
        return this.version;
    }

    public int getNumberOfshards() {
        return this.numberOfshards;
    }

    public int getNumberOfReplicas() {
        return this.numberOfReplicas;
    }

    public String getIndexName() {
        return this.indexName;
    }

    public Class getClazz() {
        return this.clazz;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setNumberOfshards(int numberOfshards) {
        this.numberOfshards = numberOfshards;
    }

    public void setNumberOfReplicas(int numberOfReplicas) {
        this.numberOfReplicas = numberOfReplicas;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public IndexInfo() {
    }

    public String toString() {
        return "IndexInfo(alias=" + this.getAlias() + ", order=" + this.getOrder() + ", version=" + this.getVersion() + ", numberOfshards=" + this.getNumberOfshards() + ", numberOfReplicas=" + this.getNumberOfReplicas() + ", indexName=" + this.getIndexName() + ", clazz=" + this.getClazz() + ")";
    }
}
