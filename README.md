# LDA in Java 8

Latent Dirichlet Allocation using Java 8.
Latent Dirichlet Allocation (LDA) [Blei+ 2003] is the basic probabilistic topic model.
Please see following for more details:

- [Latent Dirichlet allocation - Wikipedia, the free encyclopedia](http://en.wikipedia.org/wiki/Latent_Dirichlet_allocation)

This library supports [collapsed Gibbs sampling](http://psiexp.ss.uci.edu/research/papers/sciencetopics.pdf) [Griffiths and Steyvers 2004] for model inference.

)
This repository includes datasets from [UCI Machine Learning Repository](https://archive.ics.uci.edu/ml/datasets) [Lichman 2013].

## Requierments

- Java 8
- Apache Commons
  - Math
  - Lang
- Maven

For unit testing, these libraries are also needed.

- JUnit
- Mockito

## Usage

### dataset form

The form of bag-of-words dataset follows [Bag of Words Data Set](https://archive.ics.uci.edu/ml/datasets/Bag+of+Words) in [UCI Machine Learning Repository](https://archive.ics.uci.edu/ml/index.html).
The form of doc-vocab-count dataset is following:

    #Documents
	#Vocabularies
	#NonZeros
    docID vocabID count
	docID vocabID count
	...
    docID vocabID count

The form of vocabularies dataset is following:

    vocab1
    vocab2
    vocab3
    ...
    vocab{#Vocabularies}

Each number of lines is `vocabID`.

### Example

There is `lda.BagOfWords` to read dataset from files.
When using LDA, `lda.BagOfWords` object and other parameters are passed to initialize `lda.LDA`.
For example:

    BagOfWords bow = new BagOfWords("/path/to/docvocabcounts");
    LDA lda = new LDA(0.1                    /* initial alpha */,
     				  0.1                    /* initial beta */,
     				  50                     /* the number of topics */,
     				  bow                    /* bag-of-words */,
     				  LDAInferenceMethod.CGS /* use collapsed Gibbs sampler for inference */,
                      "/path/to/properties"  /* path of properties file */);
    lda.run();

These items are available as properties:

    numIteration=<the number of iteration of collapsed Gibbs sampling>
    seed=<seed for the pseudo random number generator>

The results of topics can be refered as follows:

    lda.readVocabs("/path/to/vocabs");
    // Get the probability descending order topic-0 vocabularies list
    List<Pair<String, Double>> vocabs
        = LDAUtils.getProbDescOrderedVocabs(lda, 0 /* = topic ID */);
    vocabs.get(0).getLeft();  // the top probability vocabulary in topic-0
    vocabs.get(0).getRight(); // the probability value of the top vocabulary in topic-0

Please see `example.Example#main` for more details.
Execute these commands at the directory `LDA` to build and execute `example.Example`.

    $ mvn clean package dependency:copy-dependencies -DincludeScope=runtime
	$ java -jar target/LDA-<version>.jar

## License

- [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
