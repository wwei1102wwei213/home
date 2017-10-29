package com.zeyuan.kyq.utils.PayHttp;




import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016-08-30.
 *
 *
 *
 * @author wwei
 */
public class PayUtils {

    private static Map<String,String> map;
    private static final String APP_ID = "wx02cca444de652c20";//"wx35f8def43e0149c8";
    public static final String MCH_ID = "1370371202";
    private static final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String urlCallBack = "http://help.kaqcn.com/help/pay_callback";
    private static final String PayUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    public interface GetPrepayCallBack{
        void getPrepaySuccess(String prepay,String sign);
        void getPrepayFailed();
    }

    public interface GetMoneyCallBack{
        void getMoneySuccess(String req,String tradeID);
        void getMoneyFailed();
    }

    /*public static void getPrepayID(final GetPrepayCallBack callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
                    parameters.put("appid", APP_ID);
                    parameters.put("mch_id", MCH_ID);
                    parameters.put("nonce_str", genNonceStr());
                    parameters.put("body", "泽愿科技支付测试");
                    parameters.put("out_trade_no", getTradeID());
                    parameters.put("total_fee", "100");
                    parameters.put("spbill_create_ip","127.0.0.1");
                    parameters.put("notify_url", urlCallBack);
                    parameters.put("trade_type", "APP");
                    String sign = createSign("UTF-8", parameters);
                    parameters.put("sign", sign);
                    String requestXML = getRequestXml(parameters);
                    String req = postData(url, requestXML, null);
                    callback.getPrepaySuccess(req,sign);
                }catch (Exception e){
                    callback.getPrepayFailed();
                }
            }
        }).start();
    }

    public static void getMoney(final String openid,final GetMoneyCallBack callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String tradePayID = getTradePayID();
                    SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
                    parameters.put("amount", "100");
                    parameters.put("mch_appid", "wx35f8def43e0149c8");
//                    parameters.put("body", "泽愿科技体现测试");
                    parameters.put("check_name","NO_CHECK");
                    parameters.put("desc","提现测试");
                    parameters.put("mchid", MCH_ID);
                    parameters.put("nonce_str", genNonceStr());
                    parameters.put("openid",openid);
                    parameters.put("partner_trade_no", tradePayID);
                    parameters.put("spbill_create_ip","127.0.0.1");
                    String sign = createSign("UTF-8", parameters);
                    parameters.put("sign", sign);
                    String requestXML = getRequestXml(parameters);
                    String req = uploadPostMethod(PayUrl, requestXML.getBytes());
                    System.out.println(req);
                    callback.getMoneySuccess(req, tradePayID);
                }catch (Exception e){
                    callback.getMoneyFailed();
                }
            }
        }).start();
    }

    public static String getMoneyXml(final String openid){
        String tradePayID = getTradePayID();
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("amount", "100");
        parameters.put("mch_appid", APP_ID);
        parameters.put("body", "泽愿科技体现测试");
        parameters.put("check_name","NO_CHECK");
        parameters.put("desc","提现测试");
        parameters.put("mchid", MCH_ID);
        parameters.put("nonce_str", genNonceStr());
        parameters.put("openid",openid);
        parameters.put("partner_trade_no", tradePayID);
        parameters.put("spbill_create_ip","127.0.0.1");
        String sign = createSign("UTF-8", parameters);
        parameters.put("sign", sign);
        String requestXML = getRequestXml(parameters);
        return requestXML;
    }

    private static String getTradeID(){
        return "zeyuan"+System.currentTimeMillis();
    }

    private static String getTradePayID(){
        return "zeyuanpay"+System.currentTimeMillis();
    }


    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }


    *//**
     * @author
     * @date
     * @Description 将请求参数转换为xml格式的string
     * @param parameters  请求参数
     * @return
     *//*
    public static String getRequestXml(SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
                sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
            }else {
                sb.append("<"+k+">"+v+"</"+k+">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static byte[] callMapToXML(Map map) {
//        logger.info("将Map转成Xml, Map：" + map.toString());
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><bizdata>");
        mapToXMLTest2(map, sb);
        sb.append("</bizdata>");
//        logger.info("将Map转成Xml, Xml：" + sb.toString());
        try {
            return sb.toString().getBytes("UTF-8");
        } catch (Exception e) {
//            logger.error(e);
        }
        return null;
    }

    private static void mapToXMLTest2(Map map, StringBuffer sb) {
        Set set = map.keySet();
        for (Iterator it = set.iterator(); it.hasNext();) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value)
                value = "";
            if (value.getClass().getName().equals("java.util.ArrayList")) {
                ArrayList list = (ArrayList) map.get(key);
                sb.append("<" + key + ">");
                for (int i = 0; i < list.size(); i++) {
                    HashMap hm = (HashMap) list.get(i);
                    mapToXMLTest2(hm, sb);
                }
                sb.append("</" + key + ">");
            } else {
                if (value instanceof HashMap) {
                    sb.append("<" + key + ">");
                    mapToXMLTest2((HashMap) value, sb);
                    sb.append("</" + key + ">");
                } else {
                    sb.append("<" + key + ">" + value + "</" + key + ">");
                }
            }
        }
    }


    private final static int CONNECT_TIMEOUT = 5000; // in milliseconds
    private final static String DEFAULT_ENCODING = "UTF-8";

    public static String postData(String urlStr, String data){
        return postData(urlStr, data, null);
    }

    public static String postData(String urlStr, String data, String contentType) {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(CONNECT_TIMEOUT);
//            if(StringUtils.isNotBlank(contentType))
//            conn.setRequestProperty("content-type", contentType);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);
            if(data == null)
                data = "";
            writer.write(data);
            writer.flush();
            writer.close();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
            System.out.println(sb.toString());
            return sb.toString();
        } catch (IOException e) {
//            logger_.error("Error connecting to " + urlStr + ": " + e.getMessage());
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    private static final String API_KEY ="DASDsddweddsewwdsdweesdwee12dqqq";


    public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);

        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    *//**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     *//*
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if(null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }

    *//**
     * 获取子结点的xml
     * @param children
     * @return String
     *//*
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    public static String uploadPostMethod(String path, byte[] body) throws Exception{
        byte[] entitydata = body;
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(entitydata.length));
        OutputStream os = conn.getOutputStream();
        os.write(entitydata);
        os.flush();
        os.close();
        String req = "";
        if(conn.getResponseCode() == 200){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                req += line;
            }
            try{
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
            return req.trim();
        }
        return null;
    }*/
}
