package ru.study.neuralnetworks.managers;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import ru.study.neuralnetworks.entity.NeuroInput;
import ru.study.neuralnetworks.entity.NeuronEdge;
import ru.study.neuralnetworks.neurons.Neuron;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class NeuroNetworkSolver {
    private final DirectedSparseGraph<Neuron, NeuronEdge> network;

    public NeuroNetworkSolver(DirectedSparseGraph<Neuron, NeuronEdge> network) {
        this.network = network;
    }

    public ArrayList<Neuron> solve() {
        ArrayList<Neuron> allVertex = new ArrayList<Neuron>(network.getVertices());

        //Результат остановки цикла = все нейроны обсчитали свои входы
        while (!isAllNeuronsReady(allVertex)) {

            ArrayList<Neuron> ready = getReadyNeurons(allVertex);
            //Передаем результат готовых нейронов дальше
            for (Neuron source : ready) {
                //Это список всех путей выходящих из готовой вершины
                ArrayList<NeuronEdge> outEdges = new ArrayList<NeuronEdge>(network.getOutEdges(source));
                for (NeuronEdge outEdge : outEdges) {
                    //destination - вершина, в которую надо передать значение готового нейрона
                    Neuron destination = network.getOpposite(source, outEdge);
                    NeuroInput input = new NeuroInput(source, outEdge.getWeight());
                    destination.addInput(input); //Не добавится если точно такой же NeuroInput существует уже.
                }
            }
            // Если схемма потроенна правильно -
            // после этого одному из нейронов будет достаточно данных для расчета,
            // он расчитает свой выход, и список готовых нейтронов пополнится
            // И все повторится - комуто передадутся его данные
            // Быдлокод... Данные о готовности передаются повторно..
            // Отсекаются за счет того что используется hashset в neuron
        }
        return getFinalVertex(allVertex);
    }

    private ArrayList<Neuron> getFinalVertex(ArrayList<Neuron> allVertex) {
        ArrayList<Neuron> finalVertex = new ArrayList<Neuron>();
        for (Neuron anAllVertex : allVertex) {
            if (anAllVertex.getType() == Neuron.NEURON_TYPE_FINAL) {
                finalVertex.add(anAllVertex);
            }
        }
        return finalVertex;
    }

    //@return true если все нейроны в списке имеют выходные значения(т.е. уже обсчитали их)
    private boolean isNeuronsHasOutputs(ArrayList<Neuron> neurons) {
        if (neurons.isEmpty()) {
            return true;
        }
        for (Neuron neuron : neurons) {
            if (!neuron.hasOut()) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllNeuronsReady(ArrayList<Neuron> allVertex) {
        return getReadyNeurons(allVertex).size() == network.getVertexCount();
    }

    private ArrayList<Neuron> getReadyNeurons(ArrayList<Neuron> neurons) {
        ArrayList<Neuron> ready = new ArrayList<Neuron>();
        for (Neuron neuron : neurons) {
            if (neuron.hasOut()) {
                ready.add(neuron);
            }
        }
        return ready;
    }
}
