package org.big.entityVO;

import org.springframework.web.multipart.MultipartFile;

public class BaseParamsForm {
	
	private String mSeq;
	private String mTreeID;//旧系统treeID
	private String mLoginUser;
	private String mTaxasetId;
	private String mSourcesid;
	private String mTaxtreeId;
	private String mImagepath;
	private String mInputtimeStr;
	private int status = 1;
	private int synchstatus = 0;
	private int taxonCondition = 1;

	private String txtUrl;//txt文件路径
	private String code;//文件编码
	
	private boolean insert;//是否录入数据库,boolean类型的参数应当避免使用is开头进行命名，因为该类型的参数的set方法会自动命名为is+字段名，如果字段名中是is开头，会产生问题。
	
	private String filePath;//文件路径
	
	private MultipartFile multipartFile;//上传的文件
	
	private String remark;
	
	private String mActionUrl;
	
	private String mExpert; //审核人
	
	
	
	

	public String getmExpert() {
		return mExpert;
	}
	public void setmExpert(String mExpert) {
		this.mExpert = mExpert;
	}
	public String getmActionUrl() {
		return mActionUrl;
	}
	public void setmActionUrl(String mActionUrl) {
		this.mActionUrl = mActionUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}
	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}
	
	
	public boolean isInsert() {
		return insert;
	}
	public void setInsert(boolean insert) {
		this.insert = insert;
	}
	public String getTxtUrl() {
		return txtUrl;
	}
	public void setTxtUrl(String txtUrl) {
		this.txtUrl = txtUrl;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getSynchstatus() {
		return synchstatus;
	}
	public void setSynchstatus(int synchstatus) {
		this.synchstatus = synchstatus;
	}
	public int getTaxonCondition() {
		return taxonCondition;
	}
	public void setTaxonCondition(int taxonCondition) {
		this.taxonCondition = taxonCondition;
	}
	public String getmSeq() {
		return mSeq;
	}
	public void setmSeq(String mSeq) {
		this.mSeq = mSeq;
	}
	public String getmTreeID() {
		return mTreeID;
	}
	public void setmTreeID(String mTreeID) {
		this.mTreeID = mTreeID;
	}
	public String getmLoginUser() {
		return mLoginUser;
	}
	public void setmLoginUser(String mLoginUser) {
		this.mLoginUser = mLoginUser;
	}
	public String getmTaxasetId() {
		return mTaxasetId;
	}
	public void setmTaxasetId(String mTaxasetId) {
		this.mTaxasetId = mTaxasetId;
	}
	public String getmSourcesid() {
		return mSourcesid;
	}
	public void setmSourcesid(String mSourcesid) {
		this.mSourcesid = mSourcesid;
	}
	public String getmTaxtreeId() {
		return mTaxtreeId;
	}
	public void setmTaxtreeId(String mTaxtreeId) {
		this.mTaxtreeId = mTaxtreeId;
	}
	public String getmImagepath() {
		return mImagepath;
	}
	public void setmImagepath(String mImagepath) {
		this.mImagepath = mImagepath;
	}
	public String getmInputtimeStr() {
		return mInputtimeStr;
	}
	public void setmInputtimeStr(String mInputtimeStr) {
		this.mInputtimeStr = mInputtimeStr;
	}
	
	
	

}
