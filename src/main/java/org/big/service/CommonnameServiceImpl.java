package org.big.service;

import java.sql.Timestamp;

import org.big.common.UUIDUtils;
import org.big.entity.Commonname;
import org.big.repository.CommonnameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *<p><b>Commonname的Service类</b></p>
 *<p> Commonname的Service类，与Commonname有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class CommonnameServiceImpl implements CommonnameService {

    @Autowired
    private CommonnameRepository commonnameRepository;

  
    @Override
    public Commonname findbyID(String ID) {
        return this.commonnameRepository.getOne(ID);
    }

    @Override
    public void saveOne(Commonname thisCommoname) {
        if(thisCommoname.getId()==null||thisCommoname.getId().equals("")||thisCommoname.getId().length()<=0){
            thisCommoname.setId(UUIDUtils.getUUID32());
            thisCommoname.setInputtime(new Timestamp(System.currentTimeMillis()));
            thisCommoname.setSynchdate(null);
        }
        this.commonnameRepository.save(thisCommoname);
    }

    @Override
    public void removeOne(String ID) {
        this.commonnameRepository.deleteById(ID);
    }

	@Override
	public void save(Commonname entity) {
		commonnameRepository.save(entity);
		
	}



}
