package com.kingmed.applets.service;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

/**
 * @author BIN
 *
 */
@Service
public class AnalysisService {
	
	 /**
     * 解析xml
     * @param soucePath 文件路径
     * @return
     */
    public Document useDomReadXml(String soucePath){
        File file = new File(soucePath);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            return doc;
        } catch (Exception e) {
            System.err.println("读取该xml文件失败");
            e.printStackTrace();
        }
        return null;
    }
}
