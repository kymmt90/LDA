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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public final class LDAUtils {
    private LDAUtils() {}
    
    /**
     * Compute the perplexity of trained LDA for the test bag-of-words dataset.
     * @param lda trained LDA
     * @param testBow
     * @return the perplexity for the test bag-of-words dataset
     */
    public static double computePerplexity(LDA lda, BagOfWords testBow) {
        double loglikelihood = 0.0;
        for (int d = 1; d <= testBow.getNumDocs(); ++d) {
            for (int w = 0; w < testBow.getDocLength(d); ++w) {
                for (int t = 0; t < lda.getNumTopics(); ++t) {
                    loglikelihood += Math.log(lda.getTheta(d, t)) + Math.log(lda.getPhi(t, testBow.getWords(d).get(w))); 
                }
            }
        }
        return Math.exp(-loglikelihood / lda.getBow().getNumWords());
    }
    
    /**
     * Get the vocabularies list where elements are arranged by topic-vocab probability descending order.
     * @param lda
     * @param topicID
     * @return the vocabularies list where elements are arranged by topic-vocab probability descending order
     * @throws IllegalArgumentException topicID < 0 || #topics <= topicID
     * @throws IllegalStateException the vocabularies file has not been read yet
     */
    public static List<Pair<String, Double>> getProbDescOrderedVocabs(LDA lda, int topicID) {
        if (topicID < 0 || lda.getNumTopics() <= topicID) throw new IllegalArgumentException();
        if (lda.getBow().getNumVocabs() == 0) throw new IllegalStateException();
        
        List<Pair<String, Double>> vocabProbPairs = new ArrayList<>();
        for (int v = 1; v <= lda.getBow().getNumVocabs(); ++v) {
            vocabProbPairs.add(new ImmutablePair<String, Double>(lda.getVocab(v), lda.getPhi(topicID, v)));
        }
        Collections.sort(vocabProbPairs, (p1, p2) -> Double.compare(p2.getRight(), p1.getRight()));
        return vocabProbPairs;
    }
}
