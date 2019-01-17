package org.big.service;

import java.sql.Timestamp;


import org.big.common.UUIDUtils;
import org.big.entity.Commonname;
import org.big.repository.CommonnameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @Description 俗名
 * @author ZXY
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
       commonnameRepository.save(thisCommoname);
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
