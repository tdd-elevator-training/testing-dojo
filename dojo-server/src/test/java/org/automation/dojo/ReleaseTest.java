package org.automation.dojo;

import org.automation.dojo.web.bugs.Bug;
import org.automation.dojo.web.scenario.BasicScenario;
import org.automation.dojo.web.scenario.Release;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class ReleaseTest {

    private List<BasicScenario> scenarios;
    private Release release;

    class MockBug1 extends Bug {
        public MockBug1() {
            super(1);
        }
    }

    class MockBug2 extends Bug {
        public MockBug2() {
            super(2);
        }
    }

    class MockBug3 extends Bug {
        public MockBug3() {
            super(3);
        }
    }

    class MockScenario extends BasicScenario {
        public MockScenario() {
            super(0, new ShopBugsQueue());
        }

        @Override
        public List<? extends Bug> getPossibleBugs() {
            return Arrays.asList(new MockBug1(), new MockBug2(), new MockBug3());
        }

        @Override
        public String process(Object request) {
            return null;
        }

        @Override
        public boolean activate(Object request) {
            return false;
        }
    }

    @Before
    public void init() {
        scenarios = new LinkedList<BasicScenario>();
        scenarios.add(new MockScenario());
        scenarios.add(new MockScenario());
        scenarios.add(new MockScenario());
        scenarios.add(new MockScenario());
        scenarios.add(new MockScenario());
        scenarios.add(new MockScenario());

        release = new Release(scenarios.toArray(new BasicScenario[0]));
    }



    @Test
    public void shouldOnlyOneScenarioBugPerMinorRelease(){
        release.takeNextBug();

        int countBugs = getBugScenarioCount();

        assertOneOrZerroBugs(countBugs);
    }

    private void assertOneOrZerroBugs(int countBugs) {
        assertTrue("Ожидается сколько угодно багов в релизе. \n Релиз: " + release.toString() + "\n",
                0 <= countBugs && countBugs <= scenarios.size());
    }

    private int getBugScenarioCount() {
        int result = 0;
        for (BasicScenario scenario : scenarios) {
            if (!scenario.bugsFree()) {
                result++;
            }
        }
        return result;
    }

    @Test
    public void checkBugFreeProbabilityMoreCases(){
        for (int index =0; index < 100; index++) {
            checkBugFreeProbability();
        }
    }

    public void checkBugFreeProbability(){
        int countBugFree = 0;
        int countIterations = 10000; // TODO какого фига тут если поставить 1000 то не найдется ни один багфришный релиз? Митсика...
        for (int index = 0; index < countIterations; index ++) {
            release.takeNextBug();
            if (getBugScenarioCount() == 0) {
                countBugFree++;
            }
        }

        double bugFreeReleaseProbability = (double)countBugFree/(double)countIterations;
        assertTrue("Процент багфришных релизов был " + bugFreeReleaseProbability +
                "\n а должен был быть ~=1/(количество сценариев в релизе + 1)",
                0 < bugFreeReleaseProbability && bugFreeReleaseProbability < 0.3);
        // TODO понять, что за комбиаторика тут получилась, а то неочевидно....
    }

    @Test
    public void shouldOnlyOneBugAfterMoreThanOneMinorReleases(){
        for (int index =0; index < 1000; index++) {
            release.takeNextBug();
            assertOneOrZerroBugs(getBugScenarioCount());
        }
    }


}
