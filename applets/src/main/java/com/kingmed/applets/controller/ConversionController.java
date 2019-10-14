package com.kingmed.applets.controller;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.kingmed.applets.service.TurnService;
import com.kingmed.applets.tools.Log;

/**
 * 转换
 * @author BIN
 *
 */
@RestController
public class ConversionController {
	
	@Autowired
	private TurnService ts;
	
	/**
	 * 获取方案
	 * @return
	 *//*
	@RequestMapping("/getProgram")
	public List<Project> getProgram() {
	    //Document doc = useDomReadXml(PROGRAMPATH);
	    Document doc = useDomReadXml("");
	    //读取xml内部节点集合
	    NodeList nlst = doc.getElementsByTagName("title");
	    List<Project> pList = new ArrayList<Project>();
	    Project project = null; 
	    //遍历集合内容
	    for (int i = 0; i < nlst.getLength(); i++) {
	    	project = new Project(); 
	        String name = doc.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
	        String flag = doc.getElementsByTagName("flag").item(i).getFirstChild().getNodeValue();         
	        project.setFlag(flag);
	        project.setName(name);
	        pList.add(project);
	    }
		return pList;
	}*/
	/**
	 * 转换处理
	 * @param file
	 * @param response
	 * @param program 方案
	 * @throws IOException
	 */
	@RequestMapping("/conversion")
	@Log()
	public void conversion(MultipartFile file, HttpServletResponse response, String program) {
		// 转换
		ts.conversionDealWith(file, program, response);
	}
}
