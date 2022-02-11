package quoters;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

import java.util.HashMap;
import java.util.Map;

public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {
    // имя бина всегда сохраняется
    //два способа: CJLIB - наследование от класса -> переопредеоение методов родителя + добавление логики -> подмена;
    //dynamic Proxy - имплементация всех имплементированных в классе I в новом классе + добавление логики -> подмена
    private Map<String, Class> map = new HashMap<>();// имя бина, класс
    private ProfilingController controller = new ProfilingController();// controller to enable or disable logic

    public ProfilingHandlerBeanPostProcessor() throws Exception {//для регистрации MBean
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();//получаем инстанс мбин сервера, в который можно регистрировать бины
        platformMBeanServer.registerMBean(controller, new ObjectName("profiling", "name", "controller"));
    }

    @Override

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Profiling.class)) {
            map.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        Class beanClass = map.get(beanName);
        if (beanClass != null) {//Proxy.newProxyInstance создает объект из нового класса, который сгенерирует на лету
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), new InvocationHandler() { //InvocationHandler() инкапсулирует логику которая попадет во все методы класса, генерируемого на лету
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {//логикая которая будет в каждом методе класса, который сгенеририруется на лету, который имплементирует интерфейсы оригинального класса
                    if (controller.isEnabled()) {
                        System.out.println("Profiling..........................");

                        long before = System.nanoTime();

                        Object returnedValue = method.invoke(bean, args);//передаем оригинальный бин и его оригинальные методы

                        long after = System.nanoTime();
                        System.out.println();
                        System.out.println("Measured time :");
                        System.out.println(after - before);
                        System.out.println();

                        System.out.println("Profiling END");
                        return returnedValue;
                    } else {
                        return method.invoke(bean, args);
                    }
                }
            });
        }
        return bean;
    }
}
