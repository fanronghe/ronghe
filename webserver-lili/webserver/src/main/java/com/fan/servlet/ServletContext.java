package com.fan.servlet;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * servlet上下文对象
 * servlet的ID:请求方式和请求路径拼接而成的字符串
 * servlet/impl目录中类:特定的请求处理器
 * 维护了servlet的ID和servlet/impl目录中类的全路径的映射关系
 * @author fanronghe
 * @Date 21.9.18 下午 8:03
 */
public class ServletContext {

    /**
     * 定义servlet的ID和servlet/impl目录中类的全路径的映射表
     * key为servlet的ID
     * value为servlet/impl目录中类的全名
     */
    private static Map<String, HttpServlet> servletMapping = new HashMap<>();
    static {
        initServletMapping(); //对映射表初始化
    }

    /**
     * 借助dom4j依赖中的SAXReader工具类
     * 从servlets.xml映射表文件取出映射数据导入到映射查找表中
     */
    private static void initServletMapping() {
        try {
            SAXReader reader = new SAXReader();
            //在java文件中路径./和不写是等效的表示当前工作路径Working Directory
            //而一般都要加上/表示绝对路径,在linux系统上好使,一般都使用相对路径,前面不加/即可
            //如果要想使用target中classes或者jar包中的文件可以使用getResource()方法,并必须配合/根路径使用
            //并且getResource()后面不可以加getPath()方法,不然就会报错
            Document doc = reader.read( ServletContext.class.getResource( "/config/servlets.xml" ) ); //xml配置文件的定位
            Element root = doc.getRootElement(); //获取根节点(元素)root,一个xml文件仅能有一个根节点
            List<Element> list = root.elements( "servlet" );
            for( Element e : list ){ //使用新循环遍历
                String id = e.attributeValue( "id" ); //获取servlet的ID
                String className = e.attributeValue( "className" ); //获取servlet/impl目录中某个类的全名

                Class cls = Class.forName( className ); //利用反射根据类的全名由框架自动创建该对象bean
                HttpServlet bean = ( HttpServlet ) cls.newInstance();

                servletMapping.put( id, bean ); //将bean对象的引用存入map中,索引为id(method+uri)
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * 根据处理器的id(method+uri)取出相应的处理器HttpServlet
     * @param id 请求处理器id
     * @return 请求处理器
     */
    public static HttpServlet getController( String id ){
        return servletMapping.get( id );
    }


}
