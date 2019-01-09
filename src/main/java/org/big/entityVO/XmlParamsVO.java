package org.big.entityVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class XmlParamsVO {

	private String treeID;
	private String loginUser;
	private String taxasetId;
	private String sourcesid;
	private String taxtreeId;
	private String imagePath;
	private String inputtimeStr;
	private String name;
	private String seq;

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTreeID() {
		return treeID;
	}

	public void setTreeID(String treeID) {
		this.treeID = treeID;
	}

	public String getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(String loginUser) {
		this.loginUser = loginUser;
	}

	public String getTaxasetId() {
		return taxasetId;
	}

	public void setTaxasetId(String taxasetId) {
		this.taxasetId = taxasetId;
	}

	public String getSourcesid() {
		return sourcesid;
	}

	public void setSourcesid(String sourcesid) {
		this.sourcesid = sourcesid;
	}

	public String getTaxtreeId() {
		return taxtreeId;
	}

	public void setTaxtreeId(String taxtreeId) {
		this.taxtreeId = taxtreeId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getInputtimeStr() {
		return inputtimeStr;
	}

	public void setInputtimeStr(String inputtimeStr) {
		this.inputtimeStr = inputtimeStr;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this,
				new SerializerFeature[] { SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty,
						SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero,
						SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.UseISO8601DateFormat });

	}

}
