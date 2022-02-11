package quoters;

public class ProfilingController implements ProfilingControllerMBean { //ProfilingControllerMBean именно имя класса + MBean обязательно для возможности обращения к методам на лету
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
