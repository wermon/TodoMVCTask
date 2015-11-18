package com.mvasylchuk.hw5.experiments;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Max on 10.11.2015.
 */
public class TestKoans {
    @Test
    public void forLoopContinueLabel() {
        int count = 0;

        for(int i = 0; i < 5; i++) {
            outerLabel:
            for(int j = 0; j < 5; j++)
            {
                count++;
                if(count > 2) {
                    continue outerLabel;
                }
            }
            count += 10;
        }
        // What does continue with a label mean?
        // What gets executed? Where does the program flow continue?

    }
    @Test
    public void forLoopBreakLabel() {
        int count = 0;

        for(int i = 0; i < 5; i++) {
            outerLabel:
            for(int j = 0; j < 5; j++)
            {
                count++;
                if(count > 2) {
                    break outerLabel;
                }
            }
            count += 10;
        }
    }

}
