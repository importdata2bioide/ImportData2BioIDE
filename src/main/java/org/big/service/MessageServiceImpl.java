package org.big.service;

import org.big.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *<p><b>Message的Service类</b></p>
 *<p> Message的Service类，与Message有关的业务逻辑方法</p>
 * @author WangTianshan (王天山)
 *<p>Created date: 2017/9/6 21:35</p>
 *<p>Copyright: The Research Group of Biodiversity Informatics (BiodInfo Group) - 中国科学院动物研究所生物多样性信息学研究组</p>
 * @version: 0.1
 * @since JDK 1.80_144
 */
@Service
public class MessageServiceImpl implements MessageService{

   
    @Autowired
    private MessageRepository messageRepository;

   
    
    @Override
    public void removeOne(String ID) {
        this.messageRepository.deleteById(ID);
    }


}
