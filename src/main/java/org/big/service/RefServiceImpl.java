package org.big.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.apache.commons.lang.StringUtils;
import org.big.common.CommUtils;
import org.big.common.UUIDUtils;
import org.big.entity.Ref;
import org.big.entity.UserDetail;
import org.big.entityVO.LanguageEnum;
import org.big.entityVO.PtypeEnum;
import org.big.entityVO.RefTypeEnum;
import org.big.repository.RefRepository;
import org.big.sp2000.entity.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class RefServiceImpl implements RefService {
	@Autowired
	private RefRepository refRepository;

	@Autowired
	private ToolService toolService;

	@Override
	public Ref findOneById(String id) {
		return this.refRepository.findOneById(id);
	}

	@Override
	public void saveOne(Ref thisRef) {
		refRepository.save(thisRef);
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
	public Ref insertRefIfNotExist(String refstr, String inputerId, String remark) {
		Ref ref = refRepository.findByRefstrAndInputer(refstr, inputerId);
		if (ref == null || CommUtils.isStrEmpty(ref.getId())) {
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
			if (ref.getLanguages().equals("1")) {// 中文
				obj.setTitleC(ref.getTitle());
				obj.setAuthorC(ref.getAuthor());
				obj.setSourceC(ref.getRefstr());
			} else {
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

	@Override
	public String addRefJson(String oldRefstr, String newRefId, RefTypeEnum refType, String refS, String refE) {
		if (StringUtils.isEmpty(newRefId)) {
			throw new ValidationException("newRefId参数不能为空");
		}
		JSONArray jsonArray = null;
		if (StringUtils.isNotEmpty(oldRefstr)) {
			jsonArray = JSONArray.parseArray(oldRefstr);
		} else {
			jsonArray = new JSONArray();
		}
		boolean add = true;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			String existRefId = jsonObject.get("refId").toString(); // 得到 每个对象中的属性值
			if (existRefId.equals(newRefId)) {
				add = false;
			}
		}
		if (add) {
			JSONObject newObj = new JSONObject();
			newObj.put("refS", refS);
			newObj.put("refE", refE);
			newObj.put("refType", String.valueOf(refType.getIndex()));
			newObj.put("refId", newRefId);
			jsonArray.add(newObj);
		}
		if (jsonArray.size() == 0) {
			return null;
		}
		return jsonArray.toJSONString();
	}

	@Override
	public void parseLineChineseOrEng(Ref ref, String line) {
		int spos = toolService.getYearStart(line);
		if(spos== -1) {
			ref.setTitle(line);
			ref.setRefstr(line);
			ref.setRemark(line);
			return;
		}
		String author = null;
		author = line.substring(0, spos).trim();
		if (author.endsWith(",") || author.endsWith("，")) {
			author = author.substring(0, author.length() - 1);
		}
		String year = line.substring(spos, spos + 4).trim();
		String title = line.substring(spos + 4).trim();
		if (title.startsWith("-")) {
			year = line.substring(spos, spos + 9).trim();
			title = line.substring(spos + 10).trim();
		} else {
			title = line.substring(spos + 5).trim();
		}

		if (title.startsWith(".")) {
			title = line.substring(spos + 6).trim();
			year = line.substring(spos, spos + 6).trim();
		}
		year = year.replace(".", "");

		String language = null;
		if (isChineseChar(author)) {
			language = String.valueOf(LanguageEnum.chinese.getIndex());
			author = author.replace(".", "");
		} else {
			author = author.replace(".", "");
			language = String.valueOf(LanguageEnum.English.getIndex());
		}
		ref.setAuthor(author);
		ref.setPyear(year);
		ref.setTitle(title);
		ref.setRefstr(line);
		ref.setRemark(line);
		ref.setLanguages(language);

	}

	public boolean isChineseChar(String str) {
		boolean temp = false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			temp = true;
		}
		return temp;
	}

}
