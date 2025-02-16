package Modelo;

/**
 *
 * @author Emilio Mayer
 */
public class Proveedor {
    private int idProveedor;
    private String razonSocial;
    private String cuit;
    private String telefono;
    private String email;
    
    //Constructores

    public Proveedor() {
    }

    public Proveedor(int idProveedor, String razonSocial, String cuit, String telefono, String email) {
        this.idProveedor = idProveedor;
        this.razonSocial = razonSocial;
        this.cuit = cuit;
        this.telefono = telefono;
        this.email = email;
    }
    
    //Gets Sets

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
