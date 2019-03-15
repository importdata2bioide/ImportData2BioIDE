package org.big.controller;

import java.util.List;

import org.big.entityVO.ResultMsg;
import org.big.entityVO.ResultStatusCode;
import org.big.service.DistributiondataService;
import org.big.service.RefService;
import org.big.service.TaxonService;
import org.big.sp2000.entity.Distribution;
import org.big.sp2000.entity.Family;
import org.big.sp2000.entity.Reference;
import org.big.sp2000.entity.ScientificName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiController {
	@Autowired
	private TaxonService taxonService;
	@Autowired
	private RefService refService;
	
	@Autowired
	private DistributiondataService distributiondataService;
	/**
	 * 
	 * @Description 参考文献
	 * @param userId
	 * @return
	 * @author ZXY
	 */
	@GetMapping("/getRefsByUserId/{userId}")
	public Object getRefsByUserId(@PathVariable String userId) {
		ResultMsg resultMsg = null;
		try {
			List<Reference> list = refService.findRefByUserTurnToReference(userId);
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
		}
		return resultMsg;
	}
	
	
	/**
	 * 
	 * @Description 高阶元
	 * @param taxasetId
	 * @param taxtreeId
	 * @return
	 * @author ZXY
	 */
	@GetMapping("/getFamilyDataByTaxaset/{taxasetId}/{taxtreeId}")
	public Object getFamilyDataByTaxaset(@PathVariable String taxasetId,@PathVariable String taxtreeId) {
		ResultMsg resultMsg = null;
		try {
			List<Family> list = taxonService.getFamilyDataByTaxaset(taxasetId,taxtreeId);
			System.out.println("发送数据条数："+list.size());
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
		}
		return resultMsg;
	}

	
	/**
	 * 
	 * @Description 低阶元
	 * @param taxasetId
	 * @param taxtreeId
	 * @return
	 * @author ZXY
	 */
	@GetMapping("/getScientificNamesByTaxaset/{taxasetId}/{taxtreeId}")
	public Object getScientificNamesByTaxaset(@PathVariable String taxasetId,@PathVariable String taxtreeId) {
		ResultMsg resultMsg = null;
		try {
			List<ScientificName> list = taxonService.getScientificNamesByTaxaset(taxasetId,taxtreeId);
			System.out.println("发送数据条数："+list.size());
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
		}
		return resultMsg;
	}
	
	/**
	 * 
	 * @Description 分布
	 * @param taxasetId
	 * @return
	 * @author ZXY
	 */
	@GetMapping("/getDistributionByTaxaset/{taxasetId}")
	public Object getDistributionByTaxaset(@PathVariable String taxasetId) {
		ResultMsg resultMsg = null;
		try {
			List<Distribution> list = distributiondataService.getDistributionByTaxaset(taxasetId);
			System.out.println("发送数据条数："+list.size());
			resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
		} catch (Exception e) {
			e.printStackTrace();
			resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getErrcode(), ResultStatusCode.SYSTEM_ERR.getErrmsg()+","+e.getMessage(), null);
		}
		return resultMsg;
	}
	
//	@GetMapping("/getFamilyByTaxaset/{taxasetId}")
//	public Object getFamilyByTaxaset(@PathVariable String taxasetId) {
//		List<PartTaxonVO> list = taxonService.findFamilyByTaxaset(taxasetId);
//		ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
//		return resultMsg;
//	}
//	
//	@GetMapping("/getHigherThanFamilyByTaxaset/{taxasetId}")
//	public Object getHigherThanFamilyByTaxaset(@PathVariable String taxasetId) {
//		List<PartTaxonVO> list = taxonService.findHigherThanFamilyByTaxaset(taxasetId);
//		ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
//		return resultMsg;
//	}
//	
//	@GetMapping("/getRelation/{taxtreeId}")
//	public Object getRelation(@PathVariable String taxtreeId) {
//		List<TaxonHasTaxtree> list = taxtreeService.findByTaxtreeId(taxtreeId);
//		ResultMsg resultMsg = new ResultMsg(ResultStatusCode.OK.getErrcode(), ResultStatusCode.OK.getErrmsg(), list);
//		return resultMsg;
//	}

}
