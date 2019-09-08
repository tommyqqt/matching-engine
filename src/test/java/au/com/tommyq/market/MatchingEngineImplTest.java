package au.com.tommyq.market;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertTrue;

public class MatchingEngineImplTest {
    private MatchingEngineImpl matchingEngine;
    private List<InstrumentConfig> instruments;
    private Set<String> outputs;

    @Before
    public void setUp() throws Exception {
        instruments = new ArrayList<>();
        outputs = new HashSet<>();
    }

    @After
    public void tearDown() throws Exception {
        instruments.clear();
        outputs.clear();
    }

    @Test
    public void testOutputEventToConsole() throws Exception {
        instruments.add(new InstrumentConfig("USDCAD", 2.90, 3.10));
        instruments.add(new InstrumentConfig("GBPEUR", 1.05, 2.2));
        instruments.add(new InstrumentConfig("EURUSD", 1.04, 2.1));
        matchingEngine = new MatchingEngineImpl(instruments, newSingleThreadExecutor(), outputs::add);

        matchingEngine.start();
        matchingEngine.placeOffer("AbcCapital", "USDCAD", 10, 2.99);
        matchingEngine.placeBid("SuperInvestment", "USDCAD", 5, 2.98);
        matchingEngine.display("USDCAD");
        matchingEngine.placeBid("OneBank", "USDCAD", 10, 2.97);
        matchingEngine.placeOffer("TravelExchanger", "USDCAD", 7, 2.97);
        matchingEngine.display("USDCAD");

        matchingEngine.placeBid("AbcCapital", "GBPEUR", 1, 1.1);
        matchingEngine.placeBid("AbcCapital", "GBPEUR", 2, 1.2);
        matchingEngine.placeBid("AbcCapital", "GBPEUR", 2, 1.3);
        matchingEngine.placeBid("AbcCapital", "GBPEUR", 5, 1.3);
        matchingEngine.placeOffer("AbcCapital", "GBPEUR", 2, 1.5);
        matchingEngine.placeOffer("AbcCapital", "GBPEUR", 2, 1.5);
        matchingEngine.placeOffer("AbcCapital", "GBPEUR", 5, 1.6);
        matchingEngine.placeOffer("OneBank", "GBPEUR", 11, 1.15);
        matchingEngine.display("GBPEUR");

        matchingEngine.placeOffer("AbcCapital", "EURUSD", 2, 1.5);
        matchingEngine.placeOffer("AbcCapital", "EURUSD", 17, 1.6);
        matchingEngine.placeOffer("Sally", "EURUSD", 16, 1.6);
        matchingEngine.placeBid("OneBank", "EURUSD", 8, 1.61);
        matchingEngine.display("EURUSD");


        final CountDownLatch latch = new CountDownLatch(1);
        final Thread waitThread = new Thread(() -> {
           while(outputs.size() < 11){
               LockSupport.parkNanos(1);
           }
           latch.countDown();
        });
        waitThread.start();

        latch.await(1000, MILLISECONDS);
        Arrays.asList(
                "5 $2.98 | $2.99 10",
                "OneBank bought 2 USDCAD from TravelExchanger @ $2.97",
                "8 $2.97 | $2.99 10",
                "SuperInvestment bought 5 USDCAD from TravelExchanger @ $2.98",
                "1 $1.10 | $1.15 2",
                "0 NaN | $1.60 27",
                "AbcCapital bought 5 GBPEUR from OneBank @ $1.30",
                "AbcCapital bought 2 GBPEUR from OneBank @ $1.20",
                "AbcCapital bought 2 GBPEUR from OneBank @ $1.30",
                "AbcCapital sold 2 EURUSD from OneBank @ $1.50",
                "AbcCapital sold 6 EURUSD from OneBank @ $1.60"
                )
                .stream().forEach(s -> assertTrue(outputs.contains(s)));
    }
}