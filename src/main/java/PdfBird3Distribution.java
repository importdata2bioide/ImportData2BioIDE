

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ValidationException;

import org.big.common.UUIDUtils;
import org.big.entity.Distributiondata;
import org.big.entity.Geoobject;
import org.big.entity.Taxon;
import org.big.repository.DistributiondataRepository;
import org.big.repository.GeoobjectRepository;
import org.big.repository.TaxonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class PdfBird3Distribution {
	
	@Autowired
	private TaxonRepository taxonRepository;
	@Autowired
	private DistributiondataRepository distributiondataRepository;
	@Autowired
	private GeoobjectRepository geoobjectRepository;
	Timestamp date = null;
	
	@RequestMapping(value = "/birdThreeDistributionInsert")
	@ResponseBody
	public String insertBird3Distribution() {
		List<Geoobject> list  = geoobjectRepository.findAll();
		String geoAll = toGeoStr(list);
		List<part> parts = new ArrayList<>();
		String[] lines = ReadPdf.getContent();
//		for(String line : lines) {
//			System.out.println(line);
//		}
		int i= 0;
		for(String line : lines) {
			i++;
			
			if(line.contains("见于各省")) {
				System.out.println("查找	"+i+"	"+line);
				boolean isTaxon = false;
				int index = i-1;
				String currentLine = "";
				int count = 0;
				List<Taxon> taxons = null;
				while(!isTaxon) {
					count++;
					if(count>20) {
						throw new ValidationException("找不到..."+line);
					}
					index = index-1;
					currentLine = lines[index];
					taxons = isTaxon(currentLine);
					if(taxons!=null && taxons.size()>0) {
						isTaxon = true;
						part e = new part();
						e.setTaxons(taxons);
						e.setDistribution(line);
						parts.add(e);
					}else {
						isTaxon = false;
					}
				}
				System.out.println("结果："+line+"    "+taxons.get(0).getScientificname());
				
			}
			
		}
		System.out.println(parts.size());
		List<Distributiondata> distributions = insertDistribution(parts,geoAll);
		for (Distributiondata d : distributions) {
			System.out.println("结果2："+d.getDiscontent()+"    "+d.getTaxon().getScientificname()+" "+d.getGeojson());
		}
		distributiondataRepository.saveAll(distributions);
		System.out.println("finish。。。");
		return "123";
		
	}
	private String toGeoStr(List<Geoobject> list) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"geoIds\": \"");
		for(int i = 0;i<list.size();i++) {
			if(i == list.size()-1) {
				sb.append(list.get(i).getId());
			}else {
				sb.append(list.get(i).getId()+"&");
			}
		}
		sb.append("\"}");
		return sb.toString();
	}
	public List<Distributiondata> insertDistribution(List<part> parts,String geoAll) {
		List<Distributiondata> distributiondatas = new ArrayList<>();
		System.out.println(parts.size());
		for (part part : parts) {
			String disContent =  part.getDistribution();
			String distribution = part.getDistribution();
			String geojson = "";
			if(distribution.trim().equals("见于各省")) {
				geojson = geoAll;
			}else {
				
				String distri = distribution.trim();
				String subGeo = distri.substring(distri.indexOf("除")+1, distri.indexOf("外，"));
				String[] Geos = subGeo.split("、");
				List<String> list2 = Arrays.asList(Geos);
				List<String> ids  = new ArrayList<>();
				for (String cngeoname : list2) {
					if("宁夏，新疆".equals(cngeoname)) {
						Geoobject w = geoobjectRepository.findOneByLikeCngeoname("%宁夏%");
						Geoobject e = geoobjectRepository.findOneByLikeCngeoname("%新疆%");
						ids.add(w.getId());
						ids.add(e.getId());
						
					}else if("宁夏，西藏，青海".equals(cngeoname)) {
						Geoobject w = geoobjectRepository.findOneByLikeCngeoname("%宁夏%");
						Geoobject e = geoobjectRepository.findOneByLikeCngeoname("%西藏%");
						Geoobject r = geoobjectRepository.findOneByLikeCngeoname("%青海%");
						ids.add(w.getId());
						ids.add(e.getId());
						ids.add(r.getId());
					}else {
						Geoobject geoobject = geoobjectRepository.findOneByLikeCngeoname("%"+cngeoname+"%");
						if(geoobject == null || !isStrNotEmpty(geoobject.getId())) {
							throw new ValidationException("找不到。。。"+cngeoname);
						}
						ids.add(geoobject.getId());
					}
					
				}
				//查询数据库
				List<Geoobject> list = geoobjectRepository.findByIdNotIn(ids);
				geojson = toGeoStr(list);
			}
			
			List<Taxon> taxons = part.getTaxons();
			if(taxons.size()<3) {
				for (Taxon t : taxons) {
					Distributiondata d = new Distributiondata();
					d.setGeojson(geojson);
					d.setId(UUIDUtils.getUUID32());
					d.setTaxonid(t.getId());
					d.setTaxon(t);
					d.setLng(0.0);
					d.setLat(0.0);
					d.setSourcesid("9e7a74f7-4845-4703-99dd-869993895614");
					d.setStatus(1);
					d.setInputer("95c24cdc24794909bd140664e2ee9c3b");
					d.setInputtime(getDate());
					d.setSynchstatus(0);
					d.setSynchdate(getDate());
					d.setGeojson(geojson);
					d.setDiscontent(disContent);
					distributiondatas.add(d);
				}
			}else {
				//
				Taxon t = part.getTaxons().get(0);
				Distributiondata d = new Distributiondata();
				d.setGeojson(geojson);
				d.setId(UUIDUtils.getUUID32());
				d.setTaxonid(t.getId());
				d.setTaxon(t);
				d.setLng(0.0);
				d.setLat(0.0);
				d.setSourcesid("9e7a74f7-4845-4703-99dd-869993895614");
				d.setStatus(1);
				d.setInputer("95c24cdc24794909bd140664e2ee9c3b");
				d.setInputtime(getDate());
				d.setSynchstatus(0);
				d.setSynchdate(getDate());
				d.setGeojson(geojson);
				d.setDiscontent(disContent);
				distributiondatas.add(d);
				
			}
			
			
		}
		return distributiondatas;
	}
	
	
	private  List<Taxon> isTaxon(String currentLine){
		if(!isStrNotEmpty(currentLine)) {
			return null;
		}
		if(currentLine.contains(",")) {
			return null;
		}
		if(currentLine.contains("。")) {
			return null;
		}
		Pattern p = Pattern.compile("[0-9]");
		Matcher m = p.matcher(currentLine);
		if (m.find()) {
			return null;
		}
		currentLine = currentLine.trim();
		currentLine = currentLine.replace("—", "%");
		currentLine = currentLine.replace(".", "%");
		currentLine = currentLine.replace("①", "");
		currentLine = currentLine.replace("②", "");
		currentLine = currentLine.replace("③", "");
		currentLine = currentLine.replace("④", "");
		if(!currentLine.contains("%")) {
			currentLine = "%"+currentLine+"%";
		}
		try {
			List<Taxon> taxons = taxonRepository.findLikeScientificname(currentLine);
			if(taxons == null || taxons.size() == 0) {
				return null;
			}else {
				return taxons;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
	}
	private boolean isStrNotEmpty(String str) {
		if (str != null && str.length() != 0 && !str.equals("") && !str.equals(" ")) {
			return true;
		}
		return false;
	}
	
	
	class part{
		
		private List<Taxon> taxons;
		private String distribution;
		
		public List<Taxon> getTaxons() {
			return taxons;
		}
		public void setTaxons(List<Taxon> taxons) {
			this.taxons = taxons;
		}
		public String getDistribution() {
			return distribution;
		}
		public void setDistribution(String distribution) {
			this.distribution = distribution;
		}
		
		
	}
	
	private Timestamp getDate() {
		date = new Timestamp(System.currentTimeMillis());
		String tsStr = "2018-10-27 22:00:00";
		try {
			date = Timestamp.valueOf(tsStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
}
