package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import quoters.Quoter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.assertEquals;

public class TerminatorTest {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("appContext.xml");

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    //private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    //private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        //System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        //System.setErr(originalErr);
    }

    @Test
    public void sayTest() {
        context.getBean(Quoter.class).sayQuote();
        //System.out.print("i'll be back");
        assertEquals("i'll be back", outContent.toString());
    }


}
