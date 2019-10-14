package com.kingmed.applets.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.kingmed.applets.bean.Project;

@Service
public class TurnService {
	

	@Autowired
	private AlgorithmService as;
	@Autowired
	private AnalysisService ans;
	
	private final static String PROGRAMPATH = "D:\\kingMed\\program.xml";
	private final static String RULEPATH = "config/rule.xml";
	// 缓存一下
	// 封装方案所用的列
    private List<Project> onePList = new ArrayList<Project>();
    private List<Project> twoPList = new ArrayList<Project>();
    // 存定量值的flag
    private String quantitativeFlag = "";
	
	/**
	 * 转换处理 
	 * @param file
	 * @param program 方案 one为过敏原 two为免疫
	 * @return
	 */
	public void conversionDealWith(MultipartFile file, String program, HttpServletResponse response) {
		// 输出到本地
		OutputStreamWriter oswLocal = null;
		FileOutputStream fos = null;
		// 输出到页面
		OutputStreamWriter oswPage = null;
		OutputStream out = null;
		
        CSVPrinter csvPrinterLocal = null;
        CSVPrinter csvPrinterPage = null;
		try {
			Document doc = ans.useDomReadXml(PROGRAMPATH);
			// 服务端保存一份
		    String path = "";
		    Project project = null; 
		    List<Project> pList = null;
		    
		    if ("one".equals(program)) {
		    	path = "\\过敏原";
		    	if (onePList != null && onePList.size() > 0) {
			    	pList = onePList;
			    } else {
			    	// 遍历集合内容
			        NodeList nameList = doc.getElementsByTagName(program + "-name");
			        NodeList useList = doc.getElementsByTagName(program + "-use");    
			        // 封装
			        for (int i = 0; i < nameList.getLength(); i++) {
			        	project = new Project(); 
			        	String name = nameList.item(i).getFirstChild().getNodeValue();
			        	String flag = useList.item(i).getFirstChild().getNodeValue();
			        	project.setName(name);
			        	project.setFlag(flag);
			        	onePList.add(project);
			        }
			        pList = onePList;
			    }
		    } else if ("two".equals(program)) {
		    	path = "\\乙肝表面抗体";
		    	if (twoPList != null && twoPList.size() > 0) {
			    	pList = twoPList;
			    } else {
			    	// 遍历集合内容
			        NodeList nameList = doc.getElementsByTagName(program + "-name");
			        NodeList useList = doc.getElementsByTagName(program + "-use");    
			        // 封装
			        for (int i = 0; i < nameList.getLength(); i++) {
			        	project = new Project(); 
			        	String name = nameList.item(i).getFirstChild().getNodeValue();
			        	String flag = useList.item(i).getFirstChild().getNodeValue();
			        	if ("定量值".equals(name)) {
			        		quantitativeFlag = flag;
			        	}
			        	project.setName(name);
			        	project.setFlag(flag);
			        	twoPList.add(project);
			        }
			        pList = twoPList;
			    }
		    }
		    
			// 获得文件名
		    String fileName = file.getOriginalFilename();
		    // 创建Workbook工作薄对象，表示整个excel
		    Workbook workbook = null;
	        // 获取excel文件的io流
	        InputStream is = file.getInputStream();
	        // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
	        if(fileName.endsWith("xls")){
	           // 2003
	           workbook = new HSSFWorkbook(is);
	        }else if(fileName.endsWith("xlsx")){
	           // 2007
	           workbook = new XSSFWorkbook(is);
	        }
	    
	        // 内容
    	    List<String> sList = null;
    	    // 名
	        String fName = new SimpleDateFormat("yyMMdd-HHmm").format(new Date()) + ".csv";
        	// 下载位置
			fos = new FileOutputStream("D:\\kingMed" + path + "/" + fName);
			oswLocal = new OutputStreamWriter(fos, "GBK");
			response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + fName);
            out = response.getOutputStream();
			oswPage = new OutputStreamWriter(out, "GBK");
	        // 记录需要打印的位置 -1定性值 -2实验号
	        List<Integer> iList = new ArrayList<Integer>(pList.size());
	        
			switch(program) {
				case "one" : {
			       for(int i = 0; i < workbook.getNumberOfSheets(); i++){
			    	   Sheet sheet = workbook.getSheetAt(i);
			    	   int sheetFlag = sheet.getLastRowNum();
			    	   if (sheetFlag != 0) {
			    		   sheetFlag++;
			    	   } else {
			    		   continue;
			    	   }
			    	   for (int j = sheet.getFirstRowNum(); j < sheetFlag ; j++) {
			    		   sList = new ArrayList<String>(16);
			    		   Row row = sheet.getRow(j);
					       if (j == 0) {
					    	   // 第一行为表头，将需要的打印并记录需要打印的位置
					    	   for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {  
					    		   Cell cell = row.getCell(y);  
					    		   String cellStr = cell + "";
					    		   for (Project p : pList) {
					    			   if (p.getName().equals(cellStr)) {
					    				   sList.add(cellStr);
					    				   iList.add(y);
					    				   break;
					    			   }
					    		   }
					    	   }  
						       csvPrinterPage = new CSVPrinter(oswPage, CSVFormat.DEFAULT);
						       csvPrinterLocal = new CSVPrinter(oswLocal, CSVFormat.DEFAULT);
					       } else {
			            	   for (int iFlag = 0; iFlag < iList.size(); iFlag++) {
			            		   Cell cell = row.getCell(iList.get(iFlag));  
				               	   String flag = pList.get(iFlag).getFlag();
				               	   String cellResult = "";
				               	   // 结果处理一下
				                   cellResult = as.conversion(flag, cell.toString());
				                   sList.add(cellResult);
			            	   }
			               }
					       csvPrinterPage.printRecord(sList);
					       csvPrinterLocal.printRecord(sList);
					   }
				    }
				    break;
				}
				case "two" : {
					// 定性值
					int qualitativeValue = 0;
					// 实验号
					int experimentNumber = 0;
					for(int i = 0; i < workbook.getNumberOfSheets(); i++){
			    	   Sheet sheet = workbook.getSheetAt(i);
			    	   int sheetFlag = sheet.getLastRowNum();
			    	   if (sheetFlag != 0) {
			    		   sheetFlag++;
			    	   } else {
			    		   continue;
			    	   }
			    	   for (int j = sheet.getFirstRowNum(); j < sheetFlag; j++) {  
			    		   sList = new ArrayList<String>(16);
			    		   Row row = sheet.getRow(j);
					       if (j == 0) {
					    	   Cell cell = row.getCell(0); 
		    				   sList.add(cell + "");
		    				   csvPrinterPage = new CSVPrinter(oswPage, CSVFormat.DEFAULT);
						       csvPrinterLocal = new CSVPrinter(oswLocal, CSVFormat.DEFAULT);
					       } else if (j == 1) {
					    	   // 第一行为表头，将需要的打印并记录需要打印的位置
					    	   for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) { 
					    		   // 获取第j行第y个值
					    		   Cell cell = row.getCell(y);  
					    		   String cellStr = cell + "";
					    		   for (Project p : pList) {
					    			   if (p.getName().equals(cellStr)) {
					    				   sList.add(cellStr);
					    				   iList.add(y);
					    				   if ("定量值".equals(cellStr)) {
					    					   qualitativeValue = y;
					    				   } else if ("样本号".equals(cellStr)) {
					    					   experimentNumber = y;
					    				   }
					    				   break;
					    			   }
					    		   }
					    	   }  
		    				   sList.add("定性值");
		    				   iList.add(-1);
		    				   sList.add("实验号");
		    				   iList.add(-2);
					       } else {
					    	   Cell qCell = row.getCell(qualitativeValue); 
		            		   String qCellResult = "";
		            		   qCellResult = as.conversion(quantitativeFlag, qCell.toString());
		            		   // \t就是空，空就不用显示
		            		   if (!"\t".equals(qCellResult)) {
				            	   for (int iFlag = 0; iFlag < iList.size(); iFlag++) {
				            		   int iResult = iList.get(iFlag);
				            		   if (iResult == -1) {
				            			   iResult = qualitativeValue;  
				            		   } else if (iResult == -2) {
				            			   iResult = experimentNumber;  
				            		   }
				            		   Cell cell = row.getCell(iResult);  
					               	   String flag = pList.get(iFlag).getFlag();
					               	   String cellResult = "";
					               	   // 结果处理一下
					                   cellResult = as.conversion(flag, cell.toString());
					       		       sList.add(cellResult);
				            	   }
					       		}
			               }
					       csvPrinterPage.printRecord(sList);
					       csvPrinterLocal.printRecord(sList);
					   }
				    }
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			// 设置返回值
			PrintWriter writer = null;
			try {
				response.setContentType("text/html;charset=utf-8");
				writer = response.getWriter();
				writer.print("转换失败，建议核对一下数据!");
			} catch (Exception e1) {
			    e1.printStackTrace();
		 	} finally {
		 		if (writer != null) {
		 			writer.flush();
					writer.close();
		 		}
		 	}
		} finally {
			try {
				if (csvPrinterLocal != null) {
					csvPrinterLocal.flush();
					csvPrinterLocal.close();
				}
				if (csvPrinterPage != null) {
					csvPrinterPage.flush();
					csvPrinterPage.close();
				}
				if (oswLocal != null) {
					oswLocal.close();
				}
				if (oswPage != null) {
					oswPage.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (out != null) {
					out.flush();
		            out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
