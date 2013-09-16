package ru.study.core.entity;

import ru.study.core.neurons.BlackNeuron;
import ru.study.core.neurons.Neuron;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class NeuroInput {
    private Double value;
    private Double weight;
    private int sourceObjectHash;

    public NeuroInput(Neuron from, Double weight) {
        this.value = from.getOut();
        this.weight = weight;
        sourceObjectHash = from.hashCode();
    }

    public NeuroInput(double value, double weight) {
        this.value = value;
        this.weight = weight;
        sourceObjectHash = new BlackNeuron(Neuron.NEURON_TYPE_VIRTUAL, 0).hashCode();
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NeuroInput that = (NeuroInput) o;

        if (sourceObjectHash != that.sourceObjectHash) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + sourceObjectHash;
        return result;
    }

    @Override
    public String toString() {
        return "NeuroInput{" +
                "value=" + value +
                ", weight=" + weight +
                ", sourceObjectHash=" + sourceObjectHash +
                '}';
    }
}
