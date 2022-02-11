package quoters;

public interface ProfilingControllerMBean { // указываем те методы, которые хотим чтобы были доступны через JMX console
    void setEnabled(boolean enabled);
}
