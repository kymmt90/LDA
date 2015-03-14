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

import java.util.List;

public class Dataset {
    private BagOfWords bow;
    private Vocabularies vocabs;
    
    public Dataset(String bagOfWordsFileName, String vocabsFileName) throws Exception {
        bow = new BagOfWords(bagOfWordsFileName);
        vocabs = new Vocabularies(vocabsFileName);
    }

    public int getNumDocs() {
        return bow.getNumDocs();
    }

    public int getDocLength(int docID) {
        return bow.getDocLength(docID);
    }

    public List<Integer> getWords(int docID) {
        return bow.getWords(docID);
    }

    public int getNumVocabs() {
        return bow.getNumVocabs();
    }

    public int getNumNNZ() {
        return bow.getNumNNZ();
    }

    public int getNumWords() {
        return bow.getNumWords();
    }
    
    public Vocabulary get(int id) {
        return vocabs.get(id - 1);
    }
    
    public int size() {
        return vocabs.size();
    }
    
    public List<Vocabulary> vocabularies() {
        return vocabs.vocabularies();
    }
}
