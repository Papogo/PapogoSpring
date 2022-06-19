package com.papo.spring;


import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class PapoApplicationContext {
    private Class configClass;

    //存属性 生成对象存在map
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    //单例池map
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();


    // Spring容器
    public PapoApplicationContext(Class configClass) {
        this.configClass = configClass;

        //扫描--->得到BeanDefinition对象-->存到beanDefinitionMap
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value(); //扫描路径 com.papo.service
            //path转换目录
            path = path.replace(".","/"); //路径变为 com/papo/service

            //类加载器 获取绝对路径URL
            ClassLoader classLoader = PapoApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);

            //扫描路径文件夹判断 可表示文件或目录
            File file = new File(resource.getFile());

            //System.out.println(file);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    //System.out.println(fileName);
                    if (fileName.endsWith(".class")){

                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));

                        className = className.replace("\\",".");

                        //System.out.println(className);
                        try {
                            Class<?> clazz = classLoader.loadClass(className);

                            //判断是否有Component注解，有就是bean 没有就不是bean
                            if (clazz.isAnnotationPresent(Component.class)) {

                                Component component = clazz.getAnnotation(Component.class);
                                String beanName = component.value();

                                //扫描beanname ，没配置默认null空指针
                                //decapitalize(String name){}  name前两个字母大写返回name，若不是 首字母小写
                                if (beanName.equals("")) {
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }

                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                //bean判断单例还是多例，查看类里是否有Scope注解
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                    beanDefinition.setScope(scopeAnnotation.value());
                                }else {
                                    beanDefinition.setScope("singleton");
                                }

                                //存到MAP对象里
                                beanDefinitionMap.put(beanName, beanDefinition);

                            }

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }

        // 创建单例Bean对象（实例化单例Bean）
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")){
                //创建Bean对象
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);

            }

        }

    }

    private Object createBean(String beanName, BeanDefinition beanDefinition){

        Class clazz = beanDefinition.getType();

        try {
            Object instance = clazz.getConstructor().newInstance();

            //简单依赖注入 默认byTyppe找不到用byName
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(Autowired.class)) {
                    f.setAccessible(true);
                    f.set(instance, getBean(f.getName()));
                }
            }

            // Aware回调（直接使用）
            if (instance instanceof BeanNameAware){
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            //初始化（调用方法）
            if (instance instanceof InitializingBean){
                ((InitializingBean)instance).afterPropertiesSet();
            }

            //BeanPostProcesser(bean后置处理器)  初始化后 AOP



            return instance;

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getBean(String beanName){

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
       //通过传入的beanName找类（对于单例还是多例）
        if (beanDefinition == null){
            throw new NullPointerException();
        }else {
            String scope = beanDefinition.getScope();
            if (scope.equals("singleton")){
                Object bean = singletonObjects.get(beanName);
                if (bean == null){
                    Object bean1 = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName, bean1);
                }
                return bean;
            }else{
            //原型是多例
                return createBean(beanName, beanDefinition);

            }
        }





    }


}
