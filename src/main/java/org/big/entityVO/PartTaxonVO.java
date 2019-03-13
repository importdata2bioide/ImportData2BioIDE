package org.big.entityVO;

public class PartTaxonVO {
	private String id;
	private String scientificname;
	private String chname;
	private int rankId;
	private String epithet;
	private String authorstr;
	private String remark;
	
	




	/**
	 * 自定义的实体类属性只需要顺序和数据类型与查询结果对应即可，
	 * 名字无需一致，当然一般也把名字对应起来方便阅读。
	 * @param id
	 * @param scientificname
	 * @param chname
	 * @param rankId
	 */
	public PartTaxonVO(String id, String scientificname, String chname, int rankId,String epithet, String authorstr,String remark) {
		super();
		this.id = id;
		this.scientificname = scientificname;
		this.chname = chname;
		this.rankId = rankId;
		this.epithet = epithet;
		this.authorstr = authorstr;
		this.remark = remark;
	}
	
	
	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getAuthorstr() {
		return authorstr;
	}
	public void setAuthorstr(String authorstr) {
		this.authorstr = authorstr;
	}
	
	public String getEpithet() {
		return epithet;
	}




	public void setEpithet(String epithet) {
		this.epithet = epithet;
	}




	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScientificname() {
		return scientificname;
	}
	public void setScientificname(String scientificname) {
		this.scientificname = scientificname;
	}
	public String getChname() {
		return chname;
	}
	public void setChname(String chname) {
		this.chname = chname;
	}
	public int getRankId() {
		return rankId;
	}
	public void setRankId(int rankId) {
		this.rankId = rankId;
	}
	
	
	

}
