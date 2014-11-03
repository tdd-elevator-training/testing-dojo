package org.automation.dojo;

import java.util.Random;

/**
 * Created by Sanja on 03.11.2014.
 */
public class RandomDice implements Dice {
    @Override
    public int next(int max) {
        return new Random().nextInt(max);
    }
}
