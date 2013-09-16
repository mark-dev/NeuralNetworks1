package ru.study.core.managers;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import ru.study.core.entity.NeuroInput;
import ru.study.core.entity.NeuronEdge;
import ru.study.core.neurons.Neuron;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class NeuroNetworkSolver {
    private DirectedSparseGraph<Neuron, NeuronEdge> network;

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
        ArrayList<Neuron> finalVertex = getFinalVertex(allVertex);
        return finalVertex;
    }

    private ArrayList<Neuron> getFinalVertex(ArrayList<Neuron> allVertex) {
        ArrayList<Neuron> finalVertex = new ArrayList<Neuron>();
        for (int i = 0; i < allVertex.size(); i++) {
            if (allVertex.get(i).getType() == Neuron.NEURON_TYPE_FINAL) {
                finalVertex.add(allVertex.get(i));
            }
        }
        return finalVertex;
    }

    //@return true если все нейроны в списке имеют выходные значения(т.е. уже обсчитали их)
    private boolean isNeuronsHasOutputs(ArrayList<Neuron> neurons) {
        if (neurons.isEmpty()) {
            return true;
        }
        for (int i = 0; i < neurons.size(); i++) {
            if (!neurons.get(i).hasOut()) {
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
        for (int i = 0; i < neurons.size(); i++) {
            if (neurons.get(i).hasOut()) {
                ready.add(neurons.get(i));
            }
        }
        return ready;
    }
}
