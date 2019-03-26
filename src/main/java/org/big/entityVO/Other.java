package org.big.entityVO;
import java.util.List;

public class Other {
	private String ref;//参考文献
	private String commname;//俗名
	private String distribution;//分布
	private String protectLevel;//保护等级
	private List<String> citation;
	
	
	public List<String> getCitation() {
		return citation;
	}
	public void setCitation(List<String> citation) {
		this.citation = citation;
	}
	public String getProtectLevel() {
		return protectLevel;
	}
	public void setProtectLevel(String protectLevel) {
		this.protectLevel = protectLevel;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getCommname() {
		return commname;
	}
	public void setCommname(String commname) {
		this.commname = commname;
	}
	public String getDistribution() {
		return distribution;
	}
	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}
	
	

}
