package com.kingmed.applets.service;

import org.springframework.stereotype.Service;

/**
 * 算法
 * @author BIN
 *
 */
@Service
public class AlgorithmService {
	
	/**
     * 根据rule将element转换为对应的值
     * @param rule 规则，如（one）
     * @param element 需要转换的数
     * @return
     */
    public String conversion(String rule, String element) {
    	
    	String result = "";
    	switch(rule) {
    		case "one" : {
    			Double dbResult = Double.parseDouble(element);
    			int dbInt = dbResult.intValue();
    			if (dbInt < 10) {
    				result = "K00" + dbInt;
    			} else if (dbInt >= 10 && dbInt < 100) {
    				result = "K0" + dbInt;
    			} else if (dbInt >= 100) {
    				result = "K" + dbInt;
    			}
    			break;
    		}
    		case "two" : {
    			Double dbResult = Double.parseDouble(element);
    			if (dbResult < 0.35) {
    				result = "0级";
    			} else if (dbResult >= 0.35 && dbResult <= 0.70) {
    				result = "1级";
    			} else if (dbResult >= 0.71 && dbResult <= 3.50) {
    				result = "2级";
    			} else if (dbResult >= 3.51 && dbResult <= 17.50) {
    				result = "3级";
    			} else if (dbResult >= 17.51 && dbResult <= 50.00) {
    				result = "4级";
    			} else if (dbResult >= 50.01 && dbResult <= 100.00) {
    				result = "5级";
    			} else if (dbResult > 100.00) {
    				result = "6级";
    			}
    			break;
    		}
    		case "three" : {
    			// 不加\t0.000只显示0
    			result = element + "\t";
    			break;
    		}
    		case "four" : {
    			try {
					Double dbResult = Double.parseDouble(element);
					if (dbResult < 2.00) {
						result = "阴性（-）";
					} else if (dbResult >= 2.00) {
						result = "阳性（+）";
					} 
				} catch (Exception e) {
					// TODO: handle exception
					break;
				}
				break;
    		}
    		case "five" : {
    			result = "XKY"  + element;
    			break;
    		}
    	}
    	return result;
    }

}
