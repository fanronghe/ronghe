package com.fan.http;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http协议(底层(传输层)是tcp可靠传输协议),http是对tcp的封装是应用层协议
 * 本类是http协议的上下文类对象,可以根据文件后缀(suffix)名得到http中的类型专有Content-Type
 * 注意前缀的英文为(prefix)
 * 这种查找需要一个web.xml文件,以及在项目启动初始化时,把文件中的数据导入到查找表Map中
 * 由于需要解析xml文件,这里需要添加一个解析xml文件的依赖:
 * @author fanronghe
 * @Date 21.9.18 上午 9:41
 */
public class HttpContext {


    /**
     * 定义内存查找表,key为String类型的文件后缀名(suffix),value也为String类型的http专有Content-Type文件传输类型
     */
    private static Map<String, String> mimeMapping = new HashMap<>();

    /**
     * 对查找表初始化
     */
    static {
        initMimeMapping();
    }

    /**
     * 借助dom4j依赖中的SAXReader工具类
     * 在项目启动时把文件中的全部目标数据导入到查找表中
     */
    private static void initMimeMapping() {
        try {
            SAXReader reader = new SAXReader();
            /**
             * 但凡路径可以接收URL类型的,均可以使用jar包中的文件,若没有就不可以
             * 且jar包中的文件是不可以封装为File类型的,这是做不到的
             */
            //为了可以从target/classes文件夹中获取资源或者从jar包内部获取资源,可以使用getResource()方法
            //此时必须要加/ 以表示生产环境下的根目录
            Document doc = reader.read( HttpContext.class.getResource( "/config/web.xml" ) ); //xml配置文件的定位  /表示当前Working Directory
            Element root = doc.getRootElement(); //获取根节点(元素)root,一个xml文件仅能有一个根节点
            List<Element> list = root.elements( "mime-mapping" ); //获取根节点下的叫"mime-mapping"的子节点
            list.forEach( e -> {
                String key = e.elementText( "extension" ); //获取后缀suffix
                String value = e.elementText( "mime-type" ); //获取类型
                mimeMapping.put( key, value ); //一个一个导入map中
            } );
        } catch ( DocumentException e ) {
            e.printStackTrace();
        }
    }

    /**
     * 本上下文类对象提供唯一的静态方法,
     * 根据后缀名,可取出类型
     * @param suffix 响应文件的后缀名
     * @return 响应文件的http类型
     */
    public static String getMimeType( String suffix ){
        return mimeMapping.get( suffix ); //从查找表中获取http类型
    }

}
