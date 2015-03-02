package lda;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class TopicTest {
    public static class WhenInitialize {
        Topic sut;

        @Before
        public void setUp() {
            sut = new Topic(0, 100);
        }

        @Test
        public void id_returns_0() throws Exception {
            assertThat(sut.id(), is(0));
        }

        @Test
        public void getVocabCount_returns_0() throws Exception {
            for (int i = 1; i <= 100; ++i) {
                assertThat(sut.getVocabCount(i), is(0));
            }
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void getVocabCount_0_throws_IllegalArgumentException() throws Exception {
            sut.getVocabCount(0);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void getVocabCount_101_throws_IllegalArgumentException() throws Exception {
            sut.getVocabCount(101);
        }

        @Test
        public void getSumCount_returns_0() throws Exception {
            assertThat(sut.getSumCount(), is(0));
        }
    }
    
    public static class WhenIncrement1TimesForEachVocab {
        Topic sut;

        @Before
        public void setUp() {
            sut = new Topic(0, 100);
            for (int i = 1; i <= 100; ++i) {
                sut.incrementVocabCount(i);
            }
        }

        @Test
        public void getVocabCount_returns_1() throws Exception {
            for (int i = 1; i <= 100; ++i) {
                assertThat(sut.getVocabCount(i), is(1));
            }
        }
        
        @Test
        public void getSumCount_returns_100() throws Exception {
            assertThat(sut.getSumCount(), is(100));
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void incrementVocabCount_0_throws_IllegalArgumentException() throws Exception {
            sut.incrementVocabCount(0);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void incrementVocabCount_101_throws_IllegalArgumentException() throws Exception {
            sut.incrementVocabCount(101);
        }
    }
    
    public static class WhenIncrement100TimesAndDecrement50Times {
        Topic sut;

        @Before
        public void setUp() {
            sut = new Topic(0, 100);
            for (int i = 0; i < 100; ++i) {
                sut.incrementVocabCount(1);
            }
            for (int i = 0; i < 50; ++i) {
                sut.decrementVocabCount(1);
            }
        }

        @Test
        public void getVocabCount_1_returns_50() throws Exception {
            assertThat(sut.getVocabCount(1), is(50));
        }
        
        @Test
        public void getSumCount_returns_50() throws Exception {
            assertThat(sut.getSumCount(), is(50));
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void decrementVocabCount_0_throws_IllegalArgumentException() throws Exception {
            sut.incrementVocabCount(0);
        }
        
        @Test(expected = IllegalArgumentException.class)
        public void decrementVocabCount_101_throws_IllegalArgumentException() throws Exception {
            sut.incrementVocabCount(101);
        }
    }
}