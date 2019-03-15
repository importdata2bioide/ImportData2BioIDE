package org.big.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.big.common.CommUtils;
import org.big.common.UUIDUtils;
import org.big.entity.Ref;
import org.big.entity.UserDetail;
import org.big.entityVO.LanguageEnum;
import org.big.entityVO.PtypeEnum;
import org.big.repository.RefRepository;
import org.big.sp2000.entity.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RefServiceImpl implements RefService {
	@Autowired
	private RefRepository refRepository;

	@Override
	public Ref findOneById(String id) {
		return this.refRepository.findOneById(id);
	}

	@Override
	public void saveOne(@Valid Ref thisRef) {
		thisRef.setId(UUID.randomUUID().toString());
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		thisRef.setInputer(thisUser.getId());
		thisRef.setSynchdate(new Timestamp(System.currentTimeMillis()));
		thisRef.setSynchstatus(0);
		thisRef.setStatus(1);

		this.refRepository.save(thisRef);
	}

	@Override
	public void removeOne(String Id) {
		this.refRepository.deleteOneById(Id);
	}

	@Override
	public boolean logicRemove(String id) {
		Ref thisRef = this.refRepository.findOneById(id);
		if (null != thisRef && 1 == thisRef.getStatus()) {
			thisRef.setStatus(0);
			this.refRepository.save(thisRef);
			return true;
		}
		return false;
	}

	@Override
	public void updateOneById(@Valid Ref thisRef) {
		thisRef.setSynchdate(new Timestamp(System.currentTimeMillis()));
		this.refRepository.save(thisRef);
	}

	

	@Override
	public Ref insertRefIfNotExist(String refstr, String inputerId,String remark) {
		Ref ref = refRepository.findByRefstrAndInputer(refstr,inputerId);
		if(ref == null || CommUtils.isStrEmpty(ref.getId())) {
			ref = new Ref();
			ref.setId(UUIDUtils.getUUID32());
			ref.setRefstr(refstr);
			ref.setPtype(String.valueOf(PtypeEnum.other.getIndex()));
			ref.setLanguages(String.valueOf(LanguageEnum.chinese.getIndex()));
			ref.setRemark(remark);
			
			ref.setInputer(inputerId);
			Timestamp timestamp = CommUtils.getTimestamp(CommUtils.getCurrentDate());
			ref.setInputtime(timestamp);
			ref.setSynchdate(timestamp);
			ref.setStatus(1);
			ref.setSynchstatus(0);
			refRepository.save(ref);
		}
		return ref;
	}

	@Override
	public List<Reference> findRefByUserTurnToReference(String userId) throws Exception {
		List<Ref> refslist = refRepository.findAllByUserId(userId);
		List<Reference> resultlist = new ArrayList<>();
		for (Ref ref : refslist) {
			Reference obj = new Reference();
			obj.setRecordId(ref.getId());
			if(ref.getLanguages().equals("1")) {//中文
				obj.setTitleC(ref.getTitle());
				obj.setAuthorC(ref.getAuthor());
				obj.setSourceC(ref.getRefstr());
			}else {
				obj.setTitle(ref.getTitle());
				obj.setAuthor(ref.getAuthor());
				obj.setSource(ref.getRefstr());
			}
			obj.setYear(ref.getPyear());
			obj.setDatabaseId(null);
			resultlist.add(obj);
		}
		return resultlist;
	}

	
}
