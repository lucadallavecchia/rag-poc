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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
public class CustomEmbedder {

    Logger logger = LoggerFactory.getLogger(CustomEmbedder.class);
    private ZooModel<String, float[]> model;
    private Predictor<String, float[]> predictor;

    public CustomEmbedder(@Value("${model.url:djl://ai.djl.huggingface.pytorch/sentence-transformers/all-MiniLM-L6-v2}") String modelUrl,
                          @Value("${model.engine:PyTorch}") String modelEngine) throws ModelException, IOException {
        Criteria<String, float[]> criteria = Criteria.builder()
                .setTypes(String.class, float[].class)
                .optApplication(Application.NLP.TEXT_EMBEDDING)
                .optModelUrls(modelUrl)
                .optEngine(modelEngine)
                .optProgress(new ai.djl.training.util.ProgressBar())
                .build();

        long currentTime = System.currentTimeMillis();

        logger.info("Loading model...");
        model = ModelZoo.loadModel(criteria);
        logger.info("Embedder model loading duration: {} ms", (System.currentTimeMillis()) - currentTime);

        logger.info("|||Model Configs|||");
        logger.info("Loaded model: {}", model.getName());
        logger.info("Model artifacts: {}", model.getArtifactNames() != null ? Arrays.toString(model.getArtifactNames()) : "");
        logger.info("Model properties: {}", model.getProperties());

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

