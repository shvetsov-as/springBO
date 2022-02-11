package quoters;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Profiling
public class TerminatorQuoter implements Quoter {

    @InjectRandomInt(min = 2, max = 7)
    private int repeat;

    private String message;

    @PostConstruct
    public void init() {
        System.out.println("__________Phase 2__________");
        System.out.println("__________init() + @PostConstruct__________");
    }

    public TerminatorQuoter() {
        System.out.println("__________Phase 1__________");
        System.out.println("__________Constructor__________");
        System.out.println(repeat);// так делать нельзя - сначала создается объект бина спрингом, потом спринг его настраивает по аннотациям. repeat тут еще пустой
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void sayQuote() {
        System.out.println();
        System.out.println("__________sayQuote()__________");
        for (int i = 0; i < repeat; i++) {
            System.out.println("message = " + message);
        }

    }
}
