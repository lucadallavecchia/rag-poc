package com.ldv.rag_poc.vector;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;

import java.io.IOException;

public class BertEmbedder {
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
        model = ModelZoo.loadModel(criteria);

        // DEBUG
        System.out.println("|||Model Configs|||");
        System.out.println("Loaded model: " + model.getName());
        System.out.println("Model artifacts: " + model.getArtifactNames());
        System.out.println("Model properties: " + model.getProperties());

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

