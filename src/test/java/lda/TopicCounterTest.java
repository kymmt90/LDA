/*
* Copyright 2015 Kohei Yamamoto
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package lda;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Before;
import org.junit.Test;

public class TopicCounterTest {
    final int size = 100;
    TopicCounter sut = new TopicCounter(size);
    
    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < 10; ++j) sut.incrementTopicCount(i);
        }
    }
    
    @Test
    public void getDocLength_returns_1000() throws Exception {
        assertThat(sut.getDocLength(), is(1000));
    }
    
    @Test
    public void getTopicCount_0_returns_10() throws Exception {
        assertThat(sut.getTopicCount(0), is(10));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getTopicCount_minus_1_throws_IllegalArgumentException() throws Exception {
        sut.getTopicCount(-1);
    }
    
    @Test
    public void getTopicCount_99_returns_10() throws Exception {
        assertThat(sut.getTopicCount(99), is(10));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getTopicCount_100_throws_IllegalArgumentException() throws Exception {
        sut.getTopicCount(100);
    }
}
