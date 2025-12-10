package br.edu.ifpb.smh.hardware;

import java.util.Random;

public class SimuladorSHA {
    private String id;
    private double leituraAcumulada;
    private Random random = new Random();

    public SimuladorSHA(String id, double inicial) {
        this.id = id;
        this.leituraAcumulada = inicial;
    }

    public String getId() { return id; }

    public double realizarLeitura() {
        // Simula consumo aleatório entre 0.1 e 1.0 litro
        this.leituraAcumulada += 0.1 + (0.9 * random.nextDouble());
        return this.leituraAcumulada;
    }
}