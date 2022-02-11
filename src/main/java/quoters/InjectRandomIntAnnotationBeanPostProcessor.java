package quoters;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Random;

public class InjectRandomIntAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException { // before init()

        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);

            if (annotation != null) {
                int min = annotation.min();
                int max = annotation.max();
                Random random = new Random();
                int randBetween = min + random.nextInt(max - min);
                field.setAccessible(true);// to get ability to write in private field
                //field.set(randBetween); -- don't do it because of throwing exceptions that we can't handle (catch)
                ReflectionUtils.setField(field, bean, randBetween); //ReflectionUtils.setField(for which field to set the value , for which object, value)
            }
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {// after init()
        return bean;
    }
}
