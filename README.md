# sqlroot  

## 简介<br/>   
轻量级sql配置化实现<br/>   

## 集成<br/>   
采用sql xml配置化解决代码集成sql语句的问题,采用底层原生开发,无依赖第三方api<br/>  
1.将sqlroot.properties放入classpath路径<br/>  
mapper.location=/resource/mapper/ mapper映射文件位置,不限制命名<br/>  
mapper.logger=true 是否打印sql语句,调试时候建议打开,会打印出执行的完整sql语句<br/>   
2.将sqlroot-1.0.jar加入build path<br/>  
3.代码中使用,可在父类或者接口中申明或者继承<br/>  
private SQLRoot mapper = SQLRoot.instance();<br/>  

## 异常<br/>
com.awn.sqlroot.exception.ConfigNotFoundException 配置未找到<br/>  
com.awn.sqlroot.exception.MultiDefinedException SQLKEY重复定义<br/>  
com.awn.sqlroot.exception.SqlNotFoundException 未找到需要查询的SQL<br/>  
com.awn.sqlroot.exception.XmlNotFoundException mapper中未找到xml文件<br/>  

## 方法<br/>   
String sql(String sqlKey);<br/>   
String sql(String sqlKey,Map<String,Object> param);<br/>   
String sql();<br/>   
String sql(Map<String,Object> param);<br/>   
mapper.size();<br/>   

# changelog
## 1.0 change log<br/>   
1.mapper.sql(sqlKey);指定SQL-KEY作为SQL查询<br/>   
2.mapper.sql(sqlKey,param);指定SQL-KEY作为SQL查询,xml中 ${_param} 格式参数可用于表 字段等,与mybatis一致 xml中 #{_param} 格式参数可用于字符参数等,与mybatis一致<br/>   
3.mapper.size() 返回当前mapper扫描到的所有SQL数量<br/>   


## 1.1 change log<br/>   
1.mapper.sql();获取当前方法定义SQL,无需SQL-KEY则表示使用当前调用的方法名字作为key<br/>   
2.mapper.sql(param); 获取当前方法定义SQL,无需SQL-KEY则表示使用当前调用的方法名字作为key xml中 ${_param} 格式参数可用于表 字段等,与mybatis一致 xml中 #{_param} 格式参数可用于字符参数等,与mybatis一致<br/>   

## 1.2 change log<br/>   
修复由于参数值为null导致的空指针异常<br/>   

## 1.3 change log<br/>   
1.修复参数为空导致的空指针<br/>   
2.优化整体性能<br/>   
3.加入FileAdapt监控文件修改,实时调整内存数据 <br/>  
4.增加配置mapper.scan.interval,表示mapper扫描间隔,默认1000<br/>   
