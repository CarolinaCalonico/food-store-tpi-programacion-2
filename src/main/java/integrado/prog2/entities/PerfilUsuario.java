package integrado.prog2.entities;

public class PerfilUsuario extends Base {
    private String direccion;
    private String observaciones;

    public PerfilUsuario(Long id, String direccion, String observaciones) {
        super(id);
        this.direccion = direccion;
        this.observaciones = observaciones;
    }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return "Perfil ID: " + getId() + " | Direccion: " + direccion + " | Observaciones: " + observaciones;
    }
}
