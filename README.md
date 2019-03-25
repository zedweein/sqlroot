sqlroot  

简介  
轻量级sql配置化实现  

集成  
采用sql xml配置化解决代码集成sql语句的问题,采用底层原生开发,无依赖第三方api  
1.将sqlroot.properties放入classpath路径  
mapper.location=/resource/mapper/ mapper映射文件位置,不限制命名  
mapper.logger=true 是否打印sql语句,调试时候建议打开,会打印出执行的完整sql语句   
2.将sqlroot-1.0.jar加入build path  
3.代码中使用,可在父类或者接口中申明或者继承  
private SQLRoot mapper = SQLRoot.instance();  

异常  
com.awn.sqlroot.exception.ConfigNotFoundException 配置未找到  
com.awn.sqlroot.exception.MultiDefinedException SQLKEY重复定义  
com.awn.sqlroot.exception.SqlNotFoundException 未找到需要查询的SQL  
com.awn.sqlroot.exception.XmlNotFoundException mapper中未找到xml文件  

方法  
String sql(String sqlKey);  
String sql(String sqlKey,Map<String,Object> param);  
String sql();  
String sql(Map<String,Object> param);  
mapper.size()  


1.0 change log  
1.mapper.sql(sqlKey);指定SQL-KEY作为SQL查询  
2.mapper.sql(sqlKey,param);指定SQL-KEY作为SQL查询,xml中 ${_param} 格式参数可用于表 字段等,与mybatis一致 xml中 #{_param} 格式参数可用于字符参数等,与mybatis一致  
3.mapper.size() 返回当前mapper扫描到的所有SQL数量  


1.1 change log  
1.mapper.sql();获取当前方法定义SQL,无需SQL-KEY则表示使用当前调用的方法名字作为key  
2.mapper.sql(param); 获取当前方法定义SQL,无需SQL-KEY则表示使用当前调用的方法名字作为key xml中 ${_param} 格式参数可用于表 字段等,与mybatis一致 xml中 #{_param} 格式参数可用于字符参数等,与mybatis一致  

1.2 change log  
修复由于参数值为null导致的空指针异常  

1.2 change log  
1.修复参数为空导致的空指针  
2.优化整体性能  
3.加入FileAdapt监控文件修改,实时调整内存数据  
4.增加配置mapper.scan.interval,表示mapper扫描间隔,默认1000  
