package org.big.entityVO;

import org.big.entity.Ref;

/**
 * <p><b>类说明摘要</b></p>
 *
 * @Description 类说明详情</ p>
 * @ClassName UploadedRef
 * @Author WangTianshan (王天山)
 * @Date 18-12-2 12:42</p>
 * @Version: 0.1
 * <p>Copyright: WangTianshan - 王天山</p>
 * @Since JDK 1.80_171
 */
public class UploadedRef extends Ref {
  
	private static final long serialVersionUID = 1L;
	private String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

	public UploadedRef() {
	}
    
}
