package ru.study.neuralnetworks.entity;

import ru.study.neuralnetworks.neurons.Neuron;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 9:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ComporatorInfo {
    private Neuron max;
    private Neuron aStroke;
    private Neuron bStroke;

    public Neuron getaStroke() {
        return aStroke;
    }

    public void setaStroke(Neuron aStroke) {
        this.aStroke = aStroke;
    }

    public Neuron getbStroke() {
        return bStroke;
    }

    public void setbStroke(Neuron bStroke) {
        this.bStroke = bStroke;
    }

    public Neuron getMax() {

        return max;
    }

    public void setMax(Neuron max) {
        this.max = max;
    }
}
