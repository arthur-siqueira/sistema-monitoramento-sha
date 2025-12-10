package br.edu.ifpb.smh.services;

import br.edu.ifpb.smh.dto.HistoricoConsumoDTO;
import java.util.HashMap;
import java.util.Map;

public class SensorService {
    private Map<String, Double> bancoDados = new HashMap<>();

    public void registrarLeitura(String shaId, double valor) {
        bancoDados.put(shaId, valor);
        System.out.println("   [BD-Sensores] SHA " + shaId + " atualizado: " + String.format("%.2f", valor));
    }

    public HistoricoConsumoDTO getLeitura(String shaId) {
        double valor = bancoDados.getOrDefault(shaId, 0.0);
        return new HistoricoConsumoDTO(shaId, valor);
    }
}