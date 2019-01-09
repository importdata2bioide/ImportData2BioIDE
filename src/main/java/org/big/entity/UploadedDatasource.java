package org.big.entity;

/**
 * <p><b>类说明摘要</b></p>
 *
 * @Description 类说明详情</ p>
 * @ClassName UploadedRef
 * @Author BINZI
 * @Date 18-12-10 11:23</p>
 * @Version: 0.1
 * <p>Copyright: BINZI</p>
 * @Since JDK 1.80_171
 */
public class UploadedDatasource extends Datasource {
  
	private static final long serialVersionUID = 1L;
	private String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

	public UploadedDatasource() {
	}

}
