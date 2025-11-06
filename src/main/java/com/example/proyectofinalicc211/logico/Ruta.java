import java.util.UUID;

public class Ruta {
    //Arista
    private UUID id;
    private UUID id_origen;
    private UUID id_destino;
    private int tiempo; // En minutos
    private int costo; // En pesos
    private double distancia; // En km
    private int transbordos; // Cantidad de transbordos
    private String medio;

    public Ruta(UUID id_origen, UUID id_destino, int tiempo, int costo, double distancia, int transbordos, String medio) {
        this.id = UUID.randomUUID();
        this.id_origen = id_origen;
        this.id_destino = id_destino;
        this.tiempo = tiempo;
        this.costo = costo;
        this.distancia = distancia;
        this.transbordos = transbordos;
        this.medio = medio;
    }

    public Ruta(UUID id_origen, UUID id_destino, int tiempo, int costo, double distancia, int transbordos) {
        this.id = UUID.randomUUID();
        this.id_origen = id_origen;
        this.id_destino = id_destino;
        this.tiempo = tiempo;
        this.costo = costo;
        this.distancia = distancia;
        this.transbordos = transbordos;
        this.medio = "Autobus";
    }

    public String getMedio() {
        return medio;
    }

    public void setMedio(String medio) {
        this.medio = medio;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public UUID getId_destino() {
        return id_destino;
    }

    public void setId_destino(UUID id_destino) {
        this.id_destino = id_destino;
    }

    public UUID getId_origen() {
        return id_origen;
    }

    public void setId_origen(UUID id_origen) {
        this.id_origen = id_origen;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getTransbordos() {
        return transbordos;
    }

    public void setTransbordos(int transbordos) {
        this.transbordos = transbordos;
    }
}
