package com.aeolus.service;

/**
 * Description:�����������ݽӿ�
 * User: HB
 * Date: 2015/6/3
 * Time: 15:40
 */
public interface IWcfDataResquest {
	public String DataRequest_By_SimpDEs(String proceName,
			String[] paramKeys, String[] paramVals);
	
	public String DataRequest(String metodName, String metodParam,
			String proceDb, String proceName, String[] paramKeys,
			String[] paramVals);
}
