package com.ldv.rag_poc.vector;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class BertEmbedder {

    Logger logger = LoggerFactory.getLogger(BertEmbedder.class);

    private ZooModel<String, float[]> model;
    private Predictor<String, float[]> predictor;

    public BertEmbedder() throws ModelException, IOException {
        Criteria<String, float[]> criteria = Criteria.builder()
                .setTypes(String.class, float[].class)
                .optApplication(Application.NLP.TEXT_EMBEDDING)
                //.optFilter("backbone", "bert-base-uncased")
                //.optEngine("PyTorch")
                .optProgress(new ai.djl.training.util.ProgressBar())
                .build();
        long currentTime = System.currentTimeMillis();
        logger.info("Loading bertembedder model...");
        model = ModelZoo.loadModel(criteria);
        logger.info("BertEmbedder loading duration: {} ms", (System.currentTimeMillis()) - currentTime);

        logger.debug("|||Model Configs|||");
        logger.debug("Loaded model: {}", model.getName());
        logger.debug("Model artifacts: {}", Arrays.toString(model.getArtifactNames()));
        logger.debug("Model properties: {}", model.getProperties());

        predictor = model.newPredictor();
    }

    public float[] embed(String text) throws TranslateException {
        return predictor.predict(text);
    }

    public void close() {
        if (predictor != null) {
            predictor.close();
        }
        if (model != null) {
            model.close();
        }
    }
}

