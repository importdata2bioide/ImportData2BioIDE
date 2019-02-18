package org.big.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.big.common.EntityInit;
import org.big.common.UUIDUtils;
import org.big.entity.Dataset;
import org.big.entity.License;
import org.big.entity.Multimedia;
import org.big.entity.Taxaset;
import org.big.entity.Taxon;
import org.big.entity.UserDetail;
import org.big.entityVO.BaseParamsForm;
import org.big.repository.DatasetRepository;
import org.big.repository.MultimediaRepository;
import org.big.repository.TaxasetRepository;
import org.big.repository.TaxonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class MultimediaServiceImpl implements MultimediaService {
	
	private final static Logger logger = LoggerFactory.getLogger(MultimediaServiceImpl.class);
	@Autowired
	private MultimediaRepository multimediaRepository;
	@Autowired
	private TaxasetRepository taxasetRepository;
	@Autowired
	private DatasetRepository datasetRepository;
	@Autowired
	private TaxonRepository taxonRepository;
	


	@Override
	public boolean deleteOne(HttpServletRequest request) {
		String multimediaId = request.getParameter("multimediaId");
		if (StringUtils.isNotBlank(multimediaId)) {
			if (null != this.multimediaRepository.findOneById(multimediaId)) {
				this.multimediaRepository.deleteOneById(multimediaId);
			}
			return true;
		}
		return false;
	}

	@Override
	public void saveMultimedia(String taxonId, Multimedia thisMultimedia) {
		Taxon thisTaxon = this.taxonRepository.findOneById(taxonId);
		UserDetail thisUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		thisMultimedia.setInputer(thisUser.getId());
		thisMultimedia.setInputtime(new Timestamp(System.currentTimeMillis()));
		thisMultimedia.setSynchdate(new Timestamp(System.currentTimeMillis()));
		thisMultimedia.setTaxon(thisTaxon);
		thisMultimedia.setStatus(1);
		thisMultimedia.setSynchstatus(0);
		
		this.multimediaRepository.save(thisMultimedia);
	}

	@Override
	public void saveMultimedia(Taxon taxon, BaseParamsForm baseParamsForm, String imagePath) throws Exception {
		String taxasetId = baseParamsForm.getmTaxasetId();//分类单元集
		if (org.apache.commons.lang3.StringUtils.isEmpty(taxasetId)) {
			throw new ValidationException("taxasetId值为空！请初始化，无法继续...");
		}
		Taxaset taxaset = taxasetRepository.findOneById(taxasetId);
		String datasetId = taxaset.getDataset().getId();
		if (org.apache.commons.lang3.StringUtils.isEmpty(datasetId)) {
			throw new ValidationException("Taxaset 数据不规范，datasetId值为空！无法继续...");
		}
		Dataset dataset = datasetRepository.findOneById(datasetId);
		String teamId = dataset.getTeam().getId();
		if (org.apache.commons.lang3.StringUtils.isEmpty(teamId)) {
			throw new ValidationException("Dataset 数据不规范，teamId值为空！无法继续...");
		}
		String[] split = imagePath.split("\\\\");
		String imageNameAndSuffix = split[split.length-1];//原始文件名+后缀名 如 image2.png
		String onlyName = imageNameAndSuffix.split("\\.")[0];//文件名 如image2
		//保存至本地
		String suffix = imagePath.split("\\.")[1]; //后缀名，如 png
		String newFileName = UUIDUtils.getUUID32() + "." + suffix; // 新文件名
		String absolutePath= "E:\\003采集系统\\0000可放入服务器的图片\\upload\\images\\";
		String relativePath = teamId + "/" + datasetId + "/" + taxon.getId() + "/";
		
		File file = new File(imagePath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			MultipartFile multipartFile = new MockMultipartFile(imageNameAndSuffix, fis);
			// 先把文件保存到本地
			FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(absolutePath+relativePath, newFileName));
		} catch (Exception e1) {
			logger.error("文件保存到本地发生异常:" + e1.getMessage() + ",跳过继续执行");
			
		}finally {
			if(fis != null)
				fis.close();
		}
		//保存到数据库
		Multimedia multimedia = new Multimedia();
		EntityInit.initMultimedia(multimedia, baseParamsForm);
		multimedia.setTaxon(taxon);
		multimedia.setTitle(onlyName);
		multimedia.setRightsholder("未知");
		multimedia.setLisenceid("4");
		License license = new License();
		license.setId(multimedia.getLisenceid());
		multimedia.setLicense(license);
		multimedia.setMediatype("4");
		multimedia.setPath(relativePath+newFileName);
		
		multimedia.setSuffix(suffix);
		multimedia.setMediatype("1");
		multimedia.setCopyright("无");
		multimediaRepository.save(multimedia);
		
	}

	
	

}
