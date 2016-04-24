package com.aeolus.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap; 

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject; 

import com.aeolus.base.Base;

import android.util.Log;

public class CWcfDataRequest implements IWcfDataResquest {

	private String DEF_SVR_IP = "14981k9l96.imwork.net";
	private String DEF_SVR_Port = "13647";
	private String DEF_PROCE_DB = "appdatabase";
	private String DEF_METOD_NAME = "DataRequest_By_SimpDEs";
	private String DEF_METOD_PARAM = "_methodRequests";

	public CWcfDataRequest() {

	}

	public CWcfDataRequest(String mSVR_IP, String SVR_Port, String PROCE_DB,
			String METOD_NAME, String METOD_PARAM) {
		this.DEF_SVR_IP = mSVR_IP;
		this.DEF_SVR_Port = SVR_Port;
		this.DEF_PROCE_DB = PROCE_DB;
		this.DEF_METOD_NAME = METOD_NAME;
		this.DEF_METOD_PARAM = METOD_PARAM;
	}

	@Override
	public String DataRequest_By_SimpDEs(String proceName, String[] paramKeys,
			String[] paramVals) {

		return DataRequest(DEF_SVR_IP, DEF_SVR_Port, DEF_METOD_NAME,
				DEF_METOD_PARAM, DEF_PROCE_DB, proceName, paramKeys, paramVals);
	}

	@Override
	public String DataRequest(String metodName, String metodParam,
			String proceDb, String proceName, String[] paramKeys,
			String[] paramVals) {

		return DataRequest(DEF_SVR_IP, DEF_SVR_Port, metodName,
				DEF_METOD_PARAM, DEF_PROCE_DB, proceName, paramKeys, paramVals);
	}

	//
	private static String DataRequest(String svrIP, String svrPort,
			String metodName, String metodParam, String proceDb,
			String proceName, String[] paramKeys, String[] paramVals) {
		String _metodQst = null;
		try {
			_metodQst = CreateMetodRequestString(proceDb, proceName, paramKeys,
					paramVals);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (_metodQst != null) {
			String _retString = SvrRequest(svrIP, svrPort, metodName,
					metodParam, _metodQst);
			return _retString;
		} else {
			return null;
		}
	}
	// ���������������;
		private static String CreateMetodRequestString(String proceDb,
				String proceName, String[] paramKeys, String[] paramVals)
				throws JSONException {
			JSONObject _jsonObj = new JSONObject();
			_jsonObj.put("ProceDb", proceDb);
			_jsonObj.put("ProceName", proceName);
			_jsonObj.put("ParamKeys", ConvertObjectAry2JsonValAry(paramKeys));
			_jsonObj.put("ParamVals", ConvertObjectAry2JsonValAry(paramVals));
			return "[" + _jsonObj.toString() + "]";
		}

	/**
	 * ������������
	 * @param IP��ַ  svrIP
	 * @param �˿� svrPort
	 * @param ���� metodName
	 * @param �洢���� metodParam
	 * @param ����ֵ paramValue
	 * @return
	 */
	private static String SvrRequest(String svrIP, String svrPort,
			String metodName, String metodParam, String paramValue) {
		try {
			String _url = String.format("http://%s:%s/SimpDbServer", svrIP,
					svrPort);
			Log.v(Base.TAG,_url);
			URL url = new URL(_url);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			String soapActionString = String.format(
					"http://tempuri.org/ISimpDbServer/%s", metodName);
			StringBuffer SB = new StringBuffer();
			SB.append("<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><");
			SB.append(metodName);
			SB.append(" xmlns=\"http://tempuri.org/\"><");
			SB.append(metodParam);
			SB.append(">");
			SB.append(paramValue);
			SB.append("</");
			SB.append(metodParam);
			SB.append("></");
			SB.append(metodName);
			SB.append("></s:Body></s:Envelope>");
			String _sendMsg = SB.toString();
			Log.v(Base.TAG, _sendMsg);
			byte[] buffer = _sendMsg.getBytes("UTF8");
			httpConn.setRequestProperty("Content-Length",String.valueOf(buffer.length));
			httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction", soapActionString);
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setConnectTimeout(1000 * 15);
			OutputStream out = httpConn.getOutputStream();
			out.write(buffer, 0, buffer.length);
			out.close();
			byte[] datas = readInputStream(httpConn.getInputStream());
			String _retString = new String(datas);
			Log.v(Base.TAG, _retString); 

			String _headTag = "<" + metodName + "Result>"; 
			String _footTag = "</" + metodName + "Result>"; 
			int _startIndex = _retString.indexOf(_headTag) + _headTag.length();
			int _endIndex = _retString.indexOf(_footTag);

			String re = _retString.substring(_startIndex, _endIndex);

			return re;
		} catch (Exception ex102) {
			return null;
		}
	}

	/**
	 * ��������ȡ
	 * @param inStream
	 * @return byte[]
	 * @throws IOException
	 */
	private static byte[] readInputStream(InputStream inStream)
			throws IOException {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();// ��ҳ�Ķ���������
		outStream.close();
		inStream.close();
		return data;
	}

	
	// ������ת��ΪJson���� JSONArray����
	private static JSONArray ConvertObjectAry2JsonValAry(String[] objAry)
			throws JSONException {
		if (objAry == null) {
			return null;
		}
		JSONArray _jsonAry = new JSONArray();
		for (int _i = 0, _iCnt = objAry.length; _i < _iCnt; _i++) {
			_jsonAry.put(objAry[_i]);
		}
		return _jsonAry;
	} 
	/**
	 * �жϷ���˷��ص�String�Ƿ����������������
	 * @param jsString
	 * @return JSONArray
	 * @throws JSONException 
	 */
	private JSONArray JsStringCheck(String jsString)
			throws JSONException {
		//jsString = [[{"SimpDataArry":[635763573432812500,null,[["��1�ֶ�",��1�ֶ�����]],[["��1ֵ"]]},{"SimpDataArry":[635763573432812500,null,[["��1�ֶ�",��1�ֶ�����]],[["��1ֵ"]]}]]
		// �������ݸ�ʽ��
			// [
			// [
				// {
				// "SimpDataArry":
					// [
					// 635932056487998631,
					// null,
					// [
					// ["Result1",3],
					// ["Result2",3],
					// ["Result3",3],
					// ["Result4",3]
					// ],
					// [
					// ["Connection1","03 10 2016 11:20AM","2","3"],
					// ["Connection1","03 10 2016 11:20AM","2","3"]
					// ]
					// ]
				// },
				// {
				// "SimpDataArry":
					// [
					// 635932056487998631,
					// null,
					// [
					// ["Result1",3],
					// ["Result2",5],
					// ["Result3",6],
					// ["Result4",4]
					// ],
					// [
					// ["Connection2","2016/3/10 11:20:48",11.223,1]
					// ]
					// ]
				// }
			// ]
			// ]
		// 1����jsStringתΪJSONArray�������_jsonAry0
		// 2�������ĵ�һ��Ԫ�ػ���ΪJSONArray���飬�����������������
		if (jsString == null || jsString.length() <= 0) {
			return null;
		}
		JSONArray _jsonAry = new JSONArray(jsString);
		if (_jsonAry == null || _jsonAry.length() <= 0) {
			return null;
		}
		//������һ��[]
		JSONArray jsa = _jsonAry.getJSONArray(0);
		if (jsa == null || jsa.length() <= 0) {
			return null;
		}
		return jsa;
	}
	/**
	 * �������Դ
	 * @param jsString
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<ArrayList<HashMap<String, Object>>> LoadMultiDataSource(String jsString) 
			throws JSONException {
		
		JSONArray jsa = JsStringCheck(jsString); 
		ArrayList<ArrayList<HashMap<String, Object>>> arrayList = new ArrayList<ArrayList<HashMap<String, Object>>>();
		if (jsa.length()>1) {
			for (int k = 0; k < jsa.length(); k++) {
				JSONObject obj = jsa.getJSONObject(k); 
				ArrayList<HashMap<String, Object>> _arrayList = AnalysisCode(obj);
				arrayList.add(_arrayList);
			}
			return arrayList; 
		}
		return arrayList;
	}
	
	/**
	 * ��������Դ
	 * @param jsa
	 * @return
	 * @throws JSONException
	 */
	public ArrayList<HashMap<String, Object>> LoadSingleDataSource(String jsString) 
			throws JSONException { 
		JSONArray jsa = JsStringCheck(jsString); 
		ArrayList<HashMap<String, Object>> arrayList  = new  ArrayList<HashMap<String, Object>>();
		if (jsa.length()==1) {
			JSONObject obj = jsa.getJSONObject(0); 
			arrayList= AnalysisCode(obj);
			return arrayList;
		}
		return null;
		
	}
	/**
	 * ��������
	 * @param obj
	 * @return ArrayList<HashMap<String, Object>>
	 * @throws JSONException
	 */
	private ArrayList<HashMap<String, Object>> AnalysisCode(JSONObject obj)
			throws JSONException {
		ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		JSONArray _jsa = obj.getJSONArray("SimpDataArry");
		//_jsa =  [635763573432812500,null,[["Result1",3],["Result2",3],["Result3",3]],[["Connection1","Connection2","Connection3"],["Connection1","Connection2","Connection3"],["Connection1","Connection2","Connection3"],["Connection1","Connection2","Connection3"],["Connection1","Connection2","Connection3"]]]
		//_jsa._jsa.getJSONArray(0) == 635763573432812500
		//_jsa._jsa.getJSONArray(1) == null
		//_jsa._jsa.getJSONArray(2) == [["Result1",3],["Result2",3],["Result3",3]]
		//_jsa._jsa.getJSONArray(3) == [["Connection1","Connection2","Connection3"],["Connection1","Connection2","Connection3"],["Connection1","Connection2","Connection3"],["Connection1","Connection2","Connection3"],["Connection1","Connection2","Connection3"]]			 
		// ����һ��hashmap���� ���������Arraylist��
		JSONArray _retRows = _jsa.getJSONArray(3);
		for (int i = 0; i < _retRows.length(); i++) {
			JSONArray _retRow = _retRows.getJSONArray(i);
			JSONArray _retCols = _jsa.getJSONArray(2);
			HashMap<String, Object> hm = new HashMap<String, Object>();
			for (int j = 0; j < _retRow.length()
					&& _retRow.length() == _retCols.length(); j++) {
				JSONArray _col = _retCols.getJSONArray(j);
				String _colName = null;
				String _colTypeVal = null;
				if (_col.length() > 1) {
					_colName = _col.getString(0);
					_colTypeVal = _col.getString(1);
				}
				int _typeVal = Integer.parseInt(_colTypeVal);
				Object _val = null;
				switch (_typeVal) {
				// Byte = 0,
				case 0:
					// Int32 = 2,
				case 2:
					// Int16 = 7,
				case 7:
					_val = _retRow.getInt(j);
					break;
				// Int64 = 1,
				case 1:
					_val = _retRow.getLong(j);
					break;
				// String = 3,
				case 3:
					_val = _retRow.getString(j);
					break;
				// Boolean = 4,
				case 4:
					if (_retRow.getInt(j) == 1) {
						_val = true;
					} else
						_val = false;
					break;
				// DateTime = 5,
				case 5:
					_val = _retRow.getString(j);
					break;
				// Decimal = 6,
				case 6:
					_val = _retRow.getDouble(j);
					break;
				}
				hm.put(_colName, _val);
			}
			arrayList.add(hm);
		}
		return arrayList;
	}

}
